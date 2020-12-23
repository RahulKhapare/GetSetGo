package com.getsetgo.Fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.databinding.FragmentDashboardBinding;

public class DashBoardFragment extends Fragment {

    FragmentDashboardBinding binding;
    TotalUsersFragment totalUsersFragment;


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
        onClick();
        return rootView;
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
                loadTotalFragment("Total User",v);
            }
        });

        binding.rlTotalDirectUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTotalFragment("Total Direct User",v);
            }
        });
    }

    private void loadTotalFragment(String title,View v){
        Bundle bundle = new Bundle();
        bundle.putString("titleText", title);
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        totalUsersFragment = new TotalUsersFragment();
        totalUsersFragment.setArguments(bundle);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, totalUsersFragment)
                .addToBackStack(null)
                .commit();

    }
}