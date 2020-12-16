package com.getsetgo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.adoisstudio.helper.H;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivityAddNewUserBinding;
import com.getsetgo.databinding.ActivitySignUpBinding;
import com.getsetgo.util.Click;
import com.getsetgo.util.Validation;
import com.getsetgo.util.WindowView;

public class AddNewUserActivity extends AppCompatActivity {

    private AddNewUserActivity activity = this;
    private ActivityAddNewUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_user);
        initView();
    }

    private void initView() {
        onClick();
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

        binding.txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (checkValidation()) {
                }
            }
        });

    }

    private boolean checkValidation() {
        boolean value = true;
        if (TextUtils.isEmpty(binding.etFirstName.getText().toString().trim())) {
            H.showMessage(activity, "Enter first name");
            value = false;
        } else if (TextUtils.isEmpty(binding.etLastName.getText().toString().trim())) {
            H.showMessage(activity, "Enter last name");
            value = false;
        } else if (TextUtils.isEmpty(binding.etEmail.getText().toString().trim())) {
            H.showMessage(activity, "Enter email id");
            value = false;
        } else if (!Validation.validEmail(binding.etEmail.getText().toString().trim())) {
            H.showMessage(activity, "Enter valid email");
            value = false;
        } else if (TextUtils.isEmpty(binding.etIsdCode.getText().toString().trim())) {
            H.showMessage(activity, "Enter Isd Code");
            value = false;
        }else if (TextUtils.isEmpty(binding.etPassword.getText().toString().trim())) {
            H.showMessage(activity, "Enter password");
            value = false;
        }

        return value;
    }

}