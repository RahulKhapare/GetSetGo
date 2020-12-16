package com.getsetgo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.getsetgo.R;
import com.getsetgo.databinding.ActivityBankDetailsBinding;
import com.getsetgo.databinding.ActivityLoginBinding;
import com.getsetgo.util.WindowView;

public class BankDetailsActivity extends AppCompatActivity {

    private BankDetailsActivity bankDetailsActivity = this;
    private ActivityBankDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(bankDetailsActivity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bank_details);
        init();
    }
    private void init(){
        onClick();
    }
    private void onClick(){
        binding.txtSaveNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(bankDetailsActivity,UploadDocumentsActivity.class));
            }
        });
    }
}