package com.getsetgo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.adoisstudio.helper.Session;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivitySignUpBinding;
import com.getsetgo.util.App;
import com.getsetgo.util.Click;
import com.getsetgo.util.P;
import com.getsetgo.util.Validation;
import com.getsetgo.util.WindowView;

public class SignUpActivity extends AppCompatActivity {

    private final SignUpActivity activity = this;
    private ActivitySignUpBinding binding;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        initView();
    }

    private void initView() {

        onClick();
        //
    }

    private void onClick() {

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
                    Intent intent = new Intent(activity, BaseScreenActivity.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();

                    Json json = new Json();
                    json.addString(P.name,binding.etxFirstName.getText().toString() + "");
                    json.addString(P.lastname,binding.etxLastName.getText().toString() + "");
                    json.addString(P.email,binding.etxEmailAddress.getText().toString() + "");
                    json.addString(P.password,binding.etxPassword.getText().toString() + "");
                    json.addString(P.confirm_password,binding.etxConfirmPassword.getText().toString() + "");
                    callSignUpApi(json);



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
        }

        return value;
    }

    private void callSignUpApi(Json json) {
        loadingDialog = new LoadingDialog(this);

        Api.newApi(this, P.baseUrl + "register").addJson(json)
                .setMethod(Api.POST)
                .onLoading(isLoading -> {
                    if (!isDestroyed()) {
                        if (isLoading)
                            loadingDialog.show("loading...");
                        else
                            loadingDialog.hide();
                    }

                })
                .onError(() ->
                        MessageBox.showOkMessage(this, "Message", "Registration failed. Please try again", () -> {
                        }))
                .onSuccess(json1 -> {
                    loadingDialog.dismiss();
                    if (json1.getInt(P.status) == 0)
                        H.showMessage(activity, json1.getString(P.err));
                    else {
                        json1 = json1.getJson(P.data);
                        String string = json1.getString(P.token);
                        Session session = new Session(this);
                        String token = json1.getString(P.token);
                        String user_id = json1.getString(P.user_id);
                        session.addString(P.token, token + "");
                        session.addString(P.user_id, user_id + "");
                        session.addString(P.name, json1.getString(P.name) + "");
                        session.addString(P.lastname, json1.getString(P.lastname) + "");
                        session.addString(P.email, json1.getString(P.email) + "");
                        App.authToken = token;
                        App.user_id = user_id;
                        App.startHomeActivity(activity);
                        finish();
                    }
                }).run("hitRegisterApi");
    }

}