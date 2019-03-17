package com.example.konsttest2.launcher;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.text.SpannableString;

public interface LauncherView {

    void setTitle(SpannableString title);

    void setIcon(Drawable icon);

    void setBackgroundCol(int color);

    default void setColorToBackground(int color, Drawable background) {
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setStrokeWidth(10);
            ((ShapeDrawable)background).getPaint().setColor(color);
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setStroke(10, color);
        }
    }
}
