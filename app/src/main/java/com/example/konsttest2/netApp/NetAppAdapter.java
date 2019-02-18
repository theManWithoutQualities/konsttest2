package com.example.konsttest2.netApp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.konsttest2.R;
import com.example.konsttest2.listApp.AppItem;

import java.util.List;

class NetAppAdapter extends RecyclerView.Adapter<NetAppAdapter.IconsViewHolder>{
    private final List<AppItem> appItemList;
    private final Context context;

    public NetAppAdapter(List<AppItem> appItemList, Context context) {
        this.appItemList = appItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public NetAppAdapter.IconsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_net_view, viewGroup, false);
        return new IconsViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return appItemList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull IconsViewHolder iconsViewHolder, int position) {
        iconsViewHolder.bindIcon(appItemList.get(position).getIcon());
        iconsViewHolder.bindName(appItemList.get(position).getName());
    }

    public class IconsViewHolder extends RecyclerView.ViewHolder {
        private final View.OnClickListener deleteIconListener =
                (v) -> removeAt(getAdapterPosition());
        public IconsViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnLongClickListener((v) -> {
                Snackbar
                        .make(v, "Are you sure?", Snackbar.LENGTH_INDEFINITE)
                        .setDuration(5000)
                        .setAction("Yes", deleteIconListener)
                        .show();
                Log.i("ACTION", "snackbar");
                return true;
            });
        }

        public void bindIcon(Drawable icon) {
            ((SquareView)itemView).setIcon(icon);
        }
        public void bindName(String name) {
            ((SquareView)itemView).setTitle(name);
        }
    }

    public void removeAt(int position) {
        appItemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, appItemList.size());
        Log.i("ACTION", "remove icon");
    }
}
