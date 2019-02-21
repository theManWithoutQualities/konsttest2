package com.example.konsttest2.launcher.grid;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.konsttest2.R;
import com.example.konsttest2.launcher.LauncherAdapter;
import com.example.konsttest2.launcher.AppItem;

import java.util.List;

public class GridAdapter extends LauncherAdapter {

    public GridAdapter(List<AppItem> appItemList, Context context) {
        super(appItemList, context);
    }

    @NonNull
    @Override
    public LauncherHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_net_view, viewGroup, false);
        return new LauncherHolder(view);
    }
}
