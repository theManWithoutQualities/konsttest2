package com.example.konsttest2.main;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.example.konsttest2.backgroundload.CacheBackgroundHandler;
import static com.example.konsttest2.backgroundload.BackgroundLoadService.BACKGROUND_IMAGE_NAME;

public class SetBackgroundTask extends AsyncTask<Void, Void, Drawable> {

    private Activity activity;

    public SetBackgroundTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Drawable doInBackground(Void... voids) {
        return CacheBackgroundHandler
                .getInstance()
                .loadImage(activity.getApplicationContext(), BACKGROUND_IMAGE_NAME);
    }

    @Override
    protected void onPostExecute(Drawable drawable) {
        super.onPostExecute(drawable);
        activity.getWindow().setBackgroundDrawable(drawable);
    }
}
