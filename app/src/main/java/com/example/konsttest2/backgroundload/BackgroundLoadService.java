package com.example.konsttest2.backgroundload;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Log;

import static com.example.konsttest2.KonstTest2.TAG;
import static com.example.konsttest2.settings.SettingsUtils.KEY_CHANGE_WALLPAPER_NOW;
import static com.example.konsttest2.settings.SettingsUtils.KEY_WALLPAPER_SOURCE;
import static com.example.konsttest2.settings.SettingsUtils.PLACEIMG;
import static com.example.konsttest2.settings.SettingsUtils.WALLPAPER_SOURCE_LOREM;
import static com.example.konsttest2.settings.SettingsUtils.WALLPAPER_SOURCE_PICSUM;

public class BackgroundLoadService extends JobService {

    public static final int JOB_ID_LOAD_IMAGE = 21234;
    public static final String BACKGROUND_IMAGE_NAME = "background.png";
    public static final String BROADCAST_ACTION_UPDATE_IMAGE = "UPDATE_IMAGE";
    public static final String BROADCAST_EXTRA_IMAGE_NAME = "IMAGE_NAME";
    private final BackgroundDownloader mBackgroundDownloader;

    public BackgroundLoadService() {
        mBackgroundDownloader = new BackgroundDownloader();
    }



    @Override
    public boolean onStartJob(JobParameters params) {
        int jobId = params.getJobId();
        if (jobId == JOB_ID_LOAD_IMAGE) {
            Log.d(TAG, "load image by service");
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            preferences.edit().putBoolean(KEY_CHANGE_WALLPAPER_NOW, false).apply();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int widthPixels = getResources().getDisplayMetrics().widthPixels;
                    int heightPixels = getResources().getDisplayMetrics().heightPixels;
                    final String url = PreferenceManager
                            .getDefaultSharedPreferences(getApplicationContext())
                            .getString(KEY_WALLPAPER_SOURCE, WALLPAPER_SOURCE_LOREM);
                    String finalUrl = null;
                    switch (url) {
                        case WALLPAPER_SOURCE_LOREM:
                            finalUrl = url + widthPixels + "/" + heightPixels;
                            break;
                        case WALLPAPER_SOURCE_PICSUM:
                            finalUrl = url + widthPixels + "/" + heightPixels + "/?random";
                            break;
                        case PLACEIMG:
                            finalUrl = url + widthPixels + "/" + heightPixels + "/any";
                            break;
                    }
                    final Bitmap bitmap = mBackgroundDownloader
                            .loadBitmap(finalUrl);
                    final String imageName = BACKGROUND_IMAGE_NAME;
                    CacheBackgroundHandler
                            .getInstance()
                            .saveImage(getApplicationContext(), bitmap, imageName);

                    final Intent broadcastIntent = new Intent(BROADCAST_ACTION_UPDATE_IMAGE);
                    broadcastIntent.putExtra(BROADCAST_EXTRA_IMAGE_NAME, imageName);
                    sendBroadcast(broadcastIntent);
                    jobFinished(params, false);
                }
            }).start();
            return true;
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "stop job loading background");
        return false;
    }
}
