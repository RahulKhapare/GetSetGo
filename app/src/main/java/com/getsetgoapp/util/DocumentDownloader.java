package com.getsetgoapp.util;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;

import com.adoisstudio.helper.H;

import java.io.File;
import java.io.IOException;

public  class DocumentDownloader {

    public static void download(Context context,String fileURL,String title) {
        checkDirectory(context,fileURL,title);
    }

    public static void checkDirectory(Context context,String fileURL,String title){
        try{
            String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/GetSetGo/Document/";
            String fileName = title;
            destination += fileName;
            File direct = new File(destination);
            if (direct.exists()) {
                OpenFile.openPath(context,direct);
            }else {
                startDownload(context,fileURL,title,destination);
            }
        }catch (Exception e){
            H.showMessage(context,"Something went wrong, try again.");
        }

    }

    public static void startDownload(Context context,String fileURL,String title,String destination){
        H.showMessage(context,"Downloading Started....");
        final Uri uri = Uri.parse("file://" + destination);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileURL));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDescription("Downloading....");
        request.setTitle(title);
        request.setDestinationUri(uri);

        final DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                H.showMessage(ctxt,"Downloading Completed...");
                context.unregisterReceiver(this);
                File direct = new File(destination);
                try {
                    OpenFile.openPath(context,direct);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

}
