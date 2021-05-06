package com.getsetgoapp.activity;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getsetgoapp.Fragment.MyCourseDetailFragment;
import com.getsetgoapp.Fragment.MyCrashCourseDetailFragment;
import com.getsetgoapp.Model.VideoUrlModel;
import com.getsetgoapp.R;
import com.getsetgoapp.adapterview.VideoQualityAdapter;
import com.getsetgoapp.databinding.ActivityVideoPlayBinding;
import com.getsetgoapp.util.Click;
import com.getsetgoapp.util.Config;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

public class VideoPlayMyCrashCourseActivity extends AppCompatActivity implements Player.EventListener,VideoQualityAdapter.onClick {

    ActivityVideoPlayBinding activityVideoPlayBinding;
    SimpleExoPlayer exoPlayer;
    private ImageView imgFullScreen;
    private ImageView imgQuality;
    private VideoPlayMyCrashCourseActivity activity = this;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }catch (Exception e){
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityVideoPlayBinding = ActivityVideoPlayBinding.inflate(getLayoutInflater());
        setContentView(activityVideoPlayBinding.getRoot());
        playVideo(MyCrashCourseDetailFragment.videoPlayPath);

        onClick();

    }

    private void playVideo(String url){

        MyCrashCourseDetailFragment.videoPlayPath = url;

        Uri uri = Uri.parse(url);

        exoPlayer = new SimpleExoPlayer.Builder(this).build();
        activityVideoPlayBinding.playerView.setPlayer(exoPlayer);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.addListener(this);

        imgFullScreen = activityVideoPlayBinding.playerView.findViewById(R.id.exo_fullscreen_icon);
        imgQuality = activityVideoPlayBinding.playerView.findViewById(R.id.ivVideoQuality);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getApplicationContext(), getApplicationContext().getString(R.string.app_name)));

        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);

        exoPlayer.setMediaItem(MediaItem.fromUri(uri));
        exoPlayer.prepare(videoSource);

        if (MyCrashCourseDetailFragment.lastVideoPosition != C.TIME_UNSET) {
            exoPlayer.seekTo(MyCrashCourseDetailFragment.lastVideoPosition );
        }
        exoPlayer.play();

    }

    private void onClick(){

        imgFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                onBackPressed();
            }
        });

        imgQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.preventTwoClick(v);
                if (MyCrashCourseDetailFragment.videoUrlModelList!=null && !MyCrashCourseDetailFragment.videoUrlModelList.isEmpty()){
                    qualityUrlDialog(MyCrashCourseDetailFragment.videoUrlModelList);
                }
            }
        });
    }

    private void qualityUrlDialog(List<VideoUrlModel> videoUrlModelList){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_video_url_dialog);

        RecyclerView recyclerQuality = dialog.findViewById(R.id.recyclerQuality);
        recyclerQuality.setLayoutManager(new LinearLayoutManager(activity));
        VideoQualityAdapter adapter = new VideoQualityAdapter(activity,videoUrlModelList,MyCrashCourseDetailFragment.lastSelectedPosition,3);
        recyclerQuality.setAdapter(adapter);

        dialog.setCancelable(true);
        dialog.show();
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void qualityClick(VideoUrlModel model,int position) {
        if (dialog!=null){
            dialog.dismiss();
        }
        if (exoPlayer!=null){
            MyCrashCourseDetailFragment.lastSelectedPosition = position;
            MyCrashCourseDetailFragment.lastVideoPosition = exoPlayer.getCurrentPosition();
            exoPlayer.release();
            exoPlayer = null;
        }
        Log.e("TAG", "setVideoData: "+  model.getLink() );
        playVideo(model.getLink());
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if (playbackState == Player.STATE_BUFFERING) {
            activityVideoPlayBinding.pbVideoPlayer.setVisibility(View.VISIBLE);

        } else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED) {
            activityVideoPlayBinding.pbVideoPlayer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {

        if (exoPlayer != null) {
            Config.isHalfScreen = true;
            MyCrashCourseDetailFragment.lastVideoPosition = exoPlayer.getCurrentPosition();
            exoPlayer.release();
            exoPlayer = null;
            finish();
        }
    }

    public interface DataFromActivityToFragment {
        void sendData(String data);
    }

    private void closeActivity() {

    }
}