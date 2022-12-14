package com.getsetgoapp.util;

import android.content.Context;
import android.net.ConnectivityManager;

public class CheckConnection {

    public static boolean isVailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
