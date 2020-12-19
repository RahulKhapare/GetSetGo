package com.getsetgo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.getsetgo.R;
import com.getsetgo.databinding.ActivityTransactionHistoryDetailsBinding;
import com.getsetgo.util.WindowView;

public class TransactionHistoryDetails extends AppCompatActivity {


    TransactionHistoryDetails activity = this;
    ActivityTransactionHistoryDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction_history_details);
        init();
    }

    private void init() {


        binding.btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}