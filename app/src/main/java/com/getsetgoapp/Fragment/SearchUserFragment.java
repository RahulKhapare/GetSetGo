package com.getsetgoapp.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.Session;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.databinding.FragmentSearchUserBinding;
import com.getsetgoapp.util.Click;
import com.getsetgoapp.util.P;

import java.util.ArrayList;
import java.util.Calendar;

public class SearchUserFragment extends Fragment {

    FragmentSearchUserBinding binding;
    public static String crashId = "";
    public static String name = "";
    public static String email = "";
    public static String contact = "";
    public static String has_purchased = "";
    public static String is_affiliate = "";
    public static String start_date = "";
    public static String end_date = "";
    public static String program_service_id = "";
    public static String courseId = "";
    public static String purchase_start_date = "";
    public static String purchase_end_date = "";
    public static String parent_name = "";
    public static String regdPurposeId = "";

    public static int page = 1;
    boolean isFromTotalUser = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_user, container, false);
        View rootView = binding.getRoot();

        isFromTotalUser = getArguments().getBoolean("isTotalUser");

        init();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init() {

        setData();

        page = 1;
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Search User");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
        bindColorType();
        bindPurchaseType();

        initCalendar(binding.etPshFrom);
        initCalendar(binding.etPshTo);
        initCalendar(binding.etRegFrom);
        initCalendar(binding.etRegTo);

        onClick();

        Log.e("TAG", "initDSDSD: "+TotalDirectUsersFragment.directUserJson.toString() );

        if (!isFromTotalUser) {
            if (TotalDirectUsersFragment.directUserJson != null) {
                bindCourseList(TotalDirectUsersFragment.directUserJson.getJsonList("course_list"));
                bindCrashCourseList(TotalDirectUsersFragment.directUserJson.getJsonList("crash_course_list"));
                bindRegdPurposeList(TotalDirectUsersFragment.directUserJson.getJsonList("registration_purpose_list"));
            }
        } else {
            if (TotalUsersFragment.totalUserJson != null) {
                bindCourseList(TotalUsersFragment.totalUserJson.getJsonList("course_list"));
                bindCrashCourseList(TotalUsersFragment.totalUserJson.getJsonList("crash_course_list"));
                bindRegdPurposeList(TotalUsersFragment.totalUserJson.getJsonList("registration_purpose_list"));
            }
        }

    }


    private void setData(){

        binding.etUserName.setText(name);
        binding.etEmailId.setText(email);
        binding.etPhoneNumber.setText(contact);
        binding.etRegFrom.setText(start_date);
        binding.etRegTo.setText(end_date);
        binding.etPshFrom.setText(purchase_start_date);
        binding.etPshTo.setText(purchase_end_date);
        binding.etParentName.setText(parent_name);

    }

    private void onClick() {
        binding.txtSaveNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                getData();
                if (isFromTotalUser) {
                    TotalUsersFragment.isSearch = true;
                    TotalUsersFragment.totalUserJsonList.clear();
                    TotalUsersFragment.callTotalUserApi(getActivity());

                } else {
                    TotalDirectUsersFragment.isSearch = true;
                    TotalDirectUsersFragment.directUserJsonList.clear();
                    TotalDirectUsersFragment.callTotalDirectUserApi(getActivity());
                }
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStackImmediate();
                }
            }
        });

        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getFragmentManager().getBackStackEntryCount() > 0){
                    getFragmentManager().popBackStackImmediate();
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStackImmediate();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        super.onCreate(savedInstanceState);
    }

    private void bindColorType() {
        ArrayList<String> stringArrayList = new ArrayList<String>();
        stringArrayList.add("Select User");
        stringArrayList.add("Red");
        stringArrayList.add("Green");

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_display_text, stringArrayList);
        binding.spnColorType.setAdapter(stringArrayAdapter);

        if (has_purchased.equals("0")){
            binding.spnColorType.setSelection(1);
        }else if (has_purchased.equals("1")){
            binding.spnColorType.setSelection(2);
        }

        binding.spnColorType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (binding.spnColorType.getSelectedItem().toString().equals("Green")) {
                    has_purchased = "1";
                }else if (binding.spnColorType.getSelectedItem().toString().equals("Red")){
                    has_purchased = "0";
                }else {
                    has_purchased = "";
                }

                Log.e("TAG", "onItemSelected_has_purchased: "+ has_purchased );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void bindPurchaseType() {
        ArrayList<String> stringArrayList = new ArrayList<String>();
        stringArrayList.add("Select User Type");
        stringArrayList.add("Affiliate");
        stringArrayList.add("Non-Affiliate");

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_display_text, stringArrayList);
        binding.spnAffUser.setAdapter(stringArrayAdapter);

        if (is_affiliate.equals("0")){
            binding.spnAffUser.setSelection(1);
        }else if (is_affiliate.equals("1")){
            binding.spnAffUser.setSelection(2);
        }

        binding.spnAffUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (binding.spnAffUser.getSelectedItem().toString().equals("Affiliate")) {
                    is_affiliate = "1";
                }else if (binding.spnAffUser.getSelectedItem().toString().equals("Non-Affiliate")){
                    is_affiliate = "0";
                }else {
                    is_affiliate = "";
                }

                Log.e("TAG", "onItemSelected_is_affiliate: "+ is_affiliate );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private String initCalendar(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String sDate = year + "-" + month + "-" + dayOfMonth;
                        editText.setText(sDate);
                    }
                }, year, month, day);

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        return editText.getText().toString();
    }

    private void bindCourseList(JsonList jsonList) {

        int selection = 0;
        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("Select Course");
        for (int i = 0; i < jsonList.size(); i++) {
            Json json = jsonList.get(i);
            stringArrayList.add(json.getString("course_name"));
            if (courseId.equals(json.getString("id"))){
                selection = i+1;
            }
        }


        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_display_text, stringArrayList);
        binding.spnCourse.setAdapter(stringArrayAdapter);
        binding.spnCourse.setSelection(selection);
        binding.spnCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (binding.spnCourse.getSelectedItem().toString() != "Select Course") {
                    Log.d("Tag", "Selected Item is = " + binding.spnCourse.getSelectedItem().toString());
                    for (Json json : jsonList) {
                        if (binding.spnCourse.getSelectedItem().toString().equalsIgnoreCase(json.getString("course_name"))) {
                            courseId = json.getString("id");
                        }
                    }

                }else {
                    courseId = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void bindRegdPurposeList(JsonList jsonList) {
        int selection = 0;
        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("Select Purpose");
        for (int i = 0; i < jsonList.size(); i++) {
            Json json = jsonList.get(i);
            stringArrayList.add(json.getString("purpose_name"));
            if (regdPurposeId.equals(json.getString("id"))){
                selection = i+1;
            }
        }


        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_display_text, stringArrayList);
        binding.spnRegdPurpose.setAdapter(stringArrayAdapter);
        binding.spnRegdPurpose.setSelection(selection);
        binding.spnRegdPurpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (binding.spnRegdPurpose.getSelectedItem().toString() != "Select Purpose") {
                    Log.d("Tag", "Selected Item is = " + binding.spnRegdPurpose.getSelectedItem().toString());
                    for (Json json : jsonList) {
                        if (binding.spnRegdPurpose.getSelectedItem().toString().equalsIgnoreCase(json.getString("purpose_name"))) {
                            regdPurposeId = json.getString("id");
                        }
                    }

                }else {
                    regdPurposeId = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void bindCrashCourseList(JsonList jsonList) {
        int selection = 0;
        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("Select Crash Course");
        for (int i = 0; i < jsonList.size(); i++) {
            Json json = jsonList.get(i);
            stringArrayList.add(json.getString("name"));
            if (program_service_id.equals(json.getString("id"))){
                selection = i+1;
            }
        }

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_display_text, stringArrayList);
        binding.spnCrashCourse.setAdapter(stringArrayAdapter);
        binding.spnCrashCourse.setSelection(selection);
        binding.spnCrashCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (binding.spnCrashCourse.getSelectedItem().toString() != "Select Crash Course") {
                    Log.d("Tag", "Selected Item is = " + binding.spnCrashCourse.getSelectedItem().toString());
                    for (Json json : jsonList) {
                        if (binding.spnCrashCourse.getSelectedItem().toString().equalsIgnoreCase(json.getString("name"))) {
                            crashId = json.getString("id");
                            program_service_id = json.getString("id");
                        }
                    }


                }else {
                    crashId = "";
                    program_service_id = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getData() {
        name = binding.etUserName.getText().toString();
        email = binding.etEmailId.getText().toString();
        contact = binding.etPhoneNumber.getText().toString();
        purchase_start_date = initCalendar(binding.etPshFrom);
        purchase_end_date = initCalendar(binding.etPshTo);
        start_date = initCalendar(binding.etRegTo);
        end_date = initCalendar(binding.etRegFrom);
        parent_name = binding.etParentName.getText().toString();
    }


}