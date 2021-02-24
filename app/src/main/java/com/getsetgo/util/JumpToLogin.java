package com.getsetgo.util;

import android.content.Context;
import android.content.Intent;

import com.adoisstudio.helper.Json;
import com.getsetgo.activity.LoginActivity;

public class JumpToLogin {

    public static void call(Json json, Context context){
        if (json!=null && json.has("status_code")){
            if (json.getString("status_code").equals("401")){
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        }
    }
}
