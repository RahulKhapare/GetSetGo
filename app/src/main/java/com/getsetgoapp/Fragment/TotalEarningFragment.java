package com.getsetgoapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.adoisstudio.helper.Json;
import com.getsetgoapp.R;
import com.getsetgoapp.databinding.FragmentTotalearningBinding;
import com.getsetgoapp.util.P;

public class TotalEarningFragment extends Fragment {

    public static FragmentTotalearningBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_totalearning, container, false);
        View rootView = binding.getRoot();
        init(rootView);
        return rootView;

    }

    private void init(View view) {
        setUpTotalIncome(EarningsFragment.totalEarnJson);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public static void setUpTotalIncome(Json json){
        if(json != null) {
            int refIncome = json.getInt(P.referral_income);
            int eventIncome = json.getInt(P.event_income);
            binding.txtIncome.setText(String.valueOf(refIncome));
            binding.txtCrash.setText(String.valueOf(eventIncome));
        }
    }


}