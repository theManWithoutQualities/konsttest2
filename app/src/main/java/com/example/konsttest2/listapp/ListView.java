package com.example.konsttest2.listapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.konsttest2.R;

public class ListView extends LinearLayout {

    public ListView(Context context) {
        super(context);
    }

    public ListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTitle(String title) {
        final TextView titleView = findViewById(R.id.contact_title);
        titleView.setText(title);
    }

    public void setIcon(Drawable icon) {
        final View avatar = findViewById(R.id.avatar);
        avatar.setBackground(icon);
    }
}
