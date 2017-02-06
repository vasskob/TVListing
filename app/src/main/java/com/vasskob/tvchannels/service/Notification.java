package com.vasskob.tvchannels.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.vasskob.tvchannels.MainActivity;
import com.vasskob.tvchannels.R;


public class Notification extends android.app.Notification {
    static void sendNotification(Context context, String msg, int id) {
        Resources resources = context.getResources();
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        Notification notification = (Notification) new NotificationCompat.Builder(context)
                .setTicker(msg)
                .setSmallIcon(R.drawable.ic_stat_file_download)
                .setContentTitle(resources.getString(R.string.app_name))
                .setContentText(msg)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .build();
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);
        notificationManager.notify(id, notification);


    }
}
