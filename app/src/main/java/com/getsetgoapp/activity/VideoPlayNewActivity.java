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

import com.getsetgoapp.Fragment.CourseDetailFragment;
import com.getsetgoapp.Fragment.MyCourseDetailFragment;
import com.getsetgoapp.Model.VideoUrlModel;
import com.getsetgoapp.R;
import com.getsetgoapp.adapterview.VideoCourseQualityAdapter;
import com.getsetgoapp.adapterview.VideoQualityAdapter;
import com.getsetgoapp.databinding.ActivityVideoPlayNewBinding;
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

public class VideoPlayNewActivity extends AppCompatActivity implements Player.EventListener, VideoCourseQualityAdapter.onClick {

    ActivityVideoPlayNewBinding activityVideoPlayBinding;
    SimpleExoPlayer exoPlayer;
    private ImageView imgFullScreen;
    private ImageView imgQuality;
    private VideoPlayNewActivity activity = this;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activityVideoPlayBinding = ActivityVideoPlayNewBinding.inflate(getLayoutInflater());
        setContentView(activityVideoPlayBinding.getRoot());
        playVideo(CourseDetailFragment.videoPlayPath);

        onClick();
    }

    private void playVideo(String url){

        CourseDetailFragment.videoPlayPath = url;

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

        if (CourseDetailFragment.lastVideoPosition != C.TIME_UNSET) {
            exoPlayer.seekTo(CourseDetailFragment.lastVideoPosition );
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
                if (CourseDetailFragment.videoUrlModelList!=null && !CourseDetailFragment.videoUrlModelList.isEmpty()){
                    qualityUrlDialog(CourseDetailFragment.videoUrlModelList);
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
        VideoCourseQualityAdapter adapter = new VideoCourseQualityAdapter(activity,videoUrlModelList,CourseDetailFragment.lastSelectedPosition,2);
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
            CourseDetailFragment.lastSelectedPosition = position;
            CourseDetailFragment.lastVideoPosition = exoPlayer.getCurrentPosition();
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
            CourseDetailFragment.lastVideoPosition = exoPlayer.getCurrentPosition();
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