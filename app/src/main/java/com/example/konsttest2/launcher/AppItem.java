package com.example.konsttest2.launcher;

import android.graphics.drawable.Drawable;

import java.util.Date;

public class AppItem {
    private Drawable icon;
    private String name;
    private String packageName;
    private Integer count;
    private Boolean system;

    public Boolean getSystem() {
        return system;
    }

    public AppItem setSystem(Boolean system) {
        this.system = system;
        return this;
    }

    public Integer getCount() {
        return count;
    }

    public AppItem setCount(Integer count) {
        this.count = count;
        return this;
    }

    public Date getInstallDate() {
        return installDate;
    }

    public AppItem setInstallDate(Date installDate) {
        this.installDate = installDate;
        return this;
    }

    private Date installDate;

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
