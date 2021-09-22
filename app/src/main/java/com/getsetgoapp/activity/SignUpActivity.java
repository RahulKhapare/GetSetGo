package com.getsetgoapp.activity;

import android.app.Dialog;
import android.content.DialogInterface;
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
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.adoisstudio.helper.Session;
import com.getsetgoapp.Adapter.CountryCodeSelectionAdapter;
import com.getsetgoapp.Adapter.RegisterForAdapter;
import com.getsetgoapp.Model.CountryCodeModel;
import com.getsetgoapp.Model.RegisterForModel;
import com.getsetgoapp.R;
import com.getsetgoapp.databinding.ActivitySignUpBinding;
import com.getsetgoapp.util.App;
import com.getsetgoapp.util.Click;
import com.getsetgoapp.util.JumpToLogin;
import com.getsetgoapp.util.P;
import com.getsetgoapp.util.Validation;
import com.getsetgoapp.util.WindowView;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class SignUpActivity extends AppCompatActivity{

    private final SignUpActivity activity = this;
    private ActivitySignUpBinding binding;
    private LoadingDialog loadingDialog;
    private String referrerUrl;
    private String countryCode;
    private String countryID;
    String relationShipManager = "Relationship Manager ";
    JSONArray registration_purpose_id;
    List<RegisterForModel> registerForModelList;

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

        registration_purpose_id = new JSONArray();
        registerForModelList = new ArrayList<>();

        List<CountryCodeModel> countryCodeModelList = new ArrayList<>();

        CountryCodeModel model1 = new CountryCodeModel();
        model1.setCountry_id("");
        model1.setCountry_code("");
        model1.setCountry_name("");
        model1.setCountry_shortname("Country Code");
        countryCodeModelList.add(model1);

        int selection = 0;
        JsonList country_list = SplashActivity.country_list;
        if (country_list!=null && !country_list.isEmpty()){

            for (int i=0; i<country_list.size(); i++){
                Json json = country_list.get(i);
                CountryCodeModel model = new CountryCodeModel();
                model.setCountry_id(json.getString(P.country_id));
                model.setCountry_code(json.getString(P.country_code));
                model.setCountry_name(json.getString(P.country_name));
                model.setCountry_shortname(json.getString(P.country_shortname));
                countryCodeModelList.add(model);
                if (model.getCountry_code().equals("91")){
                    selection = i;
                }
            }
        }

        CountryCodeSelectionAdapter adapter = new CountryCodeSelectionAdapter(activity, countryCodeModelList,1);
        binding.spinnerCode.setAdapter(adapter);
//        binding.spinnerCode.setSelection(selection);

        binding.spinnerCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryCodeModel model = countryCodeModelList.get(position);
                if (!TextUtils.isEmpty(model.getCountry_id())){
                    countryCode = model.getCountry_code();
                    countryID = model.getCountry_id();
                }else {
                    countryCode = "";
                    countryID = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        callSponsorIDApi();
        onClick();

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
                if (!TextUtils.isEmpty(newText) && newText.length() == 9) {
                    getSponsorIDApi(binding.etxSponserId.getText().toString().trim());
                } else if (newText.length() < 9 || newText.length() > 9) {
                    binding.txtSponsorName.setText(relationShipManager);
                }
            }
        };

        binding.etxSponserId.addTextChangedListener(textWatcher);
        if (referrerUrl.matches("[0-9]+") && referrerUrl.length() == 9) {
            binding.etxSponserId.setText(referrerUrl);
            binding.etxSponserId.setFocusable(false);
            binding.etxSponserId.setFocusableInTouchMode(false);
            binding.etxSponserId.setClickable(false);
        }

        setRegisterForData();
    }

    private void setRegisterForData(){
        JsonList registration_purpose_list = SplashActivity.registration_purpose_list;
        if (registration_purpose_list!=null && registration_purpose_list.size()!=0){
            for (Json json : registration_purpose_list){
                RegisterForModel model = new RegisterForModel();
                model.setId(json.getString(P.id));
                model.setPurpose_name(json.getString(P.purpose_name));
                registerForModelList.add(model);
            }
        }

        binding.recyclerRegisterFor.setLayoutManager(new LinearLayoutManager(activity));
        binding.recyclerRegisterFor.setHasFixedSize(true);
        binding.recyclerRegisterFor.setNestedScrollingEnabled(false);
        RegisterForAdapter adapter = new RegisterForAdapter(activity,registerForModelList);
        binding.recyclerRegisterFor.setAdapter(adapter);
    }

    private void onClick() {

        binding.txtTermsMessage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                webDialog(SplashActivity.termConditionUrl);
            }
        });

        binding.txtTermsMessage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                webDialog(SplashActivity.termConditionUrl);
            }
        });

        binding.radioIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.radioCompany.setChecked(false);
                visibleIndividual();
            }
        });

        binding.radioCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.radioIndividual.setChecked(false);
                visibleCompany();
            }
        });

        binding.txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);

                registration_purpose_id = new JSONArray();
                for (RegisterForModel model :registerForModelList ){
                    if (model.getValue()==1){
                        registration_purpose_id.put(Integer.parseInt(model.getId()));
                    }
                }

                if (checkValidation()) {
                    String registerAs;
                    if (binding.radioIndividual.isChecked()) {
                        registerAs = "1";
                    } else {
                        registerAs = "2";
                    }
                    Json json = new Json();
                    json.addString(P.registered_as, registerAs);

                    if (binding.radioCompany.isChecked()) {
                        json.addString(P.name, binding.etxCompanyName.getText().toString() + "");
                        json.addString(P.lastname, "");
                    } else if (binding.radioIndividual.isChecked()) {
                        json.addString(P.name, binding.etxFirstName.getText().toString() + "");
                        json.addString(P.lastname, binding.etxLastName.getText().toString() + "");
                    }

                    json.addString(P.email, binding.etxEmailAddress.getText().toString() + "");
                    json.addString(P.country_id, countryID);
                    json.addString(P.country_code, countryCode);
                    json.addString(P.contact, binding.etxPhone.getText().toString() + "");
                    json.addString(P.password, binding.etxPassword.getText().toString() + "");
                    json.addString(P.confirm_password, binding.etxConfirmPassword.getText().toString() + "");
                    json.addString(P.sponsor_id, binding.etxSponserId.getText().toString() + "");
                    json.addString(P.fcm_value, new Session(activity).getString(P.fcm_value));
                    json.addJSONArray(P.registration_purpose_id, registration_purpose_id);
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

    private void visibleIndividual() {
        binding.etxCompanyNameView.setVisibility(View.GONE);

        binding.etxFirstNameView.setVisibility(View.VISIBLE);
        binding.etxLastNameView.setVisibility(View.VISIBLE);
    }

    private void visibleCompany() {
        binding.etxCompanyNameView.setVisibility(View.VISIBLE);

        binding.etxFirstNameView.setVisibility(View.GONE);
        binding.etxLastNameView.setVisibility(View.GONE);
    }


    private boolean checkValidation() {
        boolean value = true;

        if (binding.radioCompany.isChecked()) {
            if (TextUtils.isEmpty(binding.etxCompanyName.getText().toString().trim())) {
                H.showMessage(activity, "Enter company name");
                value = false;
            } else {
                value = performCheck(value);
            }
        } else if (binding.radioIndividual.isChecked()) {
            if (TextUtils.isEmpty(binding.etxFirstName.getText().toString().trim())) {
                H.showMessage(activity, "Enter first name");
                value = false;
            } else if (TextUtils.isEmpty(binding.etxLastName.getText().toString().trim())) {
                H.showMessage(activity, "Enter last name");
                value = false;
            } else {
                value = performCheck(value);
            }
        }
        return value;
    }


    private boolean performCheck(boolean value) {

        if (TextUtils.isEmpty(binding.etxEmailAddress.getText().toString().trim())) {
            H.showMessage(activity, "Enter email id");
            value = false;
        } else if (!Validation.validEmail(binding.etxEmailAddress.getText().toString().trim())) {
            H.showMessage(activity, "Enter valid email");
            value = false;
        }  else if (TextUtils.isEmpty(countryCode)) {
            H.showMessage(activity, "Select your ISD code");
            value = false;
        } else if (TextUtils.isEmpty(binding.etxPhone.getText().toString().trim())) {
            H.showMessage(activity, "Enter your phone number");
            value = false;
        } else if (countryCode.equals("91") &&
                binding.etxPhone.getText().toString().length() != 10) {
            H.showMessage(activity, "Enter valid phone number");
            value = false;
        }else if (TextUtils.isEmpty(binding.etxPassword.getText().toString().trim())) {
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
        } else if (registration_purpose_id!=null && registration_purpose_id.length()==0){
            H.showMessage(activity, "Select registering for");
            value = false;
        }
        else if (TextUtils.isEmpty(binding.etxSponserId.getText().toString().trim())) {
            H.showMessage(activity, "Enter relationship manager id");
            value = false;
        } else if (!binding.checkTermCondition.isChecked()){
            H.showMessage(activity, "Please allow check for term and conditions");
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
                        if (Json1.getInt(P.status) == 1) {
                            Intent mainIntent = new Intent(activity, OTPVerficationActivity.class);
                            mainIntent.putExtra(P.email, binding.etxEmailAddress.getText().toString());
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
                        } else {
                            H.showMessage(activity, Json1.getString(P.err));
                        }
                    }

                }).run("register");
    }


    private void callSponsorIDApi() {
        loadingDialog = new LoadingDialog(activity, false);
        Json json = new Json();
        json.addString(P.ip, getLocalIpAddress());
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
                        if (Json1.getInt(P.status) == 1) {
                            Json1 = Json1.getJson(P.data);
                            Json sponsorData = Json1.getJson(P.sponsor);
                            String sponsor_id = sponsorData.getString(P.sponsor_id);
                            String sponsor_name = sponsorData.getString(P.sponsor_name);
                            binding.etxSponserId.setText(sponsor_id);
                            binding.txtSponsorName.setText( relationShipManager + sponsor_name);
                        } else {
                            H.showMessage(activity, Json1.getString(P.err));
                        }
                    }

                }).run("callSponsorIDApi");
    }

    private void getSponsorIDApi(String sponsorId) {
        loadingDialog = new LoadingDialog(activity, false);
        Json json = new Json();
        json.addString(P.sponsor_id, sponsorId);
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
                        if (Json1.getInt(P.status) == 1) {
                            Json1 = Json1.getJson(P.data);
                            Json sponsorData = Json1.getJson(P.sponsor);
                            String sponsor_id = sponsorData.getString(P.sponsor_id);
                            String sponsor_name = sponsorData.getString(P.sponsor_name);

//                            binding.etxSponserId.setText(sponsor_id);
                            binding.txtSponsorName.setText(relationShipManager + sponsor_name);
                        } else {
                            binding.txtSponsorName.setText(relationShipManager);
                        }
                    }

                }).run("getSponsorIDApi");
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
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
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    public String getWifiIPAddress() {
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        return Formatter.formatIpAddress(ip);
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
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }


    private void webDialog(String url) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_webview_data);

        TextView txtCancel = dialog.findViewById(R.id.txtCancel);
        WebView webView = dialog.findViewById(R.id.webView);
        loadUrl(webView,url);

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }


    private void loadUrl(WebView webView,String url) {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
//        webSettings.setBuiltInZoomControls(true);
        webView.loadUrl(url);
    }

}