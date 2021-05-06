package com.getsetgoapp.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.adoisstudio.helper.Session;
import com.getsetgoapp.Adapter.CurriculumLectureAdapter;
import com.getsetgoapp.Adapter.StudentsFeedbackAdapter;
import com.getsetgoapp.Model.InstructorModel;
import com.getsetgoapp.Model.VideoUrlModel;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.activity.SplashActivity;
import com.getsetgoapp.activity.VideoPlayCrashCourseActivity;
import com.getsetgoapp.activity.VideoPlayNewActivity;
import com.getsetgoapp.adapterview.InstructorAdapter;
import com.getsetgoapp.adapterview.VideoCourseQualityAdapter;
import com.getsetgoapp.others.CustomVideoView;
import com.getsetgoapp.util.App;
import com.getsetgoapp.util.Click;
import com.getsetgoapp.util.Config;
import com.getsetgoapp.util.JumpToLogin;
import com.getsetgoapp.util.P;
import com.getsetgoapp.util.RemoveHtml;
import com.google.android.exoplayer2.C;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.content.Context.AUDIO_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.getsetgoapp.util.Utilities.pxFromDp;

public class CrashCourseDetailFragment extends Fragment implements GestureDetector.OnGestureListener, Api.OnHeaderRequestListener, Player.EventListener,VideoCourseQualityAdapter.onClick {

    CurriculumLectureAdapter curriculumLectureAdapter;
    StudentsFeedbackAdapter studentsFeedbackAdapter;
    LinearLayoutManager layoutManager;
    LinearLayoutManager mLayoutManagerStudentFeedback;
    TextView txtMoreFeedback;
    BuyCourseFragment buyCourseFragment;
    public static List<VideoUrlModel> videoUrlModelList;

    RecyclerView recyclerViewLecture;
    LinearLayout llCourseIncludes, llLearn, llCourseVideo, llCourseContent, llCourse, llCollapse;

    TextView txtVideoError;
    TextView[] t = new TextView[0];
    TextView[] tC;
    ImageView imageView;
    public static boolean videoIsRunning;
    public boolean isFromHome;

    //private CustomMediaController customMediaController;
    private CustomVideoView customVideoView;
    private MediaPlayer mediaPlayer;
    private VideoControllerForCourse videoController;
    private LoadingDialog loadingDialog;
    private final String seriesId = "";
    private final String videoId = "";
    public static boolean refreshHome;

    private String checkUrl = "";
    private String apiUrl = "";
//    private String videoUrl = "https://player.vimeo.com/external/355047357.sd.mp4?s=70acd77556613cd48ac925134cac183d18287c45&profile_id=164";
    private String videoUrl = "";
    String id = "";
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
    JsonList jsonList = new JsonList();


    // following is for brightness and volume
    private ProgressBar brightnessProgressBar, volumeProgressBar;
    View v;
    TextView txtShowMore, txtViewMore, txtDesc, txtCourseTitle, txtTimeLect,
            txtViewCategoryNewPrice, txtViewCategoryOldPrice, txtBuyNow, txtTitle, txtLectureTitle, txtVideoDetails, txtPreview;
    RelativeLayout rlBuyNow;
    Context context;
    CheckBox chkExpClp;
//    ImageButton chkExpClp;

    private SimpleExoPlayer exoPlayer;
    private PlayerView playerView;
    private ProgressBar pbVideoPlayer;
    private ImageView imgFullScreen;
    private ImageView imgQuality;

    private Dialog qualityDialog;
    public static long lastVideoPosition = 0;
    public static int lastSelectedPosition = 0;
    public static String videoPlayPath = "";

    private List<InstructorModel> instructorModelList;
    private InstructorAdapter instructorAdapter;
    private RecyclerView recyclerInstructor;

    public CrashCourseDetailFragment() {
    }

    public static CrashCourseDetailFragment newInstance() {
        CrashCourseDetailFragment fragment = new CrashCourseDetailFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_crash_course_details, container, false);

        init(v);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                if (getFragmentManager().getBackStackEntryCount() > 0) {
//                    getFragmentManager().popBackStackImmediate();
                    callback();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (exoPlayer!=null){
            exoPlayer.stop(true);
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

    private void playVideo(String videoPath,boolean replay){
        Log.e("TAG", "playVideo: " + videoPath );
        videoPlayPath = videoPath;
        exoPlayer = new SimpleExoPlayer.Builder(getActivity()).build();
        playerView.setPlayer(exoPlayer);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.addListener(this);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getApplicationContext(), getApplicationContext().getString(R.string.app_name)));

        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(videoPath));

