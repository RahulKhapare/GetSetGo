package com.getsetgo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.adoisstudio.helper.H;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivityLoginBinding;
import com.getsetgo.util.Click;
import com.getsetgo.util.Validation;
import com.getsetgo.util.WindowView;

public class LoginActivity extends AppCompatActivity {

    private LoginActivity activity = this;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initView();
    }

    private void initView(){

        onClick();
    }

    private void onClick(){

        binding.txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });
        binding.txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                //if (checkValidation()){
                    Intent intent = new Intent(activity,HomeScreenActivity.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                //}
            }
        });
        binding.txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Intent intent = new Intent(activity,SignUpActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
            }
        });
    }

    private boolean checkValidation(){
        boolean value = true;

        if (TextUtils.isEmpty(binding.etxEmailAddress.getText().toString().trim())){
            H.showMessage(activity,"Enter email id");
            value = false;
        }else if (!Validation.validEmail(binding.etxEmailAddress.getText().toString().trim())){
            H.showMessage(activity,"Enter valid email");
            value = false;
        }else if (TextUtils.isEmpty(binding.etxPassword.getText().toString().trim())){
            H.showMessage(activity,"Enter password");
            value = false;
        }

        return value;
    }
}