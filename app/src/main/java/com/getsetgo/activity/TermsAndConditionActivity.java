package com.getsetgo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.getsetgo.R;
import com.getsetgo.databinding.ActivityTermsAndConditionBinding;
import com.getsetgo.databinding.ActivityTotalUserBinding;
import com.getsetgo.util.WindowView;

public class TermsAndConditionActivity extends AppCompatActivity {

    private TermsAndConditionActivity activity = this;
    private ActivityTermsAndConditionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_terms_and_condition);
        init();
    }

    private void init() {
        onClick();
    }

    private void onClick() {

        binding.cbTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.txtExpandTerms.setVisibility(View.VISIBLE);
                    isCollapse(binding.cbCancellation);
                    isCollapse(binding.cbPrivacy);
                    isCollapse(binding.cbAboutUs);
                } else {
                    binding.txtExpandTerms.setVisibility(View.GONE);
                }
            }
        });
        binding.cbPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.txtExpandPrivacy.setVisibility(View.VISIBLE);
                    isCollapse(binding.cbCancellation);
                    isCollapse(binding.cbAboutUs);
                    isCollapse(binding.cbTerms);
                } else {
                    binding.txtExpandPrivacy.setVisibility(View.GONE);
                }
            }
        });

        binding.cbCancellation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.txtExpandCancellation.setVisibility(View.VISIBLE);
                    isCollapse(binding.cbAboutUs);
                    isCollapse(binding.cbPrivacy);
                    isCollapse(binding.cbTerms);
                } else {
                    binding.txtExpandCancellation.setVisibility(View.GONE);
                }
            }
        });

        binding.cbAboutUs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.txtExpandAboutUs.setVisibility(View.VISIBLE);
                    isCollapse(binding.cbCancellation);
                    isCollapse(binding.cbPrivacy);
                    isCollapse(binding.cbTerms);
                } else {
                    binding.txtExpandAboutUs.setVisibility(View.GONE);
                }
            }
        });
    }

    private void isCollapse(CheckBox checkBox){
        checkBox.setChecked(false);
    }
}