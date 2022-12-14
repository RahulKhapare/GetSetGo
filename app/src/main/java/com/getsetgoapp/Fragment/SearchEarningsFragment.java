package com.getsetgoapp.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.databinding.FragmentSearchEarningsBinding;
import com.getsetgoapp.util.Click;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SearchEarningsFragment extends Fragment {

    FragmentSearchEarningsBinding binding;

    public static int CoursePage = 1;
    public static int CrashCoursePage = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_earnings, container, false);
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

    @Override
    public void onStart() {
        super.onStart();

    }

    private void init() {
        CoursePage = 1;
        CrashCoursePage = 1;
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Search Earnings");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
        initCalendar();
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (isFormValidation()) {
                    if (checkDateValidation()) {
                        if (EarningsFragment.pos == 0) {
                            EarningsFragment.isFromBack = true;
                            EarningsFragment.startDate = binding.etStartDate.getText().toString();
                            EarningsFragment.endDate = binding.etEndDate.getText().toString();
                            EarningsFragment.callCourseEarningApi(getActivity());
                            if (getFragmentManager().getBackStackEntryCount() > 0) {
                                getFragmentManager().popBackStackImmediate();
                            }

                        }

                        if (EarningsFragment.pos == 1) {
                            EarningsFragment.isFromBack = true;
                            EarningsFragment.crashstartDate = binding.etStartDate.getText().toString();
                            EarningsFragment.crashendDate = binding.etEndDate.getText().toString();
                            EarningsFragment.callCrashCourseEarningApi(getActivity());
                            if (getFragmentManager().getBackStackEntryCount() > 0) {
                                getFragmentManager().popBackStackImmediate();
                            }
                        }
                    }
                }
            }
        });
        binding.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etStartDate.setText("");
                binding.etEndDate.setText("");
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
                        String sDate = year + "-" + month + "-" + dayOfMonth;
                        binding.etStartDate.setText(sDate);
                    }
                }
                        , year, month, day);

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
                        String sDate = year + "-" + month + "-" + dayOfMonth;
                        binding.etEndDate.setText(formatedDate(sDate));
                    }
                }
                        , eyear, emonth, eday);

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
    }

    private boolean checkDateValidation() {
        boolean value = true;
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
            Toast.makeText(getActivity(), "End Date can't be smaller than Start Date", Toast.LENGTH_SHORT).show();
        }
        return value;
    }

    private String formatedDate(String stringDate) {
        String orderDate = stringDate;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
            Date date = dateFormat.parse(orderDate);
            orderDate = dateFormat.format(date);
        } catch (Exception e) {
        }
        return orderDate;
    }

    public boolean isFormValidation() {
        if (binding.etStartDate.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Start Date should not be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (binding.etEndDate.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "End Date should not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}