package com.getsetgo.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgo.Adapter.ViewAllCategoriesAdapter;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentSearchUserBinding;
import com.getsetgo.databinding.FragmentViewAllCategoriesBinding;

import java.util.ArrayList;

public class ViewAllCategoriesFragment extends Fragment {

    FragmentViewAllCategoriesBinding binding;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_view_all_categories, container, false);
        View rootView = binding.getRoot();
        init(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init(View view) {
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Categories");
        String myTitle = this.getArguments().getString("subTitle");
        BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.VISIBLE);
        BaseScreenActivity.binding.incFragmenttool.txtSubCat.setText(myTitle);

        setupRecyclerViewForViewAllCategories(binding.recyclerViewAllCategory);

    }

    private void setupRecyclerViewForViewAllCategories(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        ViewAllCategoriesAdapter viewAllCategoriesAdapter = new ViewAllCategoriesAdapter(getActivity());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(viewAllCategoriesAdapter);
        viewAllCategoriesAdapter.notifyDataSetChanged();
    }


}