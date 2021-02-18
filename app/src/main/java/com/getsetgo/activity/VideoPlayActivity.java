package com.getsetgo.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.getsetgo.R;
import com.getsetgo.databinding.ActivityVideoPlayBinding;
import com.getsetgo.util.Click;
import com.getsetgo.util.Config;
import com.google.android.exoplayer2.C;
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
    long time = 0;
    ActivityVideoPlayBinding activityVideoPlayBinding;
    SimpleExoPlayer exoPlayer;
    private ImageView imgFullScreen;
    private ImageView imgQuality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        activityVideoPlayBinding = ActivityVideoPlayBinding.inflate(getLayoutInflater());
        setContentView(activityVideoPlayBinding.getRoot());

        url = Config.url;
        time = Config.time;

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

        if (time != C.TIME_UNSET) {
            exoPlayer.seekTo(time);
        }

        exoPlayer.play();
        onClick();
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
            }
        });
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
            Config.time = exoPlayer.getContentPosition();

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