        exoPlayer.prepare(videoSource);
        exoPlayer.setMediaItem(MediaItem.fromUri(videoPath));

        if (replay){
            if (lastVideoPosition != C.TIME_UNSET) {
                exoPlayer.seekTo(lastVideoPosition);
            }
            exoPlayer.play();
        }else {
            exoPlayer.play();
        }

    }

    private void initializePlayer(View view){
        lastVideoPosition = 0;
        lastSelectedPosition = 0;
        pbVideoPlayer = view.findViewById(R.id.pbVideoPlayer);
        playerView = view.findViewById(R.id.playerView);
        imgFullScreen = playerView.findViewById(R.id.exo_fullscreen_icon);
        imgQuality = playerView.findViewById(R.id.ivVideoQuality);

        imgFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(exoPlayer!=null){
                    exoPlayer.pause();
                    lastVideoPosition =  exoPlayer.getCurrentPosition();
                    Intent intent = new Intent(getContext(), VideoPlayCrashCourseActivity.class);
                    startActivity(intent);
                }
            }
        });

        imgQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (videoUrlModelList!=null && !videoUrlModelList.isEmpty()){
                    qualityUrlDialog(videoUrlModelList);
                }
            }
        });

    }

    private void qualityUrlDialog(List<VideoUrlModel> videoUrlModelList){
        qualityDialog = new Dialog(getActivity());
        qualityDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        qualityDialog.setContentView(R.layout.activity_video_url_dialog);

        RecyclerView recyclerQuality = qualityDialog.findViewById(R.id.recyclerQuality);
        recyclerQuality.setLayoutManager(new LinearLayoutManager(context));
        VideoCourseQualityAdapter adapter = new VideoCourseQualityAdapter(context,videoUrlModelList, CrashCourseDetailFragment.this,lastSelectedPosition,3);
        recyclerQuality.setAdapter(adapter);

        qualityDialog.setCancelable(true);
        qualityDialog.show();
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }


    @Override
    public void qualityClick(VideoUrlModel model, int position) {
        if (qualityDialog!=null){
            qualityDialog.dismiss();
        }
        if (exoPlayer!=null){
            lastSelectedPosition = position;
            lastVideoPosition = exoPlayer.getCurrentPosition();
            exoPlayer.release();
            exoPlayer = null;
        }
        Log.e("TAG", "setVideoData: "+  model.getLink() );
        playVideo(model.getLink(),true);
    }


    private void init(View view) {

        initializePlayer(view);

        String title = this.getArguments().getString("title");
        String slug = this.getArguments().getString("slug");
        isFromHome = this.getArguments().getBoolean("isFromHome");
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Crash Course Details");
        BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);
