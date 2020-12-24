package com.getsetgo.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgo.Adapter.CategoriesCommonAdapter;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentCategoriesBinding;

public class CategoriesFragment extends Fragment {

    ViewAllCategoriesFragment viewAllCategoriesFragment;
    FragmentCategoriesBinding binding;


    public CategoriesFragment() {
    }

    public static CategoriesFragment newInstance() {
        CategoriesFragment fragment = new CategoriesFragment();
        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_categories, container, false);
        View rootView = binding.getRoot();
        init();
        BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private void init() {
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Categories");
        binding.txtSmartSchoolViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(view,binding.txtSmartSchool.getText().toString());
            }
        });

        binding.txtArtViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(view,binding.txtArt.getText().toString());
            }
        });

        binding.txtCreInnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(view,binding.txtCreInn.getText().toString());
            }
        });

        binding.txtProfessionalViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(view,binding.txtProfessional.getText().toString());
            }
        });

        binding.txtTechnologyViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(view,binding.txtTechnology.getText().toString());
            }
        });
        setupRecyclerViewForCategories(binding.recyclerViewSmartSchool);
        setupRecyclerViewForCategories(binding.recyclerViewProfessional);
        setupRecyclerViewForCategories(binding.recyclerViewArt);
        setupRecyclerViewForCategories(binding.recyclerViewTechnology);
        setupRecyclerViewForCategories(binding.recyclerViewCreInn);
    }

    private void setupRecyclerViewForCategories(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        CategoriesCommonAdapter commonAdapter = new CategoriesCommonAdapter(getActivity());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(commonAdapter);
        commonAdapter.notifyDataSetChanged();
    }

    private void loadFragment(View v,String title){
        Bundle bundle = new Bundle();
        bundle.putString("subTitle", title);
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        viewAllCategoriesFragment = new ViewAllCategoriesFragment();
        viewAllCategoriesFragment.setArguments(bundle);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, viewAllCategoriesFragment)
                .addToBackStack(null)
                .commit();
    }



}