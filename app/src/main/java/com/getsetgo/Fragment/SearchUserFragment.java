package com.getsetgo.Fragment;

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

import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentSearchUserBinding;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_user, container, false);
        View rootView = binding.getRoot();
        init();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init() {
        page = 1;
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Search User");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
        bindColorType();

        initCalendar(binding.etPshFrom);
        initCalendar(binding.etPshTo);
        initCalendar(binding.etRegFrom);
        initCalendar(binding.etRegTo);

        onClick();

        if (TotalUsersFragment.totalUserJson != null) {
            bindCourseList(TotalUsersFragment.totalUserJson.getJsonList("course_list"));
            bindCrashCourseList(TotalUsersFragment.totalUserJson.getJsonList("crash_course_list"));
            bindRegdPurposeList(TotalUsersFragment.totalUserJson.getJsonList("registration_purpose_list"));
        }
    }

    private void onClick() {
        binding.txtSaveNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TotalUsersFragment.isSearch = true;
                getData();
                TotalUsersFragment.totalUserJsonList.clear();
                TotalUsersFragment.callTotalUserApi(getActivity());
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStackImmediate();
                }
                //startActivity(new Intent(getActivity(), SearchUserIdActivity.class));
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
        stringArrayList.add("Select");
        stringArrayList.add("Red");
        stringArrayList.add("Green");

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_display_text, stringArrayList);
        binding.spnColorType.setAdapter(stringArrayAdapter);
        binding.spnColorType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (binding.spnColorType.getSelectedItem().toString() != "Green") {
                    Log.d("Tag", "Selected Item is = " + binding.spnColorType.getSelectedItem().toString());
                }
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

        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("Select");
        for (int i = 0; i < jsonList.size(); i++) {
            Json json = jsonList.get(i);
            stringArrayList.add(json.getString("course_name"));
        }


        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_display_text, stringArrayList);
        binding.spnCourse.setAdapter(stringArrayAdapter);
        binding.spnCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (binding.spnCourse.getSelectedItem().toString() != "Select") {
                    Log.d("Tag", "Selected Item is = " + binding.spnCourse.getSelectedItem().toString());
                    for (Json json : jsonList) {
                        if (binding.spnCourse.getSelectedItem().toString().equalsIgnoreCase(json.getString("course_name"))) {
                            courseId = json.getString("id");
                        }
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void bindRegdPurposeList(JsonList jsonList) {

        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("Select");
        for (int i = 0; i < jsonList.size(); i++) {
            Json json = jsonList.get(i);
            stringArrayList.add(json.getString("purpose_name"));
        }


        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_display_text, stringArrayList);
        binding.spnRegdPurpose.setAdapter(stringArrayAdapter);
        binding.spnRegdPurpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (binding.spnRegdPurpose.getSelectedItem().toString() != "Select") {
                    Log.d("Tag", "Selected Item is = " + binding.spnRegdPurpose.getSelectedItem().toString());
                    for (Json json : jsonList) {
                        if (binding.spnRegdPurpose.getSelectedItem().toString().equalsIgnoreCase(json.getString("purpose_name"))) {
                            regdPurposeId = json.getString("id");
                        }
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void bindCrashCourseList(JsonList jsonList) {

        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("Select");
        for (int i = 0; i < jsonList.size(); i++) {
            Json json = jsonList.get(i);
            stringArrayList.add(json.getString("name"));
        }


        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_display_text, stringArrayList);
        binding.spnCrashCourse.setAdapter(stringArrayAdapter);
        binding.spnCrashCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (binding.spnCrashCourse.getSelectedItem().toString() != "Select") {
                    Log.d("Tag", "Selected Item is = " + binding.spnCrashCourse.getSelectedItem().toString());
                    for (Json json : jsonList) {
                        if (binding.spnCrashCourse.getSelectedItem().toString().equalsIgnoreCase(json.getString("name"))) {
                            crashId = json.getString("id");
                        }
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getData(){
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