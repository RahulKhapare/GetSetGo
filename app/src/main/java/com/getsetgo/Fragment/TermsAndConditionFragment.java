package com.getsetgo.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.getsetgo.Adapter.SupportHelpViewPagerAdapter;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentHelpandsupportBinding;
import com.getsetgo.databinding.FragmentTermsandconditionBinding;

public class TermsAndConditionFragment extends Fragment {

    private FragmentTermsandconditionBinding binding;

    public TermsAndConditionFragment() {
    }

    public static TermsAndConditionFragment newInstance() {
        TermsAndConditionFragment fragment = new TermsAndConditionFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_termsandcondition, container, false);
        View rootView = binding.getRoot();

        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Terms & Condition");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);

        initView();
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void initView() {
        binding.txtExpandTerms.setText(getString(R.string.dummy_text));
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

    private void isCollapse(CheckBox checkBox) {
        checkBox.setChecked(false);
    }


}