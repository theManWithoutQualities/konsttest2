package com.example.konsttest2.launcher.desktop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.InputStream;

import io.fabric.sdk.android.services.concurrency.AsyncTask;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    ImageView image;
    public DownloadImageTask(ImageView image) {
        this.image = image;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = "https://favicon.yandex.net/favicon/" + urls[0] + "?size=120";
        Bitmap bmp = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            bmp = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }
    protected void onPostExecute(Bitmap result) {
        image.setImageBitmap(result);
    }
}
