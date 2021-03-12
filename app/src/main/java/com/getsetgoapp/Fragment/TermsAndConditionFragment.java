package com.getsetgoapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.databinding.FragmentTermsandconditionBinding;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    BaseScreenActivity.callBack();
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


    private void initView() {
        binding.txtReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.txtReadMore.getText().toString().equalsIgnoreCase(getString(R.string.read_more))) {
                    binding.txtExpandTerms.setMaxLines(Integer.MAX_VALUE);
                    binding.txtReadMore.setText(R.string.read_less);
                } else {
                    binding.txtExpandTerms.setMaxLines(5);
                    binding.txtReadMore.setText(R.string.read_more);
                }
            }
        });
        onClick();
    }

    private void onClick() {

        binding.cbTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.txtExpandTerms.setVisibility(View.VISIBLE);
                    binding.txtReadMore.setVisibility(View.VISIBLE);
                    isCollapse(binding.cbCancellation);
                    isCollapse(binding.cbPrivacy);
                    isCollapse(binding.cbAboutUs);
                } else {
                    binding.txtExpandTerms.setVisibility(View.GONE);
                    binding.txtReadMore.setVisibility(View.GONE);
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

        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    BaseScreenActivity.callBack();
                }
            }
        });
    }

    private void isCollapse(CheckBox checkBox) {
        checkBox.setChecked(false);
    }


}