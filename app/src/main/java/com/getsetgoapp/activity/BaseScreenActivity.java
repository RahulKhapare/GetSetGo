package com.getsetgoapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.getsetgoapp.Fragment.AllCrashCourseFragment;
import com.getsetgoapp.Fragment.BankDetailsFragment;
import com.getsetgoapp.Fragment.BestSellingCourseFragment;
import com.getsetgoapp.Fragment.CourseDetailFragment;
import com.getsetgoapp.Fragment.CurrentLearningFragment;
import com.getsetgoapp.Fragment.DashBoardFragment;
import com.getsetgoapp.Fragment.EarningsFragment;
import com.getsetgoapp.Fragment.EditProfileFragment;
import com.getsetgoapp.Fragment.FreeCourseFragment;
import com.getsetgoapp.Fragment.HelpAndSupportFragment;
import com.getsetgoapp.Fragment.HomeFragment;
import com.getsetgoapp.Fragment.IncentivesFragment;
import com.getsetgoapp.Fragment.KYCDocumentFragment;
import com.getsetgoapp.Fragment.LiveCourseFragment;
import com.getsetgoapp.Fragment.MyCrashCourseFragment;
import com.getsetgoapp.Fragment.MyOrderFragment;
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
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.getsetgoapp.Fragment.CurrentLearningFragment.PLAYBACK_POSITION;
import static com.getsetgoapp.Fragment.CurrentLearningFragment.REQUEST_CODE;
import static com.getsetgoapp.Fragment.CurrentLearningFragment.RESULT_CODE;


public class BaseScreenActivity extends AppCompatActivity implements Player.EventListener {

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
    AllCrashCourseFragment allCrashCourseFragment;
    BestSellingCourseFragment bestSellingCourseFragment;
    AddNewUserFragment addNewUserFragment;
    SearchUserIdFragment searchUserIdFragment;
    YourCourseFragment yourCourseFragment;
    MyCrashCourseFragment myCrashCourseFragment;
    TotalUsersFragment totalUsersFragment;
    TotalDirectUsersFragment totalDirectUsersFragment;
    CurrentLearningFragment currentLearningFragment;
    LinearLayout lnrDashboard, lnrCrashCourse, lnrUser, lnrEarning, lnrBusiness, lnrTransaction, lnrInsentive, lnrPoints;
    CheckBox cbMyEarning, cbCrashCourse, cbUsers, cbBusiness, cbTransaction;
    OnBackPressedCallback onBackPressedCallback;
    private LoadingDialog loadingDialog;
    LiveCourseFragment liveCourseFragment;

    private static final int REQUEST_GALLARY = 9;
    private static final int PIC_CROP = 22;
    private static final int REQUEST_CAMERA = 10;
    private Uri cameraURI;
    private int click;
    private int cameraClick = 0;
    private int galleryClick = 1;
    private String base64Image = "";

    CircleImageView circleProfileImageView;
    private static final int READ_WRITE = 20;
    private String pdf_url = "";
    private String pdf_title = "";

    private int clickFOR = 0;
    private int clickPDF = 1;
    private int clickIMAGE = 2;

    public static JsonList occupation_list = null;
    public static JsonList marital_status_list = null;
    public static JsonList gender_list = null;

    public static String termConditionUrl = "";
    public static String privacyPolicyUrl = "";
    public static String organization_chart_url = "";
    public static String faq_url = "";

    private SimpleExoPlayer exoPlayer;
    private PlayerView playerView;
    private ProgressBar pbVideoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowView.getWindow(activity);

        try {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } catch (Exception e) {
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_base_screen);
        getAccess();
        init();

