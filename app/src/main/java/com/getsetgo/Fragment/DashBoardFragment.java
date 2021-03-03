package com.getsetgo.Fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.nfc.tech.TagTechnology;
import android.os.Bundle;
import android.util.Log;
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
import com.getsetgo.databinding.FragmentDashboardBinding;
import com.getsetgo.util.Click;
import com.getsetgo.util.P;

public class DashBoardFragment extends Fragment {

    FragmentDashboardBinding binding;


    public DashBoardFragment() {
        // Required empty public constructor
    }

    public static DashBoardFragment newInstance() {
        DashBoardFragment fragment = new DashBoardFragment();

        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        View rootView = binding.getRoot();

        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Dashboard");
        BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
        // The callback can be enabled or disabled here or in handleOnBackPressed()
        onClick();
        return rootView;
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void onClick() {

        binding.imvCopyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = binding.txtAffCode.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", text);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), clipboard.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.imvCopyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = binding.txtAffLink.getText().toString();
                ClipboardManager clipboard = (ClipboardManager)getActivity(). getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label link", text);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), clipboard.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.rlTotalUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                TotalUsersFragment totalUsersFragment = new TotalUsersFragment();
                loadFragment(totalUsersFragment,v);
            }
        });

        binding.rlTotalDirectUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                TotalDirectUsersFragment totalDirectUsersFragment = new TotalDirectUsersFragment();
                loadFragment(totalDirectUsersFragment,v);
            }
        });

        binding.rlShareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                shareApp(getActivity(),new Session(getActivity()).getString(P.app_link));
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
    }

    public void shareApp(Context context,String link)
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, link);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    private void loadFragment(Fragment fragment,View v){
        TotalUsersFragment.isSearch = false;
        TotalDirectUsersFragment.isSearch = false;
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();

    }

}