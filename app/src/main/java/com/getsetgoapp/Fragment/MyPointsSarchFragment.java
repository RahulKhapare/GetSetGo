package com.getsetgoapp.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.adoisstudio.helper.H;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.databinding.FragmentSearchMyPointsBinding;
import com.getsetgoapp.util.Click;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MyPointsSarchFragment extends Fragment {

    FragmentSearchMyPointsBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_my_points, container, false);
        View rootView = binding.getRoot();
        init();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

    private void init() {

        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Search My Points");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
        bindActionType();
        initCalendar();

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (checkDateValidation()) {
                    MyPointsFragment.MY_POINTS_FILTER = true;
                    MyPointsFragment.START_DATE = binding.etStartDate.getText().toString().trim();
                    MyPointsFragment.END_DATE = binding.etEndDate.getText().toString().trim();
                    if (getFragmentManager().getBackStackEntryCount() > 0) {
                        getFragmentManager().popBackStackImmediate();
                    }
                }
            }
        });

        binding.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPointsFragment.MY_POINTS_FILTER = true;
                binding.etStartDate.setText("");
                binding.etEndDate.setText("");
                binding.spnActionType.setSelection(0);
                MyPointsFragment.START_DATE = "";
                MyPointsFragment.END_DATE = "";
                MyPointsFragment.ACTION_TYPE = "";
            }
        });

        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStackImmediate();
                }
            }
        });

        binding.etStartDate.setText(MyPointsFragment.START_DATE);
        binding.etEndDate.setText(MyPointsFragment.END_DATE);

    }



    private boolean checkDateValidation() {
        boolean value = true;

        if (!TextUtils.isEmpty(binding.etStartDate.getText().toString())
                && !TextUtils.isEmpty(binding.etEndDate.getText().toString())) {

            SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = null;
            Date d2 = null;
            try {
                d1 = dfDate.parse(binding.etStartDate.getText().toString());
                d2 = dfDate.parse(binding.etEndDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (d1.after(d2)) {
                value = false;
                H.showMessage(getActivity(),"End Date can't be smaller than Start Date");
            }
        }
        return value;
    }

    private void initCalendar() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Calendar ecalendar = Calendar.getInstance();
        int eyear = ecalendar.get(Calendar.YEAR);
        int emonth = ecalendar.get(Calendar.MONTH);
        int eday = ecalendar.get(Calendar.DAY_OF_MONTH);

        binding.etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String startDate = year + "-" + month + "-" + dayOfMonth;
                        binding.etStartDate.setText(startDate);
                    }
                }, year, month, day);

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        binding.etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String endDate = year + "-" + month + "-" + dayOfMonth;
                        binding.etEndDate.setText(endDate);
                    }
                }, eyear, emonth, eday);

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

    }

    private void bindActionType() {

        ArrayList<String> stringArrayList = new ArrayList<String>();
        stringArrayList.add("Select");
        stringArrayList.add("D");
        stringArrayList.add("A");

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_display_text, stringArrayList);
        binding.spnActionType.setAdapter(stringArrayAdapter);
        binding.spnActionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    MyPointsFragment.ACTION_TYPE = "";
                }else if (position==1){
                    MyPointsFragment.ACTION_TYPE = "D";
                }else if (position==2){
                    MyPointsFragment.ACTION_TYPE = "A";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (MyPointsFragment.ACTION_TYPE.equals("D")){
            binding.spnActionType.setSelection(1);
        }else if (MyPointsFragment.ACTION_TYPE.equals("A")){
            binding.spnActionType.setSelection(2);
        }

    }

}