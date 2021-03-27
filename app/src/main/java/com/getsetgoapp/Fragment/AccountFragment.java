package com.getsetgoapp.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.adoisstudio.helper.Session;
import com.getsetgoapp.R;

import com.getsetgoapp.activity.BaseScreenActivity;

import com.getsetgoapp.databinding.FragmentAccountBinding;
import com.getsetgoapp.util.App;
import com.getsetgoapp.util.Click;
import com.getsetgoapp.util.Config;
import com.getsetgoapp.util.JumpToLogin;
import com.getsetgoapp.util.P;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class AccountFragment extends Fragment {

    FragmentAccountBinding binding;
    BankDetailsFragment bankDetailsFragment;
    NotificationsFragment notificationsFragment;
    MyOrderFragment myOrderFragment;
    ChangePasswordFragment changePasswordFragment;
    EditProfileFragment editProfileFragment;
    WebViewFragment webViewFragment;
    boolean isFromBottom;

    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        View rootView = binding.getRoot();

        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("My Account");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
        onClick();
        setProfileData();
        isFromBottom = getArguments().getBoolean("isFromBottom");
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void setProfileData(){
        Session session = new Session(getActivity());
        String profile_picture = session.getString(P.profile_picture);
        if (!TextUtils.isEmpty(profile_picture)){
            Picasso.get().load(profile_picture).placeholder(R.drawable.ic_profile_imag).error(R.drawable.ic_profile_imag).into(binding.cvProfile);
        }
        binding.txtProfileName.setText(session.getString(P.name) + " " + session.getString(P.lastname));
        binding.txtProfileEmail.setText(session.getString(P.email));

        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            binding.txtVersionName.setText(getResources().getString(R.string.app_name) + " v"+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void onClick() {


        binding.llOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
                myOrderFragment = new MyOrderFragment();
                loadFragment(v,myOrderFragment);
            }
        });

        binding.llNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
                notificationsFragment = new NotificationsFragment();
                loadFragment(v,notificationsFragment);
            }
        });

        binding.lnrChnagePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
                changePasswordFragment = new ChangePasswordFragment();
                loadFragment(v,changePasswordFragment);
            }
        });

        binding.lnrReferAndEarn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                hitReferralApi(getActivity());
            }
        });

        binding.lnrEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
                editProfileFragment = new EditProfileFragment();
                loadFragment(v,editProfileFragment);
            }
        });

        binding.txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
                editProfileFragment = new EditProfileFragment();
                loadFragment(v,editProfileFragment);
            }
        });

        binding.llSupportFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
                Config.flag = Config.faq;
                Config.webViewUrl = BaseScreenActivity.faq_url;
                webViewFragment = new WebViewFragment();
                loadFragment(v,webViewFragment);
            }
        });

        binding.llTermCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
                Config.flag = Config.term;
                Config.webViewUrl = BaseScreenActivity.termConditionUrl;
                webViewFragment = new WebViewFragment();
                loadFragment(v,webViewFragment);
            }
        });

        binding.llPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
                Config.flag = Config.privacy;
                Config.webViewUrl = BaseScreenActivity.privacyPolicyUrl;
                webViewFragment = new WebViewFragment();
                loadFragment(v,webViewFragment);
            }
        });

        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    BaseScreenActivity.callBack();
                }
            }
        });

        binding.txtSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                ((BaseScreenActivity)getActivity()).onLogout();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    BaseScreenActivity.callBack();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        super.onCreate(savedInstanceState);
    }

    private void loadFragment(View v,Fragment fragment){
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFromBottom", isFromBottom);
        fragment.setArguments(bundle);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void hitReferralApi(Context context) {
        LoadingDialog loadingDialog = new LoadingDialog(context, false);
        Api.newApi(context, P.baseUrl + "referral_users")
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
                        } else if (Json1.getInt(P.status) == 1) {
                            Json1 = Json1.getJson(P.data);
                            String referral_users = Json1.getString(P.referral_users);
                            QRCodeDialog(referral_users);
                        }
                    }

                }).run("hitReferralApi");
    }


    private void QRCodeDialog(String count) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_qr_code);

        ImageView imgQR = dialog.findViewById(R.id.imgQR);
        TextView txtQR = dialog.findViewById(R.id.txtQR);
        TextView txtMessage = dialog.findViewById(R.id.txtMessage);


        txtMessage.setText("Users registered using your referral - " + count);
        String qr_code = "qr_code";
        String imagePath = new Session(getActivity()).getString(qr_code);
        if (!imagePath.equals("")){
            qr_code = imagePath;
        }
        Picasso.get().load(qr_code).error(R.drawable.ic_no_image).into(imgQR);

        txtQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dialog.dismiss();
                shareApp(new Session(getActivity()).getString(P.app_link));
            }
        });

        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    public void shareApp(String link)
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, link);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

}