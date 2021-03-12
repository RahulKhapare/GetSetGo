package com.getsetgoapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.WindowManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.adoisstudio.helper.Session;
import com.getsetgoapp.BuildConfig;
import com.getsetgoapp.R;
import com.getsetgoapp.databinding.ActivitySplashBinding;
import com.getsetgoapp.util.App;
import com.getsetgoapp.util.CheckConnection;
import com.getsetgoapp.util.JumpToLogin;
import com.getsetgoapp.util.P;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    private final SplashActivity activity = this;
    private ActivitySplashBinding binding;
    public static int deviceWidth;
    public static int deviceHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            );
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        getFirebaseToken();
        initView();
        if (CheckConnection.isVailable(activity)){
            hitInitApi(activity);
        }else {
            MessageBox.showOkMessage(activity, "Message", "No internet connection available", () -> {
            });
        }

    }

    private void initView() {
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
        String token = FirebaseInstanceId.getInstance().getToken();
        new Session(SplashActivity.this).addString(P.fcmToken, token);
        H.log("fcmTokenIs", token);
    }

    private void startNextActivity() {
        final String token = new Session(this).getString(P.token);
        final String user_id = new Session(this).getString(P.user_id);

        new Handler().postDelayed(() -> {
            if (token == null || token.isEmpty()) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                H.log("tokenIs", token + "");
                App.startHomeActivity(SplashActivity.this);
                App.authToken = token;
                App.user_id = user_id;
                deviceWidth = H.getDeviceWidth(this);
                deviceHeight = H.getDeviceHeight(this);
            }
            finish();
        }, 2000);
    }

    private void showUpdatePopUP()
    {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Message");
        adb.setMessage("New Update available.");
        adb.setCancelable(false);
        adb.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startNextActivity();
            }
        });
        adb.setPositiveButton("update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                redirectToPlayStore();
            }
        }).show();
    }

    private void redirectToPlayStore()
    {
        final String appPackageName = this.getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void hitInitApi(Context context) {
        LoadingDialog loadingDialog = new LoadingDialog(context, false);
        Api.newApi(context, P.baseUrl + "init")
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
                            String android_min_version = Json1.getString(P.android_min_version);
                            String android_current_version = Json1.getString(P.android_current_version);
                            if (!TextUtils.isEmpty(android_min_version) || !android_min_version.equals("null")){
                                int versionCode = BuildConfig.VERSION_CODE;
                                String versionName = BuildConfig.VERSION_NAME;
                                try {
                                    int currentVersion = Integer.parseInt(android_min_version);
                                    if (versionCode<currentVersion){
                                        showUpdatePopUP();
                                    }else {
                                        startNextActivity();
                                    }
                                }catch (Exception e){
                                    startNextActivity();
                                }
                            }else {
                                startNextActivity();
                            }
                        }
                    }

                }).run("hitInitApi");
    }

    public void getFirebaseToken() {
        Session session = new Session(this);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            String newToken = instanceIdResult.getToken();
            session.addString(P.fcm_value,newToken);
        });
    }

}