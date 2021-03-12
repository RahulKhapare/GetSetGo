package com.getsetgoapp.util;

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

    public static void hide(LoadingDialog loadingDialog){
        if (loadingDialog!=null){
            loadingDialog.hide();
        }
    }


    public static void killed(LoadingDialog loadingDialog){
        try {
            loadingDialog.dismiss();
        }catch (Exception e){

        }
    }

}
