package com.example.konsttest2.imageload;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CacheBackgroundHandler {

    private static final Object mLock = new Object();
    private static volatile CacheBackgroundHandler sInstance;

    private static final String DIRECTORY_NAME = "images";

    public static CacheBackgroundHandler getInstance() {
        if (null == sInstance) {
            synchronized (mLock) {
                if (null == sInstance) {
                    sInstance = new CacheBackgroundHandler();
                }
            }
        }

        return sInstance;
    }

    private CacheBackgroundHandler() {
    }

    public void saveImage(final Context context, final Bitmap bitmap, final String fileName) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(createFile(context, fileName));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull
    private File createFile(final Context context, final String fileName) {
        File directory = context.getDir(DIRECTORY_NAME, Context.MODE_PRIVATE);

        return new File(directory, fileName);
    }

    public Bitmap loadImage(final Context context, final String fileName) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(createFile(context, fileName));
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
