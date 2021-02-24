package com.getsetgo.Fragment;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.adoisstudio.helper.Api;
import com.adoisstudio.helper.H;
import com.adoisstudio.helper.Json;
import com.adoisstudio.helper.JsonList;
import com.adoisstudio.helper.LoadingDialog;
import com.adoisstudio.helper.MessageBox;
import com.getsetgo.R;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.activity.VideoPlayActivity;
import com.getsetgo.databinding.FragmentCurrentLearningBinding;
import com.getsetgo.util.App;
import com.getsetgo.util.JumpToLogin;
import com.getsetgo.util.P;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.getsetgo.util.Utilities.pxFromDp;

public class CurrentLearningFragment extends Fragment implements Player.EventListener, VideoPlayActivity.DataFromActivityToFragment {

    LinearLayout llCollapse;
    SimpleExoPlayer exoPlayer;
    String url = "https://html5demos.com/assets/dizzy.mp4";
    boolean fullscreen = false;
    ImageView ivFullScreen, ivQuality;
    PlayerView playerView;
    private Dialog mFullScreenDialog;
    private boolean mExoPlayerFullscreen = false;
    String selected = "";

    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";

    public static final int REQUEST_CODE = 101;
    public static final int RESULT_CODE = 12;
    public static final String PLAYBACK_POSITION = "playback_position";

    private int mResumeWindow;
    private long mResumePosition;

    FragmentCurrentLearningBinding fragmentCurrentLearningBinding;

    public CurrentLearningFragment() {
        // Required empty public constructor
    }

    public static CurrentLearningFragment newInstance() {
        CurrentLearningFragment fragment = new CurrentLearningFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);

        super.onSaveInstanceState(outState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentCurrentLearningBinding = FragmentCurrentLearningBinding.inflate(inflater, container, false);
        BaseScreenActivity.binding.incFragmenttool.txtTittle.setText("Student's Brain Booster");
        BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.GONE);
        BaseScreenActivity.binding.bottomNavigation.setVisibility(View.GONE);

        exoPlayer = ExoPlayerFactory.newSimpleInstance(getApplicationContext());
        playerView = fragmentCurrentLearningBinding.playerView.findViewById(R.id.playerView);

        ivFullScreen = fragmentCurrentLearningBinding.playerView.findViewById(R.id.exo_fullscreen_icon);
        ivQuality = fragmentCurrentLearningBinding.playerView.findViewById(R.id.ivVideoQuality);

//        BaseScreenActivity.binding.incFragmenttool.llSubCategory.setVisibility(View.VISIBLE);
//        BaseScreenActivity.binding.incFragmenttool.txtSubCat.setText(title);
        callCourseDetailsApi(getActivity(), /*slug*/"super-brain-booster");

//        exoPlayer = new SimpleExoPlayer.Builder(getActivity()).build();
//        fragmentCurrentLearningBinding.playerView.setPlayer(exoPlayer);
//        exoPlayer.setPlayWhenReady(true);
//        exoPlayer.addListener(this);

//        initializePlayer();

