package com.example.konsttest2.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.example.konsttest2.KonstTest2.TAG;
import static com.example.konsttest2.settings.SettingsUtils.KEY_SORT;
import static com.example.konsttest2.settings.SettingsUtils.SORT_ALPHABETIC;
import static com.example.konsttest2.settings.SettingsUtils.SORT_ALPHABETIC_REVERSE;
import static com.example.konsttest2.settings.SettingsUtils.SORT_DATE;
import static com.example.konsttest2.settings.SettingsUtils.SORT_FREQUENCY;

public class LauncherFragment extends Fragment {

    public static final int TOP_FREQUENT_COUNT = 3;
    public static final String EMPTY = "";
    public static final String ACTION_APP_CLICKED = "ACTION_APP_CLICKED";

    protected final List<AppItem> appItemList = new ArrayList<>();
    protected LauncherAdapter launcherAdapter;
    protected final BroadcastReceiver refreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "receive intent add/del app");
            if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
                launcherAdapter.getDbHelper().deleteAppInfo(intent.getDataString());
            }
            loadApps();
        }
    };
    protected final BroadcastReceiver clickBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "receive intent click app");
            loadApps();
        }
    };

    public LauncherAdapter getLauncherAdapter() {
        return launcherAdapter;
    }

    public List<AppItem> getAppItemList() {
        return appItemList;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final IntentFilter filterRefreshApps = new IntentFilter();
        filterRefreshApps.addAction("android.intent.action.PACKAGE_ADDED");
        filterRefreshApps.addAction("android.intent.action.PACKAGE_REMOVED");
        filterRefreshApps.addDataScheme("package");
        context.registerReceiver(refreshBroadcastReceiver, filterRefreshApps);
        Log.d(TAG, "register receiver for refresh apps");
        context.registerReceiver(clickBroadcastReceiver, new IntentFilter(ACTION_APP_CLICKED));
        Log.d(TAG, "register receiver for click apps");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unregisterReceiver(refreshBroadcastReceiver);
        getActivity().unregisterReceiver(clickBroadcastReceiver);
        Log.d(TAG, "unregister receiver for refresh apps");
        Log.d(TAG, "unregister receiver for click apps");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "fragment onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "fragment onPause");
    }

    protected void loadApps() {
        new FillAppItemListTask(this).execute();
    }

}
