package com.example.konsttest2.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
            Log.d("Konst", "receive intent add/del app");
            if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
                launcherAdapter.getDbHelper().deleteAppInfo(intent.getDataString());
            }
            loadApps();
        }
    };
    protected final BroadcastReceiver clickBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Konst", "receive intent click app");
            loadApps();
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final IntentFilter filterRefreshApps = new IntentFilter();
        filterRefreshApps.addAction("android.intent.action.PACKAGE_ADDED");
        filterRefreshApps.addAction("android.intent.action.PACKAGE_REMOVED");
        filterRefreshApps.addDataScheme("package");
        context.registerReceiver(refreshBroadcastReceiver, filterRefreshApps);
        Log.d("Konst", "register receiver for refresh apps");
        context.registerReceiver(clickBroadcastReceiver, new IntentFilter(ACTION_APP_CLICKED));
        Log.d("Konst", "register receiver for click apps");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unregisterReceiver(refreshBroadcastReceiver);
        getActivity().unregisterReceiver(clickBroadcastReceiver);
        Log.d("Konst", "unregister receiver for refresh apps");
        Log.d("Konst", "unregister receiver for click apps");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Konst", "fragment onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Konst", "fragment onPause");
    }

    protected void loadApps() {
        appItemList.clear();
        final List<AppItem> tempList = new ArrayList<>();
        final PackageManager packageManager = getActivity().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);

        for (ResolveInfo resolveInfo : resolveInfoList) {
            AppItem appItem = new AppItem();
            appItem.setIcon(resolveInfo.activityInfo.loadIcon(packageManager));
            appItem.setName(resolveInfo.loadLabel(packageManager).toString());
            final String packageName = resolveInfo.activityInfo.packageName;
            appItem.setPackageName(packageName);
            try {
                appItem.setInstallDate(
                        new Date(
                                packageManager
                                        .getPackageInfo(packageName, 0)
                                        .firstInstallTime
                        )
                );
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            final Integer count = launcherAdapter.getDbHelper().getCount(packageName);
            appItem.setCount(count == null ? 0 : count);

            tempList.add(appItem);
        }

        fillPopularPositions(tempList);
        sort(tempList);
        appItemList.addAll(tempList);
        launcherAdapter.notifyDataSetChanged();
    }

    private void fillPopularPositions(List<AppItem> list) {
        Collections.sort(list, (a1, a2) -> a2.getCount().compareTo(a1.getCount()));
        if (list.size() >= TOP_FREQUENT_COUNT) {
            for (int num = 0; num < TOP_FREQUENT_COUNT; num++) {
                appItemList.add(list.get(num));
            }
        } else {
            appItemList.addAll(list);
            for (int num = appItemList.size() - 1; num < TOP_FREQUENT_COUNT; num++) {
                appItemList.add(
                        new AppItem()
                                .setIcon(new ColorDrawable(Color.TRANSPARENT))
                                .setName(EMPTY)
                );
            }
        }
    }

    private void sort(List<AppItem> list) {
        final String sortName = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(KEY_SORT, SORT_ALPHABETIC);
        switch (sortName) {
            case SORT_ALPHABETIC:
                Collections.sort(list, (a1, a2) -> a1.getName().compareTo(a2.getName()));
                break;
            case SORT_ALPHABETIC_REVERSE:
                Collections.sort(list, (a1, a2) -> a2.getName().compareTo(a1.getName()));
                break;
            case SORT_DATE:
                Collections.sort(list, (a1, a2) ->
                        a1.getInstallDate().compareTo(a2.getInstallDate()));
                break;
            case SORT_FREQUENCY:
                Collections.sort(list, (a1, a2) -> a1.getCount().compareTo(a2.getCount()));
                break;
            default:
                break;
        }
    }
}