        String mobile_terms_accepted = new Session(activity).getString(P.mobile_terms_accepted);
        if (TextUtils.isEmpty(mobile_terms_accepted) || mobile_terms_accepted.equals("0") || mobile_terms_accepted.equals("null")) {
            welcomeDialog(Config.WELCOME_VIDEO, Config.WELCOME_MESSAGE);
        }

    }

    private void onItemClick() {

        lnrCrashCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbCrashCourse.isChecked()) {
                    cbCrashCourse.setChecked(false);
                } else {
                    cbCrashCourse.setChecked(true);
                }
            }
        });

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
        lnrCrashCourse = findViewById(R.id.lnrCrashCourse);
        lnrUser = findViewById(R.id.lnrUser);
        lnrEarning = findViewById(R.id.lnrEarning);
        lnrBusiness = findViewById(R.id.lnrBusiness);
        lnrTransaction = findViewById(R.id.lnrTransaction);
        lnrPoints = findViewById(R.id.lnrPoints);
        lnrInsentive = findViewById(R.id.lnrInsentive);


        cbCrashCourse = findViewById(R.id.cbCrashCourse);
        cbUsers = findViewById(R.id.cbUsers);
        cbMyEarning = findViewById(R.id.cbMyEarning);
        cbBusiness = findViewById(R.id.cbBusiness);
        cbTransaction = findViewById(R.id.cbTransaction);

        checkBoxCrashCourse();
        checkBoxUsers();
        checkBoxMyEarning();
        checkBoxBisness();
        checkBoxTransaction();
        onItemClick();

        Log.e("TAG", "onCheckViewTTT:  " + new Session(activity).getString(P.token));
    }

    private void checkBoxCrashCourse() {
        LinearLayout llCbCrashCourse = findViewById(R.id.llCbCrashCourseExpand);
        cbCrashCourse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    llCbCrashCourse.setVisibility(View.VISIBLE);
                    isCollapse(cbUsers);
                    isCollapse(cbMyEarning);
                    isCollapse(cbBusiness);
                    isCollapse(cbTransaction);
                } else {
                    llCbCrashCourse.setVisibility(View.GONE);
                }
            }
        });
    }

    private void checkBoxUsers() {
        LinearLayout llCbUsers = findViewById(R.id.llCbUsersExpand);
        cbUsers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    llCbUsers.setVisibility(View.VISIBLE);
                    isCollapse(cbCrashCourse);
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
                    isCollapse(cbCrashCourse);
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
                    isCollapse(cbCrashCourse);
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
                    isCollapse(cbCrashCourse);
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
//            lnrCrashCourse.setVisibility(View.GONE);
            lnrUser.setVisibility(View.GONE);
            lnrEarning.setVisibility(View.GONE);
            lnrBusiness.setVisibility(View.GONE);
            lnrTransaction.setVisibility(View.GONE);
            lnrPoints.setVisibility(View.GONE);
            lnrInsentive.setVisibility(View.GONE);
        }

    }

    private void init() {

        final String token = new Session(this).getString(P.token);
        final String user_id = new Session(this).getString(P.user_id);
        App.authToken = token;
        App.user_id = user_id;
        SplashActivity.deviceWidth = H.getDeviceWidth(this);
        SplashActivity.deviceHeight = H.getDeviceHeight(this);

        Log.e("TAG", "UserTokenInit: " + token);

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

        hitInitApi(activity);
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
            case R.id.txtUserProfile:
                loadEditProfileFragment();
                break;
            case R.id.txtAccount:
                if (accountFragment == null)
                    bundle.putBoolean("isFromBottom", false);
                accountFragment = AccountFragment.newInstance();
                accountFragment.setArguments(bundle);
                fragmentLoader(accountFragment, true);
                break;
            case R.id.txtMyOrder:
                loadMyOrderFragment();
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
                Bundle bm = new Bundle();
                bm.putBoolean("isFromDashboard", false);
                myPointsFragment.setArguments(bm);
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
                Config.FROM_ALL_CRASH_COURSE = false;
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
            case R.id.txtReferEarn:
                hitReferralApi(activity);
                break;

            case R.id.txtAllCrashCourse:
                Config.FROM_ALL_CRASH_COURSE = true;
                if (allCrashCourseFragment == null)
                    allCrashCourseFragment = AllCrashCourseFragment.newInstance();
                fragmentLoader(allCrashCourseFragment, true);
                break;
            case R.id.txtMyCrashCourse:
                if (myCrashCourseFragment == null)
                    myCrashCourseFragment = MyCrashCourseFragment.newInstance();
                fragmentLoader(myCrashCourseFragment, true);
                break;
            case R.id.txtLiveCourses:
                if (liveCourseFragment == null)
                    liveCourseFragment = LiveCourseFragment.newInstance();
                fragmentLoader(liveCourseFragment, true);
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
        shareApp(activity, new Session(activity).getString(P.app_link));
    }

    public void shareApp(Context context, String link) {

        String shareMessage = Config.SHARE_MESSAGE_1 + "\n\n" + "Link: " + link + "\nRelationship Manager ID: " + new Session(activity).getString(P.referral_code) + "\n\n" + Config.SHARE_MESSAGE_2;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share Using"));

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

        switch (requestCode) {
            case REQUEST_GALLARY:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    try {
                        Uri selectedImage = data.getData();
                        setImageData(selectedImage);
                    } catch (Exception e) {
                    }
                }
                break;
            case REQUEST_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        performCrop(cameraURI);
                    } catch (Exception e) {
                    }
                }
                break;
            case PIC_CROP:
                if (resultCode == Activity.RESULT_OK) {
                    try {

                        if (data != null) {
                            Bundle extras = data.getExtras();
                            Bitmap bitmap = extras.getParcelable("data");
                            base64Image = encodeImage(bitmap);
                            EditProfileFragment.newInstance().hitUploadImage(activity, base64Image, circleProfileImageView);
                        }
                    } catch (Exception e) {
                    }
                }
                break;
        }
    }

    public void checkPDFPath(CourseDocumentModel model) {
        clickFOR = clickPDF;
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
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
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
                    if (clickFOR == clickPDF) {
                        checkDirectory(this, pdf_url, pdf_title);
                    } else if (clickFOR == clickIMAGE) {
                        if (click == cameraClick) {
                            openCamera();
                        } else if (click == galleryClick) {
                            openGallery();
                        }
                    }
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

    private void getNotificationAction() {

        String action = getIntent().getStringExtra(P.action);
        String action_data = getIntent().getStringExtra(P.action_data);
        String title = getIntent().getStringExtra(P.title);

        if (!TextUtils.isEmpty(action) && !action.equals("null")) {
            if (action.equals(Config.ACTION_COURSE)) {
                if (!TextUtils.isEmpty(action_data) && !action_data.equals("null")) {
                    loadCourseFragment(title, action_data);
                }
            } else if (action.equals(Config.ACTION_GENERAL)) {
                Bundle bundle = new Bundle();
                BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
                BaseScreenActivity.binding.incBasetool.content.setVisibility(View.GONE);
                BaseScreenActivity.binding.incFragmenttool.content.setVisibility(View.VISIBLE);
                BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);
                bundle.putBoolean("isFromBottom", false);
                NotificationsFragment courseDetailFragment = new NotificationsFragment();
                courseDetailFragment.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, courseDetailFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    private void loadCourseFragment(String title, String slug) {
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


    private void loadEditProfileFragment() {
        Config.FROM_DASHBOARD = true;
        EditProfileFragment editProfileFragment = EditProfileFragment.newInstance();
        Bundle b2 = new Bundle();
        b2.putBoolean("isFromBottom", false);
        editProfileFragment.setArguments(b2);
        fragmentLoader(editProfileFragment, true);
    }

    private void loadNotificationFragment() {
        NotificationsFragment notificationsFragment = NotificationsFragment.newInstance();
        Bundle b2 = new Bundle();
        b2.putBoolean("isFromBottom", false);
        notificationsFragment.setArguments(b2);
        fragmentLoader(notificationsFragment, true);
    }

    private void loadMyOrderFragment() {
        MyOrderFragment notificationsFragment = MyOrderFragment.newInstance();
        Bundle b2 = new Bundle();
        b2.putBoolean("isFromBottom", false);
        notificationsFragment.setArguments(b2);
        fragmentLoader(notificationsFragment, true);
    }

    private void welcomeDialog(String url, String message) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_welcome);

        pbVideoPlayer = dialog.findViewById(R.id.pbVideoPlayer);
        playerView = dialog.findViewById(R.id.playerView);

        TextView txtAgree = dialog.findViewById(R.id.txtAgree);
        LinearLayout lnrTele = dialog.findViewById(R.id.lnrTele);
        LinearLayout lnrFace = dialog.findViewById(R.id.lnrFace);


        txtAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                updateFistVisitStatus();
            }
        });
        lnrTele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exoPlayer != null) {
                    exoPlayer.pause();
                }
                openLink(Config.WELCOME_TELE);
            }
        });
        lnrFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exoPlayer != null) {
                    exoPlayer.pause();
                }
                openLink(Config.WELCOME_FACE);
            }
        });

        playVideo(url, false);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (exoPlayer != null) {
                    exoPlayer.stop(true);
                }
            }
        });

        dialog.setCancelable(false);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }


    private void playVideo(String videoPath, boolean replay) {
        exoPlayer = new SimpleExoPlayer.Builder(activity).build();
        playerView.setPlayer(exoPlayer);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.addListener(this);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getApplicationContext(), getApplicationContext().getString(R.string.app_name)));

        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(videoPath));

        exoPlayer.prepare(videoSource);
        exoPlayer.setMediaItem(MediaItem.fromUri(videoPath));
        exoPlayer.play();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.stop(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (exoPlayer != null) {
            exoPlayer.pause();
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if (playbackState == Player.STATE_BUFFERING) {
            pbVideoPlayer.setVisibility(View.VISIBLE);

        } else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED) {
            pbVideoPlayer.setVisibility(View.INVISIBLE);
        }
    }

    private void QRCodeDialog(String count) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_qr_code);

        ImageView imgQR = dialog.findViewById(R.id.imgQR);
        TextView txtQR = dialog.findViewById(R.id.txtQR);
        TextView txtMessage = dialog.findViewById(R.id.txtMessage);


        txtMessage.setText("Users registered using your referral - " + count);
        String qr_code = "qr_code";
        String imagePath = new Session(activity).getString(qr_code);
        if (!imagePath.equals("")) {
            qr_code = imagePath;
        }
        Picasso.get().load(qr_code).error(R.drawable.ic_no_image).into(imgQR);

        txtQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dialog.dismiss();
                shareApp(activity, new Session(activity).getString(P.app_link));
            }
        });

        txtMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Config.FROM_DASBOARD = true;
                if (totalDirectUsersFragment == null)
                    totalDirectUsersFragment = TotalDirectUsersFragment.newInstance();
                fragmentLoader(totalDirectUsersFragment, true);
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        dialog.setCancelable(true);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }

    private void hitReferralApi(Context context) {
        LoadingDialog loadingDialog = new LoadingDialog(context, false);
        Api.newApi(context, P.baseUrl + "referral_users")
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
                            String referral_users = Json1.getString(P.referral_users);
                            QRCodeDialog(referral_users);
                        }
                    }

                }).run("hitReferralApi");
    }


    public void onUploadClick(CircleImageView image) {
        circleProfileImageView = image;
        clickFOR = clickIMAGE;
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_upload_dialog);
        dialog.findViewById(R.id.txtRemove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dialog.cancel();
                EditProfileFragment.newInstance().removePIC(image);
            }
        });
        dialog.findViewById(R.id.txtCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dialog.cancel();
                click = cameraClick;
                getPermission();
            }
        });
        dialog.findViewById(R.id.txtGallary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                dialog.cancel();
                click = galleryClick;
                getPermission();
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    private void openCamera() {
        try {
            String fileName = String.format("%d.jpg", System.currentTimeMillis());
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = new File(Environment.getExternalStorageDirectory(),
                    fileName);
            cameraURI = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraURI);
            startActivityForResult(intent, REQUEST_CAMERA);
        } catch (Exception e) {
            H.showMessage(activity, "Whoops - your device doesn't support capturing images!");
        }
    }

    private void openGallery() {
        try {
            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 0);
            intent.putExtra("aspectY", 0);
            startActivityForResult(intent, REQUEST_GALLARY);
        } catch (Exception e) {
        }
    }

    private void performCrop(Uri picUri) {
        try {

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 128);
            cropIntent.putExtra("outputY", 128);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, PIC_CROP);
        } catch (Error anfe) {
            H.showMessage(activity, "Whoops - your device doesn't support the crop action!");
        }
    }

    private void setImageData(Uri uri) {
        base64Image = "";
        try {
            InputStream imageStream = getContentResolver().openInputStream(uri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            base64Image = encodeImage(selectedImage);
            EditProfileFragment.newInstance().hitUploadImage(activity, base64Image, circleProfileImageView);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("TAG", "setImageDataEE: " + e.getMessage());
            H.showMessage(activity, "Unable to get image, try again.");
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }

    public void openLink(String url) {
        if (!TextUtils.isEmpty(url) && !url.equals("null")) {
            try {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url.trim()));
                startActivity(i);
            } catch (Exception e) {
                H.showMessage(activity, "Something went wrong to open link");
            }
        } else {
            H.showMessage(activity, "Path not present, try after some time");
        }

    }


    private void updateFistVisitStatus() {
        loadingDialog = new LoadingDialog(activity, false);
        Json json = new Json();
        Api.newApi(activity, P.baseUrl + "mobile_terms_accepted")
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
                            new Session(activity).addString(P.mobile_terms_accepted, "1");
                            H.showMessage(activity, Json1.getString(P.msg));
                            binding.bottomNavigation.setSelectedItemId(R.id.menu_freeCourse);
                        }
                    }

                }).run("updateFistVisitStatus");
    }

}