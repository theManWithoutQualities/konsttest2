package com.example.konsttest2.netApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.konsttest2.R;
import com.example.konsttest2.listApp.AppItem;

import java.util.ArrayList;
import java.util.List;

import static com.example.konsttest2.ChooseDensityFragment.DENSITY_STANDARD;
import static com.example.konsttest2.ChooseDensityFragment.KEY_DENSITY;

public class NetAppFragment extends Fragment {

    private final List<AppItem> appItemList = new ArrayList<>();
    private NetAppAdapter netAppAdapter;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadApps();
            getNetAppAdapter().notifyDataSetChanged();
        }
    };

    public NetAppAdapter getNetAppAdapter() {
        return netAppAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_net, container, false);

        final RecyclerView netRecyclerView = view.findViewById(R.id.netRecyclerView);
        netRecyclerView.setHasFixedSize(true);
        final String density = PreferenceManager
                .getDefaultSharedPreferences(getActivity())
                .getString(KEY_DENSITY, DENSITY_STANDARD);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                getContext(),
                calculateSpans(
                        density,
                        getResources().getConfiguration().orientation
                )
        );
        netRecyclerView.setLayoutManager(gridLayoutManager);
        netAppAdapter = new NetAppAdapter(appItemList, getContext());
        netRecyclerView.setAdapter(netAppAdapter);
        final int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.offset);
        netRecyclerView.addItemDecoration(new CustomDecoration(dimensionPixelOffset));

        loadApps();

        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("REFRESH_APPS"));

        return view;
    }

    private int calculateSpans(String density, int orientation) {
        if ("standard".equals(density)) {
            switch (orientation) {
                case Configuration.ORIENTATION_PORTRAIT:
                    return 4;
                case Configuration.ORIENTATION_LANDSCAPE:
                    return 6;
                default:
                    return 4;
            }
        } else if ("high".equals(density)){
            switch (orientation) {
                case Configuration.ORIENTATION_PORTRAIT:
                    return 5;
                case Configuration.ORIENTATION_LANDSCAPE:
                    return 7;
                default:
                    return 5;
            }
        }
        return 5;
    }

    public void loadApps() {
        appItemList.clear();
        final PackageManager packageManager = getActivity().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);

        for (ResolveInfo resolveInfo : resolveInfoList) {
            AppItem appItem = new AppItem();
            appItem.setIcon(resolveInfo.activityInfo.loadIcon(packageManager));
            appItem.setName(resolveInfo.loadLabel(packageManager).toString());
            appItem.setPackageName(resolveInfo.activityInfo.packageName);
            appItemList.add(appItem);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }
}
