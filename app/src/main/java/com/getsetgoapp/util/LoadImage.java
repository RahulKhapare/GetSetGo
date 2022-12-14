package com.getsetgoapp.util;

import android.text.TextUtils;
import android.widget.ImageView;

import com.getsetgoapp.R;
import com.squareup.picasso.Picasso;

public class LoadImage {

    public static void picasso(ImageView imageView,String imagePath){

        if (!TextUtils.isEmpty(imagePath)){
            Picasso.get().load(imagePath).error(R.drawable.ic_no_image).into(imageView);
        }else {
            Picasso.get().load(R.drawable.ic_no_image).into(imageView);
        }
    }
}
