package com.getsetgo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.getsetgo.R;
import com.getsetgo.databinding.ActivitySearchTransactionBinding;
import com.getsetgo.util.WindowView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class SearchTransactionActivity extends AppCompatActivity {

    SearchTransactionActivity activity = this;
    ActivitySearchTransactionBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_transaction);
        init();
    }

    private void init() {
        bindActionType();
        bindIncomeType();
        initCalendar();

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormValidation()) {
                    if (checkDateValidation()) {

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
            Toast.makeText(activity, "End Date can't be smaller than Start Date", Toast.LENGTH_SHORT).show();
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String startDate = year + "-" + month + "-" + dayOfMonth;
                        binding.etStartDate.setText(startDate);
                    }
                }, year, month, day);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        binding.etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String endDate = year + "-" + month + "-" + dayOfMonth;
                        binding.etEndDate.setText(endDate);
                    }
                }, eyear, emonth, eday);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

    }


    public boolean isFormValidation() {
        if (binding.etStartDate.getText().toString().isEmpty()) {
            Toast.makeText(activity, "Start Date should not be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (binding.etEndDate.getText().toString().isEmpty()) {
            Toast.makeText(activity, "End Date should not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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

    private void bindActionType() {

        ArrayList<String> stringArrayList = new ArrayList<String>();
        stringArrayList.add("Action Type");
        stringArrayList.add("Income Type");
        stringArrayList.add("Text Type");
        stringArrayList.add("Dummy Type");
        stringArrayList.add("Normal Type");


        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(activity,
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
        stringArrayList.add("Income Type");
        stringArrayList.add("Text Type");
        stringArrayList.add("Dummy Type");
        stringArrayList.add("Normal Type");


        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(activity,
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