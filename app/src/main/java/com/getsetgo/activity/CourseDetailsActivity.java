package com.getsetgo.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.Session;


import com.getsetgo.Adapter.CurriculumLectureAdapter;
import com.getsetgo.Adapter.StudentsFeedbackAdapter;
import com.getsetgo.Others.CustomVideoView;
import com.getsetgo.Others.VideoControllerForCourse;
import com.getsetgo.R;
import com.getsetgo.util.App;

import com.getsetgo.util.P;
import com.getsetgo.util.WindowView;

import java.util.ArrayList;
import java.util.Map;


public class CourseDetailsActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, Api.OnHeaderRequestListener {

    CurriculumLectureAdapter curriculumLectureAdapter;
    StudentsFeedbackAdapter studentsFeedbackAdapter;
    LinearLayoutManager layoutManager;
    LinearLayoutManager mLayoutManagerStudentFeedback;

    RecyclerView recyclerViewLecture, recyclerViewFeedback;
    LinearLayout llCourseIncludes, llLearn;

    TextView t[];
    TextView tC[];

    Context context;


    //private CustomMediaController customMediaController;
    private CustomVideoView customVideoView;
    private MediaPlayer mediaPlayer;
    private VideoControllerForCourse videoController;
    private LoadingDialog loadingDialog;
    private String seriesId = "";
    private String videoId = "";
    public static boolean refreshHome;

    private String checkUrl = "";
    private String apiUrl = "";
    private String videoUrl = "https://player.vimeo.com/external/355047357.sd.mp4?s=70acd77556613cd48ac925134cac183d18287c45&profile_id=164";
    private int videoProgress;
    private int isFavourite;
    private boolean startFromPreviousPosition;
    private int brightness;
    private int volume;
    private int maxVolume;
    private GestureDetectorCompat gestureDetectorCompat;
    private AudioManager audioManager;

    public static boolean isTrailer;
    public static ArrayList<String> fullVideoResolutionList = new ArrayList<>();
    public static ArrayList<String> fullVideoUrlList = new ArrayList<>();
    public static ArrayList<String> trailerVideoResolutionList = new ArrayList<>();
    public static ArrayList<String> trailerVideoUrlList = new ArrayList<>();

