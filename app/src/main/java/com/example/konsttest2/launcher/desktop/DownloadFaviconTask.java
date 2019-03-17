package com.example.konsttest2.launcher.desktop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import com.example.konsttest2.R;
import java.io.InputStream;

import static com.example.konsttest2.KonstTest2.TAG;

public class DownloadFaviconTask extends AsyncTask<String, Void, Bitmap> {

    private static final int LINK_SIZE = 120;
    private final ImageView image;
    private final Context context;

    public DownloadFaviconTask(ImageView image, Context context) {
        this.image = image;
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = "https://favicon.yandex.net/favicon/" + urls[0] + "?size=" + LINK_SIZE;
        Bitmap bmp = null;
        try (InputStream in = new java.net.URL(urldisplay).openStream()) {
            bmp = BitmapFactory.decodeStream(in);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bmp != null && (bmp.getWidth() < LINK_SIZE
                || bmp.sameAs(Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig())))) {
            return null;
        }
        return bmp;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        Log.d(TAG, "DownloadImageTask#onPostExecute, bitmap = " + result);
        if (result == null) {
            image.setImageDrawable(context.getDrawable(R.drawable.link_img));
        } else {
            image.setImageDrawable(new BitmapDrawable(context.getResources(), result));
        }
    }
}