//        BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.VISIBLE);
        BaseScreenActivity.binding.incFragmenttool.txtSubCat.setText(title);

        videoUrlModelList = new ArrayList<>();

        txtVideoError = view.findViewById(R.id.txtVideoError);
        txtMoreFeedback = view.findViewById(R.id.txtMoreFeedback);
        recyclerViewLecture = view.findViewById(R.id.recyclerViewLecture);
        llCourseIncludes = view.findViewById(R.id.llCourseIncludes);
        llLearn = view.findViewById(R.id.llLearn);
        txtShowMore = view.findViewById(R.id.txtShowMore);
        txtViewMore = view.findViewById(R.id.txtViewMore);
        txtDesc = view.findViewById(R.id.txtDesc);
        txtCourseTitle = view.findViewById(R.id.txtCourseTitle);
        txtViewCategoryNewPrice = view.findViewById(R.id.txtViewCategoryNewPrice);
        txtViewCategoryOldPrice = view.findViewById(R.id.txtViewCategoryOldPrice);
        txtTimeLect = view.findViewById(R.id.txtTimeLect);
        txtBuyNow = view.findViewById(R.id.txtBuyNow);
        rlBuyNow = view.findViewById(R.id.rlBuyNow);
        llCourseVideo = view.findViewById(R.id.llCourseVideo);
        recyclerInstructor = view.findViewById(R.id.recyclerInstructor);

        imageView = v.findViewById(R.id.imageView);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mLayoutManagerStudentFeedback = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        instructorModelList = new ArrayList<>();
        instructorAdapter = new InstructorAdapter(getActivity(),instructorModelList);
        recyclerInstructor.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerInstructor.setHasFixedSize(true);
        recyclerInstructor.setAdapter(instructorAdapter);

        callCourseDetailsApi(getActivity(), slug);


        txtShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtShowMore.getText().toString().equalsIgnoreCase(getString(R.string.show_more))) {
                    txtDesc.setMaxLines(Integer.MAX_VALUE);
                    txtShowMore.setText(R.string.show_less);
                } else {
                    txtDesc.setMaxLines(5);
                    txtShowMore.setText(R.string.show_more);
                }
            }
        });

        /*imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPlayClick();
            }
        });*/

        BaseScreenActivity.binding.incFragmenttool.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    callback();
                }
            }
        });

        videoInit(view, "");
    }



    private void callCourseDetailsApi(Context context, String slug) {

        LoadingDialog loadingDialog = new LoadingDialog(context, false);
//        String apiParam = "?title" + title;
        Api.newApi(context, P.baseUrl + "crash_course_details" + "/" + slug)
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
                        JumpToLogin.call(Json1,context);
                        loadingDialog.dismiss();
                        if (Json1.getInt(P.status) == 0) {
                            H.showMessage(context, Json1.getString(P.err));
                        } else {
                            Json1 = Json1.getJson(P.data);
                            setData(Json1.getJson("list"));
                        }
                    }

                }).run("course_details");

    }

    private void setData(Json list) {
        Log.d("Hardik", "Reviews " + list);

        id = list.getString("id");
        txtDesc.setText(RemoveHtml.html2text(list.getString("description")));
        txtCourseTitle.setText(list.getString("name"));
        txtViewCategoryNewPrice.setText("₹ " + list.getString("saleprice"));
        txtViewCategoryOldPrice.setText("₹ " + list.getString("price"));

        if (list.getBoolean("has_purchased")) {
            rlBuyNow.setVisibility(View.GONE);
        } else {
            rlBuyNow.setVisibility(View.VISIBLE);

        }

        try {
            ArrayList<String> mVideoList = new ArrayList<>();

            JSONArray instructorList = list.getJsonArray("instructor");

            if (instructorList!=null && instructorList.length()!=0){
                for (int i = 0; i < instructorList.length(); i++) {
                    JSONObject jsonObject = instructorList.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    String name = jsonObject.getString("name");
                    String image = jsonObject.getString("image");
                    String joining_date = jsonObject.getString("joining_date");
                    String course_count = jsonObject.getString("crash_course_count");

                    InstructorModel instructorModel = new InstructorModel();
                    instructorModel.setId(id);
                    instructorModel.setName(name);
                    instructorModel.setImage(image);
                    instructorModel.setJoining_date(joining_date);
                    instructorModel.setCourse_count(course_count);
                    instructorModelList.add(instructorModel);

                }

                instructorAdapter.notifyDataSetChanged();

                Log.e("TAG", "sdsdsdsdsds: "+ instructorModelList.size() );
            }

            JSONObject courseInclusion = list.getJsonObject("crash_course_inclusion");
            String duration = courseInclusion.getString("duration");
            String newDuration = duration.replace(":", "").replace("H", "h").replace("M", "m");

            Log.e("TAG", "setDataSSDSD: " + duration );
//            txtTimeLect.setText("Lectures: " + courseInclusion.getString("videos") + " " + "Total time(" + newDuration + ")");
            txtTimeLect.setText("Total time ( " + newDuration + " )");

            Json jsonObject = list.getJson("crash_course_videos");
            Log.e("TAG", "setDataOBJ: "+ jsonObject.toString() );
            JSONObject element;
            Iterator<?> keys = jsonObject.keys();
            Map<String, ArrayList<JSONObject>> mMap = new LinkedHashMap<>();
            while (keys.hasNext()) {

                String key = (String) keys.next();

                JsonList arr = jsonObject.getJsonList(key);
                mVideoList.add(key);

                ArrayList<JSONObject> mVideoNameList = new ArrayList<>();

                for (int i = 0; i < arr.size(); i++) {
                    element = arr.get(i);
                    mVideoNameList.add(element);
                }
                mMap.put(key, mVideoNameList);
            }

            for (Map.Entry<String, ArrayList<JSONObject>> video : mMap.entrySet()) {

                int count = 1;
                CourseDetailsParentViewHolder courseDetailsParentViewHolder = new CourseDetailsParentViewHolder();
                courseDetailsParentViewHolder.txtTitle.setText(video.getKey());
                ArrayList<JSONObject> mTempList = new ArrayList<>();
                for (int i = 0; i < video.getValue().size(); i++) {
                    mTempList.add(video.getValue().get(i));
                }

                for (int i = 0; i < mTempList.size(); i++) {
                    CourseDetailsChildViewHolder courseDetailsChildViewHolder = new CourseDetailsChildViewHolder();

                    courseDetailsChildViewHolder.txtLectureTitle.setText(mTempList.get(i).getString("video_name"));
                    courseDetailsChildViewHolder.txtVideoDetails.setText("Duration " + mTempList.get(i).getString("video_duration") + " mins");
                    courseDetailsChildViewHolder.txtCount.setText(String.valueOf(count));
                    count++;
                    courseDetailsParentViewHolder.llChild.addView(courseDetailsChildViewHolder.getViewGroup());

                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins((int) pxFromDp(getActivity(), 8), (int) pxFromDp(getActivity(), 4), (int) pxFromDp(getActivity(), 8), (int) pxFromDp(getActivity(), 4));

                courseDetailsParentViewHolder.llParent.setLayoutParams(params);
                llCourseVideo.addView(courseDetailsParentViewHolder.getViewGroup());

            }
            JsonList video_url_list = list.getJsonList("video_urls");
            if (video_url_list!=null && video_url_list.size()!=0){
                txtVideoError.setVisibility(View.GONE);
                setVideoData(video_url_list);
            }else {
                txtVideoError.setVisibility(View.VISIBLE);
            }
            videoUrl = list.getString("vimeo_url");
            videoInit(v, videoUrl);


        } catch (JSONException e) {

            Log.e("TAG", "setDataWWEEE: " + e.getMessage() );
            e.printStackTrace();
        }

        dynamicTextView(getActivity(), list);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPlayClick();
            }
        });

        txtBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (exoPlayer!=null){
                    exoPlayer.pause();
                }
                loadFragment(id);
            }
        });
    }

    private void setVideoData(JsonList jsonList){

        videoUrlModelList.clear();

        for (Json json : jsonList){
            Log.e("TAG", "setVideoData122: "+ json.toString() );
            VideoUrlModel model = new VideoUrlModel();
            model.setType(json.getString(P.type));
            model.setLink(json.getString(P.link));
            model.setQuality(json.getString(P.quality));
            videoUrlModelList.add(model);
        }

        String path = videoUrlModelList.get(0).getLink();
        Log.e("TAG", "setVideoData: "+  path );
        if (exoPlayer!=null){
            exoPlayer.release();
            exoPlayer = null;
        }
        playVideo(path,false);
    }


    private void dynamicTextView(FragmentActivity context, Json list) {
        Typeface typeface = ResourcesCompat.getFont(context, R.font.nunito_sans_regular);
        Typeface typefaceHeader = ResourcesCompat.getFont(context, R.font.nunito_sans_semibold);
//        LinearLayout.LayoutParams dim = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        JSONArray learningOutcomes = list.getJsonArray("learning_outcomes");
//        String[] arr = new String[0];

        for (int i = 0; i < learningOutcomes.length(); i++) {

            try {
                if (learningOutcomes.getJSONObject(i).getString("is_heading").contains("1")) {
                    TextView textView = new TextView(getActivity());
                    textView.setPadding(0, 4, 0, 4);
                    textView.setText(learningOutcomes.getJSONObject(i).getString("text").trim());
                    textView.setTextColor(getActivity().getResources().getColor(R.color.colorTextDark));
                    textView.setTypeface(typefaceHeader);
                    textView.setTextSize(16f);
                    llCourseIncludes.addView(textView);

                } else {
                    TextView textView = new TextView(getActivity());
                    textView.setText("\u2022  " + learningOutcomes.getJSONObject(i).getString("text").trim());
                    textView.setTextColor(getActivity().getResources().getColor(R.color.colorTextDark));
                    textView.setTypeface(typeface);
                    textView.setTextSize(14f);
                    llCourseIncludes.addView(textView);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void videoInit(View view, String checkUrl) {
        loadingDialog = new LoadingDialog(getActivity());
//        videoProgress = this.getArguments().getInt("videoProgress", 0);
        startFromPreviousPosition = videoProgress != 0;

//        checkUrl = this.getArguments().getString(P.url);
        if (checkUrl != null && checkUrl.contains("series_check"))
            apiUrl = checkUrl.replace("series_check", "series");

        //H.log("replacedUrlIs",apiUrl);
        customVideoView = view.findViewById(R.id.customVideoView);

        //hitSeriesApi();

        audioManager = (AudioManager) getActivity().getSystemService(AUDIO_SERVICE);

        maxVolume = getMaxVolume();
        volume = getDefaultVolumeInPercent();
        volumeProgressBar = view.findViewById(R.id.volumeProgressBar);
        volumeProgressBar.setProgress(volume);
        volumeProgressBar.setMax(maxVolume);

        brightnessProgressBar = view.findViewById(R.id.brightnessProgressBar);
        brightness = getDefaultBrightness();
        brightnessProgressBar.setProgress(brightness);

        FrameLayout frameLayout = view.findViewById(R.id.videoParentLayout);
        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && videoController != null)
                    videoController.handleVisibilityOfVideoController();
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && videoController != null)
                    videoController.handleVisibilityOfVideoController();

                //  H.log("onTouch", event.toString());
                try {
                    gestureDetectorCompat.onTouchEvent(motionEvent);

                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        v.findViewById(R.id.brightnessLinearLayout).setVisibility(View.GONE);
                        v.findViewById(R.id.volumeLinearLayout).setVisibility(View.GONE);

                        allowSecondSwipe = true;
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.toString().contains("ACTION_POINTER_UP"))
                        allowSecondSwipe = true;
                    else if (motionEvent.toString().contains("ACTION_POINTER_DOWN"))
                        allowSecondSwipe = false;

                } catch (Exception e) {
                    e.printStackTrace();
                    H.log("exceptionIs", e + "");
                }


                return true;
            }
        });

    }

    public void onPlayClick() {
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
        // ((FrameLayout) v.getParent()).setVisibility(View.GONE);
        FrameLayout fl = v.findViewById(R.id.fL);
        fl.setVisibility(View.GONE);


        v.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
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
            v.findViewById(R.id.progressBar).setVisibility(View.GONE);
            H.log("onPrepared", "isExecuted");

            videoController = new VideoControllerForCourse(getContext());
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
        final FrameLayout fL = v.findViewById(R.id.fL);
        final ProgressBar progressBar = v.findViewById(R.id.progressBar);
        final TextView textView = v.findViewById(R.id.textView);

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

                if (H.isInternetAvailable(getContext()))
                    textView.setVisibility(View.GONE);
                else if (customVideoView.getBufferPercentage() != 100)
                    textView.setVisibility(View.VISIBLE);

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);

                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                } else
                    getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE);

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
                        onPlayClick();
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


    @Override
    public void onPause() {
        super.onPause();

        if (exoPlayer!=null){
            exoPlayer.pause();
        }

        handler.removeCallbacks(runnable);

        try {
            customVideoView.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }

        H.log("onPause", "isExecuted");
    }



    @Override
    public void onResume() {
        super.onResume();

        if (Config.isHalfScreen){
            Config.isHalfScreen = false;
            playVideo(videoPlayPath,true);
        }

        try {
            customVideoView.resume();
            customVideoView.seekTo(videoProgress);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                if (getFragmentManager().getBackStackEntryCount() > 0) {
//                    getFragmentManager().popBackStackImmediate();
                    callback();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

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
            dialog = new Dialog(getActivity());
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

                v.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

                if (videoIsRunning)
                    customVideoView.start();
            }

            dialog.hide();
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void onLoading(boolean isLoading) {
        if (!getActivity().isDestroyed()) {
            if (isLoading)
                loadingDialog.show("loading...");
            else
                loadingDialog.hide();
        }
    }

    public void onError() {
        H.showMessage(getActivity(), "onError() is executed");
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
                v.findViewById(R.id.brightnessLinearLayout).setVisibility(View.VISIBLE);
                v.findViewById(R.id.volumeLinearLayout).setVisibility(View.GONE);
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
                v.findViewById(R.id.volumeLinearLayout).setVisibility(View.VISIBLE);
                v.findViewById(R.id.brightnessLinearLayout).setVisibility(View.GONE);

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

        WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();
        layoutParams.screenBrightness = f;

        H.log("screenBrightnessIs", f + "");
        getActivity().getWindow().setAttributes(layoutParams);
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

   /* @Override
    public boolean onTouchEvent(MotionEvent event) {
       *//* if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            return super.onTouchEvent(event);*//*

        if (event.getAction() == MotionEvent.ACTION_DOWN && videoController != null)
            videoController.handleVisibilityOfVideoController();

        //  H.log("onTouch", event.toString());
        try {
            this.gestureDetectorCompat.onTouchEvent(event);

            if (event.getAction() == MotionEvent.ACTION_UP) {
                v.findViewById(R.id.brightnessLinearLayout).setVisibility(View.GONE);
                v.findViewById(R.id.volumeLinearLayout).setVisibility(View.GONE);

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
    }*/

    private int getMaxVolume() {
        int i = 0;
        if (audioManager != null)
            i = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        return i;
    }

    private int getDefaultBrightness() {
        int i = Settings.System.getInt(getActivity().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);
        //i = (i*100)/255;
        return i;
    }

    private int getDefaultVolumeInPercent() {
        int i = 0;

        if (audioManager != null) {
            int j = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int k = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

            i = k / j * 100;
        }

        return i;
    }


    public class VideoControllerForCourse implements SeekBar.OnSeekBarChangeListener {
        private final Context context;
        private final CustomVideoView customVideoView;
        private final FrameLayout parentLayout;
        public ImageView setting, zoom, back, playPause, forward;
        private final SeekBar seekBar;
        private final TextView time;
        private final TextView totalTime;
        private final LinearLayout videoControllerLayout;

        public VideoControllerForCourse(Context context) {
            this.context = context;
            customVideoView = v.findViewById(R.id.customVideoView);
            parentLayout = v.findViewById(R.id.videoParentLayout);

            videoControllerLayout = v.findViewById(R.id.videoControllerLayout);
            handleVisibilityOfVideoController();
            setting = videoControllerLayout.findViewById(R.id.settingImageView);
            zoom = videoControllerLayout.findViewById(R.id.zoomImageView);
            zoom.setVisibility(View.GONE);
            back = videoControllerLayout.findViewById(R.id.backImageView);
            playPause = videoControllerLayout.findViewById(R.id.ppImageView);
            forward = videoControllerLayout.findViewById(R.id.forwardImageView);
            seekBar = videoControllerLayout.findViewById(R.id.seekBar);
            time = videoControllerLayout.findViewById(R.id.timeTextView);
            totalTime = videoControllerLayout.findViewById(R.id.totalTimeTextView);

            RelativeLayout relativeLayout = videoControllerLayout.findViewById(R.id.imageViewContainer);
            for (int i = 0; i < relativeLayout.getChildCount(); i++)
                relativeLayout.getChildAt(i).setOnClickListener(this::onClick);

        }


        public void setTotalTimeOfVideo() {
            int i = customVideoView.getDuration();

            seekBar.setMax(i);

            String string = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(i),
                    TimeUnit.MILLISECONDS.toMinutes(i) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(i)),
                    TimeUnit.MILLISECONDS.toSeconds(i) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(i)));

            string = string.substring(string.indexOf(":") + 1);

            totalTime.setText(string);
        }

        public void setBufferedVideoProgress() {
            int i = customVideoView.getBufferPercentage();
            i = i * customVideoView.getDuration();
            i /= 100;
            seekBar.setSecondaryProgress(i);
        }

        public void setSeekBarProgress() {
            seekBar.setProgress(customVideoView.getCurrentPosition());
        }

        public void onClick(View view) {
            int i = view.getId();

            switch (i) {
                case R.id.settingImageView:
                    onSettingIconClick();
                    break;

                case R.id.zoomImageView:
                    handleZoomInAndOut(view);
                    break;

                case R.id.backImageView:
                    forwardBy10sec(false);
                    break;

                case R.id.ppImageView:
                    handlePlayPause(false);
                    handleVisibilityOfVideoController();
                    break;

                case R.id.forwardImageView:
                    forwardBy10sec(true);
                    break;

           /* case R.id.customVideoView:
                handleVisibilityOfVideoController();
                break;*/
            }
        }

        public void handleVisibilityOfVideoController() {
            videoControllerLayout.setVisibility(View.VISIBLE);

            videoControllerLayout.postDelayed(() -> {
                if (customVideoView.isPlaying())
                    videoControllerLayout.setVisibility(View.GONE);
            }, 3210);
        }

        private void handleZoomInAndOut(View view) {
            String string = view.getTag().toString();

            if (string.equalsIgnoreCase("zoomIn")) {
                ((BaseScreenActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                view.setTag("zoomOut");

            } else {
                ((BaseScreenActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                view.setTag("zoomIn");
            }
        }

        private void onSettingIconClick() {
            if (isTrailer) {
                showPopUp(trailerVideoResolutionList, trailerVideoUrlList);
                H.log("isTrailer", "true");
            } else {
                showPopUp(fullVideoResolutionList, fullVideoUrlList);
                H.log("isTrailer", "false");
            }
        }

        private void forwardBy10sec(boolean bool) {
            int i = customVideoView.getCurrentPosition();
            if (bool) {
                i += 10000;
                customVideoView.seekTo(i);
                seekBar.setProgress(i);
            } else {
                i -= 10000;
                customVideoView.seekTo(i);
                seekBar.setProgress(i);
            }
        }

        public void handlePlayPause(boolean isVideoChanged) {
            if (customVideoView.isPlaying() && !isVideoChanged) {
                customVideoView.pause();
                playPause.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
                videoIsRunning = false;
            } else {
                customVideoView.start();
                playPause.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause_black_24dp));
                videoIsRunning = true;
            }

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            //H.log("fromUserIs", fromUser + "");

            if (fromUser) {
                customVideoView.seekTo(progress);
                //((CourseDetailsActivity)context).hitVideoProgressApi(customVideoView.getCurrentPosition() / 1000.0f);

                if (videoControllerLayout.getVisibility() != View.VISIBLE)
                    handleVisibilityOfVideoController();
            }
        /*else if (progress>0 && progress<1230)
            ((CourseDetailsActivity)context).hitVideoProgressApi(customVideoView.getCurrentPosition() / 1000.0f);*/

            String string = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(progress),
                    TimeUnit.MILLISECONDS.toMinutes(progress) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(progress)),
                    TimeUnit.MILLISECONDS.toSeconds(progress) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(progress)));

            string = string.substring(string.indexOf(":") + 1);

            // if (string.contains(":00"))
            //((CourseDetailsActivity)context).hitVideoProgressApi(customVideoView.getCurrentPosition() / 1000.0f);

            time.setText(string);

            string = string.substring(string.lastIndexOf(":") + 1);
            H.log("stringIs", string);
            progress = Integer.parseInt(string);
       /* if (progress%10 == 0)
            ((CourseDetailsActivity)context).hitVideoProgressApi(customVideoView.getCurrentPosition() / 1000.0f);*/
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            //((CourseDetailsActivity)context).hitVideoProgressApi(customVideoView.getCurrentPosition() / 1000.0f);
            H.log("onStopTrackingTouch", "isCalled");
        }
    }

    public class CustomMediaControllerForCourse extends MediaController implements View.OnClickListener {
        public ImageButton imageButton;
        private final Context context;

        public CustomMediaControllerForCourse(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public void setAnchorView(View view) {
       /* ViewParent viewParent = view.getParent();
        if (viewParent instanceof LinearLayout)
        {

            LinearLayout.LayoutParams  layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);
            H.log("parentIs",""+((LinearLayout) viewParent).getChildCount());
        }
        else if (viewParent instanceof FrameLayout)
        {
            FrameLayout.LayoutParams  layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);
            H.log("parentIs",""+((FrameLayout) viewParent).getChildCount());
        }*/

            super.setAnchorView(view);

            ImageButton imageButton = setUpImageButton("fullScreen", context.getResources().getDrawable(R.drawable.ic_fullscreen_black_24dp));
            this.imageButton = imageButton;

            int i = (int) H.convertDpToPixel(30, context);
            //int marginButton = (int)H.convertDpToPixel(30,context);

            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            layoutParams.gravity = Gravity.END;

            layoutParams.setMargins(0, 0, i, i);
            addView(imageButton, layoutParams);
            imageButton.setOnClickListener(this);

            layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            ImageButton settingButton = setUpImageButton("setting", context.getResources().getDrawable(R.drawable.ic_settings_black_24dp));
            layoutParams.gravity = Gravity.START;
            layoutParams.setMargins(i, 0, 0, i);
            addView(settingButton, layoutParams);
            settingButton.setOnClickListener(this);
        }

        private ImageButton setUpImageButton(String tag, Drawable drawable) {
            ImageButton imageButton;

            imageButton = new ImageButton(context);
            imageButton.setTag(tag);

            imageButton.setImageDrawable(drawable);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                imageButton.setForegroundGravity(Gravity.CENTER_VERTICAL);
            }
            imageButton.setBackgroundColor(context.getResources().getColor(R.color.transparent));

            return imageButton;
        }

        @Override
        public void onClick(View v) {
            if (v.getTag().equals("fullScreen")) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                else {
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }

                //((CourseDetailsActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
            } else if (v.getTag().equals("setting")) {
                try {
                    if (isTrailer)
                        showPopUp(trailerVideoResolutionList, trailerVideoUrlList);
                    else
                        showPopUp(fullVideoResolutionList, fullVideoUrlList);

                } catch (Exception e) {
                    H.log("ErrorIs", e.toString());
                    e.printStackTrace();
                }

            }
        }
    }

    private void loadFragment(String course_id) {
        Bundle bundle = new Bundle();
        bundle.putString("course_id", course_id);
        bundle.putString("fromCrash", "1");
        AppCompatActivity activity = (AppCompatActivity) v.getContext();
        buyCourseFragment = new BuyCourseFragment();
        buyCourseFragment.setArguments(bundle);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, buyCourseFragment)
                .addToBackStack("")
                .commit();
    }

    class CourseDetailsParentViewHolder {

        private final ViewGroup viewParentLayout;
        TextView txtTitle;
        ImageView ivExpand;
        LinearLayout llParent, llChild;

        public CourseDetailsParentViewHolder() {

            viewParentLayout = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.layout_lecture_parent, null);

            llParent = viewParentLayout.findViewById(R.id.llParent);
            llChild = viewParentLayout.findViewById(R.id.llChild);
            txtTitle = viewParentLayout.findViewById(R.id.txtTitle);
            ivExpand = viewParentLayout.findViewById(R.id.ivExpClp);

            llParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickParent(v);
                }
            });
        }

        ViewGroup getViewGroup() {
            return viewParentLayout;
        }

        public void onClickParent(View v) {

            switch (v.getId()) {

                case R.id.llParent:

                    if (llChild.getChildCount() > 0) {
                        if (llChild.getVisibility() == View.GONE) {
                            llChild.setVisibility(View.VISIBLE);
                            ivExpand.setImageResource(R.drawable.ic_collapse);
                        } else {
                            llChild.setVisibility(View.GONE);
                            ivExpand.setImageResource(R.drawable.ic_expand);

                        }
                    }

                    break;
            }
        }
    }


    class CourseDetailsChildViewHolder {

        private final ViewGroup viewChildLayout;

        TextView txtLectureTitle, txtVideoDetails, txtPreview, txtCount;

        public CourseDetailsChildViewHolder() {
            viewChildLayout = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.layout_lecture_child, null);

            txtLectureTitle = viewChildLayout.findViewById(R.id.txtLectureTitle);
            txtVideoDetails = viewChildLayout.findViewById(R.id.txtVideoDetails);
            txtPreview = viewChildLayout.findViewById(R.id.txtPreview);
            txtCount = viewChildLayout.findViewById(R.id.txtCount);
            llCollapse = viewChildLayout.findViewById(R.id.llCollapse);

            llCollapse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onClickChild(v);
                }
            });
        }

        private void onClickChild(View v) {

            switch (v.getId()) {

                case R.id.llCollapse:

                    break;
            }
        }

        ViewGroup getViewGroup() {
            return viewChildLayout;
        }
    }

    private void callback() {
//        categoriesCoursesList.clear();
        if (Config.POP_HOME){
            Config.POP_HOME = false;
            getFragmentManager().popBackStackImmediate();
//            BaseScreenActivity.binding.bottomNavigation.setVisibility(View.VISIBLE);
        }else if (isFromHome) {
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            BaseScreenActivity.callBack();
        }
        else {
            getFragmentManager().popBackStackImmediate();
        }
    }

}