package com.getsetgo.Fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.adoisstudio.helper.Session;
import com.getsetgo.R;

import com.getsetgo.activity.BaseScreenActivity;

import com.getsetgo.databinding.FragmentAccountBinding;
import com.getsetgo.util.Click;
import com.getsetgo.util.Config;
import com.getsetgo.util.P;
import com.squareup.picasso.Picasso;


public class AccountFragment extends Fragment {

    FragmentAccountBinding binding;
    BankDetailsFragment bankDetailsFragment;
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
    }

    public void onClick() {


        binding.lnrChnagePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
                loadChangePasswordFragment(v);
            }
        });

        binding.lnrEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
                loadEditProfileFragment(v);
            }
        });

        binding.txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
                loadEditProfileFragment(v);
            }
        });

        binding.llSupportFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
                loadFAQFragment(v);
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


    private void loadEditProfileFragment(View v) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFromBottom", isFromBottom);
        editProfileFragment = new EditProfileFragment();
        editProfileFragment.setArguments(bundle);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, editProfileFragment)
                .addToBackStack(null)
                .commit();
    }

    private void loadChangePasswordFragment(View v) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFromBottom", isFromBottom);
        changePasswordFragment = new ChangePasswordFragment();
        changePasswordFragment.setArguments(bundle);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, changePasswordFragment)
                .addToBackStack(null)
                .commit();
    }

    private void loadFAQFragment(View v) {
        Config.flag = Config.faq;
        Config.webViewUrl = BaseScreenActivity.faq_url;
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFromBottom", isFromBottom);
        webViewFragment = new WebViewFragment();
        webViewFragment.setArguments(bundle);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, webViewFragment)
                .addToBackStack(null)
                .commit();
    }

}