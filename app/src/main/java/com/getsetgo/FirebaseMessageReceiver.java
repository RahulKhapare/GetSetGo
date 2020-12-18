package com.getsetgo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.adoisstudio.helper.H;
import com.getsetgo.activity.NotificationsActivity;
import com.getsetgo.util.P;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

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

                if (title == null || description == null)
                    return;

                if (action != null) {
                    if (action.toString().equalsIgnoreCase("CATEGORY")){
                        showNotification(title.toString(), description.toString());
                    }
                }
            }
        }

        if (remoteMessage.getNotification() != null) {
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }

    public void showNotification(String title, String message) {
        Intent intent = new Intent(this, NotificationsActivity.class);
        String channel_id = "notification_channel";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(getApplicationContext(), channel_id)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.JELLY_BEAN) {
            builder = builder.setContent(getCustomDesign(title, message));
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

    private RemoteViews getCustomDesign(String title,
                                        String message) {
        RemoteViews remoteViews = new RemoteViews(
                getApplicationContext().getPackageName(),
                R.layout.layout_notification_row);
        remoteViews.setTextViewText(R.id.txtNotifyTitle, title);
        remoteViews.setTextViewText(R.id.txtNotifyDetails, message);
        remoteViews.setImageViewResource(R.id.imvNotification,
                R.drawable.ic_launcher_background);
        return remoteViews;
    }
}