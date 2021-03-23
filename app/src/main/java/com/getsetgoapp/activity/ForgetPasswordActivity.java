
package com.getsetgoapp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.getsetgoapp.R;
import com.getsetgoapp.databinding.ActivityForgetPasswordBinding;
import com.getsetgoapp.util.App;
import com.getsetgoapp.util.P;
import com.getsetgoapp.util.ProgressView;
import com.getsetgoapp.util.Validation;
import com.getsetgoapp.util.WindowView;

public class ForgetPasswordActivity extends AppCompatActivity {

    private ForgetPasswordActivity activity = this;
    private ActivityForgetPasswordBinding binding;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initView();
    }

    private void initView(){
        loadingDialog = new LoadingDialog(this);

        binding.txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()){
                    hitForgetPassword();
                }
            }
        });

    }

    private boolean checkValidation() {
        boolean value = true;

        if (TextUtils.isEmpty(binding.etxEmailAddress.getText().toString().trim())) {
            H.showMessage(activity, "Enter email id");
            value = false;
        } else if (!Validation.validEmail(binding.etxEmailAddress.getText().toString().trim())) {
            H.showMessage(activity, "Enter valid email");
            value = false;
        }
        return value;
    }

    private void hitForgetPassword() {
        ProgressView.show(activity,loadingDialog);
        Json j = new Json();
        j.addString(P.email,binding.etxEmailAddress.getText().toString().trim());
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

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 500);

                    }else {
                        H.showMessage(activity,"Something went wrong, try again");
                    }
                })
                .run("hitForgetPassword");
    }

}