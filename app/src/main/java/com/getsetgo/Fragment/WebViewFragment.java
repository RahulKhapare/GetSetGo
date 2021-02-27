package com.getsetgo.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentWebViewBinding;
import com.getsetgo.util.Config;

public class WebViewFragment extends Fragment {

    private FragmentWebViewBinding binding;

    public WebViewFragment() {
    }

    public static WebViewFragment newInstance() {
        WebViewFragment fragment = new WebViewFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_web_view, container, false);
        View rootView = binding.getRoot();

        String title = "";
        if (Config.flag == Config.term){
            title = "Terms & Conditions";
        }else  if (Config.flag == Config.privacy){
            title = "Privacy Policy";
        }

        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText(title);
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);

        init();

        return rootView;
    }

    private void init(){
        loadUrl(Config.webViewUrl);
        onClick();
    }

    private void loadUrl(String url){
        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);

        binding.webView.loadUrl(url);
    }

    private void onClick(){

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

}
