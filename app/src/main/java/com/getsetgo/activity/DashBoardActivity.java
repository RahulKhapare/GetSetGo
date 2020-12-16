package com.getsetgo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getsetgo.R;
import com.getsetgo.databinding.ActivityDashBoardBinding;
import com.getsetgo.databinding.ActivityTotalUserBinding;
import com.getsetgo.util.WindowView;
import com.sasank.roundedhorizontalprogress.RoundedHorizontalProgressBar;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashBoardActivity extends AppCompatActivity {

    private DashBoardActivity activity = this;
    private ActivityDashBoardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dash_board);
        init();
    }

    private void init() {
        onClick();
    }


    public void onClick() {

        binding.imvCopyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = binding.txtAffCode.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", text);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(activity, clipboard.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.imvCopyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = binding.txtAffLink.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label link", text);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(activity, clipboard.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.rlTotalUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, TotalUserActivity.class);
                intent.putExtra("titleText", "Total User");  // pass your values and retrieve them in the other Activity using keyName
                startActivity(intent);
            }
        });

        binding.rlTotalDirectUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, TotalUserActivity.class);
                intent.putExtra("titleText", "Total Direct User");  // pass your values and retrieve them in the other Activity using keyName
                startActivity(intent);
            }
        });
    }


}