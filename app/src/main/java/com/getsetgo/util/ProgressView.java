package com.getsetgo.util;

import android.content.Context;

import com.adoisstudio.helper.LoadingDialog;

public class ProgressView {

    public static void show(Context context, LoadingDialog loadingDialog){
        if (loadingDialog!=null){
            loadingDialog.show("Please wait....");
        }
    }

    public static void dismiss(LoadingDialog loadingDialog){
        if (loadingDialog!=null){
            loadingDialog.dismiss();
        }
    }

}
