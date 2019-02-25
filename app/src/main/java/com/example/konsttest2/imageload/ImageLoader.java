package com.example.konsttest2.imageload;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class ImageLoader {

    @Nullable
    Bitmap loadBitmap(String srcUrl) {
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(srcUrl).build();
            Response response = okHttpClient.newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                byte[] bitmap = responseBody.bytes();
                return BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
