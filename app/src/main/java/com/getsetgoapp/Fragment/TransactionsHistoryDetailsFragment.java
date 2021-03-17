package com.getsetgoapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.adoisstudio.helper.Json;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.databinding.FragmentTransactionsHistoryDetailsBinding;
import com.getsetgoapp.util.Config;

import org.json.JSONException;

public class TransactionsHistoryDetailsFragment extends Fragment {

    FragmentTransactionsHistoryDetailsBinding binding;

    public TransactionsHistoryDetailsFragment() {
    }

    public static TransactionsHistoryDetailsFragment newInstance() {
        TransactionsHistoryDetailsFragment fragment = new TransactionsHistoryDetailsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transactions_history_details, container, false);
        View rootView = binding.getRoot();
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
        init();
        return rootView;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init() {
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
        TransactionsHistoryFragment.isFromSearch = false;

        String jsonData = getArguments().getString("jsonObj");
        try {
            Json json = new Json(jsonData);
            binding.txtTitleName.setText(json.getString("username"));
            binding.txtAmount.setText(json.getString("amount"));
            binding.txtAction.setText(json.getString("action_type"));
            binding.txtParent.setText(json.getString("parent_username"));
            binding.txtCourse.setText(json.getString("courses"));
            binding.txtIncomeType.setText(json.getString("income_type"));
            binding.txtDescription.setText(json.getString("description"));
            binding.txtDateTime.setText(json.getString("create_date_text"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Transactions History");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);

        binding.btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionsHistoryFragment.isFromTransHistory = true;
                getFragmentManager().popBackStackImmediate();
            }
        });

        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    TransactionsHistoryFragment.isFromTransHistory = true;
                    getFragmentManager().popBackStackImmediate();
                }
            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                if(getFragmentManager().getBackStackEntryCount() > 0){
                    TransactionsHistoryFragment.isFromTransHistory = true;
                    getFragmentManager().popBackStackImmediate();

                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        super.onCreate(savedInstanceState);
    }


}