    // following is for brightness and volume
    private ProgressBar brightnessProgressBar, volumeProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        context = CourseDetailsActivity.this;
        init();
        videoInit();
    }

    private void init() {


        recyclerViewFeedback = findViewById(R.id.recyclerViewFeedback);
        recyclerViewLecture = findViewById(R.id.recyclerViewLecture);
        llCourseIncludes = findViewById(R.id.llCourseIncludes);
        llLearn = findViewById(R.id.llLearn);
        TextView txtDesc = findViewById(R.id.txtDesc);

        txtDesc.setText(R.string.dummy_text);

        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mLayoutManagerStudentFeedback = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);


        setupRecyclerViewCurriculumLecture();
        setupRecyclerViewStudenrFeedback();

        dynamicTextView();

        recyclerViewFeedback.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                    //fetch("onScrollStateChanged");

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                    fetch("onScrolled");

            }
        });

    }

    private void dynamicTextView() {
        Typeface typeface = ResourcesCompat.getFont(context, R.font.nunito_sans_regular);
        LinearLayout.LayoutParams dim = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        String[] arr = context.getResources().getStringArray(R.array.listArray);
        t = new TextView[arr.length];
        textViewMore(t, dim, typeface, arr, arr.length, llCourseIncludes);

        LinearLayout.LayoutParams llm = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        String[] arrCat = context.getResources().getStringArray(R.array.categoryArray);
        tC = new TextView[arrCat.length];
        textViewMore(tC, llm, typeface, arrCat, arrCat.length, llLearn);
    }

    private void fetch(String msg) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //H.showMessage(context,msg);
                studentsFeedbackAdapter = new StudentsFeedbackAdapter(context, 20);
                recyclerViewFeedback.setAdapter(studentsFeedbackAdapter);
                studentsFeedbackAdapter.notifyDataSetChanged();
            }
        }, 3000);
    }

    private void textViewMore(TextView tC[], LinearLayout.LayoutParams dim, Typeface typeface, String[] arrCat, int length, LinearLayout linearLayout) {
        for (int i = 0; i < length; i++) {
            tC[i] = new TextView(context);
            tC[i].setLayoutParams(dim);
            tC[i].setText("\u25CF  " + arrCat[i]);
            tC[i].setTextSize(14);
            tC[i].setTypeface(typeface);
            tC[i].setTextColor(context.getResources().getColor(R.color.colorTextDark));
            linearLayout.addView(tC[i]);
        }
    }

    private void setupRecyclerViewCurriculumLecture() {
        recyclerViewLecture.setLayoutManager(layoutManager);
        curriculumLectureAdapter = new CurriculumLectureAdapter(this, 5);
        recyclerViewLecture.setItemAnimator(new DefaultItemAnimator());
        recyclerViewLecture.setAdapter(curriculumLectureAdapter);
        curriculumLectureAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerViewStudenrFeedback() {
        recyclerViewFeedback.setLayoutManager(mLayoutManagerStudentFeedback);
        studentsFeedbackAdapter = new StudentsFeedbackAdapter(context, 10);
        recyclerViewFeedback.setItemAnimator(new DefaultItemAnimator());
        recyclerViewFeedback.setAdapter(studentsFeedbackAdapter);
        studentsFeedbackAdapter.notifyDataSetChanged();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void videoInit() {
        loadingDialog = new LoadingDialog(this);
        videoProgress = getIntent().getIntExtra("videoProgress", 0);
        startFromPreviousPosition = videoProgress == 0 ? false : true;

        checkUrl = getIntent().getStringExtra(P.url);
        if (checkUrl != null && checkUrl.contains("series_check"))
            apiUrl = checkUrl.replace("series_check", "series");

        //H.log("replacedUrlIs",apiUrl);
        customVideoView = findViewById(R.id.customVideoView);

        //hitSeriesApi();

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        maxVolume = getMaxVolume();
        volume = getDefaultVolumeInPercent();
        volumeProgressBar = findViewById(R.id.volumeProgressBar);
        volumeProgressBar.setProgress(volume);
        volumeProgressBar.setMax(maxVolume);

        brightnessProgressBar = findViewById(R.id.brightnessProgressBar);
        brightness = getDefaultBrightness();
        brightnessProgressBar.setProgress(brightness);

        FrameLayout frameLayout = findViewById(R.id.videoParentLayout);
        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && videoController != null)
                    videoController.handleVisibilityOfVideoController();
                return true;
            }
        });

    }

    private void hitSeriesApi() {

        Api.newApi(this, apiUrl).addJson(new Json())
                .setMethod(Api.GET)
                .onHeaderRequest(CourseDetailsActivity.this)
                .onLoading(this::onLoading)
                .onError(this::onError)
                .onSuccess(json -> {
                    if (json.getInt(P.status) == 0)
                        H.showMessage(context, json.getString(P.err));
                    else {
                        json = json.getJson(P.data);

                        //setCurrentVideosData(json);
                        //prepareUrlList(json);
                        //makeEpisodeList(json);
                        //setUpMoreLikeGrid(json);
                    }
                }).run("hitSeriesApi/videoScreen");
    }



    /*private void setCurrentVideosData(Json json) {
        Json seriesJson = json.getJson(P.series);
        isFavourite = seriesJson.getInt(P.is_favourite);
        if (isFavourite == 1)
            ((ImageView) findViewById(R.id.favouriteImageView)).setColorFilter(getColor(R.color.colorPrimary));
        else
            ((ImageView) findViewById(R.id.favouriteImageView)).setColorFilter(Color.parseColor("#707070"));

        json = json.getJson(P.video);
        String string = json.getString(P.banner_image) + "";
        if (string.equals("null") || string.isEmpty())
            string = "pathMustNotBeEmpty";
        ImageView imageView = findViewById(R.id.iV);
        Picasso.get().load(string).placeholder(getDrawable(R.drawable.ic_image_black_24dp)).error(getDrawable(R.drawable.ic_broken_image_black_24dp)).into(imageView);

        string = json.getString(P.title) + "";
        ((TextView) findViewById(R.id.videoTitleTextView)).setText(string);

        string = json.getString(P.description) + "";
        ((TextView) findViewById(R.id.videoDescriptionTextView)).setText(string);

        string = json.getString(P.cast) + "";
        ((TextView) findViewById(R.id.castTextView)).setText(string);

        string = json.getString(P.director);
        ((TextView) findViewById(R.id.directorTextView)).setText(string);
    }*/

    /*private void setUpMoreLikeGrid(Json json) {
        JsonList jsonList = json.getJsonList(P.similar_series);
        String string;

        int j = 1;
        LinearLayout linearLayout1 = findViewById(R.id.moreLikeContainer1);
        LinearLayout linearLayout2 = findViewById(R.id.moreLikeContainer2);
        LinearLayout linearLayout3 = findViewById(R.id.moreLikeContainer3);
        linearLayout1.removeAllViews();
        linearLayout2.removeAllViews();
        linearLayout3.removeAllViews();

        for (int i = 0; i < jsonList.size(); i++) {
            json = jsonList.get(i);
            string = json.getString(P.image) + "";
            if (string.equals("null") || string.isEmpty())
                string = "pathMustNotBeEmpty";

            View view = getLayoutInflater().inflate(R.layout.gridview_item3, null, false);
            ImageView imageView = view.findViewById(R.id.imageView);
            Picasso.get().load(string).placeholder(getDrawable(R.drawable.ic_image_black_24dp)).error(getDrawable(R.drawable.ic_broken_image_black_24dp)).into(imageView);

            view.setTag(json);
            view.setOnClickListener((v) ->
            {

                String s = P.baseUrl + "series_check/";
                Json js = (Json) v.getTag();
                String str = js.getString(P.slug);
                s = s + "/" + str;

                js = js.getJson(P.video);
                str = js.getString(P.slug);
                s = s + "/" + str;
                H.log("newApiIs", s);

                App.app.startVideoActivity(this, s, 0);
            });

            if (j == 1) {
                linearLayout1.addView(view);
                j++;
            } else if (j == 2) {
                linearLayout2.addView(view);
                j++;
            } else if (j == 3) {
                linearLayout3.addView(view);
                j = 1;
            }

            string = json.getString(P.series_name);
            ((TextView) view.findViewById(R.id.textView)).setText(string);
        }
    }*/

    /* public void onBackClick(View view) {
         onBackPressed();
     }
 */
    /* public void onFavouriteIconClick(View view) {
     *//*Json json = new Json();
        json.addString(P.series_id, seriesId);

        String string = isFavourite == 1 ? P.baseUrl + "remove_favourite_series" : P.baseUrl + "add_favourite_series";

        Api.newApi(this, string).addJson(json)
                .setMethod(Api.POST)
                .onHeaderRequest(this)
                //.onLoading(this)
                .onError(this::onError)
                .onSuccess(json1 -> {
                    if (json1.getInt(P.status) == 1) {
                        if (isFavourite == 1) {
                            ((ImageView) findViewById(R.id.favouriteImageView)).setColorFilter(Color.parseColor("#707070"));
                            isFavourite = 0;
                        } else {
                            ((ImageView) findViewById(R.id.favouriteImageView)).setColorFilter(getResources().getColor(R.color.colorPrimary));
                            isFavourite = 1;
                        }
                    } else
                        H.showMessage(VideoActivity.this, json1.getString(P.err));
                }).run("hitAddOrRemoveFavouriteApi");*//*
    }
*/
    public void onPlayIconClick(View view) {
       /* Api.newApi(this, checkUrl).addJson(new Json())
                .setMethod(Api.GET)
                .onHeaderRequest(this)
                //.onLoading(this::onLoading)
                .onError(this::onError)
                .onSuccess(json ->
                {
                    if (json.getInt(P.status) == 0)
                        H.showMessage(VideoActivity.this, json.getString(P.err));
                    else {*/
                       /* json = json.getJson(P.data);
                        if (json.getInt(P.is_purchased) == 1) {*/
                            /*if (!startFromPreviousPosition)
                                videoProgress = 0;*/
        ((FrameLayout) view.getParent()).setVisibility(View.GONE);

        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                            /*customMediaController = new CustomMediaController(this);
                            customMediaController.setAnchorView((FrameLayout) customVideoView.getParent());
                            customVideoView.setMediaController(customMediaController);*/

        //https://player.vimeo.com/external/355047357.sd.mp4?s=70acd77556613cd48ac925134cac183d18287c45&profile_id=164
        //https://player.vimeo.com/external/355047357.sd.mp4?s=70acd77556613cd48ac925134cac183d18287c45&profile_id=164
        customVideoView.setVideoURI(Uri.parse(videoUrl));
        customVideoView.start();
        H.log("videoUrlIs", videoUrl + "");

        customVideoView.setOnPreparedListener(mp ->
        {
            mediaPlayer = mp;
            customVideoView.seekTo(videoProgress);
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            H.log("onPrepared", "isExecuted");

            videoController = new VideoControllerForCourse(this);
            videoController.setTotalTimeOfVideo();
            //hitVideoProgressApi(videoProgress);
        });

        runHandlerInInfiniteLoop();
        // } else {
                           /* H.showMessage(this, "video is not purchased.");
                            Intent intent = new Intent(this, PurchasePlanActivity.class);
                            intent.putExtra("json", json.toString());
                            intent.putExtra(P.video_id, videoId);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);*/
        //}
        //}
        //}).run("hitCheckPurchaseApi");
    }

    public static Handler handler = new Handler();
    public static Runnable runnable;
    private int tempProgress;
    private int count;// for auto decrement of video quality

    public void runHandlerInInfiniteLoop() {
        final FrameLayout fL = findViewById(R.id.fL);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        final TextView textView = findViewById(R.id.textView);

        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);

                // to make full screen when activity is just opened
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    try {
                        //makeFullScreen(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (videoController != null) {
                    if (customVideoView.isPlaying())
                        videoController.setSeekBarProgress();
                    videoController.setBufferedVideoProgress();
                }

                if (Math.abs(customVideoView.getDuration() - customVideoView.getCurrentPosition()) < 1000) {
                    if (customVideoView.getCurrentPosition() > 1230) {
                        fL.setVisibility(View.VISIBLE);
                        ((ImageView) fL.findViewById(R.id.imageView)).setImageDrawable(getResources().getDrawable(R.drawable.ic_replay_black_24dp));
                        videoController.playPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
                        videoProgress = 0;
                        //hitVideoProgressApi(0f);
                    }
                }

                if (tempProgress == customVideoView.getCurrentPosition() && fL.getVisibility() == View.GONE && customVideoView.isPlaying())
                    progressBar.setVisibility(View.VISIBLE);
                else {
                    tempProgress = customVideoView.getCurrentPosition();
                    if (customVideoView.getCurrentPosition() > 1230)
                        progressBar.setVisibility(View.GONE);
                }

                if (H.isInternetAvailable(context))
                    textView.setVisibility(View.GONE);
                else if (customVideoView.getBufferPercentage() != 100)
                    textView.setVisibility(View.VISIBLE);

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);

                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                } else
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE);

                /*if (customVideoView.isPlaying() && tempProgress != customVideoView.getCurrentPosition() && allowHitVideoProgressApi)
                    hitVideoProgressApi();*/

                //following is for auto quality
                if (customVideoView.isPlaying() && tempProgress == customVideoView.getCurrentPosition()) {
                    videoProgress = customVideoView.getCurrentPosition();
                    startFromPreviousPosition = true;
                    if (count == 13) {
                        int i;
                        if (isTrailer) {
                            i = trailerVideoUrlList.indexOf(videoUrl);
                            if (i == 0)
                                return;
                            else if (i > 0)
                                videoUrl = trailerVideoUrlList.get(trailerVideoUrlList.indexOf(videoUrl) - 1);
                        } else {
                            i = fullVideoUrlList.indexOf(videoUrl);
                            if (i == 0)
                                return;
                            else if (i > 0)
                                videoUrl = fullVideoUrlList.get(fullVideoUrlList.indexOf(videoUrl) - 1);

                        }

                        customVideoView.stopPlayback();
                        if (mediaPlayer != null)
                            mediaPlayer.release();
                        onPlayIconClick(findViewById(R.id.imageView));
                    }

                    count++;
                } else
                    count = 0;
            }
        };
        handler.removeCallbacks(runnable);
        runnable.run();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        try {
            /*if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
               // makeFullScreen(true);
            else
                //makeFullScreen(false);*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void makeFullScreen(boolean b) throws Exception {
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        int i = linearLayout.getChildCount();

        FrameLayout frameLayout = findViewById(R.id.videoParentLayout);
        LinearLayout.LayoutParams layoutParams;

        // ViewGroup.LayoutParams lP = customMediaController.getLayoutParams();

        if (b) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            for (int j = 1; j < i; j++)
                linearLayout.getChildAt(j).setVisibility(View.GONE);

            //frameLayout.findViewById(R.id.frameLayout).setVisibility(View.GONE);

            gestureDetectorCompat = new GestureDetectorCompat(this, this);

            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.height = H.getDeviceHeight(this);
            layoutParams.width = H.getDeviceWidth(this);
            frameLayout.setLayoutParams(layoutParams);

            i = getResources().getIdentifier("status_bar_height", "dimen", "android");
           *//* if (i > 0 && getResources().getDimensionPixelSize(i) > H.convertDpToPixel(24, this))
                lP.width = SplashScreenActivity.deviceHeight - (int) H.convertDpToPixel(70, this);
            else
                lP.width = SplashScreenActivity.deviceHeight;*//*

            if (!new Session(this).getBool("isViewed")) {
                findViewById(R.id.includeLayout).setVisibility(View.VISIBLE);
                if (customVideoView != null)
                    customVideoView.pause();
            }

        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            for (int j = 1; j < i; j++)
                linearLayout.getChildAt(j).setVisibility(View.VISIBLE);

            //frameLayout.findViewById(R.id.frameLayout).setVisibility(View.VISIBLE);

            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) H.convertDpToPixel(255, this));
            frameLayout.setLayoutParams(layoutParams);

            //lP.width = SplashScreenActivity.deviceWidth;
        }

        // customMediaController.setLayoutParams(lP);
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);

        try {
            customVideoView.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }

        H.log("onPause", "isExecuted");
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            customVideoView.resume();
            customVideoView.seekTo(videoProgress);
        } catch (Exception e) {
            e.printStackTrace();
        }

        H.log("onResume", "isExecuted");
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        refreshHome = true;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else
            finish();
    }

    private Dialog dialog;
    private View view;

    public void showPopUp(ArrayList<String> arrayList, ArrayList<String> al) throws NullPointerException {

        H.log("showPopUp", "isExecuted");
        if (view == null) {
            view = getLayoutInflater().inflate(R.layout.video_quality_pop_up, null, false);
            RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
            String string;
            String resolution;
            RadioButton radioButton;
            for (int i = 0; i < al.size(); i++) {
                radioButton = (RadioButton) radioGroup.getChildAt(i);
                radioButton.setVisibility(View.VISIBLE);

                resolution = arrayList.get(i);
                if (i == al.size() - 1) {
                    radioButton.setChecked(true);
                    radioButton.setText(resolution);
                } else
                    radioButton.setText(resolution + " px");

                string = al.get(i);
                radioButton.setTag(string);
            }
            dialog = new Dialog(this);
            dialog.setTitle("Select Video Quality");
            dialog.setContentView(view);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        dialog.show();

        RadioGroup radioGroup = dialog.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener((group, checkedId) ->
        {
            videoProgress = customVideoView.getCurrentPosition();

            View view = group.findViewById(checkedId);
            H.log("tagIs", view.getTag().toString());
            Object object = view.getTag();
            if (object instanceof String && !object.toString().isEmpty()) {
                customVideoView.stopPlayback();
                if (mediaPlayer != null)
                    mediaPlayer.release();

                customVideoView.setVideoURI(Uri.parse(view.getTag().toString()));

                findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

                if (VideoControllerForCourse.videoIsRunning)
                    customVideoView.start();
            }

            dialog.hide();
        });
    }

    public void onWatchTrailerClick(View view) {
        H.log("onWatchTrailerClick", "isClicked");
        this.view = null;
        isTrailer = true;
        if (view.getTag() instanceof String) {

            videoUrl = view.getTag().toString();

            customVideoView.stopPlayback();
            if (mediaPlayer != null)
                mediaPlayer.release();

            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

            videoProgress = 0;
            onPlayIconClick(findViewById(R.id.imageView));

            if (videoController != null)
                videoController.handlePlayPause(true);
        }
    }

    public void hitVideoProgressApi(float videoProgress) {
        H.log("methodIs", "called");
        if (isTrailer)
            return;

        /*Json json = new Json();
        json.addString(P.video_id, videoId);
        json.addString(P.series_id, seriesId);
        json.addFloat(P.time, videoProgress);*/


     /*   Api.newApi(this, P.baseUrl + "add_viewed_video")
                .addJson(json)
                .onHeaderRequest(this)
                .setMethod(Api.POST)
                //.onLoading(this::onLoading)
                .onError(this::onError)
                .onSuccess(json1 -> {
                    if (json1.getInt(P.status) == 0)
                        H.showMessage(activity, json1.getString(P.err));
                }).run("hitVideoProgressApi");*/
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void onLoading(boolean isLoading) {
        if (!isDestroyed()) {
            if (isLoading)
                loadingDialog.show("loading...");
            else
                loadingDialog.hide();
        }
    }

    public void onError() {
        H.showMessage(context, "onError() is executed");
    }

    @Override
    public Map<String, String> getHeaders() {
        return App.getHeaders();
    }

    // following variables are for storing temp value which will be used for comparison with previous value.
    private int bY, bX;
    private int vY, vX;

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
       /* H.log("e1Is", e1.toString());
        H.log("e2Is", e2.toString());*/
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && e2.getAction() == MotionEvent.ACTION_MOVE) {
            if (e2.getX(0) < SplashActivity.deviceHeight / 2f && allowSecondSwipe) {
                findViewById(R.id.brightnessLinearLayout).setVisibility(View.VISIBLE);
                findViewById(R.id.volumeLinearLayout).setVisibility(View.GONE);
                // edit brightness
                if (e2.getY(0) > bY)// && e2.getX() - bX < 50)
                {
                    H.log("brightness", "isReduced");
                    if (brightness > 10)
                        brightness -= 10;
                } else if (brightness < 246)// because 255 is max brightness
                {
                    H.log("brightness", "isHiked");
                    brightness += 10;
                }
                bY = (int) e2.getY();// for vertical
               /* if (bX == 0)
                    bX = (int) e2.getX();// to avoid horizontal*/
                setBrightness();
            } else if (allowSecondSwipe) {
                findViewById(R.id.volumeLinearLayout).setVisibility(View.VISIBLE);
                findViewById(R.id.brightnessLinearLayout).setVisibility(View.GONE);

                // edit volume

                if (e2.getY(0) > vY)// && e2.getX() - vX < 50)
                {
                    H.log("volume", "isReduced");
                    if (volume > 0)
                        volume--;
                } else if (volume < maxVolume) {
                    H.log("volume", "isHiked");
                    volume++;
                }
                vY = (int) e2.getY();// for vertical
               /* if (vX == 0)
                    vX = (int) e2.getX();// to avoid horizontal*/
                if (audioManager != null) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
                    volumeProgressBar.setProgress(volume);
                }
            }
        }
        return true;
    }

    private void setBrightness() {
        brightnessProgressBar.setProgress(brightness);

        int i = (brightness * 100) / 255;
        float f = i / 100f;

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = f;

        H.log("screenBrightnessIs", f + "");
        getWindow().setAttributes(layoutParams);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    private boolean allowSecondSwipe = true;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
       /* if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            return super.onTouchEvent(event);*/

        if (event.getAction() == MotionEvent.ACTION_DOWN && videoController != null)
            videoController.handleVisibilityOfVideoController();

        //  H.log("onTouch", event.toString());
        try {
            this.gestureDetectorCompat.onTouchEvent(event);

            if (event.getAction() == MotionEvent.ACTION_UP) {
                findViewById(R.id.brightnessLinearLayout).setVisibility(View.GONE);
                findViewById(R.id.volumeLinearLayout).setVisibility(View.GONE);

                allowSecondSwipe = true;
            } else if (event.getAction() == MotionEvent.ACTION_DOWN || event.toString().contains("ACTION_POINTER_UP"))
                allowSecondSwipe = true;
            else if (event.toString().contains("ACTION_POINTER_DOWN"))
                allowSecondSwipe = false;

        } catch (Exception e) {
            e.printStackTrace();
            H.log("exceptionIs", e + "");
        }
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    private int getMaxVolume() {
        int i = 0;
        if (audioManager != null)
            i = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        return i;
    }

    private int getDefaultBrightness() {
        int i = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);
        H.log("currentBrightnessIs", i + "");
        //i = (i*100)/255;
        H.log("brightnessInPercentIs", i + "");
        return i;
    }

    private int getDefaultVolumeInPercent() {
        int i = 0;

        if (audioManager != null) {
            int j = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            H.log("maxVolumeIs", j + "");
            int k = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            H.log("currentVolumeIs", k + "");

            i = k / j * 100;
        }

        return i;
    }

    public void onGotItClick(View v) {
        Session session = new Session(this);
        session.addBool("isViewed", true);
        findViewById(R.id.includeLayout).setVisibility(View.GONE);
        //customVideoView.resume();
        customVideoView.start();
    }

}