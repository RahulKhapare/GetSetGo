package com.getsetgo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.getsetgo.Adapter.ActiveCourseAdapter;
import com.getsetgo.Adapter.BestSellingCourseAdapter;
import com.getsetgo.Adapter.OtherCategoriesAdapter;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentHomeBinding;
import com.getsetgo.util.App;
import com.getsetgo.util.P;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    Context context;
    ActiveCourseAdapter activeCourseAdapter;
    OtherCategoriesAdapter otherCategoriesAdapter;
    BestSellingCourseAdapter bestSellingCourseAdapter;

    LinearLayoutManager mLayoutManagerActiveCourse, mLayoutManagerBestSelling, mLayoutManagerOtherCategories;

    JsonList otherCategoriesJsonList = new JsonList();
    JsonList bestSellingCourseJsonList = new JsonList();
    JsonList activeCourseJsonList = new JsonList();

    int categoriesPage = 1;
    int bestSellingPage = 1;
    int activeCoursePage = 1;

    boolean categoriesNextPage = false;
    boolean bestSellingNextPage = false;
    boolean activeCourseNextPage = false;

    boolean isOther = false;
    boolean isActive = false;
    boolean isBest = false;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View rootView = binding.getRoot();
        context = inflater.getContext();
        init();

        return rootView;
    }

    private void init() {
        setupRecyclerViewForActiveCourse();
        setupRecyclerViewForOthersCategories();
        setupRecyclerViewForBestSellingCourse();
        onScrollRecyclerview();
    }

    private void onScrollRecyclerview() {

        binding.recyclerViewOtherCategories.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isOther = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int currentItem, totalItems, scrollOutItems;
                currentItem = mLayoutManagerOtherCategories.getChildCount();
                totalItems = mLayoutManagerOtherCategories.getItemCount();
                scrollOutItems = mLayoutManagerOtherCategories.findFirstVisibleItemPosition();

                if (isOther && (currentItem + scrollOutItems) >= totalItems) {
                    if(categoriesNextPage) {
                        isOther = false;
                        //callOtherCategoriesAPI(context);
                    }
                }
            }
        });

        binding.recyclerViewCources.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isActive = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int currentItem, totalItems, scrollOutItems;
                currentItem = mLayoutManagerActiveCourse.getChildCount();
                totalItems = mLayoutManagerActiveCourse.getItemCount();
                scrollOutItems = mLayoutManagerActiveCourse.findFirstVisibleItemPosition();
                if (isActive && (currentItem + scrollOutItems) >= totalItems) {
                    isActive = false;
                }

            }
        });

        binding.recyclerBestSellingCources.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isBest = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int currentItem, totalItems, scrollOutItems;
                currentItem = mLayoutManagerBestSelling.getChildCount();
                totalItems = mLayoutManagerBestSelling.getItemCount();
                scrollOutItems = mLayoutManagerBestSelling.findFirstVisibleItemPosition();
                if (isBest && (currentItem + scrollOutItems) >= totalItems) {
                    isBest = false;
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupRecyclerViewForActiveCourse() {
        mLayoutManagerActiveCourse = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerViewCources.setLayoutManager(mLayoutManagerActiveCourse);
        activeCourseAdapter = new ActiveCourseAdapter(getActivity(),activeCourseJsonList);
        binding.recyclerViewCources.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewCources.setAdapter(activeCourseAdapter);
        activeCourseAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerViewForOthersCategories() {
        mLayoutManagerOtherCategories = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerViewOtherCategories.setLayoutManager(mLayoutManagerOtherCategories);
        otherCategoriesAdapter = new OtherCategoriesAdapter(getActivity(),otherCategoriesJsonList);
        binding.recyclerViewOtherCategories.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewOtherCategories.setAdapter(otherCategoriesAdapter);
    }

    private void setupRecyclerViewForBestSellingCourse() {
        mLayoutManagerBestSelling = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerBestSellingCources.setLayoutManager(mLayoutManagerBestSelling);
        bestSellingCourseAdapter = new BestSellingCourseAdapter(getActivity(),bestSellingCourseJsonList);
        binding.recyclerBestSellingCources.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerBestSellingCources.setAdapter(bestSellingCourseAdapter);
        bestSellingCourseAdapter.notifyDataSetChanged();
    }

    private void callDashboardCourseAPI(Context context) {
        LoadingDialog loadingDialog = new LoadingDialog(context);
        Api.newApi(context, P.baseUrl + "dashbord")
                .setMethod(Api.POST)
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
                            Json1 = Json1.getJson(P.data);
                            JsonList jsonList = new JsonList();
                            jsonList = Json1.getJsonList("total_courses");
                            if (jsonList != null && !jsonList.isEmpty()) {
                            }
                        }
                    }

                }).run("dashbord");
    }
    private void callOtherCategoriesAPI(Context context) {
        LoadingDialog loadingDialog = new LoadingDialog(context);
        Api.newApi(context, P.baseUrl + "dashbord")
                .setMethod(Api.POST)
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
                            Json1 = Json1.getJson(P.data);
                            int numRows = Json1.getInt(P.num_rows);
                            JsonList jsonList = Json1.getJsonList("total_courses");
                            if (jsonList != null && !jsonList.isEmpty()) {
                                otherCategoriesJsonList.addAll(jsonList);
                                otherCategoriesAdapter.notifyDataSetChanged();
                                if(otherCategoriesJsonList.size() < numRows){
                                    categoriesPage++;
                                    categoriesNextPage = true;
                                }else{
                                    categoriesNextPage = false;
                                }
                            }
                        }
                    }

                }).run("dashbord");
    }
}