package com.getsetgo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentChangePasswordBinding;
import com.getsetgo.util.App;
import com.getsetgo.util.Click;
import com.getsetgo.util.JumpToLogin;
import com.getsetgo.util.P;
import com.getsetgo.util.ProgressView;

public class ChangePasswordFragment extends Fragment {

    private FragmentChangePasswordBinding binding;
    boolean isFromBottom;
    LoadingDialog loadingDialog;

    public ChangePasswordFragment() {
    }

    public static ChangePasswordFragment newInstance() {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        return fragment;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false);
        View rootView = binding.getRoot();
        loadingDialog = new LoadingDialog(getActivity());
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Change Password");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
        isFromBottom = getArguments().getBoolean("isFromBottom");

        onClick();

        return rootView;
    }

    private void onClick(){

        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackClick();
            }
        });

        binding.txtSavePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);

                if (TextUtils.isEmpty(binding.etxCurrentPassword.getText().toString().trim())){
                    H.showMessage(getActivity(),"Enter Current Password");
                }else if (TextUtils.isEmpty(binding.etxNewPassword.getText().toString().trim())){
                    H.showMessage(getActivity(),"Enter New Password");
                }else if (TextUtils.isEmpty(binding.etxConformNewPassword.getText().toString().trim())){
                    H.showMessage(getActivity(),"Enter Confirm Password");
                }else if (!binding.etxNewPassword.getText().toString().trim().equals(binding.etxConformNewPassword.getText().toString().trim())){
                    H.showMessage(getActivity(),"Confirm Password Password not matched");
                }else {
                    hitChangePassword(getActivity());
                }

            }
        });
    }

    private void hitChangePassword(Context context) {
        ProgressView.show(context,loadingDialog);
        Json j = new Json();
        j.addString(P.old_password,binding.etxCurrentPassword.getText().toString().trim());
        j.addString(P.new_password,binding.etxNewPassword.getText().toString().trim());
        j.addString(P.confirm_password,binding.etxConformNewPassword.getText().toString().trim());

        Api.newApi(context, P.baseUrl + "change_password").addJson(j)
                .setMethod(Api.POST)
                .onHeaderRequest(App::getHeaders)
                .onError(() -> {
                    ProgressView.dismiss(loadingDialog);
                })
                .onSuccess(json ->
                {
                    JumpToLogin.call(json,context);
                    ProgressView.dismiss(loadingDialog);
                    if (json.getInt(P.status) == 1) {
                        H.showMessage(context,"Password change successfully");
                        final Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getFragmentManager().popBackStackImmediate();
                            }
                        }, 1000);
                    }
                })
                .run("hitChangePassword");
    }


    private void onBackClick(){
        if(getFragmentManager().getBackStackEntryCount() > 0){
            if(isFromBottom){
                getFragmentManager().popBackStackImmediate();
                BaseScreenActivity.binding.bottomNavigation.setVisibility(View.VISIBLE);
                BaseScreenActivity.binding.bottomNavigation.setSelectedItemId(R.id.menu_Account);
            }else{
                getFragmentManager().popBackStackImmediate();
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
