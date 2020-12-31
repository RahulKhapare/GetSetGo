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
import com.getsetgo.databinding.FragmentSearchEarningsBinding;
import com.getsetgo.databinding.FragmentSearchIncentivesBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SearchIncentivesFragment extends Fragment {

    FragmentSearchIncentivesBinding binding;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_search_incentives, container, false);
        View rootView = binding.getRoot();
        init();
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                if(getFragmentManager().getBackStackEntryCount() > 0){
                    getFragmentManager().popBackStackImmediate();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private void init(){
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Search Incentives");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
        bindStatus();
        initCalendar();

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFormValidation()){

                }
            }
        });

    }

    public boolean isFormValidation() {
        if (binding.etDate.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Date should not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }else if(binding.spnStatus.getSelectedItem().toString().equalsIgnoreCase("Select")){
            Toast.makeText(getActivity(), "Please select status", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    private void initCalendar(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        binding.etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        String sDate = dayOfMonth + "/" + month+ "/"+ year;
                        binding.etDate.setText(sDate);
                    }
                }
                        ,year,month,day);

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

    }

    private void bindStatus() {

        ArrayList<String> stringArrayList = new ArrayList<String>();
        stringArrayList.add("Active");
        stringArrayList.add("Pending");
        stringArrayList.add("Transferred");


        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_display_text, stringArrayList);
        binding.spnStatus.setAdapter(stringArrayAdapter);
        binding.spnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (binding.spnStatus.getSelectedItem() != "Active") {
                    Log.d("Tag", "Active =" + binding.spnStatus.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


}