package com.example.konsttest2.launcher.list;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.konsttest2.R;
import com.example.konsttest2.launcher.LauncherFragment;

public class ListFragment extends LauncherFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_list, container, false);

        final RecyclerView listRecyclerView = view.findViewById(R.id.listRecyclerView);
        listRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        listRecyclerView.setLayoutManager(linearLayoutManager);
        launcherAdapter = new ListAdapter(appItemList, getContext());
        listRecyclerView.setAdapter(launcherAdapter);

        loadApps();

        return view;
    }
}
