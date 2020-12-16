package com.getsetgo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.getsetgo.R;
import com.getsetgo.databinding.ActivityBankDetailsBinding;
import com.getsetgo.databinding.ActivitySearchUserBinding;
import com.getsetgo.util.WindowView;

import java.util.ArrayList;

public class SearchUserActivity extends AppCompatActivity {

    private SearchUserActivity activity = this;
    private ActivitySearchUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_user);
        init();
    }

    private void init() {
        bindColorType();
    }

    private void bindColorType() {

        ArrayList<String> stringArrayList = new ArrayList<String>();
        stringArrayList.add("Select");
        stringArrayList.add("Red");
        stringArrayList.add("Green");
        stringArrayList.add("Blue");
        stringArrayList.add("White");


        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(activity,
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
}