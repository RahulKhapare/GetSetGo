package com.getsetgo.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.Nullable;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgo.Adapter.ActiveCourseAdapter;
import com.getsetgo.Adapter.BestSellingCourseAdapter;
import com.getsetgo.Adapter.OtherCategoriesAdapter;
import com.getsetgo.R;

public class HomeFragment extends Fragment {


    public ImageView ivNotify, ivMenu, ivCourseImage;
    public TextView txtTech, txtDesciption, txtProgramme, txtStatus, txtViewAll, txtViewAllBestCourse;
    public RecyclerView recyclerViewCources, recyclerViewOtherCategories, recyclerBestSellingCources;
    CardView cardViewCurrentLearning;
    ActiveCourseAdapter activeCourseAdapter;
    OtherCategoriesAdapter otherCategoriesAdapter;
    BestSellingCourseAdapter bestSellingCourseAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_home, container, false);

        init(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init(View view) {
        ivNotify = view.findViewById(R.id.ivNotify);
        ivMenu = view.findViewById(R.id.ivMenu);
        ivCourseImage = view.findViewById(R.id.imvCourse);
        txtTech = view.findViewById(R.id.textTech);
        txtDesciption = view.findViewById(R.id.txtDes);
        txtProgramme = view.findViewById(R.id.txtProgramme);
        txtViewAll = view.findViewById(R.id.txtViewAll);
        txtStatus = view.findViewById(R.id.txtStatus);

        recyclerViewCources = view.findViewById(R.id.recyclerViewCources);
        txtViewAllBestCourse = view.findViewById(R.id.txtViewAllBestCourse);
        cardViewCurrentLearning = view.findViewById(R.id.cardViewCurrentLearning);
        recyclerBestSellingCources = view.findViewById(R.id.recyclerBestSellingCources);
        recyclerViewOtherCategories = view.findViewById(R.id.recyclerViewOtherCategories);


        setupRecyclerViewForActiveCourse();
        setupRecyclerViewForOthersCategories();
        setupRecyclerViewForBestSellingCourse();


    }

    private void setupRecyclerViewForActiveCourse() {
        recyclerViewCources.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        activeCourseAdapter = new ActiveCourseAdapter(getActivity());
        recyclerViewCources.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCources.setAdapter(activeCourseAdapter);
        activeCourseAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerViewForOthersCategories() {
        recyclerViewOtherCategories.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        otherCategoriesAdapter = new OtherCategoriesAdapter(getActivity());
        recyclerViewOtherCategories.setItemAnimator(new DefaultItemAnimator());
        recyclerViewOtherCategories.setAdapter(otherCategoriesAdapter);
        otherCategoriesAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerViewForBestSellingCourse() {
        recyclerBestSellingCources.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        bestSellingCourseAdapter = new BestSellingCourseAdapter(getActivity());
        recyclerBestSellingCources.setItemAnimator(new DefaultItemAnimator());
        recyclerBestSellingCources.setAdapter(bestSellingCourseAdapter);
        bestSellingCourseAdapter.notifyDataSetChanged();
    }


}