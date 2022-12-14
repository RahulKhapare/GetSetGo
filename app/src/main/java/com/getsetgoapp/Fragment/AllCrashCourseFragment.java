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
import com.getsetgoapp.Adapter.AllCrashCourseAdapter;
import com.getsetgoapp.Adapter.ParentItemAdapter;
import com.getsetgoapp.Model.AllCrashCourseModel;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.databinding.FragmentAllCrashCourseBinding;
import com.getsetgoapp.databinding.FragmentParentCategoriesBinding;
import com.getsetgoapp.util.App;
import com.getsetgoapp.util.JumpToLogin;
import com.getsetgoapp.util.P;

import java.util.ArrayList;
import java.util.List;

public class AllCrashCourseFragment extends Fragment {

    FragmentAllCrashCourseBinding binding;
    List<AllCrashCourseModel> allCrashCourseModelList;
    AllCrashCourseAdapter adapter;

    public AllCrashCourseFragment() {

    }

    public static AllCrashCourseFragment newInstance() {
        AllCrashCourseFragment fragment = new AllCrashCourseFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_crash_course, container, false);
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
//        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("All Crash Courses");
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("All Courses");

        allCrashCourseModelList = new ArrayList<>();
        adapter = new AllCrashCourseAdapter(getActivity(), allCrashCourseModelList);
        binding.recyclerviewAllCrashCurse.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerviewAllCrashCurse.setHasFixedSize(true);
        binding.recyclerviewAllCrashCurse.setAdapter(adapter);

        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackClick();
            }
        });

        callAllCrashCourseAPI(getActivity());

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

    private void callAllCrashCourseAPI(Context context) {

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
                            Json jsonData = Json1.getJson(P.data);
                            JsonList jsonListData = jsonData.getJsonList(P.list);
                            if (jsonListData != null && !jsonListData.isEmpty()) {
                                for (Json jsonValue : jsonListData) {
                                    AllCrashCourseModel model = new AllCrashCourseModel();
                                    model.setId(jsonValue.getString(P.id));
                                    model.setInstructor_id(jsonValue.getString(P.instructor_id));
                                    model.setName(jsonValue.getString(P.name));
                                    model.setSlug(jsonValue.getString(P.slug));
                                    model.setProgram_date(jsonValue.getString(P.program_date));
                                    model.setProgram_end_date(jsonValue.getString(P.program_end_date));
                                    model.setProgram_time(jsonValue.getString(P.program_time));
                                    model.setProgram_end_time(jsonValue.getString(P.program_end_time));
                                    model.setSession(jsonValue.getString(P.session));
                                    model.setPrice(jsonValue.getString(P.price));
                                    model.setMlm_price(jsonValue.getString(P.mlm_price));
                                    model.setCategory_name(jsonValue.getString(P.category_name));
                                    model.setSkill_level(jsonValue.getString(P.skill_level));
                                    model.setLanguage(jsonValue.getString(P.language));
                                    model.setShare_url(jsonValue.getString(P.share_url));
                                    model.setIs_purchased(jsonValue.getInt(P.is_purchased));
                                    model.setInstructors(jsonValue.getJsonList(P.instructors));
                                    allCrashCourseModelList.add(model);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                }).run("all_crash_course");
    }

    private void onBackClick() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            BaseScreenActivity.callBack();
        }
    }
}