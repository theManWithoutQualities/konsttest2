package com.example.konsttest2.imageload;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;

import static com.example.konsttest2.settings.SettingsUtils.KEY_WALLPAPER_SOURCE;
import static com.example.konsttest2.settings.SettingsUtils.WALLPAPER_SOURCE_LOREM;

public class ImageLoadService extends JobService {

    public static final int JOB_ID_LOAD_IMAGE = 21234;
    public static final String BROADCAST_ACTION_UPDATE_IMAGE =
            "UPDATE_IMAGE";
    public static final String BROADCAST_EXTRA_IMAGE_NAME =
            "IMAGE_NAME";
    private final ImageLoader mImageLoader;

    public ImageLoadService() {
        mImageLoader = new ImageLoader();
    }



    @Override
    public boolean onStartJob(JobParameters params) {
        int jobId = params.getJobId();
        if (jobId == JOB_ID_LOAD_IMAGE) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int widthPixels = getResources().getDisplayMetrics().widthPixels;
                    int heightPixels = getResources().getDisplayMetrics().heightPixels;
                    final String url = PreferenceManager
                            .getDefaultSharedPreferences(getApplicationContext())
                            .getString(KEY_WALLPAPER_SOURCE, WALLPAPER_SOURCE_LOREM);
                    final Bitmap bitmap = mImageLoader
                            .loadBitmap(url + widthPixels + "/" + heightPixels);
                    final String imageName = "background.png";
                    CacheImageHandler.getInstance().saveImage(getApplicationContext(), bitmap, imageName);

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
        return false;
    }
}
