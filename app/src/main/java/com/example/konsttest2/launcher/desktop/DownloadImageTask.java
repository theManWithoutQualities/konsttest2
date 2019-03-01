package com.example.konsttest2.launcher.desktop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import com.example.konsttest2.R;
import java.io.InputStream;
import io.fabric.sdk.android.services.concurrency.AsyncTask;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private static final int LINK_SIZE = 120;
    private final ImageView image;
    private final Context context;

    public DownloadImageTask(ImageView image, Context context) {
        this.image = image;
        this.context = context;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = "https://favicon.yandex.net/favicon/" + urls[0] + "?size=" + LINK_SIZE;
        Bitmap bmp = null;
        try (InputStream in = new java.net.URL(urldisplay).openStream()) {
            bmp = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }
    protected void onPostExecute(Bitmap result) {
        Log.d("Konst", "DownloadImageTask#onPostExecute, bitmap = " + result);
        if (result == null || result.getWidth() < LINK_SIZE
                || result.sameAs(Bitmap.createBitmap(result.getWidth(), result.getHeight(), result.getConfig()))) {
            image.setImageDrawable(context.getDrawable(R.drawable.link_img));
        } else {
            image.setImageBitmap(result);
        }
    }
}
