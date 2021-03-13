package com.getsetgoapp.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.adoisstudio.helper.Session;
import com.getsetgoapp.Fragment.AccountFragment;
import com.getsetgoapp.Fragment.AddNewUserFragment;
import com.getsetgoapp.Fragment.BankDetailsFragment;
import com.getsetgoapp.Fragment.BestSellingCourseFragment;
import com.getsetgoapp.Fragment.CourseDetailFragment;
import com.getsetgoapp.Fragment.CurrentLearningFragment;
import com.getsetgoapp.Fragment.DashBoardFragment;
import com.getsetgoapp.Fragment.EarningsFragment;
import com.getsetgoapp.Fragment.FreeCourseFragment;
import com.getsetgoapp.Fragment.HelpAndSupportFragment;
import com.getsetgoapp.Fragment.HomeFragment;
import com.getsetgoapp.Fragment.IncentivesFragment;
import com.getsetgoapp.Fragment.KYCDocumentFragment;
import com.getsetgoapp.Fragment.MyPointsFragment;
import com.getsetgoapp.Fragment.NomineeDetailsFragment;
import com.getsetgoapp.Fragment.NotificationsFragment;
import com.getsetgoapp.Fragment.ParentCategoriesFragment;
import com.getsetgoapp.Fragment.SearchFragment;
import com.getsetgoapp.Fragment.SearchUserIdFragment;
import com.getsetgoapp.Fragment.TermsAndConditionFragment;
import com.getsetgoapp.Fragment.TotalDirectUsersFragment;
import com.getsetgoapp.Fragment.TotalUsersFragment;
import com.getsetgoapp.Fragment.TransactionsHistoryFragment;
import com.getsetgoapp.Fragment.WebViewFragment;
import com.getsetgoapp.Fragment.YourCourseFragment;
import com.getsetgoapp.Model.CourseDocumentModel;
import com.getsetgoapp.R;
import com.getsetgoapp.databinding.ActivityBaseScreenBinding;
import com.getsetgoapp.util.App;
import com.getsetgoapp.util.Click;
import com.getsetgoapp.util.Config;
import com.getsetgoapp.util.DocumentDownloader;
import com.getsetgoapp.util.JumpToLogin;
import com.getsetgoapp.util.OpenFile;
import com.getsetgoapp.util.P;
import com.getsetgoapp.util.WindowView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

import static com.getsetgoapp.Fragment.CurrentLearningFragment.PLAYBACK_POSITION;
import static com.getsetgoapp.Fragment.CurrentLearningFragment.REQUEST_CODE;
import static com.getsetgoapp.Fragment.CurrentLearningFragment.RESULT_CODE;


public class BaseScreenActivity extends AppCompatActivity {


    private final BaseScreenActivity activity = this;
    public static ActivityBaseScreenBinding binding;

    private HomeFragment homeFragment;
    DashBoardFragment dashBoardFragment;
    AccountFragment accountFragment;
    NotificationsFragment notificationsFragment;
    MyPointsFragment myPointsFragment;
    IncentivesFragment incentivesFragment;
    HelpAndSupportFragment helpAndSupportFragment;
    TermsAndConditionFragment termsAndConditionFragment;
    NomineeDetailsFragment nomineeDetailsFragment;
    KYCDocumentFragment kycDocumentFragment;
    BankDetailsFragment bankDetailsFragment;

    ParentCategoriesFragment parentCategoriesFragment;
    BestSellingCourseFragment bestSellingCourseFragment;
    AddNewUserFragment addNewUserFragment;
    SearchUserIdFragment searchUserIdFragment;
    YourCourseFragment yourCourseFragment;
    TotalUsersFragment totalUsersFragment;
    TotalDirectUsersFragment totalDirectUsersFragment;
    CurrentLearningFragment currentLearningFragment;
    LinearLayout lnrDashboard, lnrUser, lnrEarning, lnrBusiness, lnrTransaction, lnrInsentive, lnrPoints;
    CheckBox cbMyEarning, cbUsers, cbBusiness, cbTransaction;
    OnBackPressedCallback onBackPressedCallback;
    private LoadingDialog loadingDialog;

    private static final int READ_WRITE = 20;
    private String pdf_url = "";
    private String pdf_title = "";

