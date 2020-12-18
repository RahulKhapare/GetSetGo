package com.getsetgo.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.adoisstudio.helper.H;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivityLoginBinding;
import com.getsetgo.util.Click;
import com.getsetgo.util.Validation;
import com.getsetgo.util.WindowView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private LoginActivity activity = this;
    private ActivityLoginBinding binding;
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    private static final String USER_POSTS = "user_posts";
    private static final String AUTH_TYPE = "rerequest";
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initView();
    }

    private void initView() {

        onClick();
        setUpFaceBookLogIn();
        setUpGoogle();
    }

    private void setUpGoogle() {

        GoogleSignInOptions sign = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, sign)
                .build();
    }

    private void onClick() {

        binding.txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });
        binding.txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                //if (checkValidation()){
                Intent intent = new Intent(activity, HomeScreenActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                //}
            }
        });
        binding.txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Intent intent = new Intent(activity, SignUpActivity.class);
                startActivity(intent);
            }
        });

        binding.ivFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                LoginManager.getInstance().logOut();
                LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList(EMAIL, USER_POSTS));
            }
        });

        binding.ivGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                signIn();
            }
        });
    }

    private void setUpFaceBookLogIn() {

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        //loadFacebookInfo(loginResult.getAccessToken());
                        Log.e("onSuccess", "isExecuted");
                        setResult(RESULT_OK);
                    }

                    @Override
                    public void onCancel() {
                        Log.e("onCancel", "isExecuted");
                        setResult(RESULT_CANCELED);
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e("exceptionIs", exception.getMessage());

                        H.showMessage(LoginActivity.this, "Could not login. Please try another login method");
                    }
                });
    }

    private boolean checkValidation() {
        boolean value = true;

        if (TextUtils.isEmpty(binding.etxEmailAddress.getText().toString().trim())) {
            H.showMessage(activity, "Enter email id");
            value = false;
        } else if (!Validation.validEmail(binding.etxEmailAddress.getText().toString().trim())) {
            H.showMessage(activity, "Enter valid email");
            value = false;
        } else if (TextUtils.isEmpty(binding.etxPassword.getText().toString().trim())) {
            H.showMessage(activity, "Enter password");
            value = false;
        }

        return value;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 31) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleIntent(result);
        }

    }

    private void handleIntent(GoogleSignInResult signInResult) {
        Log.d("TAG", "handleSignInResult:" + signInResult.isSuccess());
        if (signInResult.isSuccess()) {
            GoogleSignInAccount signInAccount = signInResult.getSignInAccount();
            Log.e("TAG", "display name: " + signInAccount.getDisplayName());
        } else {
        }

    }

    private void signIn() {
        Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signIntent, 31);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("TAG", "onConnectionFailed:" + connectionResult);
    }
}