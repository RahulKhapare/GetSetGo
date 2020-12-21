package com.getsetgo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Session;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivitySplashBinding;
import com.getsetgo.util.P;
import com.getsetgo.util.WindowView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    private SplashActivity activity = this;
    private ActivitySplashBinding binding;
    public static int deviceWidth;
    public static int deviceHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        initView();
    }

    private void initView(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(activity,LoginActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
            }
        },3000);
        generateFcmToken();


        deviceWidth = H.getDeviceWidth(this);
        deviceHeight = H.getDeviceHeight(this);
    }



    private void generateFcmToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }
                    String token = Objects.requireNonNull(task.getResult()).getToken();
                    String id = FirebaseInstanceId.getInstance().getId();
                    new Session(SplashActivity.this).addString(P.fcmToken, token);
                    H.log("fcmTokenIs", token);
                  //  H.log("idIs", id);
                });
        String token =FirebaseInstanceId.getInstance().getToken();
        new Session(SplashActivity.this).addString(P.fcmToken, token);
        H.log("fcmTokenIs", token);
    }
}