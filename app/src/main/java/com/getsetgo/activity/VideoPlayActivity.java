package com.getsetgo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.getsetgo.Fragment.CurrentLearningFragment;
import com.getsetgo.R;
import com.getsetgo.databinding.ActivityVideoPlayBinding;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class VideoPlayActivity extends AppCompatActivity implements Player.EventListener {

    String url = "";
    ActivityVideoPlayBinding activityVideoPlayBinding;
    SimpleExoPlayer exoPlayer;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        activityVideoPlayBinding = ActivityVideoPlayBinding.inflate(getLayoutInflater());
        setContentView(activityVideoPlayBinding.getRoot());

        url = getIntent().getStringExtra("url");
        Uri uri = Uri.parse(url);

        Log.d("Video", "Url: " + url);
        exoPlayer = new SimpleExoPlayer.Builder(this).build();
        activityVideoPlayBinding.playerView.setPlayer(exoPlayer);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.addListener(this);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getApplicationContext(), getApplicationContext().getString(R.string.app_name)));

        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);

        exoPlayer.setMediaItem(MediaItem.fromUri(uri));
        exoPlayer.prepare(videoSource);
        exoPlayer.play();
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
        super.onBackPressed();

        if (exoPlayer != null) {
            playWhenReady = exoPlayer.getPlayWhenReady();
            playbackPosition = exoPlayer.getCurrentPosition();
            currentWindow = exoPlayer.getCurrentWindowIndex();
            Intent intent = new Intent();
            intent.putExtra(CurrentLearningFragment.PLAYBACK_POSITION, playbackPosition);
            setResult(CurrentLearningFragment.RESULT_CODE, intent); // You can also send result without any data using setResult(int resultCode)
            exoPlayer.release();
            exoPlayer = null;
            finish();

        }
    }

    /*    @Override
    protected void onDestroy() {
        super.onDestroy();


//        exoPlayer.pause();
//        exoPlayer.release();
    }*/

    public interface DataFromActivityToFragment {
        void sendData(String data);
    }

    private void closeActivity() {
    }
}