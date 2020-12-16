package com.getsetgo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getsetgo.R;
import com.getsetgo.databinding.ActivityAccountBinding;
import com.getsetgo.databinding.ActivityBankDetailsBinding;
import com.getsetgo.util.WindowView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {

    private AccountActivity accountActivity = this;
    private ActivityAccountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(accountActivity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account);
        init();
    }

    private void init() {
        onClick();
    }


    public void onClick() {


        binding.llBankDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(accountActivity, BankDetailsActivity.class));
            }
        });


    }


}