package com.example.konsttest2.launcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.preference.PreferenceManager;

import com.example.konsttest2.data.LauncherDbHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.example.konsttest2.launcher.LauncherFragment.EMPTY;
import static com.example.konsttest2.launcher.LauncherFragment.TOP_FREQUENT_COUNT;
import static com.example.konsttest2.settings.SettingsUtils.KEY_SORT;
import static com.example.konsttest2.settings.SettingsUtils.SORT_ALPHABETIC;
import static com.example.konsttest2.settings.SettingsUtils.SORT_ALPHABETIC_REVERSE;
import static com.example.konsttest2.settings.SettingsUtils.SORT_DATE;
import static com.example.konsttest2.settings.SettingsUtils.SORT_FREQUENCY;

public class FillAppItemListTask extends AsyncTask<Void, Void, List<AppItem>> {

    private LauncherFragment launcherFragment;
    private PackageManager packageManager;
    private String packageName;
    private LauncherDbHelper dbHelper;
    private Context context;

    public FillAppItemListTask(LauncherFragment launcherFragment) {
        this.launcherFragment = launcherFragment;
        packageManager = launcherFragment.getActivity().getPackageManager();
        packageName = launcherFragment.getContext().getPackageName();
        dbHelper = launcherFragment.getLauncherAdapter().getDbHelper();
        context = launcherFragment.getContext();
    }

    @Override
    protected List<AppItem> doInBackground(Void... voids) {
        final List<AppItem> initialList = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfoList) {
            if (resolveInfo.activityInfo.packageName.equals(packageName)) {
                continue;
            }
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
            final Integer count = launcherFragment
                    .getLauncherAdapter()
                    .getDbHelper()
                    .getCount(packageName);
            appItem.setCount(count == null ? 0 : count);
            final boolean isSystem =
                    (resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1;
            appItem.setSystem(isSystem);
            initialList.add(appItem);
        }
        List<AppItem> result = getPopularPositions(initialList);
        sort(initialList);
        result.addAll(initialList);
        return result;
    }

    @Override
    protected void onPostExecute(List<AppItem> tempList) {
        super.onPostExecute(tempList);
        launcherFragment.getAppItemList().clear();
        launcherFragment.getAppItemList().addAll(tempList);
        launcherFragment.getLauncherAdapter().notifyDataSetChanged();
    }

    private List<AppItem> getPopularPositions(List<AppItem> list) {
        Collections.sort(list, (a1, a2) -> a2.getCount().compareTo(a1.getCount()));
        final List<AppItem> appItemList = new ArrayList<>();
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
        return appItemList;
    }

    private void sort(List<AppItem> list) {
        final String sortName = PreferenceManager
                .getDefaultSharedPreferences(context)
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
