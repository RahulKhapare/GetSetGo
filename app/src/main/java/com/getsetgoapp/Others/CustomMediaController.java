package com.getsetgoapp.Others;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;

import com.adoisstudio.helper.H;
import com.getsetgoapp.R;

public class CustomMediaController extends MediaController implements View.OnClickListener {
    public ImageButton imageButton;
    private final Context context;

    public CustomMediaController(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void setAnchorView(View view)
    {
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

        imageButton.setForegroundGravity(Gravity.CENTER_VERTICAL);
        imageButton.setBackgroundColor(context.getResources().getColor(R.color.transparent));

        return imageButton;
    }

    @Override
    public void onClick(View v)
    {
        if (v.getTag().equals("fullScreen"))
        {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                ((VideoActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            else
            {
                ((VideoActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            //((VideoActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        }
        else if (v.getTag().equals("setting"))
        {
            try {
                if (VideoActivity.isTrailer)
                    ((VideoActivity) context).showPopUp(VideoActivity.trailerVideoResolutionList,VideoActivity.trailerVideoUrlList);
                else
                    ((VideoActivity) context).showPopUp(VideoActivity.fullVideoResolutionList,VideoActivity.fullVideoUrlList);

            } catch (Exception e) {
                H.log("ErrorIs", e.toString());
                e.printStackTrace();
            }

        }
    }
}
