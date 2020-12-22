package com.getsetgo.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.getsetgo.Adapter.MyCourseAdapter;
import com.getsetgo.R;
import com.getsetgo.databinding.FragmentComposeBinding;
import com.getsetgo.databinding.FragmentYourCourseBinding;

public class YourCourseFragment extends Fragment {

    FragmentYourCourseBinding binding;
    MyCourseAdapter myCourseAdapter;

    public YourCourseFragment() {
        // Required empty public constructor
    }

    public static YourCourseFragment newInstance() {
        YourCourseFragment fragment = new YourCourseFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_your_course, container, false);
        View rootView = binding.getRoot();

        binding.icYourCourseToolbar.txtTittle.setText("Your Course");
        setupRecyclerViewForYourCourse();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupRecyclerViewForYourCourse() {
        binding.recyclerViewCourse.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        myCourseAdapter = new MyCourseAdapter(getContext());
        binding.recyclerViewCourse.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewCourse.setAdapter(myCourseAdapter);
        myCourseAdapter.notifyDataSetChanged();
    }


}