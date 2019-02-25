package com.example.konsttest2.imageload;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import static com.example.konsttest2.MainActivity.RESTART_IMAGE_SERVICE;
import static com.example.konsttest2.settings.SettingsUtils.CHANGE_WALLPAPER_PERIODIC_15_MIN;
import static com.example.konsttest2.settings.SettingsUtils.CHANGE_WALLPAPER_PERIODIC_1_HOUR;
import static com.example.konsttest2.settings.SettingsUtils.CHANGE_WALLPAPER_PERIODIC_24_HOURS;
import static com.example.konsttest2.settings.SettingsUtils.CHANGE_WALLPAPER_PERIODIC_8_HOURS;
import static com.example.konsttest2.settings.SettingsUtils.KEY_CHANGE_WALLPAPER_PERIODIC;

public class RestartImageLoadServiceBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        String action = intent.getAction();
        if (RESTART_IMAGE_SERVICE.equals(action)) {
            Log.d("Konst", "Receive restart service action");
            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            if (jobScheduler != null) {
                jobScheduler.cancel(ImageLoadService.JOB_ID_LOAD_IMAGE);
                long periodic;
                final String periodicString = PreferenceManager
                        .getDefaultSharedPreferences(context)
                        .getString(KEY_CHANGE_WALLPAPER_PERIODIC, CHANGE_WALLPAPER_PERIODIC_15_MIN);
                switch (periodicString) {
                    case CHANGE_WALLPAPER_PERIODIC_15_MIN:
                        periodic = 1000 * 60 * 15;
                        break;
                    case CHANGE_WALLPAPER_PERIODIC_1_HOUR:
                        periodic = 1000 * 60 * 60;
                        break;
                    case CHANGE_WALLPAPER_PERIODIC_8_HOURS:
                        periodic = 1000 * 60 * 60 * 8;
                        break;
                    case CHANGE_WALLPAPER_PERIODIC_24_HOURS:
                        periodic = 1000 * 60 * 60 * 24;
                        break;
                    default:
                        periodic = 1000 * 60 * 15;
                }
                jobScheduler.schedule(
                        new JobInfo.Builder(ImageLoadService.JOB_ID_LOAD_IMAGE,
                                new ComponentName(context, ImageLoadService.class))
                                .setPeriodic(periodic)
                                .setOverrideDeadline(0)
                                .build()
                );
            }

        }
    }
}
