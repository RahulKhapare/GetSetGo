package com.getsetgo.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.getsetgo.Model.CourseDocumentModel;
import com.getsetgo.Model.CourseLinkModel;
import com.getsetgo.Model.CourseModuleModel;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.activity.VideoPlayActivity;
import com.getsetgo.adapterview.CourseDocumentAdapter;
import com.getsetgo.adapterview.CourseLinkAdapter;
import com.getsetgo.adapterview.CourseModuleAdapter;
import com.getsetgo.databinding.FragmentMyCourseDetailsBinding;
import com.getsetgo.util.App;
import com.getsetgo.util.Click;
import com.getsetgo.util.Config;
import com.getsetgo.util.P;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MyCourseDetailFragment extends Fragment implements Player.EventListener{

    private FragmentMyCourseDetailsBinding binding;
    private Context context;
    private SimpleExoPlayer exoPlayer;
    private PlayerView playerView;

    private ImageView imgFullScreen;
    private ImageView imgQuality;

    private String courseSlug;

    String dummyPath = "http://html5videoformatconverter.com/data/images/happyfit2.mp4";
    public static final int REQUEST_CODE = 101;
    public static final int RESULT_CODE = 12;

    private CourseModuleAdapter courseModuleAdapter;
    private CourseDocumentAdapter courseDocumentAdapter;
    private CourseLinkAdapter courseLinkAdapter;

    private List<CourseModuleModel> courseModuleModelList;
    private List<CourseDocumentModel> courseDocumentModelList;
    private List<CourseLinkModel> courseLinkModelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_course_details, container, false);
            context = inflater.getContext();
            courseSlug = Config.myCourseSlug;
            BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Course Details");
            BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);
            BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);
            initView();

        }

        return binding.getRoot();
    }

    private void initView(){

        exoPlayer = ExoPlayerFactory.newSimpleInstance(getApplicationContext());
        playerView = binding.playerView.findViewById(R.id.playerView);
        imgFullScreen = binding.playerView.findViewById(R.id.exo_fullscreen_icon);
        imgQuality = binding.playerView.findViewById(R.id.ivVideoQuality);

        courseModuleModelList = new ArrayList<>();
        courseModuleAdapter = new CourseModuleAdapter(context,courseModuleModelList);
        binding.recyclerModule.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerModule.setNestedScrollingEnabled(false);
        binding.recyclerModule.setAdapter(courseModuleAdapter);

        courseDocumentModelList = new ArrayList<>();
        courseDocumentAdapter = new CourseDocumentAdapter(context,courseDocumentModelList);
        binding.recyclerDocument.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerDocument.setNestedScrollingEnabled(false);
        binding.recyclerDocument.setAdapter(courseDocumentAdapter);

        courseLinkModelList = new ArrayList<>();
        courseLinkAdapter = new CourseLinkAdapter(context,courseLinkModelList);
        binding.recyclerLink.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerLink.setNestedScrollingEnabled(false);
        binding.recyclerLink.setAdapter(courseLinkAdapter);

        callCourseDetailsApi(context,courseSlug);
        playVideo(dummyPath);
        onClick();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Config.isHalfScreen){
            Config.isHalfScreen = false;
            long time = Config.time;

            exoPlayer.setPlayWhenReady(true);
            if (time != C.TIME_UNSET) {
                exoPlayer.seekTo(time);
            }
            exoPlayer.play();
        }
    }

    private void onClick(){

        imgFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                exoPlayer.pause();
                Config.url = dummyPath;
                Config.time = exoPlayer.getContentPosition();
                Intent intent = new Intent(getContext(), VideoPlayActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        imgQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });

        binding.btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
            }
        });
    }

    private void playVideo(String videoPath){
        binding.playerView.setPlayer(exoPlayer);
        binding.playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getApplicationContext(), getApplicationContext().getString(R.string.app_name)));

        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(videoPath));

        exoPlayer.prepare(videoSource);
        exoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        exoPlayer.stop(true);
    }

    @Override
    public void onPause() {
        super.onPause();

        exoPlayer.pause();
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if (playbackState == Player.STATE_BUFFERING) {
            binding.videoProgressBar.setVisibility(View.VISIBLE);

        } else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED) {
            binding.videoProgressBar.setVisibility(View.INVISIBLE);
        }
    }


    private void callCourseDetailsApi(Context context, String slug) {

        LoadingDialog loadingDialog = new LoadingDialog(context, false);
        Api.newApi(context, P.baseUrl + "active_course" + "/" + slug)
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
                        loadingDialog.dismiss();
                        if (Json1.getInt(P.status) == 0) {
                            H.showMessage(context, Json1.getString(P.err));
                        } else {
                            Json1 = Json1.getJson(P.data);
                            setData(Json1);
                        }
                    }

                }).run("callCourseDetailsApi");

    }


    private void setData(Json jsonData){

        Json courseObject = jsonData.getJson(P.course);
        Json course_videos_Object = jsonData.getJson(P.course_videos);
        String courseName = courseObject.getString(P.course_name);
        String progress = courseObject.getString(P.complete_percentage);

        if (checkString(progress,binding.txtCourseName)){
            binding.txtCourseName.setText(courseName);
        }

        if (checkString(progress,binding.txtPercentage)){
            binding.txtPercentage.setText(progress + "%" + " " + "Completed");
        }

        if (checkString(progress)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.progressBar.setProgress(Integer.parseInt(progress), true);
            }
        }

    }

    @Override
    public void onDestroyView()
    {
        if (binding.getRoot() != null)
        {
            ViewGroup parentViewGroup = (ViewGroup) binding.getRoot().getParent();

            if (parentViewGroup != null)
            {
                parentViewGroup.removeAllViews();
            }
        }

        super.onDestroyView();
    }

    public static MyCourseDetailFragment newInstance() {
        MyCourseDetailFragment fragment = new MyCourseDetailFragment();
        return fragment;
    }

    private boolean checkString(String string, TextView textView){
        boolean value = true;

        if (TextUtils.isEmpty(string) || string.equals("null")){
            value = false;
            textView.setVisibility(View.GONE);
        }
        return value;
    }

    private boolean checkString(String string){
        boolean value = true;

        if (TextUtils.isEmpty(string) || string.equals("null")){
            value = false; }
        return value;
    }
}
