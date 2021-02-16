package com.getsetgo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.getsetgo.Adapter.ViewAllCategoriesAdapter;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentViewAllCategoriesBinding;
import com.getsetgo.util.App;
import com.getsetgo.util.P;

public class ViewAllCategoriesFragment extends Fragment {

    FragmentViewAllCategoriesBinding binding;
    public boolean isFromHome;
    ViewAllCategoriesAdapter viewAllCategoriesAdapter;
    JsonList categoriesCoursesList = new JsonList();
    Context context;

    LinearLayoutManager mLayoutManager;

    int Page = 1;
    boolean NextPage = false;
    int currentItem, totalItems, scrollOutItems;
    boolean isScrolling = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_all_categories, container, false);
        View rootView = binding.getRoot();
        context = inflater.getContext();
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
        String slug = this.getArguments().getString("slug");
        isFromHome = this.getArguments().getBoolean("isFromHome");
//        JsonList jsonList = (JsonList) this.getArguments().getSerializable("categoryList");
//        categoriesCoursesList.addAll(jsonList);
        BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.VISIBLE);
        BaseScreenActivity.binding.incFragmenttool.txtSubCat.setText(myTitle);
        callCategoriesCoursesAPI(context, slug);
        setupRecyclerViewForViewAllCategories();


        /*binding.recyclerViewAllCategory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItem = mLayoutManager.getChildCount();
                totalItems = mLayoutManager.getItemCount();
                scrollOutItems = mLayoutManager.findFirstVisibleItemPosition();
                if (isScrolling && (currentItem + scrollOutItems) >= totalItems) {
                    isScrolling = false;
                }
            }
        });*/


        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    callback();
                }
            }
        });

    }

    private void callback() {
        categoriesCoursesList.clear();
        if (isFromHome) {
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            BaseScreenActivity.callBack();
        } else {
            getFragmentManager().popBackStackImmediate();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    callback();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        super.onCreate(savedInstanceState);
    }


    private void setupRecyclerViewForViewAllCategories() {
//        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        binding.recyclerViewAllCategory.setLayoutManager(mLayoutManager);
        viewAllCategoriesAdapter = new ViewAllCategoriesAdapter(getActivity(), categoriesCoursesList);
        binding.recyclerViewAllCategory.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewAllCategory.setAdapter(viewAllCategoriesAdapter);
    }

    private void callCategoriesCoursesAPI(Context context, String title) {
        LoadingDialog loadingDialog = new LoadingDialog(context, false);
//        String apiParam = "?title" + title;
        Api.newApi(context, P.baseUrl + "category_courses" + "/" + title)
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

                            Json1 = Json1.getJson(P.data);
                            int numRows = Json1.getInt(P.num_rows);
                            JsonList jsonList = Json1.getJsonList("course_list");
                            if (jsonList != null && !jsonList.isEmpty()) {
                                categoriesCoursesList.clear();
                                categoriesCoursesList.addAll(jsonList);
                                viewAllCategoriesAdapter.notifyDataSetChanged();
                                if (categoriesCoursesList.size() < numRows) {
                                    Page++;
                                    NextPage = true;
                                } else {
                                    NextPage = false;
                                }
                            }
                        }
                    }

                }).run("categories_courses");
    }


}