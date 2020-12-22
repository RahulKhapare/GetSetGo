package com.getsetgo.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.adoisstudio.helper.H;
import com.getsetgo.R;
import com.getsetgo.activity.NotificationsActivity;
import com.getsetgo.util.LoadImage;
import com.getsetgo.util.P;
import com.google.android.datatransport.Priority;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class FirebaseMessageReceiver
        extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessageReceiver";
    private Bitmap bitmap;
    private static ArrayList<Map<String, String>> arrayList = new ArrayList<>();

    @Override
    public void
    onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());

            Map data = remoteMessage.getData();
            arrayList.add(data);
            H.log("arrayListIs", arrayList + "");

            for (Map map : arrayList) {
                Object action = map.get(P.action);
                Object title = map.get(P.title);
                Object description = map.get(P.description);
                Uri s = null;

                if (title == null || description == null)
                    return;


                if (action != null) {
                    if (action.toString().equalsIgnoreCase("CATEGORY")) {
                        showNotification(title.toString(), description.toString(), s);
                    }
                }
            }
        }

        if (remoteMessage.getNotification() != null) {
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getImageUrl());
        }
    }

    public void showNotification(String title, String message, Uri imageUrl) {
        Intent intent = new Intent(this, NotificationsActivity.class);
        String channel_id = "notification_channel";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(getApplicationContext(), channel_id)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setOnlyAlertOnce(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000})
                .setLights(Color.RED, 3000, 3000)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.JELLY_BEAN) {
                builder = builder.setContent(getCustomDesign(title, message, imageUrl));
        } else {
            builder = builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_launcher_background);
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel_id, "web_app", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(0, builder.build());
    }

    private RemoteViews getCustomDesign(String title,String message,Uri imageUrl) {
        RemoteViews remoteViews = new RemoteViews(
                getApplicationContext().getPackageName(),
                R.layout.layout_notification_row);
        remoteViews.setTextViewText(R.id.txtNotifyTitle, title);
        remoteViews.setTextViewText(R.id.txtNotifyDetails, message);
        if (imageUrl != null) {
            bitmap = getBitmapfromUrl(imageUrl.toString());
        }
        remoteViews.setImageViewBitmap(R.id.imvNotification, bitmap);

        return remoteViews;
    }

    private RemoteViews getSpecialDesign(String title,
                                        String message, Uri imageUrl) {
        RemoteViews remoteViews = new RemoteViews(
                getApplicationContext().getPackageName(),
                R.layout.layout_special_notification_row);
        remoteViews.setTextViewText(R.id.txtSpecialTitle, title);
        remoteViews.setTextViewText(R.id.txtSpecialDetails, message);
        if (imageUrl != null) {
            bitmap = getBitmapfromUrl(imageUrl.toString());
        }
        remoteViews.setImageViewBitmap(R.id.imvSpecial, bitmap);

        return remoteViews;
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
}