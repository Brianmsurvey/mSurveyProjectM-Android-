package com.msurvey.projectm.msurveyprojectm.instantapp.Utilities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.msurvey.projectm.msurveyprojectm.instantapp.R;

public class NotificationUtils {


//    public void createNotificationChannel(Context context) {
//
//        if (Build.VERSION.SDK_INT < 26) {
//
//            return;
//
//        } else {
//
//            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            NotificationChannel channel = new NotificationChannel("default",
//                    "Channel name",
//                    NotificationManager.IMPORTANCE_DEFAULT);
//
//            channel.setDescription("Channel Descripion");
//            notificationManager.createNotificationChannel(channel);
//        }
//    }

    public static void createNotification(Context context) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "default")
                .setContentTitle("Test Notification title")
                .setContentText("Testing. That's all")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    public static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";
            String description = "It does stuff";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Channel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}