package com.getsetgo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
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
import com.getsetgo.util.App;
import com.getsetgo.util.Click;
import com.getsetgo.util.JumpToLogin;
import com.getsetgo.util.P;
import com.getsetgo.util.Validation;
import com.getsetgo.util.WindowView;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private final LoginActivity activity = this;
    private ActivityLoginBinding binding;
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    private static final int RC_SIGN_IN = 31;
    private static final String PROFILE_PUBLIC = "public_profile";
    private LoadingDialog loadingDialog;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions mGoogleSignInOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initView();
        setUpGoogle();
    }

    private void initView() {

        onClick();
        setUpFaceBookLogIn();
    }

    private void setUpGoogle() {

        /*GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.CREDENTIALS_API)
                .build();

        HintRequest hintRequest = new HintRequest.Builder()
                .setEmailAddressIdentifierSupported(true)
                .build();

        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(googleApiClient, hintRequest);

        try {
            startIntentSenderForResult(intent.getIntentSender(), RC_SIGN_IN, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }*/

        mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions);
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
                if (checkValidation()) {
                    callLoginApi();
                }
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
//                setUpGoogle();
                startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
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
                            session.addString(P.token, json.getString("token") + "");

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

        if (resultCode == RESULT_OK && requestCode == RC_SIGN_IN) {
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
            session.addString(P.email_id, credential.getId() + "");

            if (credential.getId() != null) {

            }
            // hitSocialLoginApi(session, 2);
            else
                H.showMessage(this, "Could not login. Please try another login methods");

        }

    }

    private void callLoginApi() {
        loadingDialog = new LoadingDialog(activity, false);
        Json json = new Json();
        json.addString(P.email, binding.etxEmailAddress.getText().toString());
        json.addString(P.password, binding.etxPassword.getText().toString());

        Api.newApi(activity, P.baseUrl + "login")
                .addJson(json)
                .setMethod(Api.POST)
                .onHeaderRequest(App::getHeaders)
                .onLoading(isLoading -> {
                    if (!isDestroyed()) {
                        if (isLoading)
                            loadingDialog.show("loading...");
                        else
                            loadingDialog.dismiss();
                    }
                })
                .onError(() ->
                        MessageBox.showOkMessage(this, "Message", "Failed to login. Please try again", () -> {
                            loadingDialog.dismiss();
                        }))
                .onSuccess(Json1 -> {
                    if (Json1 != null) {
                        JumpToLogin.call(Json1, this);
                        loadingDialog.dismiss();
                        if (Json1.getInt(P.status) == 0) {
                            H.showMessage(activity, Json1.getString(P.err));
                        }
                        else if (Json1.getInt(P.status) == 2){
                            Intent mainIntent = new Intent(activity, OTPVerficationActivity.class);
                            mainIntent.putExtra(P.email,binding.etxEmailAddress.getText().toString());
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
                        }
                        else if (Json1.getInt(P.status) == 1){
                            Json1 = Json1.getJson(P.data);
                            Json1 = Json1.getJson(P.userdata);
                            Session session = new Session(this);
                            String token = Json1.getString(P.token);
                            String user_id = Json1.getString(P.user_id);
                            session.addString(P.token, token + "");
                            session.addString(P.user_id, user_id + "");
                            session.addString(P.is_affiliate, Json1.getString(P.is_affiliate));
                            session.addString(P.name, Json1.getString(P.name) + "");
                            session.addString(P.lastname, Json1.getString(P.lastname) + "");
                            session.addString(P.email, Json1.getString(P.email) + "");
                            session.addString(P.contact, Json1.getString(P.contact) + "");
                            session.addString(P.dob, Json1.getString(P.dob) + "");
                            session.addString(P.occupation_id, Json1.getString(P.occupation_id) + "");
                            session.addString(P.marital_status_id, Json1.getString(P.marital_status_id) + "");
                            session.addString(P.gender, Json1.getString(P.gender) + "");
                            session.addString(P.profile_picture, Json1.getString(P.profile_picture) + "");
                            session.addString(P.app_link, Json1.getString(P.app_link) + "");
                            session.addString(P.referral_link, Json1.getString(P.referral_link) + "");

                            App.authToken = token;
                            App.user_id = user_id;

                            App.startHomeActivity(activity);
                            finish();

                        }
                    }

                }).run("login");
    }
}