        return fragmentCurrentLearningBinding.getRoot();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        exoPlayer.stop(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void callCourseDetailsApi(Context context, String slug) {

        LoadingDialog loadingDialog = new LoadingDialog(context, false);
//        String apiParam = "?title" + title;
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
                        JumpToLogin.call(Json1,context);
                        loadingDialog.dismiss();
                        if (Json1.getInt(P.status) == 0) {
                            H.showMessage(context, Json1.getString(P.err));
                        } else {
                            Json1 = Json1.getJson(P.data);

                            setData(Json1);

                        }
                    }

                }).run("active_course_details");

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setData(Json list) {

        try {

            JSONObject courseObject = list.getJson("course");
            String courseName = courseObject.getString("course_name");
            String progress = courseObject.getString("complete_percentage");

            fragmentCurrentLearningBinding.txtCourseTitle.setText(courseName);
            fragmentCurrentLearningBinding.pbCourse.setProgress(Integer.parseInt(progress), true);
            fragmentCurrentLearningBinding.txtStatus.setText(progress + "%" + " " + "Completed");


            ArrayList<String> mVideoList = new ArrayList<>();

            Json jsonObject = list.getJson("course_videos");
            JSONObject element;
            Iterator<?> keys = jsonObject.keys();
            Map<String, ArrayList<JSONObject>> mMap = new LinkedHashMap<>();
            Map<String, ArrayList<JSONObject>> mAdditionalFilesMap = new LinkedHashMap<>();
            while (keys.hasNext()) {

                String key = (String) keys.next();

                JsonList arr = jsonObject.getJsonList(key);
                mVideoList.add(key);

//                JSONArray jsonList = jsonObject.getJSONArray("additional_files");
//                Log.d("Hardik","Hardik"+jsonList);
//
//                JSONObject jsonObject1 = jsonList.getJSONObject()
                ArrayList<JSONObject> mVideoNameList = new ArrayList<>();

                for (int i = 0; i < arr.size(); i++) {
                    element = arr.get(i);
                    mVideoNameList.add(element);
                }
                mMap.put(key, mVideoNameList);
            }
            Map<String, String> mVideoQualityMap = new LinkedHashMap<>();


            for (Map.Entry<String, ArrayList<JSONObject>> video : mMap.entrySet()) {

                int count = 1;
                CurrentCourseDetailsParentViewHolder currentCourseDetailsParentViewHolder = new CurrentCourseDetailsParentViewHolder();
                CurrentCourseAdditionalFilesParentViewHolder currentCourseAdditionalFilesParentViewHolder = new CurrentCourseAdditionalFilesParentViewHolder();
                currentCourseAdditionalFilesParentViewHolder.txtModuleTitle.setText(video.getKey());
                currentCourseDetailsParentViewHolder.txtTitle.setText(video.getKey());
                ArrayList<JSONObject> mTempList = new ArrayList<>();
                for (int i = 0; i < video.getValue().size(); i++) {
                    mTempList.add(video.getValue().get(i));
                }

                for (int i = 0; i < mTempList.size(); i++) {
                    CurrentCourseDetailsChildViewHolder courseDetailsChildViewHolder = new CurrentCourseDetailsChildViewHolder();

                    courseDetailsChildViewHolder.txtLectureTitle.setText(mTempList.get(i).getString("video_title"));
                    courseDetailsChildViewHolder.txtVideoDetails.setText("Video: " + mTempList.get(i).getString("duration") + " mins");
                    courseDetailsChildViewHolder.txtCount.setText(String.valueOf(count));


                    if (mTempList.get(i).getString("is_completed").equals("1")) {

                        courseDetailsChildViewHolder.txtCompleted.setVisibility(View.VISIBLE);
                        courseDetailsChildViewHolder.txtPreview.setVisibility(View.GONE);
                    } else {
                        courseDetailsChildViewHolder.txtCompleted.setVisibility(View.GONE);
                        courseDetailsChildViewHolder.txtPreview.setVisibility(View.VISIBLE);
                    }

                    if (mTempList.get(i).getJSONArray("additional_files").length() == 0) {
                        currentCourseDetailsParentViewHolder.llParent.setVisibility(View.GONE);
                    }

                    int finalI1 = i;

                    courseDetailsChildViewHolder.txtPreview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {

                                for (int k = 0; k < mTempList.get(finalI1).getJSONArray("video_urls").length(); k++) {

                                    mVideoQualityMap.put(mTempList.get(finalI1).getJSONArray("video_urls").getJSONObject(k).getString("quality"), mTempList.get(finalI1).getJSONArray("video_urls").getJSONObject(k).getString("link"));

                                }

                                Log.d("Hardik", "Hardik " + mVideoQualityMap);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });


                    for (int j = 0; j < mTempList.get(i).getJSONArray("additional_files").length(); j++) {
                        CurrentCourseAdditionalFilesChildViewHolder currentCourseAdditionalFilesChildViewHolder = new CurrentCourseAdditionalFilesChildViewHolder();

                        if (mTempList.get(i).getJSONArray("additional_files").length() > 0) {

                            currentCourseAdditionalFilesChildViewHolder.txtFileName.setText(mTempList.get(i).getJSONArray("additional_files").getJSONObject(j).getString("file_name"));
                            currentCourseAdditionalFilesChildViewHolder.txtFileName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            currentCourseAdditionalFilesChildViewHolder.txtFileName.setSelected(true);
                            currentCourseAdditionalFilesChildViewHolder.txtFileName.setSingleLine(true);
                            int finalI = i;
                            int finalJ = j;
                            currentCourseAdditionalFilesChildViewHolder.ivFileDownload.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    try {
                                        String url = mTempList.get(finalI).getJSONArray("additional_files").getJSONObject(finalJ).getString("file");
                                        String fileName = "document";
                                        File directory = getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                                        File document = File.createTempFile(fileName, ".pdf", directory);
                                        String documentPath = document.getAbsolutePath();
                                        Uri fileUri = FileProvider.getUriForFile(getActivity(), "com.getsetgo.fileProvider", document);

                                        DownloadManager dm = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                                        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(url));
                                        req.setDestinationUri(fileUri);
                                        req.setTitle("Document");

                                        BroadcastReceiver receiver = new BroadcastReceiver() {
                                            @Override
                                            public void onReceive(Context context, Intent intent) {
                                                getActivity().unregisterReceiver(this);
                                                if (document.exists()) {
//                                                    openPdfDocument(document);
                                                }
                                            }
                                        };
                                        getActivity().registerReceiver(receiver, new IntentFilter(
                                                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                                        dm.enqueue(req);

                                        Toast.makeText(getActivity(), "Url is: " + url, Toast.LENGTH_LONG).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
//                            currentCourseAdditionalFilesChildViewHolder.txtChapterTitle.setText("Chapter " + j+1 + ":");
                        } else {

                        }
                        currentCourseAdditionalFilesParentViewHolder.llAdditionalFilesChild.addView(currentCourseAdditionalFilesChildViewHolder.getViewGroup());

                    }
                    count++;
                    currentCourseDetailsParentViewHolder.llChild.addView(courseDetailsChildViewHolder.getViewGroup());

                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins((int) pxFromDp(getActivity(), 12), (int) pxFromDp(getActivity(), 4), (int) pxFromDp(getActivity(), 12), (int) pxFromDp(getActivity(), 4));

                currentCourseDetailsParentViewHolder.llParent.setLayoutParams(params);
                currentCourseAdditionalFilesParentViewHolder.llAdditionalFilesParent.setLayoutParams(params);
                fragmentCurrentLearningBinding.llCourseIncludes.addView(currentCourseDetailsParentViewHolder.getViewGroup());
                fragmentCurrentLearningBinding.llAdditionalFiles.addView(currentCourseAdditionalFilesParentViewHolder.getViewGroup());

            }

            ivQuality.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showQualityDialog(mVideoQualityMap);
                }
            });

            ivFullScreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getContext(), VideoPlayActivity.class);
                    intent.putExtra("url", "http://html5videoformatconverter.com/data/images/happyfit2.mp4");
                    startActivityForResult(intent, REQUEST_CODE);

                }
            });

            fragmentCurrentLearningBinding.playerView.setPlayer(exoPlayer);
            fragmentCurrentLearningBinding.playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getApplicationContext(), getApplicationContext().getString(R.string.app_name)));

            MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse("http://html5videoformatconverter.com/data/images/happyfit2.mp4"));

            exoPlayer.prepare(videoSource);
            exoPlayer.setPlayWhenReady(true);
