package com.example.konsttest2.listApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.konsttest2.R;

import java.util.ArrayList;
import java.util.List;

public class ListAppFragment extends Fragment {
    private final List<AppItem> appItemList = new ArrayList<>();
    private ListAppAdapter listAppAdapter;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadApps();
            getListAppAdapter().notifyDataSetChanged();
        }
    };

    public ListAppAdapter getListAppAdapter() {
        return listAppAdapter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_list, container, false);

        final RecyclerView listRecyclerView = view.findViewById(R.id.listRecyclerView);
        listRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        listRecyclerView.setLayoutManager(linearLayoutManager);
        listAppAdapter = new ListAppAdapter(appItemList, getContext());
        listRecyclerView.setAdapter(listAppAdapter);

        loadApps();

        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("REFRESH_APPS"));

        return view;
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
