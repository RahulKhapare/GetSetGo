package com.getsetgoapp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.adoisstudio.helper.Session;
import com.getsetgoapp.Adapter.CountryCodeAdapter;
import com.getsetgoapp.Model.CountryCode;
import com.getsetgoapp.R;
import com.getsetgoapp.databinding.ActivitySignUpBinding;
import com.getsetgoapp.util.App;
import com.getsetgoapp.util.Click;
import com.getsetgoapp.util.JumpToLogin;
import com.getsetgoapp.util.P;
import com.getsetgoapp.util.Validation;
import com.getsetgoapp.util.WindowView;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private final SignUpActivity activity = this;
    private ActivitySignUpBinding binding;
    private LoadingDialog loadingDialog;
    private String referrerUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        referrerUrl = new Session(activity).getString(P.referrerUrl);
        initView();

    }

    private void initView() {
//        callSponsorIDApi();
        onClick();
        binding.actvIsdCode.setText("+91");

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                String newText = s.toString();
                if (!TextUtils.isEmpty(newText) && newText.length()==9) {
                    getSponsorIDApi(binding.etxSponserId.getText().toString().trim());
                }else if (newText.length()<9 || newText.length()>9){
                    binding.txtSponsorName.setText("Sponsor By");
                }
            }
        };

        binding.etxSponserId.addTextChangedListener(textWatcher);
        if (referrerUrl.matches("[0-9]+") && referrerUrl.length() == 9) {
            binding.etxSponserId.setText(referrerUrl);
        }
    }


    private void onClick() {

        binding.actvIsdCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    binding.actvIsdCode.append("+");
                }
            }
        });
        populateIsdCode(activity, binding.actvIsdCode);

        binding.radioIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.radioCompany.setChecked(false);
            }
        });

        binding.radioCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.radioIndividual.setChecked(false);
            }
        });

        binding.txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (checkValidation()) {
                    String registerAs;
                    if(binding.radioIndividual.isChecked()){
                        registerAs = "1";
                    }else{
                        registerAs = "2";
                    }
                    Json json = new Json();
                    json.addString(P.registered_as, registerAs);
                    json.addString(P.name, binding.etxFirstName.getText().toString() + "");
                    json.addString(P.lastname, binding.etxLastName.getText().toString() + "");
                    json.addString(P.email, binding.etxEmailAddress.getText().toString() + "");
                    json.addString(P.contact, binding.etxPhone.getText().toString() + "");
                    json.addString(P.password, binding.etxPassword.getText().toString() + "");
                    json.addString(P.confirm_password, binding.etxConfirmPassword.getText().toString() + "");
                    json.addString(P.sponsor_id, binding.etxSponserId.getText().toString() + "");
                    json.addString(P.fcm_value, new Session(activity).getString(P.fcm_value));
                    callRegisterApi(json);
                }
            }
        });

        binding.lnrLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                onBackPressed();
            }
        });
    }

    public void populateIsdCode(Context context, AutoCompleteTextView autoCompleteTextView) {
        final ArrayList<CountryCode> country = new ArrayList<>();

        //forSample
        CountryCode code = new CountryCode();
        code.setCode("+91");
        country.add(code);

        CountryCodeAdapter countryFlagAdapter = new CountryCodeAdapter(context,
                R.layout.activity_sign_up, R.id.lbl_name, country);
        autoCompleteTextView.setThreshold(2);         //will start working from first character
        autoCompleteTextView.setAdapter(countryFlagAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view != null) {
                    CountryCode county = (CountryCode) view.getTag();
                    if (county.getCode() != null) {
                        binding.actvIsdCode.setText(county.getCode());
                    }
                }
            }
        });
    }

    private boolean checkValidation() {
        boolean value = true;
        if (TextUtils.isEmpty(binding.etxFirstName.getText().toString().trim())) {
            H.showMessage(activity, "Enter first name");
            value = false;
        } else if (TextUtils.isEmpty(binding.etxLastName.getText().toString().trim())) {
            H.showMessage(activity, "Enter last name");
            value = false;
        } else if (TextUtils.isEmpty(binding.etxEmailAddress.getText().toString().trim())) {
            H.showMessage(activity, "Enter email id");
            value = false;
        } else if (binding.actvIsdCode.getText().toString().length() < 2) {
            H.showMessage(activity, "Enter your isd code");
            value = false;
        } else if (TextUtils.isEmpty(binding.etxPhone.getText().toString().trim())) {
            H.showMessage(activity, "Enter your phone number");
            value = false;
        } else if (binding.actvIsdCode.getText().toString().equals("+91") &&
                binding.etxPhone.getText().toString().length() != 10) {
            H.showMessage(activity, "Enter valid phone number");
            value = false;
        } else if (!Validation.validEmail(binding.etxEmailAddress.getText().toString().trim())) {
            H.showMessage(activity, "Enter valid email");
            value = false;
        } else if (TextUtils.isEmpty(binding.etxPassword.getText().toString().trim())) {
            H.showMessage(activity, "Enter password");
            value = false;
        } else if (binding.etxPassword.getText().toString().length() < 3) {
            H.showMessage(activity, "Please enter valid password");
            value = false;
        } else if (TextUtils.isEmpty(binding.etxConfirmPassword.getText().toString().trim())) {
            H.showMessage(activity, "Enter confirm password");
            value = false;
        } else if (binding.etxConfirmPassword.getText().toString().length() < 3) {
            H.showMessage(activity, "Please enter valid confirm password");
            value = false;
        } else if (!binding.etxConfirmPassword.getText().toString().trim().equals(binding.etxPassword.getText().toString().trim())) {
            H.showMessage(activity, "Confirm password not matched with password");
            value = false;
        }else if (TextUtils.isEmpty(binding.etxSponserId.getText().toString().trim())) {
            H.showMessage(activity, "Enter sponser id");
            value = false;
        }

        return value;
    }

    private void callRegisterApi(Json json) {
        loadingDialog = new LoadingDialog(activity, false);
        Api.newApi(activity, P.baseUrl + "register")
                .addJson(json)
                .setMethod(Api.POST)
                .onHeaderRequest(App::getHeaders)
                .onLoading(isLoading -> {
                    if (!isDestroyed()) {
                        if (isLoading)
                            loadingDialog.show("loading...");
                        else
                            loadingDialog.dismiss();
                    }
                })
                .onError(() ->
                        MessageBox.showOkMessage(this, "Message", "Failed to login. Please try again", () -> {
                            loadingDialog.dismiss();
                        }))
                .onSuccess(Json1 -> {
                    if (Json1 != null) {
                        JumpToLogin.call(Json1, this);
                        loadingDialog.dismiss();
                        if (Json1.getInt(P.status) == 1){
                            Intent mainIntent = new Intent(activity,OTPVerficationActivity.class);
                            mainIntent.putExtra(P.email,binding.etxEmailAddress.getText().toString());
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
                        }else {
                            H.showMessage(activity, Json1.getString(P.err));
                        }
                    }

                }).run("register");
    }


    private void callSponsorIDApi() {
        loadingDialog = new LoadingDialog(activity, false);
        Json json = new Json();
        json.addString(P.ip,getLocalIpAddress());
        Api.newApi(activity, P.baseUrl + "get_sponsor_from_ip")
                .addJson(json)
                .setMethod(Api.POST)
                .onHeaderRequest(App::getHeaders)
                .onLoading(isLoading -> {
                    if (!isDestroyed()) {
                        if (isLoading)
                            loadingDialog.show("loading...");
                        else
                            loadingDialog.dismiss();
                    }
                })
                .onError(() ->
                        MessageBox.showOkMessage(this, "Message", "Failed to login. Please try again", () -> {
                            loadingDialog.dismiss();
                        }))
                .onSuccess(Json1 -> {
                    if (Json1 != null) {
                        JumpToLogin.call(Json1, this);
                        loadingDialog.dismiss();
                        if (Json1.getInt(P.status) == 1){
                            Json1 = Json1.getJson(P.data);
                            Json sponsorData = Json1.getJson(P.sponsor);
                            String sponsor_id = sponsorData.getString(P.sponsor_id);
                            String sponsor_name = sponsorData.getString(P.sponsor_name);
                            binding.etxSponserId.setText(sponsor_id);
                            binding.txtSponsorName.setText("Sponsor By "+sponsor_name);
                        }
                    }

                }).run("callSponsorIDApi");
    }

    private void getSponsorIDApi(String sponsorId) {
        loadingDialog = new LoadingDialog(activity, false);
        Json json = new Json();
        json.addString(P.sponsor_id,sponsorId);
        Api.newApi(activity, P.baseUrl + "get_sponsor_details")
                .addJson(json)
                .setMethod(Api.POST)
                .onHeaderRequest(App::getHeaders)
                .onLoading(isLoading -> {
                    if (!isDestroyed()) {
                        if (isLoading)
                            loadingDialog.show("loading...");
                        else
                            loadingDialog.dismiss();
                    }
                })
                .onError(() ->
                        MessageBox.showOkMessage(this, "Message", "Failed to login. Please try again", () -> {
                            loadingDialog.dismiss();
                        }))
                .onSuccess(Json1 -> {
                    if (Json1 != null) {
                        JumpToLogin.call(Json1, this);
                        loadingDialog.dismiss();
                        if (Json1.getInt(P.status) == 1){
                            Json1 = Json1.getJson(P.data);
                            Json sponsorData = Json1.getJson(P.sponsor);
                            String sponsor_id = sponsorData.getString(P.sponsor_id);
                            String sponsor_name = sponsorData.getString(P.sponsor_name);
//                            binding.etxSponserId.setText(sponsor_id);
                            binding.txtSponsorName.setText("Sponsor By "+sponsor_name);
                        }else {
                            binding.txtSponsorName.setText("Sponsor By");
                        }
                    }

                }).run("getSponsorIDApi");
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ipAddress = Formatter.formatIpAddress(inetAddress.hashCode());
                        return ipAddress;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("TAG", ex.toString());
        }
        return null;
    }


    private String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += inetAddress.getHostAddress();
                    }

                }

            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }

    public static String getMobileIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        return  addr.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }

    public String getWifiIPAddress() {
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        return  Formatter.formatIpAddress(ip);
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }

}