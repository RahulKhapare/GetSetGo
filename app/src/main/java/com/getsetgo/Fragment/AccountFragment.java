package com.getsetgo.Fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.getsetgo.R;

import com.getsetgo.activity.BaseScreenActivity;

import com.getsetgo.databinding.FragmentAccountBinding;


public class AccountFragment extends Fragment {

    FragmentAccountBinding binding;


    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();

        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        View rootView = binding.getRoot();

        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("My Account");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
        onClick();


        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public void onClick() {

        binding.llBankDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BankDetailsFragment myFragment = new BankDetailsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).commit();
            }
        });
    }
}