package com.getsetgo.util;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.adoisstudio.helper.H;
import com.adoisstudio.helper.JsonList;
import com.getsetgo.Others.VideoActivity;
import com.getsetgo.activity.BaseScreenActivity;
import com.getsetgo.activity.LoginActivity;

import java.util.HashMap;
import java.util.Map;

public class App extends Application {

    public static App app = new App();
    public static String authToken = "";
    public static String user_id = "";
    public static JsonList jsonList;


    public void startVideoActivity(Context context, String string, int videoProgress) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra(P.url, string);
        intent.putExtra("videoProgress", videoProgress);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

   /* public void startMyCourseActivity(Context context, String string, int videoProgress) {
        Intent intent = new Intent(context, CourseDetailsActivity.class);
        intent.putExtra(P.url, string);
        intent.putExtra("videoProgress", videoProgress);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }*/

    public static void startHomeActivity(Context context) {
        Intent intent = new Intent(context, BaseScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static void BackToLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static Map<String, String> getHeaders() {

        Map<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");
        headers.put("x-api-key", "123456");
        if(user_id != null && !user_id.isEmpty()){
            headers.put("token", user_id);
        }

        H.log("headersAre", headers + "");
        return headers;
    }

    /*public void hitApiForPaymentGatewayUrl(String string, String id, Context context) {
        LoadingDialog loadingDialog = new LoadingDialog(context);

        Json json = new Json();
        json.addString(P.id, id);

        Api.newApi(context, P.baseUrl + string).setMethod(Api.POST).addJson(json).onHeaderRequest(com.just.app.App::getHeaders)
                .onLoading((b) ->
                {
                    if (context instanceof PurchasePlanActivity && !((PurchasePlanActivity) context).isDestroyed()) {
                        if (b)
                            loadingDialog.show("loading...");
                        else
                            loadingDialog.hide();
                    } else if (context instanceof SubscribeActivity && !((SubscribeActivity) context).isDestroyed()) {
                        if (b)
                            loadingDialog.show("loading...");
                        else
                            loadingDialog.hide();
                    }
                })
                .onError(() -> MessageBox.showOkMessage(context, "Alert", "Something went wrong. please try again.", () -> hitApiForPaymentGatewayUrl(string, id, context)))
                .onSuccess((j) -> {
                    if (j.getInt(P.status) == 1) {
                        j = j.getJson(P.data);
                        String str = j.getString(P.payment_url);
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("pgUrl", str);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        if (context instanceof PurchasePlanActivity)
                            ((PurchasePlanActivity) context).startActivityForResult(intent, 49);
                        else
                            context.startActivity(intent);
                    }
                }).run("hitApiForPaymentGatewayUrl");
    }*/

}
