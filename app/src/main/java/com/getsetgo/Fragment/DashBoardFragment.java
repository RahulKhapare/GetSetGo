package com.getsetgo.Fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.activity.TotalUserActivity;
import com.getsetgo.databinding.FragmentDashboardBinding;
import com.getsetgo.databinding.FragmentTotalearningBinding;

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
                Intent intent = new Intent(getActivity(), TotalUserActivity.class);
                intent.putExtra("titleText", "Total User");  // pass your values and retrieve them in the other Activity using keyName
                startActivity(intent);
            }
        });

        binding.rlTotalDirectUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TotalUserActivity.class);
                intent.putExtra("titleText", "Total Direct User");  // pass your values and retrieve them in the other Activity using keyName
                startActivity(intent);
            }
        });
    }
}