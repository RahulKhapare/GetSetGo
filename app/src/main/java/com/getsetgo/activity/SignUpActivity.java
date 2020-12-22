package com.getsetgo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.adoisstudio.helper.H;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivitySignUpBinding;
import com.getsetgo.util.Click;
import com.getsetgo.util.Validation;
import com.getsetgo.util.WindowView;

public class SignUpActivity extends AppCompatActivity {

    private SignUpActivity activity = this;
    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        initView();
    }

    private void initView(){

        onClick();
        //
    }

    private void onClick(){

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
                if (checkValidation()){
                    Intent intent = new Intent(activity,MainActivity.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
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

    private boolean checkValidation(){
        boolean value = true;
        if (TextUtils.isEmpty(binding.etxFirstName.getText().toString().trim())){
            H.showMessage(activity,"Enter first name");
            value = false;
        }else if (TextUtils.isEmpty(binding.etxLastName.getText().toString().trim())){
            H.showMessage(activity,"Enter last name");
            value = false;
        }else if (TextUtils.isEmpty(binding.etxEmailAddress.getText().toString().trim())){
            H.showMessage(activity,"Enter email id");
            value = false;
        }else if (!Validation.validEmail(binding.etxEmailAddress.getText().toString().trim())){
            H.showMessage(activity,"Enter valid email");
            value = false;
        }else if (TextUtils.isEmpty(binding.etxPassword.getText().toString().trim())){
            H.showMessage(activity,"Enter password");
            value = false;
        }else if (TextUtils.isEmpty(binding.etxConfirmPassword.getText().toString().trim())){
            H.showMessage(activity,"Enter conform password");
            value = false;
        }else if (!binding.etxConfirmPassword.getText().toString().trim().equals(binding.etxPassword.getText().toString().trim())){
            H.showMessage(activity,"Confirm password not matched with password");
            value = false;
        }

        return value;
    }

}