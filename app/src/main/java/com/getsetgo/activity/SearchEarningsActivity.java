package com.getsetgo.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.getsetgo.R;
import com.getsetgo.databinding.ActivitySearchEarningsBinding;
import com.getsetgo.databinding.ActivitySearchTransactionBinding;
import com.getsetgo.util.WindowView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SearchEarningsActivity extends AppCompatActivity {

    SearchEarningsActivity activity = this;
    ActivitySearchEarningsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_earnings);
        init();
    }
    private void init(){

        initCalendar();
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormValidation()) {
                    if(checkDateValidation()){

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
    }
    private void initCalendar(){
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
                        month = month+1;
                        String sDate = year + "-" + month + "-" + dayOfMonth;
                        binding.etStartDate.setText(sDate);
                    }
                }
                ,year,month,day);

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
                        month = month+1;
                        String sDate = year + "-" + month + "-" + dayOfMonth;
                        binding.etEndDate.setText(formatedDate(sDate));
                    }
                }
                ,eyear,emonth,eday);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
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
            Toast.makeText(activity, "End Date can't be smaller than Start Date", Toast.LENGTH_SHORT).show();
        }
        return value;
    }

    private String formatedDate(String stringDate){
        String orderDate = stringDate;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
            Date date = dateFormat.parse(orderDate);
            orderDate = dateFormat.format(date);
        }catch (Exception e){
        }
        return orderDate;
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
}