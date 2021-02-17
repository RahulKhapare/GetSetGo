package com.getsetgo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.getsetgo.Adapter.MyCourseAdapter;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentYourCourseBinding;
import com.getsetgo.util.App;
import com.getsetgo.util.P;

public class YourCourseFragment extends Fragment {

    FragmentYourCourseBinding binding;
    MyCourseAdapter myCourseAdapter;
    JsonList activeCourseJsonList = new JsonList();

    public YourCourseFragment() {
        // Required empty public constructor
    }

    public static YourCourseFragment newInstance() {
        YourCourseFragment fragment = new YourCourseFragment();
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

        callActiveCourseAPI(getActivity());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupRecyclerViewForYourCourse() {
        myCourseAdapter = new MyCourseAdapter(getContext(),activeCourseJsonList);
        binding.recyclerViewCourse.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewCourse.setAdapter(myCourseAdapter);
        myCourseAdapter.notifyDataSetChanged();
    }


    private void callActiveCourseAPI(Context context) {
        LoadingDialog loadingDialog = new LoadingDialog(context, false);
        Api.newApi(context, P.baseUrl + "active_courses")
                .setMethod(Api.GET)
                .onHeaderRequest(App::getHeaders)
                .onLoading(isLoading -> {
                    if (isLoading)
                        loadingDialog.show("loading...");
                    else
                        loadingDialog.hide();
                })
                .onError(() ->
                        MessageBox.showOkMessage(context, "Message", "Failed to login. Please try again", () -> {
                            loadingDialog.dismiss();
                        }))
                .onSuccess(Json1 -> {
                    if (Json1 != null) {
                        loadingDialog.dismiss();
                        if (Json1.getInt(P.status) == 0) {
                            H.showMessage(context, Json1.getString(P.err));
                        } else {
                            Json json = Json1.getJson(P.data);
                            JsonList activeCourses = new JsonList();
                            activeCourses = json.getJsonList("active_course_list");

                            if (activeCourses != null && !activeCourses.isEmpty()) {
                                activeCourseJsonList.clear();
                                activeCourseJsonList.addAll(activeCourses);
                                myCourseAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                }).run("active_courses");
    }

}