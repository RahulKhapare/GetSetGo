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

public class SearchIncentivesActivity extends AppCompatActivity {

    Context context;
    EditText etDate;
    Spinner spnStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_incentives);
        context = SearchIncentivesActivity.this;
        init();
    }
    private void init(){

        etDate = findViewById(R.id.etDate);
        spnStatus = findViewById(R.id.spnStatus);
        bindStatus();
        initCalendar();

    }
    private void initCalendar(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        String sDate = dayOfMonth + "/" + month+ "/"+ year;
                        etDate.setText(sDate);
                    }
                }
                        ,year,month,day);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

    }

    private void bindStatus() {

        ArrayList<String> stringArrayList = new ArrayList<String>();
        stringArrayList.add("Active");
        stringArrayList.add("Pending");
        stringArrayList.add("Transferred");


        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(context,
                R.layout.spinner_display_text, stringArrayList);
        spnStatus.setAdapter(stringArrayAdapter);
        spnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spnStatus.getSelectedItem() != "Active") {
                    Log.d("Tag", "Active =" + spnStatus.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}