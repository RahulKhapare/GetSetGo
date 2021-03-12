package com.getsetgoapp.Others;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.adoisstudio.helper.H;
import com.getsetgoapp.R;

import java.util.concurrent.TimeUnit;

public class VideoController implements SeekBar.OnSeekBarChangeListener {
    private final Context context;
    private final com.getsetgoapp.others.CustomVideoView customVideoView;
    public ImageView setting, zoom, back, playPause, forward;
    private final SeekBar seekBar;
    private final TextView time;
    private final TextView totalTime;
    private final LinearLayout videoControllerLayout;
    public static boolean videoIsRunning;

    public VideoController(Context context) {
        this.context = context;
        customVideoView = ((VideoActivity) context).findViewById(R.id.customVideoView);

        videoControllerLayout = ((VideoActivity) context).findViewById(R.id.videoControllerLayout);
        handleVisibilityOfVideoController();

        setting = videoControllerLayout.findViewById(R.id.settingImageView);
        zoom = videoControllerLayout.findViewById(R.id.zoomImageView);
        back = videoControllerLayout.findViewById(R.id.backImageView);
        playPause = videoControllerLayout.findViewById(R.id.ppImageView);
        forward = videoControllerLayout.findViewById(R.id.forwardImageView);
        seekBar = videoControllerLayout.findViewById(R.id.seekBar);
        time = videoControllerLayout.findViewById(R.id.timeTextView);
        totalTime = videoControllerLayout.findViewById(R.id.totalTimeTextView);

        RelativeLayout relativeLayout = videoControllerLayout.findViewById(R.id.imageViewContainer);
        for (int i = 0; i < relativeLayout.getChildCount(); i++)
            relativeLayout.getChildAt(i).setOnClickListener(this::onClick);

        seekBar.setOnSeekBarChangeListener(this);
        //customVideoView.setOnClickListener(this::onClick);
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

    public void handleVisibilityOfVideoController()
    {
        videoControllerLayout.setVisibility(View.VISIBLE);

        videoControllerLayout.postDelayed(() -> {
            if (customVideoView.isPlaying())
                videoControllerLayout.setVisibility(View.GONE);
        }, 3210);
    }

    private void handleZoomInAndOut(View view) {
        String string = view.getTag().toString();

        if (string.equalsIgnoreCase("zoomIn")) {
            ((VideoActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            view.setTag("zoomOut");
        } else {
            ((VideoActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            view.setTag("zoomIn");
        }
    }

    private void onSettingIconClick()
    {
        if (VideoActivity.isTrailer)
        {
            ((VideoActivity) context).showPopUp(VideoActivity.trailerVideoResolutionList, VideoActivity.trailerVideoUrlList);
            H.log("isTrailer","true");
        }
        else
        {
            ((VideoActivity) context).showPopUp(VideoActivity.fullVideoResolutionList, VideoActivity.fullVideoUrlList);
            H.log("isTrailer","false");
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

    public void handlePlayPause(boolean isVideoChanged)
    {
        if (customVideoView.isPlaying() && !isVideoChanged) {
            customVideoView.pause();
            playPause.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
            videoIsRunning = false;
        }
        else
            {
            customVideoView.start();
            playPause.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_pause_black_24dp));
            videoIsRunning = true;
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //H.log("fromUserIs", fromUser + "");

        if (fromUser)
        {
            customVideoView.seekTo(progress);
            //((VideoActivity)context).hitVideoProgressApi(customVideoView.getCurrentPosition() / 1000.0f);

            if (videoControllerLayout.getVisibility() != View.VISIBLE)
                handleVisibilityOfVideoController();
        }
        /*else if (progress>0 && progress<1230)
            ((VideoActivity)context).hitVideoProgressApi(customVideoView.getCurrentPosition() / 1000.0f);*/

        String string = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(progress),
                TimeUnit.MILLISECONDS.toMinutes(progress) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(progress)),
                TimeUnit.MILLISECONDS.toSeconds(progress) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(progress)));

        string = string.substring(string.indexOf(":") + 1);

       // if (string.contains(":00"))
            //((VideoActivity)context).hitVideoProgressApi(customVideoView.getCurrentPosition() / 1000.0f);

        time.setText(string);

        string = string.substring(string.lastIndexOf(":")+1);
        H.log("stringIs",string);
        progress = Integer.parseInt(string);
       /* if (progress%10 == 0)
            ((VideoActivity)context).hitVideoProgressApi(customVideoView.getCurrentPosition() / 1000.0f);*/
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //((VideoActivity)context).hitVideoProgressApi(customVideoView.getCurrentPosition() / 1000.0f);
        H.log("onStopTrackingTouch","isCalled");
    }
}
