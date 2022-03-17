package com.getsetgoapp.Fragment;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.getsetgoapp.Adapter.LiveCourseAdapter;
import com.getsetgoapp.Model.LiveCoursesModel;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.databinding.FragmentLiveCourseBinding;
import com.getsetgoapp.util.App;
import com.getsetgoapp.util.JumpToLogin;
import com.getsetgoapp.util.P;

import java.util.ArrayList;
import java.util.List;

public class LiveCourseFragment extends Fragment {

    FragmentLiveCourseBinding binding;
    private LiveCourseAdapter liveCourseAdapter;
    private List<LiveCoursesModel> liveCoursesModelList;

    public LiveCourseFragment() {

    }

    public static LiveCourseFragment newInstance() {
        LiveCourseFragment fragment = new LiveCourseFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_live_course, container, false);
        View rootView = binding.getRoot();
        init(rootView);
        BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init(View view) {
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Live Courses");

        liveCoursesModelList = new ArrayList<>();
        liveCourseAdapter = new LiveCourseAdapter(getActivity(), liveCoursesModelList);
        binding.recyclerLiveCurses.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerLiveCurses.setHasFixedSize(true);
        binding.recyclerLiveCurses.setAdapter(liveCourseAdapter);

        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackClick();
            }
        });

//        callLiveCourseAPI(getActivity());

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                onBackClick();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        super.onCreate(savedInstanceState);
    }

    private void callLiveCourseAPI(Context context) {

        LoadingDialog loadingDialog = new LoadingDialog(context, false);
        Api.newApi(context, P.baseUrl + "all_crash_course")
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
                        JumpToLogin.call(Json1, context);
                        loadingDialog.dismiss();
                        if (Json1.getInt(P.status) == 0) {
                            H.showMessage(context, Json1.getString(P.err));
                        } else {
                            JsonList jsonListData = Json1.getJsonList(P.data);
                            if (jsonListData != null && !jsonListData.isEmpty()) {
                                for (Json jsonData : jsonListData) {
                                    LiveCoursesModel model = new LiveCoursesModel();
                                }
                                liveCourseAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                }).run("live_course");
    }

    private void onBackClick() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            BaseScreenActivity.callBack();
        }
    }
}