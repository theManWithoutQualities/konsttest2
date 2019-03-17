package com.example.konsttest2.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.konsttest2.backgroundload.RestartBackgroundLoadServiceBroadcastReceiver;

import java.util.Calendar;

import static com.example.konsttest2.KonstTest2.TAG;
import static com.example.konsttest2.main.MainActivity.RESTART_IMAGE_SERVICE;

public class AlarmClockConfigTask extends AsyncTask<Void, Void, Void> {

    private final Context context;

    public AlarmClockConfigTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 12) {
            calendar.add(Calendar.DATE, 1);
        }
        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(
                        context,
                        0,
                        new Intent(
                                context,
                                RestartBackgroundLoadServiceBroadcastReceiver.class
                        ).setAction(RESTART_IMAGE_SERVICE),
                        0
                );
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );
        Log.d(TAG, "Alarm manager is configured");
        return null;
    }
}
