package com.example.konsttest2.main;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;

@TargetApi(26)
public class NotificationChannelCreator {
    public static String getChannelId(NotificationManager notificationManager) {
        String channelId = "konstChannel";
        String name = "konstChannel";
        String description = "my channel";
        final int importanceHigh = NotificationManager.IMPORTANCE_HIGH;
        final NotificationChannel notificationChannel =
                new NotificationChannel(channelId, name, importanceHigh);
        notificationChannel.setDescription(description);
        notificationManager.createNotificationChannel(notificationChannel);
        return channelId;
    }
}
