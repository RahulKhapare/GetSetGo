package com.getsetgo.activity;

import androidx.appcompat.app.AppCompatActivity;

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

import com.getsetgo.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;

public class SearchTransactionActivity extends AppCompatActivity {

    Context context;
    TextInputEditText etStartDate,etEndDate;
    Spinner spnActionType,spnIncomeType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_transaction);
        context = SearchTransactionActivity.this;
        init();
    }
    private void init(){

        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        spnActionType = findViewById(R.id.spnActionType);
        spnIncomeType = findViewById(R.id.spnIncomeType);
        bindActionType();
        bindIncomeType();
        initCalendar();

    }
    private void initCalendar(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        String sDate = dayOfMonth + "/" + month+ "/"+ year;
                        etStartDate.setText(sDate);
                    }
                }
                ,year,month,day);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        String sDate = dayOfMonth + "/" + month+ "/"+ year;
                        etEndDate.setText(sDate);
                    }
                }
                ,year,month,day);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
    }

    private void bindActionType() {

        ArrayList<String> stringArrayList = new ArrayList<String>();
        stringArrayList.add("Action Type");
        stringArrayList.add("Text Type");
        stringArrayList.add("Dummy Type");
        stringArrayList.add("Normal Type");


        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(context,
                R.layout.spinner_display_text, stringArrayList);
        spnActionType.setAdapter(stringArrayAdapter);
        spnActionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spnActionType.getSelectedItem() != "Action Type") {
                    Log.d("Tag", "Action Type =" + spnActionType.getSelectedItem().toString());
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


        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(context,
                R.layout.spinner_display_text, stringArrayList);
        spnIncomeType.setAdapter(stringArrayAdapter);
        spnIncomeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spnIncomeType.getSelectedItem() != "Income Type") {
                    Log.d("Tag", "Income Type =" + spnIncomeType.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}