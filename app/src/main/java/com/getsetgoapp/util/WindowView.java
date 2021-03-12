package com.getsetgoapp.util;

import android.app.Activity;
import android.os.Build;

import com.getsetgoapp.R;

public class WindowView {

    public static void getWindow(Activity activity){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.colorPrimaryDark));
                activity.getWindow().setNavigationBarColor(activity.getResources().getColor(R.color.colorGray));
            }
        }catch (Exception e){
        }
    }

}
