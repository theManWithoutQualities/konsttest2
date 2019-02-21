package com.example.konsttest2.launcher.grid;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.konsttest2.R;
import com.example.konsttest2.launcher.LauncherView;

public class SquareView extends LinearLayout implements LauncherView {

    public SquareView(Context context) {
        super(context);
    }

    public SquareView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setTitle(String title) {
        final TextView titleView = findViewById(R.id.app_title);
        titleView.setText(title);
    }

    @Override
    public void setIcon(Drawable icon) {
        final View avatar = findViewById(R.id.app_icon);
        avatar.setBackground(icon);
    }

    @Override
    public void setBackgroundCol(int color) {
        findViewById(R.id.cell).setBackgroundColor(color);
    }
}
