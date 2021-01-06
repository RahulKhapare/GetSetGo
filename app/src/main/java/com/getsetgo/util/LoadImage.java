package com.getsetgo.util;

import android.text.TextUtils;
import android.widget.ImageView;

import com.getsetgo.R;
import com.squareup.picasso.Picasso;

public class LoadImage {

    public static void picasso(ImageView imageView,String imagePath){

        if (!TextUtils.isEmpty(imagePath)){
            Picasso.get().load(imagePath).error(R.mipmap.ic_launcher).into(imageView);
        }else {
            Picasso.get().load(R.mipmap.ic_launcher).into(imageView);
        }
    }
}
