package com.getsetgoapp.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

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
import com.getsetgoapp.Adapter.ActiveCourseAdapter;
import com.getsetgoapp.Adapter.BestSellingCourseAdapter;
import com.getsetgoapp.Adapter.OtherCategoriesAdapter;
import com.getsetgoapp.Model.BestSellingCourseModel;
import com.getsetgoapp.Model.HomeParentModel;
import com.getsetgoapp.Model.SliderModel;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.adapterview.HomeParentCourseAdapter;
import com.getsetgoapp.adapterview.SliderImageAdapter;
import com.getsetgoapp.databinding.FragmentHomeBinding;
import com.getsetgoapp.util.App;
import com.getsetgoapp.util.Click;
import com.getsetgoapp.util.Config;
import com.getsetgoapp.util.JumpToLogin;
import com.getsetgoapp.util.P;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    Context context;
    ActiveCourseAdapter activeCourseAdapter;
    OtherCategoriesAdapter otherCategoriesAdapter;
    BestSellingCourseAdapter bestSellingCourseAdapter;
    private List<SliderModel> sliderModelList;
    private SliderImageAdapter sliderImageAdapter;

    List<HomeParentModel> homeParentModelList;
    HomeParentCourseAdapter parentItemAdapter;

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

    public static JsonList bestselling_course_list = new JsonList();
    public static JsonList top_searches = new JsonList();

    MyCourseDetailFragment courseDetailFragment;
    Bundle bundle = new Bundle();

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
        setupSliderView();
        setupRecyclerViewForActiveCourse();
        setupRecyclerViewForOthersCategories();
        setupRecyclerViewForBestSellingCourse();
        setupHomeExternalCourse();
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

    private void setupSliderView() {
        sliderModelList = new ArrayList<>();
        sliderImageAdapter = new SliderImageAdapter(context, sliderModelList);
        binding.pager.setAdapter(sliderImageAdapter);
        binding.tabLayout.setupWithViewPager(binding.pager, true);
    }

    private void setupRecyclerViewForActiveCourse() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        binding.recyclerViewCources.setLayoutManager(linearLayoutManager);
        binding.recyclerViewCources.setHasFixedSize(true);
        binding.recyclerViewCources.setNestedScrollingEnabled(false);
        activeCourseAdapter = new ActiveCourseAdapter(getActivity(), activeCourseJsonList);
//        binding.recyclerViewCources.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewCources.setAdapter(activeCourseAdapter);
        activeCourseAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerViewForOthersCategories() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
//        binding.recyclerViewOtherCategories.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewOtherCategories.setLayoutManager(linearLayoutManager);
        binding.recyclerViewOtherCategories.setHasFixedSize(true);
        binding.recyclerViewOtherCategories.setNestedScrollingEnabled(false);
        otherCategoriesAdapter = new OtherCategoriesAdapter(getActivity(), otherCategoriesJsonList);
        binding.recyclerViewOtherCategories.setAdapter(otherCategoriesAdapter);
    }

    private void setupRecyclerViewForBestSellingCourse() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
//        binding.recyclerBestSellingCources.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerBestSellingCources.setLayoutManager(linearLayoutManager);
        binding.recyclerBestSellingCources.setHasFixedSize(true);
        binding.recyclerBestSellingCources.setNestedScrollingEnabled(false);
        bestSellingCourseModelList = new ArrayList<>();
        bestSellingCourseAdapter = new BestSellingCourseAdapter(getActivity(), bestSellingCourseModelList, 1);
        binding.recyclerBestSellingCources.setAdapter(bestSellingCourseAdapter);
        bestSellingCourseAdapter.notifyDataSetChanged();
    }

    private void setupHomeExternalCourse() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerHomeCourse.setLayoutManager(layoutManager);
        binding.recyclerHomeCourse.setHasFixedSize(true);
        binding.recyclerHomeCourse.setNestedScrollingEnabled(false);
        homeParentModelList = new ArrayList<>();
        parentItemAdapter = new HomeParentCourseAdapter(getActivity(), homeParentModelList);
        binding.recyclerHomeCourse.setAdapter(parentItemAdapter);
        binding.recyclerHomeCourse.setItemViewCacheSize(homeParentModelList.size());
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
                        JumpToLogin.call(Json1, context);
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
                            BaseScreenActivity.organization_chart_url = json.getString(P.organization_chart_url);
//                            Json currently_learning = json.getJson("currently_learning");
                            JsonList home_course_list = json.getJsonList("home_course_list");
                            JsonList banner_list = json.getJsonList("banner_list");

                            setUpSliderList(banner_list);
                            setupBestSellingCourseData(bestselling_course_list);
                            setupHomeCourseData(home_course_list);
