package com.getsetgo.others;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.VideoView;

import com.adoisstudio.helper.H;
import com.getsetgo.activity.SplashActivity;


public class CustomVideoView extends VideoView
{

    public CustomVideoView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public CustomVideoView(Context context)
    {
        super(context);
    }

    @Override
    public void setVideoURI(Uri uri)
    {
       /* MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(getContext(), uri);*/
       /* mVideoWidth = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        mVideoHeight = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));*/

        super.setVideoURI(uri);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {

        int width = getDefaultSize(SplashActivity.deviceHeight, widthMeasureSpec);
        int height = getDefaultSize(SplashActivity.deviceWidth, heightMeasureSpec);
        H.log("widthAndHeightIs",width+"&"+height);
        if (SplashActivity.deviceHeight > 0 && SplashActivity.deviceWidth > 0) {
            if (SplashActivity.deviceHeight * height > width * SplashActivity.deviceWidth)
                height = width * SplashActivity.deviceWidth / SplashActivity.deviceHeight;
            else if (SplashActivity.deviceHeight * height < width * SplashActivity.deviceWidth)
                width = height * SplashActivity.deviceHeight / SplashActivity.deviceWidth;
        }
        setMeasuredDimension(width, height);
    }
}
