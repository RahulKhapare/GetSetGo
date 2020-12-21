package com.getsetgo.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Session;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivityLoginBinding;
import com.getsetgo.util.Click;
import com.getsetgo.util.P;
import com.getsetgo.util.Validation;
import com.getsetgo.util.WindowView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private LoginActivity activity = this;
    private ActivityLoginBinding binding;
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    private static final int RC_SIGN_IN = 31;
    private static final String PROFILE_PUBLIC = "public_profile";
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
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
    }

    private void setUpGoogle() {

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.CREDENTIALS_API)
                .build();

        HintRequest hintRequest = new HintRequest.Builder()
                .setEmailAddressIdentifierSupported(true)
                .build();

        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(googleApiClient, hintRequest);

        try {
            startIntentSenderForResult(intent.getIntentSender(), 31, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
                LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList(EMAIL, PROFILE_PUBLIC));
            }
        });

        binding.ivGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                setUpGoogle();
            }
        });
    }

    private void setUpFaceBookLogIn() {

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        loadFacebookInfo(loginResult.getAccessToken());
                        Log.e("onSuccess", "isExecuted");
                        //setResult(RESULT_OK);
                    }

                    @Override
                    public void onCancel() {
                        Log.e("onCancel", "isExecuted");
                        //setResult(RESULT_CANCELED);
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

    private void loadFacebookInfo(final AccessToken token) {
        GraphRequest request = GraphRequest.newMeRequest(token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject json, GraphResponse response) {
                        //socialLogin("facebook", Api.getString(json, "name"),Api.getString(json, "email"), token.getToken());
                        Log.e("usernameIs", json.toString());

                        try {
                            Session session = new Session(LoginActivity.this);

                            session.addString(P.full_name, json.getString("name") + "");
                            session.addString(P.email_id, json.getString("email") + "");
                            session.addString(P.id, json.getString("id") + "");

                            //hitSocialLoginApi(session, 3);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.e("responseIs", response.toString());
                    }//
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 31) {
            Log.e("dataIs", data + "");
            Log.e("resultCodeIs", requestCode + "");

            Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
            credential.getId();  // will need to process phone number string

            Log.e("givenNameIs", "" + credential.getGivenName());
            Log.e("nameIs", "" + credential.getName());
            Log.e("profileUriIs", "" + credential.getProfilePictureUri());
            Log.e("idIs", "" + credential.getId());

            Session session = new Session(this);
            session.addString(P.full_name, credential.getName() + "");
            session.addString(P.profile_url, credential.getProfilePictureUri() + "");
            session.addString(P.email_id, credential.getId() + "");

            if (credential.getId() != null){

            }
               // hitSocialLoginApi(session, 2);
            else
                H.showMessage(this, "Could not login. Please try another login methods");

        }

    }

    private void handleIntent(FirebaseUser signInResult) {
        Log.e("TAG", "display name: " + signInResult.getEmail());
        Log.e("givenNameIs", "" + signInResult.getDisplayName());
        Log.e("profileUriIs", "" + signInResult.getPhotoUrl());
        Log.e("idIs", "" + signInResult.getUid());
        Session session = new Session(this);
        session.addString(P.full_name, signInResult.getDisplayName() + "");
        session.addString(P.profile_url, signInResult.getPhotoUrl() + "");
        session.addString(P.email_id, signInResult.getEmail() + "");

        if (signInResult.getUid() != null) {
            //hitSocialLoginApi(session, 2);
        } else
            H.showMessage(this, "Could not login. Please try another login methods");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("TAG", "onConnectionFailed:" + connectionResult);
    }
}