package com.getsetgoapp.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.adoisstudio.helper.H;
import com.getsetgoapp.R;
import com.getsetgoapp.activity.BaseScreenActivity;
import com.getsetgoapp.util.P;
import com.getsetgoapp.util.RemoveHtml;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class FirebaseMessageReceiver extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessageReceiver";
    private Bitmap bitmap;
    private ArrayList<Map<String, String>> arrayList = new ArrayList<>();
    int count = 0;
    int number = 0;
    public FirebaseMessageReceiver() {

    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

//        Log.e(TAG, "onMessageReceivedData1: " + remoteMessage.getNotification().getBody());

        if (remoteMessage == null)
            return;

        if (remoteMessage.getData().size() > 0) {
            arrayList.clear();
            Map data = remoteMessage.getData();
            arrayList.add(data);

            H.log("arrayListIs", arrayList + "");
            Log.e(TAG, "onMessageReceivedData: " + data.toString());

//            for (Map map : arrayList) {
//                Object action = map.get(P.action);
//                Object title = map.get(P.title);
//                Object description = map.get(P.description);
//                Object actionData = map.get(P.action_data);
//                Object imageUrl = null;
//
//                try {
//                    if (map.get(P.icon).equals("") || map.get(P.icon).equals("null")) {
//                        imageUrl = null;
//                    } else {
//                        imageUrl = map.get(P.icon);
//                    }
//                } catch (Exception e) {
//                    imageUrl = map.get(P.icon);
//                }
//
//                if (title == null || description == null)
//                    return;
//
//                if (action != null && actionData != null) {
//                    int pendingNotificationsCount = AppName.getPendingNotificationsCount() + 1;
//                    AppName.setPendingNotificationsCount(pendingNotificationsCount);
//                    number = pendingNotificationsCount;
//
//                    if (imageUrl != null){
//                        bitmap = getBitmapfromUrl(imageUrl.toString());
//                    }
//                    sendCustomNotification(action.toString(), title.toString(), description.toString(), actionData.toString(), 2);
//                }
//
//            }

        }
    }


    private void sendCustomNotification(String action, String title, String description, String actionData, int requestCode) {

        Log.e(TAG, "sendCustomNotificationCount: "+ number + "" );

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_layout);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        remoteViews.setImageViewBitmap(R.id.imageView, bitmap);
        remoteViews.setTextViewText(R.id.titleTextView, title.trim());
        remoteViews.setTextViewText(R.id.descriptionTextView, RemoveHtml.html2text(description.trim()));

        try {
            if (bitmap == null) {
                remoteViews.setViewVisibility(R.id.imageView, View.GONE);
            } else {
                remoteViews.setViewVisibility(R.id.imageView, View.VISIBLE);
            }
        } catch (Exception e) {
        }

        Intent intent = new Intent(this, BaseScreenActivity.class);
        intent.putExtra(P.action, action);
        intent.putExtra(P.action_data, actionData);
        intent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;

        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String channelId = getString(R.string.app_name);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        NotificationCompat.Builder notificationCompactBuilder = new NotificationCompat.Builder(this, channelId);
        notificationCompactBuilder.setSmallIcon(getSmallIcon());

        notificationCompactBuilder.setAutoCancel(true) ;
        notificationCompactBuilder.setNumber(number) ;

        notificationCompactBuilder.setLargeIcon(icon);
        notificationCompactBuilder.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL);
        notificationCompactBuilder.setSound(defaultSoundUri);
        notificationCompactBuilder.setContentTitle(convertFromHtml(title.trim()));
        notificationCompactBuilder.setContentText(convertFromHtml(description.trim()));

        if (Build.VERSION.SDK_INT >= 24)
            notificationCompactBuilder.setCustomBigContentView(remoteViews);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_HIGH);
            channel.setShowBadge(true);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        notificationCompactBuilder.setContentIntent(pendingIntent);

        assert notificationManager != null;
        notificationManager.notify(number, notificationCompactBuilder.build());
        count++;
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }

    private Spanned convertFromHtml(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
        else
            return Html.fromHtml(text);
    }

    private int getSmallIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
    }

}