//                            setUpCurrentLearningData(currently_learning);

                            if (categories != null && !categories.isEmpty()) {
                                otherCategoriesJsonList.clear();
                                otherCategoriesJsonList.addAll(categories);
                                otherCategoriesAdapter.notifyDataSetChanged();
                            }

                            if (otherCategoriesJsonList.isEmpty()){
                                binding.lnrExploreOtherCategory.setVisibility(View.GONE);
                            }else {
                                binding.lnrExploreOtherCategory.setVisibility(View.VISIBLE);
                            }

                            if (totalCourses != null && !totalCourses.isEmpty()) {
                                activeCourseJsonList.clear();
                                activeCourseJsonList.addAll(totalCourses);
                                activeCourseAdapter.notifyDataSetChanged();
                            }

                            if (activeCourseJsonList == null || activeCourseJsonList.size() == 0) {
                                binding.lnrActiveCourse.setVisibility(View.GONE);
                            }else {
                                binding.lnrActiveCourse.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                }).run("dashbord");
    }

    private void setUpCurrentLearningData(Json json) {

        if (!json.toString().equals("") && !json.toString().equals("null") && !json.toString().equals("{}")) {
            String id = json.getString(P.id);
            String course_name = json.getString(P.course_name);
            String slug = json.getString(P.slug);
            String image = json.getString(P.image);
            String category_name = json.getString(P.category_name);
            String completion_percent = json.getString(P.completion_percent);

            if (TextUtils.isEmpty(image)) {
                image = "empty_image";
            }

            Picasso.get().load(image).placeholder(R.drawable.ic_no_image).error(R.drawable.ic_no_image).into(binding.imgCourse);
            binding.txtCourseName.setText(course_name);
            binding.txtCategoryName.setText(category_name);
            binding.txtStatus.setText(completion_percent + "% Complete");

            if (!TextUtils.isEmpty(completion_percent) && !completion_percent.equals("null")) {
                int progress = Integer.parseInt(completion_percent);
                binding.progressBar.setProgress(progress);
            }

            binding.txtCurrentLEarning.setVisibility(View.VISIBLE);
            binding.cardViewCurrentLearning.setVisibility(View.VISIBLE);

            binding.cardViewCurrentLearning.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Click.preventTwoClick(v);
                    loadFragment(v, slug, course_name);
                }
            });

        } else {
            binding.txtCurrentLEarning.setVisibility(View.GONE);
            binding.cardViewCurrentLearning.setVisibility(View.GONE);
        }

    }

    private void setUpSliderList(JsonList jsonList) {

        sliderModelList.clear();

        if (jsonList != null && jsonList.size() != 0) {
            for (Json json : jsonList) {
                SliderModel model = new SliderModel();
                model.setBanner_image(json.getString(P.banner_image));
                sliderModelList.add(model);
            }
        }

        sliderImageAdapter.notifyDataSetChanged();

        if (sliderModelList.isEmpty()) {
            binding.lnrSlider.setVisibility(View.GONE);
        } else {
            binding.lnrSlider.setVisibility(View.VISIBLE);
        }

        Handler handler = new Handler();
        Runnable runnable = null;

        if (runnable != null)
            handler.removeCallbacks(runnable);
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 3000);
                if (binding.pager.getCurrentItem() == sliderImageAdapter.getCount() - 1)
                    binding.pager.setCurrentItem(0);
                else
                    binding.pager.setCurrentItem(binding.pager.getCurrentItem() + 1);
            }
        };
        runnable.run();
    }


    private void setupBestSellingCourseData(JsonList jsonList) {
        bestSellingCourseModelList.clear();
        if (jsonList != null || jsonList.size() != 0) {
            for (Json json : jsonList) {
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
            bestSellingCourseAdapter.notifyDataSetChanged();
        }

        if(bestSellingCourseModelList.isEmpty()){
            binding.lnrBestSellingCourse.setVisibility(View.GONE);
            binding.recyclerBestSellingCources.setVisibility(View.GONE);
        }else {
            binding.lnrBestSellingCourse.setVisibility(View.VISIBLE);
            binding.recyclerBestSellingCources.setVisibility(View.VISIBLE);
        }
    }

    private void setupHomeCourseData(JsonList jsonList) {
        homeParentModelList.clear();
        if (jsonList!=null && jsonList.size()!=0){
            for (Json json :  jsonList){
                HomeParentModel model = new HomeParentModel();
                model.setCategory_slug(json.getString("category_slug"));
                model.setCategory_name(json.getString("category_name"));
                model.setCourses(json.getJsonList("courses"));
                if (!model.getCourses().toString().equals("[]")){
                    homeParentModelList.add(model);
                }

            }
        }
        parentItemAdapter.notifyDataSetChanged();

        if (homeParentModelList.isEmpty()){
            binding.recyclerHomeCourse.setVisibility(View.GONE);
        }else {
            binding.recyclerHomeCourse.setVisibility(View.VISIBLE);
        }
    }

    private void loadFragment(View v, String courseSlug, String courseName) {
        BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
        BaseScreenActivity.binding.incBasetool.content.setVisibility(View.GONE);
        BaseScreenActivity.binding.incFragmenttool.content.setVisibility(View.VISIBLE);
        BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);
        Config.myCourseSlug = courseSlug;
        Config.myCourseTitle = courseName;
        bundle.putString("slug", courseSlug);
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        if (courseDetailFragment == null)
            courseDetailFragment = new MyCourseDetailFragment();
        courseDetailFragment.setArguments(bundle);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, courseDetailFragment)
                .addToBackStack(null)
                .commit();
    }

}