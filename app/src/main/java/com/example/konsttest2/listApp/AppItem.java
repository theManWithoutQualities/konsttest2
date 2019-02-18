package com.example.konsttest2.listApp;

import android.graphics.drawable.Drawable;

public class AppItem {
    private Drawable icon;
    private String name;
    private String packageName;

    public String getPackageName() {
        return packageName;
    }

    public AppItem setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public Drawable getIcon() {
        return icon;
    }

    public AppItem setIcon(Drawable icon) {
        this.icon = icon;
        return this;
    }

    public String getName() {
        return name;
    }

    public AppItem setName(String name) {
        this.name = name;
        return this;
    }
}