    public static JsonList occupation_list = null;
    public static JsonList marital_status_list = null;
    public static JsonList gender_list = null;

    public static String termConditionUrl = "";
    public static String privacyPolicyUrl = "";
    public static String organization_chart_url = "";
    public static String faq_url = "";
    public static String action;
    public static String action_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_base_screen);
        getAccess();
        init();
    }

    private void onItemClick() {

        lnrUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbUsers.isChecked()) {
                    cbUsers.setChecked(false);
                } else {
                    cbUsers.setChecked(true);
                }
            }
        });

        lnrEarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbMyEarning.isChecked()) {
                    cbMyEarning.setChecked(false);
                } else {
                    cbMyEarning.setChecked(true);
                }
            }
        });

        lnrBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbBusiness.isChecked()) {
                    cbBusiness.setChecked(false);
                } else {
                    cbBusiness.setChecked(true);
                }
            }
        });

        lnrTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbTransaction.isChecked()) {
                    cbTransaction.setChecked(false);
                } else {
                    cbTransaction.setChecked(true);
                }
            }
        });

    }

    protected void onCheckView() {

        lnrDashboard = findViewById(R.id.lnrDashboard);
        lnrUser = findViewById(R.id.lnrUser);
        lnrEarning = findViewById(R.id.lnrEarning);
        lnrBusiness = findViewById(R.id.lnrBusiness);
        lnrTransaction = findViewById(R.id.lnrTransaction);
        lnrPoints = findViewById(R.id.lnrPoints);
        lnrInsentive = findViewById(R.id.lnrInsentive);


        cbUsers = findViewById(R.id.cbUsers);
        cbMyEarning = findViewById(R.id.cbMyEarning);
        cbBusiness = findViewById(R.id.cbBusiness);
        cbTransaction = findViewById(R.id.cbTransaction);

        checkBoxUsers();
        checkBoxMyEarning();
        checkBoxBisness();
        checkBoxTransaction();
        onItemClick();
    }

    private void checkBoxUsers() {
        LinearLayout llCbUsers = findViewById(R.id.llCbUsersExpand);
        cbUsers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    llCbUsers.setVisibility(View.VISIBLE);
                    isCollapse(cbMyEarning);
                    isCollapse(cbBusiness);
                    isCollapse(cbTransaction);
                } else {
                    llCbUsers.setVisibility(View.GONE);
                }
            }
        });
    }

    private void checkBoxMyEarning() {
        LinearLayout llCbMyEarningExpand = findViewById(R.id.llCbMyEarningExpand);
        cbMyEarning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    llCbMyEarningExpand.setVisibility(View.VISIBLE);
                    isCollapse(cbUsers);
                    isCollapse(cbBusiness);
                    isCollapse(cbTransaction);
                } else {
                    llCbMyEarningExpand.setVisibility(View.GONE);
                }
            }
        });
    }


    private void checkBoxBisness() {
        LinearLayout llCbBusinessExpand = findViewById(R.id.llCbBusinessExpand);
        cbBusiness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    llCbBusinessExpand.setVisibility(View.VISIBLE);
                    isCollapse(cbUsers);
                    isCollapse(cbMyEarning);
                    isCollapse(cbTransaction);
                } else {
                    llCbBusinessExpand.setVisibility(View.GONE);
                }
            }
        });
    }

    private void checkBoxTransaction() {
        LinearLayout llCbTransactionExpand = findViewById(R.id.llCbTransactionExpand);
        cbTransaction.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    llCbTransactionExpand.setVisibility(View.VISIBLE);
                    isCollapse(cbUsers);
                    isCollapse(cbMyEarning);
                    isCollapse(cbBusiness);
                } else {
                    llCbTransactionExpand.setVisibility(View.GONE);
                }
            }
        });
    }

    private void checkAffiliate() {

        String is_affiliate = new Session(this).getString(P.is_affiliate);
//        String is_affiliate = "1";

        if (is_affiliate.equals("0")) {
            lnrDashboard.setVisibility(View.GONE);
            lnrUser.setVisibility(View.GONE);
            lnrEarning.setVisibility(View.GONE);
            lnrBusiness.setVisibility(View.GONE);
            lnrTransaction.setVisibility(View.GONE);
            lnrPoints.setVisibility(View.GONE);
            lnrInsentive.setVisibility(View.GONE);
        }

    }

    private void init() {

        onCheckView();
        checkAffiliate();

        onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                } else/* if (!homeFragment.isVisible()) {
                    onBackClick(findViewById(R.id.ivBack));
                } else*/ if (binding.bottomNavigation.getSelectedItemId() != R.id.menu_home) {
                    loadHomeFragment();
                } else {
                    //super.onBackPressed();
                    finish();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        if (SplashActivity.deviceHeight == 0)
            SplashActivity.deviceHeight = H.getDeviceHeight(this);
        if (SplashActivity.deviceWidth == 0)
            SplashActivity.deviceWidth = H.getDeviceWidth(this);

        loadHomeFragment();

        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                Bundle bundle = null;
                boolean isHide = false;
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        fragment = new HomeFragment();
                        isHide = false;
                        break;

                    case R.id.menu_search:
                        fragment = new SearchFragment();
                        isHide = true;
                        break;
                    case R.id.menu_yourCourse:
                        fragment = new YourCourseFragment();
                        isHide = true;
                        break;

                    case R.id.menu_freeCourse:
                        fragment = new FreeCourseFragment();
                        isHide = true;
                        break;

                    case R.id.menu_Account:
                        bundle = new Bundle();
                        bundle.putBoolean("isFromBottom", true);
                        fragment = new AccountFragment();
                        fragment.setArguments(bundle);
                        isHide = true;
                        break;
                }
                return loadFragment(fragment, isHide);
            }
        });

        try {
            setDrawerData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        hitInitApi(this);
        getNotificationAction();
    }

    private void setDrawerData() throws Exception {
        Session session = new Session(this);
        String name = session.getString(P.name);
        String lastName = session.getString(P.lastname);
        String email = session.getString(P.email);

        if (name == null || name.isEmpty())
            return;

        TextView txtName = findViewById(R.id.txtName);
        TextView txtEmail = findViewById(R.id.txtEmail);
        txtName.setText(name + " " + lastName);
        txtEmail.setText(email);
    }

    private void loadHomeFragment() {
        if (homeFragment == null)
            homeFragment = HomeFragment.newInstance();
        fragmentLoader(homeFragment, false);
    }

    private boolean loadFragment(Fragment fragment, boolean isHideToolbar) {
        if (isHideToolbar) {
            binding.incBasetool.content.setVisibility(View.GONE);
            binding.incFragmenttool.content.setVisibility(View.VISIBLE);
        } else {
            binding.incBasetool.content.setVisibility(View.VISIBLE);
            binding.incFragmenttool.content.setVisibility(View.GONE);
            binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
        }

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }

    public void onDrawerItemClick(View view) {
        int i = view.getId();
        Click.preventTwoClick(view);
        Bundle bundle = new Bundle();
        switch (i) {
            case R.id.txtDashboard:
                if (dashBoardFragment == null)
                    dashBoardFragment = DashBoardFragment.newInstance();
                fragmentLoader(dashBoardFragment, true);
                break;
            case R.id.txtAccount:
                if (accountFragment == null)
                    bundle.putBoolean("isFromBottom", false);
                accountFragment = AccountFragment.newInstance();
                accountFragment.setArguments(bundle);
                fragmentLoader(accountFragment, true);
                break;
            case R.id.txtNotifications:
                loadNotificationFragment();
                break;

//            case R.id.txtEarning:
//                EarningsFragment earningsFragment;
//                bundle.putString("tabItem", "Course Earnings");
//                earningsFragment = EarningsFragment.newInstance();
//                earningsFragment.setArguments(bundle);
//                EarningsFragment.isFromBack = false;
//                fragmentLoader(earningsFragment, true);
//                break;

            case R.id.txtCourseEarning:
                EarningsFragment coursEarning;
                bundle.putString("tabItem", "Course Earnings");
                coursEarning = EarningsFragment.newInstance();
                EarningsFragment.isFromBack = false;
                coursEarning.setArguments(bundle);
                fragmentLoader(coursEarning, true);
                break;

            case R.id.txtCrashCourseEarning:
                EarningsFragment crashEarning;
                bundle.putString("tabItem", "Crash Course Earnings");
                crashEarning = EarningsFragment.newInstance();
                EarningsFragment.isFromBack = false;
                crashEarning.setArguments(bundle);
                fragmentLoader(crashEarning, true);
                break;

            case R.id.txtTotalEaring:
                EarningsFragment totalEarning;
                bundle.putString("tabItem", "Total Earnings");
                totalEarning = EarningsFragment.newInstance();
                EarningsFragment.isFromBack = false;
                totalEarning.setArguments(bundle);
                fragmentLoader(totalEarning, true);
                break;

            case R.id.txtTransactionAll:
                bundle.putString("tabTCItem", "Course");
                TransactionsHistoryFragment transactionsHistoryFragment1;
                transactionsHistoryFragment1 = TransactionsHistoryFragment.newInstance();
                TransactionsHistoryFragment.isFromTransHistory = false;
                TransactionsHistoryFragment.isFromSearch = false;
                transactionsHistoryFragment1.setArguments(bundle);
                fragmentLoader(transactionsHistoryFragment1, true);
                break;

            case R.id.txtTransactionCrashCourse:
                bundle.putString("tabTCItem", "Crash Course");
                TransactionsHistoryFragment transactionsHistoryFragment2;
                transactionsHistoryFragment2 = TransactionsHistoryFragment.newInstance();
                TransactionsHistoryFragment.isFromTransHistory = false;
                TransactionsHistoryFragment.isFromSearch = false;
                transactionsHistoryFragment2.setArguments(bundle);
                fragmentLoader(transactionsHistoryFragment2, true);
                break;

            case R.id.txtBankDetails:
                if (bankDetailsFragment == null)
                    bankDetailsFragment = BankDetailsFragment.newInstance();
                fragmentLoader(bankDetailsFragment, true);
                break;

            case R.id.txtKYCDocument:
                if (kycDocumentFragment == null)
                    kycDocumentFragment = KYCDocumentFragment.newInstance();
                fragmentLoader(kycDocumentFragment, true);
                break;

            case R.id.txtNomineeDetails:
                if (nomineeDetailsFragment == null)
                    nomineeDetailsFragment = NomineeDetailsFragment.newInstance();
                fragmentLoader(nomineeDetailsFragment, true);
                break;

            case R.id.lnrPoints:
                if (myPointsFragment == null)
                myPointsFragment = MyPointsFragment.newInstance();
                fragmentLoader(myPointsFragment, true);
                break;

            case R.id.lnrInsentive:
                if (incentivesFragment == null)
                    IncentivesFragment.isSearch = false;
                incentivesFragment = IncentivesFragment.newInstance();
                fragmentLoader(incentivesFragment, true);
                break;

            case R.id.txtHelpSupport:
                if (helpAndSupportFragment == null)
                    helpAndSupportFragment = HelpAndSupportFragment.newInstance();
                fragmentLoader(helpAndSupportFragment, true);
                break;

            case R.id.txtViewAllBestCourse:
                Config.courseTitle = "Best Selling Course";
                Config.courseJsonList = HomeFragment.bestselling_course_list;
                if (bestSellingCourseFragment == null)
                    bestSellingCourseFragment = BestSellingCourseFragment.newInstance();
                fragmentLoader(bestSellingCourseFragment, true);
                break;

            case R.id.txtViewAllCategories:
                if (parentCategoriesFragment == null)
                    parentCategoriesFragment = ParentCategoriesFragment.newInstance();
                fragmentLoader(parentCategoriesFragment, true);
                break;

            case R.id.txtAddUser:
                if (addNewUserFragment == null)
                    addNewUserFragment = AddNewUserFragment.newInstance();
                fragmentLoader(addNewUserFragment, true);
                break;

            case R.id.txtOrganizationChart:
                Config.ORGNIZE_URL = organization_chart_url;
                if (searchUserIdFragment == null)
                    searchUserIdFragment = SearchUserIdFragment.newInstance();
                fragmentLoader(searchUserIdFragment, true);
                break;

            case R.id.txtUsersList://
                Config.FROM_DASBOARD = true;
                if (totalUsersFragment == null)
                    totalUsersFragment = TotalUsersFragment.newInstance();
                fragmentLoader(totalUsersFragment, true);
                break;

            case R.id.txtDirectUserList://
                Config.FROM_DASBOARD = true;
                if (totalDirectUsersFragment == null)
                    totalDirectUsersFragment = TotalDirectUsersFragment.newInstance();
                fragmentLoader(totalDirectUsersFragment, true);
                break;

            case R.id.txtLogout:
                onLogout();
                break;

            case R.id.txtViewAll:
                if (yourCourseFragment == null)
                    yourCourseFragment = YourCourseFragment.newInstance();
                fragmentLoader(yourCourseFragment, true);
                break;

            case R.id.txttermCondition:
                WebViewFragment termConditionFragment = WebViewFragment.newInstance();
                Bundle b = new Bundle();
                b.putBoolean("isFromBottom", false);
                termConditionFragment.setArguments(b);
                Config.flag = Config.term;
                Config.webViewUrl = termConditionUrl;
                fragmentLoader(termConditionFragment, true);
                break;

            case R.id.txtPrivacy:
                WebViewFragment privacyFragment = WebViewFragment.newInstance();
                Bundle b2 = new Bundle();
                b2.putBoolean("isFromBottom", false);
                privacyFragment.setArguments(b2);
                Config.flag = Config.privacy;
                Config.webViewUrl = privacyPolicyUrl;
                fragmentLoader(privacyFragment, true);
                break;

        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
    }


    private long l;

    public void fragmentLoader(Fragment fragment, boolean isHideToolbar) {

        if (isHideToolbar) {
            binding.bottomNavigation.setVisibility(View.GONE);
            binding.incBasetool.content.setVisibility(View.GONE);
            binding.incFragmenttool.content.setVisibility(View.VISIBLE);
            BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);
            BaseScreenActivity.binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
        }

        if (fragment instanceof HomeFragment) {
            callBack();
        }

        if (System.currentTimeMillis() - l > 321)
            l = System.currentTimeMillis();
        else
            return;
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.anim_enter, R.anim.anim_exit).addToBackStack(null).replace(R.id.fragment_container, fragment).commit();
    }


    public void Cancel(View view) {
        ((DrawerLayout) findViewById(R.id.drawerLayout)).closeDrawer(GravityCompat.START);
    }

    public void onHamClick(View view) {
        ((DrawerLayout) findViewById(R.id.drawerLayout)).openDrawer(GravityCompat.START);
    }

    public void OnNotifications(View view) {
        loadNotificationFragment();
    }

    public void OnShare(View view) {
        shareApp(activity,new Session(activity).getString(P.app_link));
    }

    public void shareApp(Context context,String link)
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, link);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }


   /* @Override
    public void onBackPressed() {


        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else if (!homeFragment.isVisible()) {
            onBackClick(findViewById(R.id.ivBack));
        } else if (binding.bottomNavigation.getSelectedItemId() != R.id.menu_home) {
            loadHomeFragment();
        } else {
            //super.onBackPressed();
            finish();
        }
    }*/

   /* public void onBackClick(View view) {

        loadHomeFragment();

    }*/


    public static void callBack() {
        binding.incBasetool.content.setVisibility(View.VISIBLE);
        binding.incBasetool.ivHamMenu.setTag("0");
        binding.bottomNavigation.setVisibility(View.VISIBLE);
        binding.bottomNavigation.setSelectedItemId(R.id.menu_home);
        binding.incFragmenttool.content.setVisibility(View.GONE);
        binding.incFragmenttool.ivFilter.setVisibility(View.GONE);
        BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);
    }

    private void isCollapse(CheckBox checkBox) {
        checkBox.setChecked(false);
    }

    private void callLogOutApi() {
        loadingDialog = new LoadingDialog(activity, false);
        Json json = new Json();

        Api.newApi(activity, P.baseUrl + "logout").setMethod(Api.POST)
                .onHeaderRequest(App::getHeaders)
                .onLoading(isLoading -> {
                    if (!isDestroyed()) {
                        if (isLoading)
                            loadingDialog.show("loading...");
                        else
                            loadingDialog.hide();
                    }
                })
                .onError(() ->
                        MessageBox.showOkMessage(this, "Message", "Failed to login. Please try again", () -> {
                        }))
                .onSuccess(Json1 -> {
                    if (Json1 != null) {
                        JumpToLogin.call(Json1, activity);
                        loadingDialog.dismiss();
                        if (Json1.getInt(P.status) == 0) {
                            H.showMessage(activity, Json1.getString(P.err));
                        } else {
                            String message = Json1.getString(P.msg);
                            Json1 = Json1.getJson(P.userdata);

                            App.BackToLogin(activity);
                            finish();
                        }
                    }

                }).run("logout");
    }

    public void onLogout() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setMessage("Do you really want to log out?");
        adb.setTitle("Alert");
        adb.setPositiveButton("Yes", (dialog, which) ->
        {
            Session session = new Session(this);
            boolean bool = session.getBool("isViewed");// for intro of video player
            session.clear();

           /* if (bool)
                session.addBool("isViewed",true);*/

            callLogOutApi();
        });
        adb.setNegativeButton("No", null);
        adb.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            String testResult = data.getStringExtra(PLAYBACK_POSITION);

            long l = Long.parseLong(testResult);

            Log.d("+", "Playback position result " + l);

            CurrentLearningFragment fragment = (CurrentLearningFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            fragment.onActivityResult(requestCode, resultCode, data);

//            Toast.makeText(getActivity(),"Playback position result "+testResult,Toast.LENGTH_SHORT).show();
        }
    }

    public void checkPDFPath(CourseDocumentModel model) {
        pdf_title = model.getFile_name();
        pdf_url = model.getFile();
        checkPDF();
    }

    private void getAccess() {
        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        } catch (Exception e) {

        }
    }

    private void checkPDF() {
        if (TextUtils.isEmpty(pdf_url) || pdf_url.equals("null")) {
            H.showMessage(this, "Something went wrong to download pdf file");
        } else {
            getPermission();
        }
    }

    private void getPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                READ_WRITE);
    }

    public void jumpToSetting() {
        H.showMessage(this, "Please allow permission from setting.");
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_WRITE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkDirectory(this, pdf_url, pdf_title);
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    jumpToSetting();
                } else {
                    getPermission();
                }
                return;
            }
        }
    }

    private void checkDirectory(Context context, String fileURL, String title) {
        try {
            String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/GetSetGo/Document/";
            String fileName = title;
            destination += fileName;
            File direct = new File(destination);
            if (direct.exists()) {
                OpenFile.openPath(context, direct);
            } else {
                DocumentDownloader.download(context, fileURL, title);
            }
        } catch (Exception e) {
            Log.e("TAG", "checkDirectory: " + e.getMessage());
            H.showMessage(context, "Something went wrong, try again.");
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
                            occupation_list = Json1.getJsonList(P.occupation_list);
                            marital_status_list = Json1.getJsonList(P.marital_status_list);
                            gender_list = Json1.getJsonList(P.gender_list);
                            termConditionUrl = Json1.getString(P.terms_and_conditions_url);
                            privacyPolicyUrl = Json1.getString(P.privacy_policy_url);
                            faq_url = Json1.getString(P.faq_url);
                        }
                    }
                }).run("hitInitApi");
    }


    private void getNotificationAction(){
        String action = getIntent().getStringExtra(P.action);
        String action_data = getIntent().getStringExtra(P.action_data);

//        action_data = "super-brain-booster";
//        action = "ACTION_COURSE";

        if (!TextUtils.isEmpty(action) && !action.equals("null") && !TextUtils.isEmpty(action_data) && !action_data.equals("null")){
            if (action.contains(Config.ACTION_COURSE)){
                loadNotificationClickFragment("Student's Brain Booster",action_data);
            }
        }
    }

    private void loadNotificationClickFragment(String title,String slug) {
        Bundle bundle = new Bundle();
        BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
        BaseScreenActivity.binding.incBasetool.content.setVisibility(View.GONE);
        BaseScreenActivity.binding.incFragmenttool.content.setVisibility(View.VISIBLE);
        BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);
        bundle.putString("title", title);
        bundle.putString("slug", slug);
        bundle.putBoolean("isFromHome", true);
        CourseDetailFragment courseDetailFragment = new CourseDetailFragment();
        courseDetailFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, courseDetailFragment)
                .addToBackStack(null)
                .commit();
    }


    private void loadNotificationFragment(){
        NotificationsFragment notificationsFragment = NotificationsFragment.newInstance();
        Bundle b2 = new Bundle();
        b2.putBoolean("isFromBottom", false);
        notificationsFragment.setArguments(b2);
        fragmentLoader(notificationsFragment, true);
    }
}