//            videoUrl = list.getString("vimeo_url");
//            videoInit(v, videoUrl);

        } catch (
                JSONException e) {
            e.printStackTrace();
        }


        /*imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPlayClick();
            }
        });*/


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            String testResult = data.getStringExtra(PLAYBACK_POSITION);
            long l = Long.parseLong(testResult);

            Log.d("Hardik", "Playback position result " + l);

//            Toast.makeText(getActivity(),"Playback position result "+testResult,Toast.LENGTH_SHORT).show();
        }
    }

    private void showQualityDialog(Map<String, String> mVideoQualityMap) {


        final String[] quality = {"240p", "320p", "480p", "540p", "720p", "1080p"};


        ArrayList<String> mKey = new ArrayList<>();

        for (Map.Entry<String, String> video : mVideoQualityMap.entrySet()) {
            for (int i = 0; i < video.getKey().length(); i++) {
                mKey.add(video.getKey());
            }
        }

        Log.d("Hardik", "Quality" + mKey);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Quality");
        builder.setSingleChoiceItems(quality, 2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                selected = quality[which];
                Toast.makeText(getActivity(), " " + selected, Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });
    }

    /*@Override
    public void onResume() {
        super.onResume();

        if (playerView == null){

            initDialog();
        }

        initExoPlayer();

    }*/

    private void initExoPlayer() {

        /*BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(this,new DefaultRenderersFactory(getActivity()), trackSelector, loadControl);
        playerView.setPlayer(player);

        boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;

        if (haveResumePosition) {
            playerView.getPlayer().seekTo(mResumeWindow, mResumePosition);
        }

        player.prepare(mVideoSource);
        player.setPlayWhenReady(true);*/
    }

    private void initDialog() {

        /*ivFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mExoPlayerFullscreen){

                    openFullscreenDialog();
                }else {
                    closeFullscreenDialog();

                }
            }
        });*/
    }

    private void closeFullscreenDialog() {

        ((ViewGroup) fragmentCurrentLearningBinding.playerView.getParent()).removeView(fragmentCurrentLearningBinding.playerView);
        ((FrameLayout) getActivity().findViewById(R.id.main_media_frame)).addView(fragmentCurrentLearningBinding.playerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
    }

    private void openFullscreenDialog() {

        ((ViewGroup) playerView.getParent()).removeView(playerView);
        mFullScreenDialog.addContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_skrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }

    /*    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        if (fragmentCurrentLearningBinding.playerView.getVisibility() == View.VISIBLE)
        {
            exoPlayer.setFullscreen(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE, true);
            super.onConfigurationChanged(newConfig);
        }
        else
        {
            super.onConfigurationChanged(newConfig);
            if (getActivity() != null)
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }*/

    @Override
    public void onPause() {
        super.onPause();

        exoPlayer.pause();
    }

    private boolean openPdfDocument(File document) {
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(document), "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        try {
            startActivity(target);
            return true;
        } catch (Exception e) {
            Toast.makeText(getActivity(), "No PDF reader found", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if (playbackState == Player.STATE_BUFFERING) {
            fragmentCurrentLearningBinding.pbVideoPlayer.setVisibility(View.VISIBLE);

        } else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED) {
            fragmentCurrentLearningBinding.pbVideoPlayer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void sendData(String data) {
        if (data != null) {

        }
    }

    class CurrentCourseDetailsParentViewHolder {

        private final ViewGroup viewParentLayout;
        TextView txtTitle;
        ImageView ivExpand;
        LinearLayout llParent, llChild;

        public CurrentCourseDetailsParentViewHolder() {

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


    class CurrentCourseDetailsChildViewHolder {

        private final ViewGroup viewChildLayout;

        TextView txtLectureTitle, txtVideoDetails, txtPreview, txtCount, txtCompleted;

        public CurrentCourseDetailsChildViewHolder() {
            viewChildLayout = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.layout_lecture_child, null);

            txtLectureTitle = viewChildLayout.findViewById(R.id.txtLectureTitle);
            txtVideoDetails = viewChildLayout.findViewById(R.id.txtVideoDetails);
            txtPreview = viewChildLayout.findViewById(R.id.txtPreview);
            txtCount = viewChildLayout.findViewById(R.id.txtCount);
            llCollapse = viewChildLayout.findViewById(R.id.llCollapse);
            txtCompleted = viewChildLayout.findViewById(R.id.txtCompleted);

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

    class CurrentCourseAdditionalFilesParentViewHolder {

        private final ViewGroup viewGroup;
        TextView txtModuleTitle;
        ImageView ivExpClp;
        LinearLayout llAdditionalFilesParent, llAdditionalFilesChild;

        public CurrentCourseAdditionalFilesParentViewHolder() {

            viewGroup = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.layout_additional_files_parent, null);

            llAdditionalFilesParent = viewGroup.findViewById(R.id.llAdditionalFilesParent);
            llAdditionalFilesChild = viewGroup.findViewById(R.id.llAdditionalFilesChild);
            txtModuleTitle = viewGroup.findViewById(R.id.txtModuleTitle);
            ivExpClp = viewGroup.findViewById(R.id.ivExpClp);

            llAdditionalFilesParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickAdditionalParent(v);
                }
            });
        }

        ViewGroup getViewGroup() {
            return viewGroup;
        }

        public void onClickAdditionalParent(View v) {

            switch (v.getId()) {

                case R.id.llAdditionalFilesParent:

                    if (llAdditionalFilesChild.getChildCount() > 0) {
                        if (llAdditionalFilesChild.getVisibility() == View.GONE) {
                            llAdditionalFilesChild.setVisibility(View.VISIBLE);
                            ivExpClp.setImageResource(R.drawable.ic_collapse);
                        } else {
                            llAdditionalFilesChild.setVisibility(View.GONE);
                            ivExpClp.setImageResource(R.drawable.ic_expand);

                        }
                    }

                    break;
            }
        }
    }

    class CurrentCourseAdditionalFilesChildViewHolder {

        private final ViewGroup viewGroup;
        TextView txtFileName, txtChapterTitle;
        ImageView ivFileDownload;
        LinearLayout llAdditionalFiles;

        public CurrentCourseAdditionalFilesChildViewHolder() {

            viewGroup = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.layout_additional_files_child, null);

            llAdditionalFiles = viewGroup.findViewById(R.id.llAdditionalFiles);
            txtFileName = viewGroup.findViewById(R.id.txtFileName);
            txtChapterTitle = viewGroup.findViewById(R.id.txtChapterTitle);
            ivFileDownload = viewGroup.findViewById(R.id.ivFileDownload);

            ivFileDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickParent(v);
                }
            });
        }

        ViewGroup getViewGroup() {
            return viewGroup;
        }

        public void onClickParent(View v) {

            switch (v.getId()) {

                case R.id.ivFileDownload:

/*                    if (llChild.getChildCount() > 0) {
                        if (llChild.getVisibility() == View.GONE) {
                            llChild.setVisibility(View.VISIBLE);
                            ivExpand.setImageResource(R.drawable.ic_collapse);
                        } else {
                            llChild.setVisibility(View.GONE);
                            ivExpand.setImageResource(R.drawable.ic_expand);

                        }
                    }*/

                    Toast.makeText(getActivity(), "Cilcked", Toast.LENGTH_SHORT).show();

                    break;
            }
        }
    }
}
