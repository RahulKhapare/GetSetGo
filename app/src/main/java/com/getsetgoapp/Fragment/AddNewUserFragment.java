package com.getsetgoapp.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.adoisstudio.helper.Session;
import com.getsetgoapp.Adapter.CountryCodeSelectionAdapter;
import com.getsetgoapp.Model.CountryCodeModel;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.activity.SplashActivity;
import com.getsetgoapp.databinding.FragmentAddNewUserBinding;
import com.getsetgoapp.util.App;
import com.getsetgoapp.util.Click;
import com.getsetgoapp.util.JumpToLogin;
import com.getsetgoapp.util.P;
import com.getsetgoapp.util.Validation;

import java.util.ArrayList;
import java.util.List;

public class AddNewUserFragment extends Fragment {

    FragmentAddNewUserBinding binding;
    Context context;
    private String countryCode;
    private String countryID;
    private String userStatus;


    public AddNewUserFragment() {
    }

    public static AddNewUserFragment newInstance() {
        AddNewUserFragment fragment = new AddNewUserFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_new_user, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View rootView = binding.getRoot();
        context = inflater.getContext();
        initView();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                onBackClick();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                onBackClick();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView() {
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Add New User");

        List<CountryCodeModel> countryCodeModelList = new ArrayList<>();

        CountryCodeModel model1 = new CountryCodeModel();
        model1.setCountry_id("");
        model1.setCountry_code("");
        model1.setCountry_name("");
        model1.setCountry_shortname("Eg. (+91)");
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

        CountryCodeSelectionAdapter adapter = new CountryCodeSelectionAdapter(context, countryCodeModelList,1);
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

        List<CountryCodeModel> statusList = new ArrayList<>();
        statusList.add(new CountryCodeModel("User Status","-"));
        statusList.add(new CountryCodeModel("Active","1"));
        statusList.add(new CountryCodeModel("Inactive","0"));

        CountryCodeSelectionAdapter statusAdapter = new CountryCodeSelectionAdapter(context, statusList,2);
        binding.spinnerActive.setAdapter(statusAdapter);
        binding.spinnerActive.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryCodeModel model = countryCodeModelList.get(position);
                userStatus = model.getCountry_code();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        onClick();

    }

    private void onClick() {


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

        binding.txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (checkValidation()) {
                    String registerAs;
                    if (binding.radioIndividual.isChecked()) {
                        registerAs = "1";
                    } else {
                        registerAs = "2";
                    }
                    Json json = new Json();
                    json.addString(P.id, new Session(context).getString(P.user_id));
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
                    json.addString(P.p, binding.etxPassword.getText().toString() + "");
                    json.addString(P.status, userStatus);

                    callAddUserAPI(context,json);
                }
            }
        });


        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackClick();
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
                H.showMessage(context, "Enter company name");
                value = false;
            } else {
                value = performCheck(value);
            }
        } else if (binding.radioIndividual.isChecked()) {
            if (TextUtils.isEmpty(binding.etxFirstName.getText().toString().trim())) {
                H.showMessage(context, "Enter first name");
                value = false;
            } else if (TextUtils.isEmpty(binding.etxLastName.getText().toString().trim())) {
                H.showMessage(context, "Enter last name");
                value = false;
            } else {
                value = performCheck(value);
            }
        }
        return value;
    }

    private boolean performCheck(boolean value) {
        if (TextUtils.isEmpty(binding.etxEmailAddress.getText().toString().trim())) {
            H.showMessage(context, "Enter email id");
            value = false;
        }
        else if (!Validation.validEmail(binding.etxEmailAddress.getText().toString().trim())) {
            H.showMessage(context, "Enter valid email");
            value = false;
        }
        else if (TextUtils.isEmpty(countryCode)) {
            H.showMessage(context, "Select your ISD code");
            value = false;
        } else if (TextUtils.isEmpty(binding.etxPhone.getText().toString().trim())) {
            H.showMessage(context, "Enter your phone number");
            value = false;
        } else if (countryCode.equals("91") &&
                binding.etxPhone.getText().toString().length() != 10) {
            H.showMessage(context, "Enter valid phone number");
            value = false;
        } else if (TextUtils.isEmpty(binding.etxPassword.getText().toString().trim())) {
            H.showMessage(context, "Enter password");
            value = false;
        } else if (binding.etxPassword.getText().toString().length() < 3) {
            H.showMessage(context, "Please enter valid password");
            value = false;
        }else if (TextUtils.isEmpty(userStatus) || userStatus.equals("0")){
            H.showMessage(context, "Please select user status");
            value = false;
        }
        return value;
    }



    private void callAddUserAPI(Context context, Json json) {
        LoadingDialog loadingDialog = new LoadingDialog(context,false);
        Api.newApi(context, P.baseUrl + "add_user").addJson(json)
                .setMethod(Api.POST)
                .onHeaderRequest(App::getHeaders)
                .onLoading(isLoading -> {
                    if (isLoading)
                        loadingDialog.show("loading...");
                    else
                        loadingDialog.hide();
                })
                .onError(() ->
                        MessageBox.showOkMessage(context, "Message", "Failed to login. Please try again", () -> {
                            loadingDialog.dismiss();
                        }))
                .onSuccess(Json1 -> {
                    if (Json1 != null) {
                        JumpToLogin.call(Json1,context);
                        loadingDialog.dismiss();
                        if (Json1.getInt(P.status) == 1) {
                            H.showMessage(context, Json1.getString(P.msg));
                            updateData();
                            final Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    onBackClick();
                                }
                            }, 500);
                        } else {
                            H.showMessage(context, Json1.getString(P.err));
                        }
                    }

                }).run("add_user");
    }

    private void updateData(){
        binding.radioIndividual.setChecked(true);
        visibleIndividual();
        binding.etxFirstName.setText("");
        binding.etxLastName.setText("");
        binding.etxCompanyName.setText("");
        binding.etxEmailAddress.setText("");
        binding.etxPhone.setText("");
        binding.etxPassword.setText("");
        binding.spinnerCode.setSelection(0);
        binding.spinnerActive.setSelection(0);

    }
    private void onBackClick(){
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getActivity().getFragmentManager().popBackStack();
            BaseScreenActivity.callBack();
        }
    }

}