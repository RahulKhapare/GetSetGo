package com.getsetgo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.getsetgo.Adapter.ActiveCourseAdapter;
import com.getsetgo.Adapter.BestSellingCourseAdapter;
import com.getsetgo.Adapter.OtherCategoriesAdapter;
import com.getsetgo.Model.BestSellingCourseModel;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentHomeBinding;
import com.getsetgo.util.App;
import com.getsetgo.util.Click;
import com.getsetgo.util.JumpToLogin;
import com.getsetgo.util.P;
import com.getsetgo.util.Utilities;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {

    FragmentHomeBinding binding;
    Context context;
    ActiveCourseAdapter activeCourseAdapter;
    OtherCategoriesAdapter otherCategoriesAdapter;
    BestSellingCourseAdapter bestSellingCourseAdapter;

    LinearLayoutManager mLayoutManagerActiveCourse, mLayoutManagerBestSelling, mLayoutManagerOtherCategories;

    JsonList otherCategoriesJsonList = new JsonList();
    JsonList activeCourseJsonList = new JsonList();
    private List<BestSellingCourseModel> bestSellingCourseModelList;

    int categoriesPage = 1;
    int bestSellingPage = 1;
    int activeCoursePage = 1;

    boolean categoriesNextPage = false;
    boolean bestSellingNextPage = false;
    boolean activeCourseNextPage = false;

    boolean isOther = false;
    boolean isActive = false;
    boolean isBest = false;

    public static  JsonList bestselling_course_list = new JsonList();
    public static  JsonList top_searches = new JsonList();

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

        binding.cardViewCurrentLearning.setOnClickListener(this);

        return rootView;
    }

    private void init() {
        setupRecyclerViewForActiveCourse();
        setupRecyclerViewForOthersCategories();
        setupRecyclerViewForBestSellingCourse();
//        onScrollRecyclerview();
        callDashboardCourseAPI(getActivity());
//        callOtherCategoriesAPI(getActivity());

        binding.txtViewAllActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                View view = BaseScreenActivity.binding.bottomNavigation.findViewById(R.id.menu_yourCourse);
                view.performClick();
            }
        });
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
                    if (categoriesNextPage) {
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
        activeCourseAdapter = new ActiveCourseAdapter(getActivity(), activeCourseJsonList);
        binding.recyclerViewCources.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewCources.setAdapter(activeCourseAdapter);
        activeCourseAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerViewForOthersCategories() {
        otherCategoriesAdapter = new OtherCategoriesAdapter(getActivity(), otherCategoriesJsonList);
        binding.recyclerViewOtherCategories.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewOtherCategories.setAdapter(otherCategoriesAdapter);
    }

    private void setupRecyclerViewForBestSellingCourse() {
        bestSellingCourseModelList = new ArrayList<>();
        bestSellingCourseAdapter = new BestSellingCourseAdapter(getActivity(), bestSellingCourseModelList,1);
        binding.recyclerBestSellingCources.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerBestSellingCources.setAdapter(bestSellingCourseAdapter);
        bestSellingCourseAdapter.notifyDataSetChanged();
    }

    private void callDashboardCourseAPI(Context context) {
        LoadingDialog loadingDialog = new LoadingDialog(context, false);
        Api.newApi(context, P.baseUrl + "dashboard")
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
                        JumpToLogin.call(Json1,context);
                        loadingDialog.dismiss();
                        if (Json1.getInt(P.status) == 0) {
                            H.showMessage(context, Json1.getString(P.err));
                        } else {
                            Json json = Json1.getJson(P.data);
                            JsonList categories = new JsonList();
                            JsonList totalCourses = new JsonList();
                            totalCourses = json.getJsonList("active_course_list");
                            categories = json.getJsonList("category_list");
                            bestselling_course_list = json.getJsonList("bestselling_course_list");
                            top_searches = json.getJsonList("top_searches");
                            setupBestSellingCourseData(bestselling_course_list);
                            if (categories != null && !categories.isEmpty()) {
                                otherCategoriesJsonList.clear();
                                otherCategoriesJsonList.addAll(categories);
                                otherCategoriesAdapter.notifyDataSetChanged();
                            }

                            if (totalCourses != null && !totalCourses.isEmpty()) {
                                activeCourseJsonList.clear();
                                activeCourseJsonList.addAll(totalCourses);
                                activeCourseAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                }).run("dashbord");
    }

    private void callOtherCategoriesAPI(Context context) {
        LoadingDialog loadingDialog = new LoadingDialog(context, false);
        Api.newApi(context, P.baseUrl + "dashbord")
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
                        JumpToLogin.call(Json1,context);
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
                                if (otherCategoriesJsonList.size() < numRows) {
                                    categoriesPage++;
                                    categoriesNextPage = true;
                                } else {
                                    categoriesNextPage = false;
                                }
                            }
                        }
                    }

                }).run("dashbord");
    }


    private void setupBestSellingCourseData(JsonList jsonList){
        bestSellingCourseModelList.clear();
        if (jsonList==null || jsonList.size()==0){
            binding.lnrBestSellingCourse.setVisibility(View.GONE);
            bestSellingCourseAdapter.notifyDataSetChanged();
        }else {
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
                bestSellingCourseModelList.add(model);
            }
            bestSellingCourseAdapter.notifyDataSetChanged();
        }

    }
    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){

            case R.id.cardViewCurrentLearning:

//                CurrentLearningFragment currentLearningFragment = CurrentLearningFragment.newInstance();
//
//                getActivity()
//                        .getSupportFragmentManager()
//                        .beginTransaction()
//                        .setCustomAnimations(R.anim.anim_enter, R.anim.anim_exit)
//                        .addToBackStack("")
//                        .add(R.id.fragment_container, currentLearningFragment).commit();
                break;

        }
    }
}