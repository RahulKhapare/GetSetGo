package com.getsetgo.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.getsetgo.Adapter.SpinnerSelectionAdapter;
import com.getsetgo.Model.SpinnerModel;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentNomineeDocumentBinding;

import java.util.ArrayList;
import java.util.List;

public class NomineeDetailsFragment extends Fragment {

    private FragmentNomineeDocumentBinding binding;

    private List<SpinnerModel> nomineeOneList;
    SpinnerSelectionAdapter nomineeOneAdapter;
    private List<SpinnerModel> nomineeTwoList;
    SpinnerSelectionAdapter nomineeTwoAdapter;



    public NomineeDetailsFragment() {
    }

    public static NomineeDetailsFragment newInstance() {
        NomineeDetailsFragment fragment = new NomineeDetailsFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                if(getFragmentManager().getBackStackEntryCount() > 0){
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    BaseScreenActivity.callBack();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nominee_document, container, false);
        View rootView = binding.getRoot();

        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Nominee Documents");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);

        init();

        return rootView;
    }

    private void init(){

        nomineeOneList = new ArrayList<>();
        SpinnerModel model1 = new SpinnerModel();
        model1.setId("");
        model1.setName("Select Relation");
        nomineeOneList.add(model1);
        nomineeOneAdapter = new SpinnerSelectionAdapter(getActivity(), nomineeOneList);
        binding.spinnerNomineeOne.setAdapter(nomineeOneAdapter);

        nomineeTwoList = new ArrayList<>();
        SpinnerModel model2 = new SpinnerModel();
        model2.setId("");
        model2.setName("Select Relation");
        nomineeTwoList.add(model2);
        nomineeTwoAdapter = new SpinnerSelectionAdapter(getActivity(), nomineeTwoList);
        binding.spinnerNomineeTwo.setAdapter(nomineeTwoAdapter);

        onClick();
    }


    private void onClick(){

        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    BaseScreenActivity.callBack();
                }
            }
        });

        binding.spinnerNomineeOne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerModel model = nomineeOneList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.spinnerNomineeTwo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerModel model = nomineeTwoList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


}
