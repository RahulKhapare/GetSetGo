package com.getsetgo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.getsetgo.R;
import com.getsetgo.databinding.ActivityBankDetailsBinding;
import com.getsetgo.databinding.ActivityUploadDocsBinding;
import com.getsetgo.util.WindowView;

public class UploadDocumentsActivity extends AppCompatActivity {

    private UploadDocumentsActivity uploadDocumentsActivity = this;
    private ActivityUploadDocsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(uploadDocumentsActivity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upload_docs);
        init();
    }

    private void init() {
        onClick();
    }

    private void onClick() {
        binding.txtSaveNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}