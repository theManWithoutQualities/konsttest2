package com.example.konsttest2.main;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.example.konsttest2.R;

public class NotificationButtonListener implements View.OnClickListener {
    private boolean prettyView;
    private Context context;

    public NotificationButtonListener(boolean prettyView, Context context) {
        this.prettyView = prettyView;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        final NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= 26) {
            builder = new NotificationCompat.Builder(
                    context,
                    NotificationChannelCreator.getChannelId(notificationManager)
            );
        } else {
            builder = new NotificationCompat.Builder(context);
        }
        builder
                .setSmallIcon(R.drawable.push);
        if (prettyView) {
            final int identifier = context
                    .getResources()
                    .getIdentifier("hello", "drawable", context.getPackageName());
            final Bitmap bitmap = ((BitmapDrawable) context.getResources().getDrawable(identifier))
                    .getBitmap();
            builder
                    .setContentTitle("Pretty push")
                    .setContentText("This is a pretty push")
                    .setColor(Color.parseColor("#00ff00"))
                    .setLargeIcon(bitmap)
                    .setStyle(
                            new NotificationCompat.BigPictureStyle()
                                    .bigPicture(bitmap)
                                    .bigLargeIcon(null)
                    );
        } else {
            builder
                    .setContentTitle("Simple push")
                    .setContentText("This is a simple push");
        }
        final Intent intent = new Intent(context, MainActivity.class);
        final TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(MainActivity.class);
        taskStackBuilder.addNextIntent(intent);
        final PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        builder.setContentIntent(pendingIntent);
        notificationManager.notify(0, builder.build());
    }
}
