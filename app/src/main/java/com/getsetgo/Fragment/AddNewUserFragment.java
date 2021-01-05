package com.getsetgo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.getsetgo.Adapter.CategoriesCommonAdapter;
import com.getsetgo.Adapter.CountryCodeAdapter;
import com.getsetgo.Model.CountryCode;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentAddNewUserBinding;
import com.getsetgo.databinding.FragmentCategoriesBinding;
import com.getsetgo.util.App;
import com.getsetgo.util.Click;
import com.getsetgo.util.P;
import com.getsetgo.util.Utilities;
import com.getsetgo.util.Validation;

import java.util.ArrayList;

public class AddNewUserFragment extends Fragment {

    FragmentAddNewUserBinding binding;
    Context context;

    public AddNewUserFragment() {
    }

    public static AddNewUserFragment newInstance() {
        AddNewUserFragment fragment = new AddNewUserFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_new_user, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View rootView = binding.getRoot();
        context = inflater.getContext();
        initView();
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if(getFragmentManager().getBackStackEntryCount() > 0){
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    BaseScreenActivity.callBack();
                }
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
        binding.actvIsdCode.setText("+91");
        onClick();

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
        populateIsdCode(getActivity(), binding.actvIsdCode);
        bindStatus();
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
                if (checkValidation(getActivity())) {

                    String registerAs;
                    if(binding.radioCompany.isSelected()){
                        registerAs = "1";
                    }else{
                        registerAs = "0";
                    }
                    Json json = new Json();
                    json.addString("id","");
                    json.addString("registered_as",registerAs);
                    json.addString("name",binding.etFirstName.getText().toString());
                    json.addString("lastname",binding.etLastName.getText().toString());
                    json.addString("email",binding.etEmail.getText().toString());
                    json.addString("country_id","91");
                    json.addString("country_code",binding.actvIsdCode.getText().toString().substring(1));
                    json.addString("contact",binding.etPhone.getText().toString());
                    json.addString("p",binding.etPassword.getText().toString());
                    json.addString("status",binding.spnStatus.getSelectedItem().toString());
                    callAddUserAPI(context,json);
                }
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
                R.layout.fragment_add_new_user, R.id.lbl_name, country);
        autoCompleteTextView.setThreshold(2);
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

    private void bindStatus() {
        ArrayList<String> stringArrayList = new ArrayList<String>();
        stringArrayList.add("Select");
        stringArrayList.add("0");
        stringArrayList.add("1");

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_display_text, stringArrayList);
        binding.spnStatus.setAdapter(stringArrayAdapter);
        binding.spnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (binding.spnStatus.getSelectedItem().toString() != "Select") {
                    Log.d("Tag", "Selected Item is = " + binding.spnStatus.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void clearFields(){
        binding.actvIsdCode.setText("91");
        binding.etFirstName.setText("");
        binding.etLastName.setText("");
        binding.etEmail.setText("");
        binding.etPhone.setText("");
        binding.etPassword.setText("");
        binding.spnStatus.setSelection(0);
    }

    private boolean checkValidation(FragmentActivity activity) {
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
        } else if (binding.actvIsdCode.getText().toString().length() < 2) {
            H.showMessage(activity, "Enter your isd code");
            value = false;
        } else if (TextUtils.isEmpty(binding.etPhone.getText().toString().trim())) {
            H.showMessage(activity, "Enter your phone number");
            value = false;
        } else if (binding.actvIsdCode.getText().toString().equals("+91") &&
                    binding.etPhone.getText().toString().length() != 10) {
            H.showMessage(activity, "Enter valid phone number");
            value = false;
        }  else if (binding.spnStatus.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            H.showMessage(activity, "Please select status");
            value = false;
        } else if (TextUtils.isEmpty(binding.etPassword.getText().toString().trim())) {
            H.showMessage(activity, "Enter password");
            value = false;
        }

        return value;
    }


    private void callAddUserAPI(Context context, Json json) {
        LoadingDialog loadingDialog = new LoadingDialog(context);
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
                        loadingDialog.dismiss();
                        if (Json1.getInt(P.status) == 0) {
                            H.showMessage(context, Json1.getString(P.err));
                        } else {
                            String msg  = Json1.getString(P.msg);
                            Json1 = Json1.getJson(P.data);
                         Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    clearFields();
                                }
                            };
                            Utilities.showPopuprunnable(context,msg,false,runnable);
                        }
                    }

                }).run("add_user");
    }




}