package com.getsetgoapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.adoisstudio.helper.H;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStackImmediate();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStackImmediate();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
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
        String fromCrash = this.getArguments().getString("fromCrash");

        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Purchase Course");
        BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);

        loadPurchaseView(course_id,fromCrash);

    }

    private void loadPurchaseView(String course_id,String fromCrash) {
        final String token = new Session(getActivity()).getString(P.token);
        String url = "";

        if (fromCrash.equals("1")){
            url = P.buy_course_base_dev + course_id + "/" + token;
        }else {
            url = P.buy_course_base_dev + course_id + "/" + token;
        }

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
                        H.showMessage(getActivity(),"Payment successfully done...!");
                        loadActiveCourseFragment(view);
                    } else if (url.contains("/failure")) {
                        H.showMessage(getActivity(),"Payment failed...!");
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
