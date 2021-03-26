package com.getsetgoapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.adoisstudio.helper.Session;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.databinding.FragmentBuyCourseBinding;
import com.getsetgoapp.util.P;

public class BuyCourseFragment extends Fragment {

    private FragmentBuyCourseBinding mBuyCourseFragmentBinding;
    private YourCourseFragment yourCourseFragment;

    public BuyCourseFragment() {
    }

    public static BuyCourseFragment newInstance() {
        BuyCourseFragment fragment = new BuyCourseFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBuyCourseFragmentBinding = FragmentBuyCourseBinding.inflate(inflater, container, false);
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_categories, container, false);
        View rootView = mBuyCourseFragmentBinding.getRoot();
        init(rootView);
//        BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init(View rootView) {

        String course_id = this.getArguments().getString("course_id");

        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Purchase Course");
        BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);

        loadPurchaseView(course_id);

    }

    private void loadPurchaseView(String course_id) {
        final String token = new Session(getActivity()).getString(P.token);
        String url = P.buy_course_base_dev + course_id + "/" + token;
        mBuyCourseFragmentBinding.wvBuyCourse.getSettings().setJavaScriptEnabled(true);
        mBuyCourseFragmentBinding.wvBuyCourse.setHorizontalScrollBarEnabled(false);

        mBuyCourseFragmentBinding.wvBuyCourse.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            //Show loader on url load
            @Override
            public void onLoadResource(WebView view, String url) {
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                try {

                    if (url.contains("/success")) {
                        loadActiveCourseFragment(view);
                    } else if (url.contains("/failure")) {
                        loadCourseDetailsFragment(view);
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        mBuyCourseFragmentBinding.wvBuyCourse.loadUrl(url);

    }

    private void loadActiveCourseFragment(View v) {

        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        if (yourCourseFragment == null)
            yourCourseFragment = new YourCourseFragment();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, yourCourseFragment)
                .addToBackStack(null)
                .commit();
    }

    private void loadCourseDetailsFragment(View v) {

        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        if (yourCourseFragment == null)
            yourCourseFragment = new YourCourseFragment();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, yourCourseFragment)
                .addToBackStack(null)
                .commit();
    }
}