package com.getsetgoapp.activity;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.getsetgoapp.R;
import com.getsetgoapp.databinding.ActivityLoginBinding;
import com.getsetgoapp.util.App;
import com.getsetgoapp.util.Click;
import com.getsetgoapp.util.JumpToLogin;
import com.getsetgoapp.util.P;
import com.getsetgoapp.util.Validation;
import com.getsetgoapp.util.WindowView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private final LoginActivity activity = this;
    private ActivityLoginBinding binding;
    private LoadingDialog loadingDialog;

    private CallbackManager callbackManager;
    private LoginManager loginManager;

    private static final int RC_SIGN_IN_GOOGLE = 1;
    private static final int facebookFlag = 11;
    private static final int googleFlag = 22;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initView();
    }

    private void initView() {
        initFacebookLogin();
        facebookLogin();
        onInitGoogle();
        onClick();
    }

    private void onClick() {

        binding.txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                Intent intent = new Intent(activity,ForgetPasswordActivity.class);
                startActivity(intent);
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

        binding.imgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                checkForFacebookLogin();
            }
        });

        binding.imgGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                requestGoogleLogin();
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

    private void callLoginApi() {
        loadingDialog = new LoadingDialog(activity, false);
        Json json = new Json();
        json.addString(P.email, binding.etxEmailAddress.getText().toString());
        json.addString(P.password, binding.etxPassword.getText().toString());
        json.addString(P.fcm_value, new Session(activity).getString(P.fcm_value));

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
                        } else if (Json1.getInt(P.status) == 2) {
                            Intent mainIntent = new Intent(activity, OTPVerficationActivity.class);
                            mainIntent.putExtra(P.email, binding.etxEmailAddress.getText().toString());
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
                        } else if (Json1.getInt(P.status) == 1) {
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
                            session.addString(P.qr_code, Json1.getString(P.qr_code) + "");
                            session.addString(P.mobile_terms_accepted, Json1.getString(P.mobile_terms_accepted) + "");

                            App.authToken = token;
                            App.user_id = user_id;

                            App.startHomeActivity(activity);
                            finish();

                        }
                    }

                }).run("login");
    }


    private void initFacebookLogin() {
        mAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(activity);
        callbackManager = CallbackManager.Factory.create();
    }


    private void onInitGoogle() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void checkForFacebookLogin() {
        loginManager.logInWithReadPermissions(
                activity,
                Arrays.asList(
                        "email",
                        "public_profile",
                        "user_birthday"));
    }

    private void requestGoogleLogin() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, RC_SIGN_IN_GOOGLE);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        H.showMessage(activity, "Connection fail.");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // add this line
        callbackManager.onActivityResult(
                requestCode,
                resultCode,
                data);

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            String name = result.getSignInAccount().getDisplayName();
            String email = result.getSignInAccount().getEmail();
            Uri image = result.getSignInAccount().getPhotoUrl();
            successDialog(googleFlag,name,email,image);
        } else {
            H.showMessage(activity, "Sign in cancel");
        }
    }

    private void logOutFromGoogle() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            requestGoogleLogin();
                        } else {
                            H.showMessage(activity, "Session not close");
                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            String name = currentUser.getDisplayName();
                            String email = currentUser.getEmail();
                            currentUser.getUid();
                            Uri image = currentUser.getPhotoUrl();
                            successDialog(facebookFlag,name,email,image);
                        } else {
                            H.showMessage(activity,"Authentication failed.");
                        }
                    }
                });
    }

    private void checkForFacebookLogin(AccessToken token){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser==null){
            handleFacebookAccessToken(token);
        }else {
            String name = currentUser.getDisplayName();
            String email = currentUser.getEmail();
            currentUser.getUid();
            Uri image = currentUser.getPhotoUrl();
            successDialog(facebookFlag,name,email,image);
        }
    }

    public void facebookLogin() {

        loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();

        loginManager.registerCallback(
                        callbackManager,
                        new FacebookCallback<LoginResult>() {

                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                GraphRequest request = GraphRequest.newMeRequest(

                                        loginResult.getAccessToken(),

                                        new GraphRequest.GraphJSONObjectCallback() {

                                            @Override
                                            public void onCompleted(JSONObject object,
                                                                    GraphResponse response) {

                                                if (object != null) {
                                                    try {
                                                        String name = object.getString("name");
                                                        String email = object.getString("email");
                                                        String fbUserID = object.getString("id");
                                                        checkForFacebookLogin(loginResult.getAccessToken());
                                                        disconnectFromFacebook();
                                                    } catch (JSONException | NullPointerException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                            }
                                        });

                                Bundle parameters = new Bundle();
                                parameters.putString(
                                        "fields",
                                        "id, name, email, gender, birthday");
                                request.setParameters(parameters);
                                request.executeAsync();
                            }

                            @Override
                            public void onCancel() {
                                H.showMessage(activity,"Something went wrong, try again");
                            }

                            @Override
                            public void onError(FacebookException error) {
                                // here write code when get error
                                H.showMessage(activity,"Something went wrong, try again");
                            }
                        });
    }



    public void disconnectFromFacebook()
    {
        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/permissions/",
                null,
                HttpMethod.DELETE,
                new GraphRequest
                        .Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse)
                    {
                        LoginManager.getInstance().logOut();
                    }
                })
                .executeAsync();
    }

    private void successDialog(int flag,String name, String email, Uri image) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setTitle("");
        dialog.setContentView(R.layout.activity_success_view);
        CircleImageView imgUser = dialog.findViewById(R.id.imgUser);
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        TextView txtUserName = dialog.findViewById(R.id.txtUserName);
        TextView txtUserEmail = dialog.findViewById(R.id.txtUserEmail);
        TextView txtChangeAccount = dialog.findViewById(R.id.txtChangeAccount);
        Picasso.get().load(image).error(R.mipmap.ic_launcher).into(imgUser);

        txtTitle.setText("Google Login");

        if (flag==facebookFlag){
            txtTitle.setText("Facebook Login");
            txtChangeAccount.setText("Cancel");
        }else if (flag==googleFlag){
            txtTitle.setText("Google Login");
        }

        txtUserName.setText(name);
        txtUserEmail.setText(email);
        txtChangeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag==facebookFlag){
                    dialog.cancel();
                }else if (flag==googleFlag){
                    dialog.cancel();
                    logOutFromGoogle();
                }
            }
        });
        dialog.findViewById(R.id.txtContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                callSignUpApi(email);
            }
        });
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
    }

    private void callSignUpApi(String emailID) {
        loadingDialog = new LoadingDialog(activity, false);
        Json json = new Json();
        json.addString(P.email, emailID);
        json.addString(P.fcm_value, new Session(activity).getString(P.fcm_value));

        Api.newApi(activity, P.baseUrl + "social_login")
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
                        if (Json1.getInt(P.status) == 1) {
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
                            session.addString(P.qr_code, Json1.getString(P.qr_code) + "");
                            session.addString(P.mobile_terms_accepted, Json1.getString(P.mobile_terms_accepted) + "");

                            App.authToken = token;
                            App.user_id = user_id;

                            App.startHomeActivity(activity);
                            finish();
                        } else {
                            H.showMessage(activity, Json1.getString(P.msg));
                        }
                    }

                }).run("callSignUpApi");
    }

}