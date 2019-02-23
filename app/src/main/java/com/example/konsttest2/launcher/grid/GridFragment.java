package com.example.konsttest2.launcher.grid;

import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.konsttest2.R;
import com.example.konsttest2.launcher.LauncherFragment;

import static com.example.konsttest2.launcher.AppEventReceiver.REFRESH_APPS;
import static com.example.konsttest2.settings.SettingsUtils.DENSITY_HIGH;
import static com.example.konsttest2.settings.SettingsUtils.DENSITY_STANDARD;
import static com.example.konsttest2.settings.SettingsUtils.KEY_DENSITY;

public class GridFragment extends LauncherFragment {

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
        launcherAdapter = new GridAdapter(appItemList, getContext());
        netRecyclerView.setAdapter(launcherAdapter);
        final int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.offset);
        netRecyclerView.addItemDecoration(new CustomDecoration(dimensionPixelOffset));

        loadApps();

        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(REFRESH_APPS));

        return view;
    }

    private int calculateSpans(String density, int orientation) {
        if (DENSITY_STANDARD.equals(density)) {
            switch (orientation) {
                case Configuration.ORIENTATION_PORTRAIT:
                    return 4;
                case Configuration.ORIENTATION_LANDSCAPE:
                    return 6;
                default:
                    return 4;
            }
        } else if (DENSITY_HIGH.equals(density)){
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }
}
