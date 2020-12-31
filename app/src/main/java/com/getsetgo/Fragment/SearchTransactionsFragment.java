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
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentSearchIncentivesBinding;
import com.getsetgo.databinding.FragmentSearchTransactionBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SearchTransactionsFragment extends Fragment {

    FragmentSearchTransactionBinding binding;

    public static int transPage = 1;
    public static int CrashPage = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_transaction, container, false);
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
        transPage = 1;
        CrashPage = 1;
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Search Transaction");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
        bindActionType();
        bindIncomeType();
        initCalendar();

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkDateValidation()) {
                    if (TransactionsHistoryFragment.pos == 1) {
                        TransactionsHistoryFragment.isFromSearch = true;
                        getCrashData();
                        TransactionsHistoryFragment.callCrashTransactionApi(getActivity());
                        if (getFragmentManager().getBackStackEntryCount() > 0) {
                            getFragmentManager().popBackStackImmediate();
                        }
                    } else {
                        getTransData();
                        TransactionsHistoryFragment.isFromSearch = true;
                        AllTransactionsFragment.callTransactionHistoryApi(getActivity());
                        if (getFragmentManager().getBackStackEntryCount() > 0) {
                            getFragmentManager().popBackStackImmediate();
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
                binding.spnActionType.setSelection(0);
                binding.spnIncomeType.setSelection(0);
            }
        });
    }

    private void getTransData() {
        AllTransactionsFragment.startDate = binding.etStartDate.getText().toString();
        AllTransactionsFragment.endDate = binding.etEndDate.getText().toString();
        if (binding.spnActionType.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            AllTransactionsFragment.actionType = "";
        } else {
            AllTransactionsFragment.actionType = binding.spnActionType.getSelectedItem().toString();
        }

        if (binding.spnIncomeType.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            AllTransactionsFragment.incomeType = "";
        } else {
            AllTransactionsFragment.incomeType = binding.spnIncomeType.getSelectedItem().toString();
        }
    }

    private void getCrashData() {
        TransactionsHistoryFragment.crashstartDate = binding.etStartDate.getText().toString();
        TransactionsHistoryFragment.crashendDate = binding.etEndDate.getText().toString();
        if (binding.spnActionType.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            TransactionsHistoryFragment.actionType = "";
        } else {
            TransactionsHistoryFragment.actionType = binding.spnActionType.getSelectedItem().toString();
        }

        if (binding.spnIncomeType.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            TransactionsHistoryFragment.incomeType = "";
        } else {
            TransactionsHistoryFragment.incomeType = binding.spnIncomeType.getSelectedItem().toString();
        }
    }

    private boolean checkDateValidation() {
        boolean value = true;

        if (!binding.etStartDate.getText().toString().isEmpty()
                || !binding.etEndDate.getText().toString().isEmpty()) {

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
                if (binding.spnActionType.getSelectedItem() != "Action Type") {
                    Log.d("Tag", "Action Type =" + binding.spnActionType.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void bindIncomeType() {

        ArrayList<String> stringArrayList = new ArrayList<String>();
        stringArrayList.add("Select");
        stringArrayList.add("T");
        stringArrayList.add("RF");
        stringArrayList.add("MF");


        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_display_text, stringArrayList);
        binding.spnIncomeType.setAdapter(stringArrayAdapter);
        binding.spnIncomeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (binding.spnIncomeType.getSelectedItem() != "Income Type") {
                    Log.d("Tag", "Income Type =" + binding.spnIncomeType.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


}