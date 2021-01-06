package com.getsetgo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.getsetgo.Adapter.CountryCodeAdapter;
import com.getsetgo.Model.CountryCode;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivitySignUpBinding;
import com.getsetgo.util.App;
import com.getsetgo.util.Click;
import com.getsetgo.util.P;
import com.getsetgo.util.Utilities;
import com.getsetgo.util.Validation;
import com.getsetgo.util.WindowView;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    private final SignUpActivity activity = this;
    private ActivitySignUpBinding binding;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initView();
    }

    private void initView() {
        onClick();
        binding.actvIsdCode.setText("+91");
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
                    Intent intent = new Intent(activity, BaseScreenActivity.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    finish();
                    String registerAs;
                    if(binding.radioCompany.isSelected()){
                        registerAs = binding.radioCompany.getText().toString();
                    }else{
                        registerAs = binding.radioIndividual.getText().toString();
                    }
                    Json json = new Json();
                    json.addString("registered_as", registerAs);
                    json.addString(P.name, binding.etxFirstName.getText().toString() + "");
                    json.addString(P.lastname, binding.etxLastName.getText().toString() + "");
                    json.addString(P.email, binding.etxEmailAddress.getText().toString() + "");
                    json.addString(P.contact, binding.etxPhone.getText().toString() + "");
                    json.addString(P.password, binding.etxPassword.getText().toString() + "");
                    json.addString(P.confirm_password, binding.etxConfirmPassword.getText().toString() + "");
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
        }

        return value;
    }

    private void callSignUpApi(Json json) {
        loadingDialog = new LoadingDialog(this,false);

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
                        Session session = new Session(this);
                        String token = json1.getString(P.token);
                        String user_id = json1.getString(P.user_id);
                        session.addString(P.token, token + "");
                        session.addString(P.user_id, user_id + "");
                        session.addString(P.name, json1.getString(P.name) + "");
                        session.addString(P.lastname, json1.getString(P.lastname) + "");
                        session.addString(P.email, json1.getString(P.email) + "");
                        session.addString(P.contact, json1.getString(P.contact) + "");
                        App.authToken = token;
                        App.user_id = user_id;
                        App.startHomeActivity(activity);
                        finish();
                    }
                }).run("register");
    }

}