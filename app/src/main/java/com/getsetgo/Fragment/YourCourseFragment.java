package com.getsetgo.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.getsetgo.Adapter.MyCourseAdapter;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_your_course, container, false);
        View rootView = binding.getRoot();
        init();
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Your Course");
        return rootView;
    }

    private void init() {
        setupRecyclerViewForYourCourse();
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