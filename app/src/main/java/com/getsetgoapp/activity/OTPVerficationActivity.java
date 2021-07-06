
package com.getsetgoapp.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.adoisstudio.helper.Session;
import com.getsetgoapp.R;
import com.getsetgoapp.databinding.ActivityOTPVerficationBinding;
import com.getsetgoapp.util.App;
import com.getsetgoapp.util.CheckConnection;
import com.getsetgoapp.util.Click;
import com.getsetgoapp.util.P;
import com.getsetgoapp.util.ProgressView;
import com.getsetgoapp.util.SmsBroadcastReceiver;
import com.getsetgoapp.util.WindowView;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OTPVerficationActivity extends AppCompatActivity {

    private OTPVerficationActivity activity = this;
    private ActivityOTPVerficationBinding binding;
    LoadingDialog loadingDialog;
    private Session session;
    private String email = "";
    private CountDownTimer timer;
    private String resetOTP;
    private int otpLimit = 4;

    private static final int REQ_USER_CONSENT = 200;
    private SmsBroadcastReceiver smsBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_o_t_p_verfication);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initView();
    }

    private void initView(){
        loadingDialog = new LoadingDialog(this);
        session = new Session(activity);
        email = getIntent().getStringExtra(P.email);
        resetOTP = "Click to resend code";

        binding.txtOTPMessage.setText("We sent code on your email and phone number") ;

//        startTimer();

        binding.txtSeconds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (binding.txtSeconds.getText().toString().equals(resetOTP)){
                    if (CheckConnection.isVailable(activity)){
//                        hitResendOtp();
                    }else {
                        H.showMessage(activity,"No internet connection");
                    }
                }
            }
        });

        binding.txtProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(binding.etxOtp.getText().toString())){
                    H.showMessage(activity,"Enter code");
                }else if (binding.etxOtp.getText().toString().length()<otpLimit || binding.etxOtp.getText().toString().length()>otpLimit){
                    H.showMessage(activity,"Enter 4 digit code");
                }else {
                    if (CheckConnection.isVailable(activity)){
                        hitVerifyOTP();
                    }else {
                        H.showMessage(activity,"No internet connection");
                    }
                }
            }
        });

        startSmsUserConsent();
    }


    private void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
               H.showMessage(activity,"OTP send successfully on your register number");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                H.showMessage(activity,"Something went wrong, try again");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                getOtpFromMessage(message);
            }
        }
    }

    private void getOtpFromMessage(String message) {
        // This will match any 4 digit number in the message
        Pattern pattern = Pattern.compile("(|^)\\d{4}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            binding.etxOtp.setText(matcher.group(0));
        }
    }

    private void registerBroadcastReceiver() {
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener =
                new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, REQ_USER_CONSENT);
                    }
                    @Override
                    public void onFailure() {
                    }
                };
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }

    private void hitVerifyOTP() {
        ProgressView.show(activity,loadingDialog);
        Json j = new Json();
        j.addString(P.email,email);
        j.addString(P.verification_code,binding.etxOtp.getText().toString().trim());
        Api.newApi(activity, P.baseUrl + "verify_registered_user").addJson(j)
                .setMethod(Api.POST)
                .onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    MessageBox.showOkMessage(this, "Message", "Registration failed. Please try again", () -> {
                    });
                })
                .onSuccess(json ->
                {
                    ProgressView.dismiss(loadingDialog);
                    if (json.getInt(P.status) == 1) {
                        json = json.getJson(P.data);
                        Json userData = json.getJson(P.userdata);
                        Session session = new Session(this);

                        String token = userData.getString(P.token);
                        String user_id = userData.getString(P.user_id);

                        session.addString(P.is_affiliate, userData.getString(P.is_affiliate));
                        session.addString(P.token, token);
                        session.addString(P.user_id, user_id);
                        session.addString(P.name, userData.getString(P.name) + "");
                        session.addString(P.lastname, userData.getString(P.lastname) + "");
                        session.addString(P.email, userData.getString(P.email) + "");
                        session.addString(P.contact, userData.getString(P.contact) + "");
                        session.addString(P.dob, userData.getString(P.dob) + "");
                        session.addString(P.occupation_id, userData.getString(P.occupation_id) + "");
                        session.addString(P.marital_status_id, userData.getString(P.marital_status_id) + "");
                        session.addString(P.gender, userData.getString(P.gender) + "");
                        session.addString(P.profile_picture, userData.getString(P.profile_picture) + "");
                        session.addString(P.app_link, userData.getString(P.app_link) + "");
                        session.addString(P.referral_link, userData.getString(P.referral_link) + "");
                        session.addString(P.qr_code, userData.getString(P.qr_code) + "");
                        session.addString(P.mobile_terms_accepted, userData.getString(P.mobile_terms_accepted) + "");
                        session.addString(P.referral_code, userData.getString(P.referral_code) + "");

                        App.authToken = token;
                        App.user_id = user_id;

                        H.showMessage(activity,json.getString(P.msg));
                        Intent mainIntent = new Intent(activity,BaseScreenActivity.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                        finish();

                    }else {
                        H.showMessage(activity,json.getString(P.err));
                    }
                })
                .run("hitVerifyOTP");
    }

    private void hitResendOtp() {
        ProgressView.show(activity,loadingDialog);
        Json j = new Json();
        Api.newApi(activity, P.baseUrl + "").addJson(j)
                .setMethod(Api.POST)
                .onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                    MessageBox.showOkMessage(this, "Message", "Registration failed. Please try again", () -> {
                    });
                })
                .onSuccess(json ->
                {
                    ProgressView.dismiss(loadingDialog);
                    if (json.getInt(P.status) == 1) {
                        json = json.getJson(P.data);
                        H.showMessage(activity,"OTP send successfully");
                        binding.etxOtp.setText("");
                        startTimer();
                    }else {
                        H.showMessage(activity,"Something went wrong, try again");
                    }
                })
                .run("hitResendOtp");
    }


    private void startTimer(){
        binding.txtResend.setVisibility(View.VISIBLE);
        timer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished / 1000;
                binding.txtSeconds.setText("00 : " + time + " " + "second");
            }
            public void onFinish() {
                binding.txtResend.setVisibility(View.GONE);
                binding.txtSeconds.setText(resetOTP);
                binding.etxOtp.setText("");
            }
        }.start();
    }

    private void closeTimer(){
        try {
            if (timer!=null){
                timer.cancel();
            }
        }catch (Exception e){
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeTimer();
    }

}