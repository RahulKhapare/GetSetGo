package com.getsetgoapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.getsetgoapp.Adapter.BestSellingCourseAdapter;
import com.getsetgoapp.Model.BestSellingCourseModel;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.databinding.FragmentBestSellingCourseBinding;
import com.getsetgoapp.util.Config;
import com.getsetgoapp.util.P;

import java.util.ArrayList;
import java.util.List;

public class BestSellingCourseFragment extends Fragment {

    private FragmentBestSellingCourseBinding binding;
    private List<BestSellingCourseModel> bestSellingCourseModelList;
    private BestSellingCourseAdapter adapter;

    public BestSellingCourseFragment() {
        // Required empty public constructor
    }

    public static BestSellingCourseFragment newInstance() {
        BestSellingCourseFragment fragment = new BestSellingCourseFragment();
        return fragment;
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_best_selling_course, container, false);
        View rootView = binding.getRoot();
        init();
        return rootView;
    }

    private void init() {
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText(Config.courseTitle);
        bestSellingCourseModelList = new ArrayList<>();
        binding.recyclerSellingCourse.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BestSellingCourseAdapter(getActivity(),bestSellingCourseModelList,2);
        binding.recyclerSellingCourse.setAdapter(adapter);
        setupBestSellingCourseData(Config.courseJsonList);

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

    private void setupBestSellingCourseData(JsonList jsonList){
        bestSellingCourseModelList.clear();
        if (jsonList!=null || jsonList.size()!=0) {
            for (Json json : jsonList){
                BestSellingCourseModel model = new BestSellingCourseModel();
                model.setId(json.getString(P.id));
                model.setCourse_name(json.getString(P.course_name));
                model.setSlug(json.getString(P.slug));
                model.setImage(json.getString(P.image));
                model.setCategory_name(json.getString(P.category_name));
                model.setInstructor_name(json.getString(P.instructor_name));
                model.setPrice(json.getString(P.price));
                model.setSale_price(json.getString(P.sale_price));
                model.setRating(json.getString(P.rating));
                model.setShare_link(json.getString(P.share_link));
                bestSellingCourseModelList.add(model);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
