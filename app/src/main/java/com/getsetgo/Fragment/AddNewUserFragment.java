package com.getsetgo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.H;
import com.getsetgo.Adapter.CategoriesCommonAdapter;
import com.getsetgo.Adapter.CountryCodeAdapter;
import com.getsetgo.Model.CountryCode;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentAddNewUserBinding;
import com.getsetgo.databinding.FragmentCategoriesBinding;
import com.getsetgo.util.Click;
import com.getsetgo.util.Validation;

import java.util.ArrayList;

public class AddNewUserFragment extends Fragment {

    FragmentAddNewUserBinding binding;

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
        View rootView = binding.getRoot();
        initView();
        return rootView;
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
                }
            }
        });
    }

    public void populateIsdCode(Context context, AutoCompleteTextView autoCompleteTextView) {
        final ArrayList<CountryCode> country = new ArrayList<>();
        CountryCode codes = new CountryCode();
        codes.setCode("+92");
        CountryCode codess = new CountryCode();
        codess.setCode("+93");
        CountryCode codeess = new CountryCode();
        codeess.setCode("+94");
        CountryCode coddess = new CountryCode();
        coddess.setCode("+95");
        country.add(codes);
        country.add(coddess);
        country.add(codeess);
        country.add(codess);
        CountryCodeAdapter countryFlagAdapter = new CountryCodeAdapter(context,
                R.layout.fragment_add_new_user, R.id.lbl_name, country);
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
        } else if (TextUtils.isEmpty(binding.actvIsdCode.getText().toString().trim())) {
            H.showMessage(activity, "Enter Isd Code");
            value = false;
        } else if (TextUtils.isEmpty(binding.etPassword.getText().toString().trim())) {
            H.showMessage(activity, "Enter password");
            value = false;
        }

        return value;
    }




}