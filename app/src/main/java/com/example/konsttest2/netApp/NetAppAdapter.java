package com.example.konsttest2.netApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.konsttest2.R;
import com.example.konsttest2.data.AppDbHelper;
import com.example.konsttest2.listApp.AppItem;

import java.util.List;

class NetAppAdapter extends RecyclerView.Adapter<NetAppAdapter.IconsViewHolder>{
    private final List<AppItem> appItemList;
    private final Context context;
    AppDbHelper dbHelper;

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
        View.OnClickListener startItemListener = (v) -> {
            dbHelper.addClick(appItemList.get(getAdapterPosition()).getPackageName());
            startAt(getAdapterPosition());
            return;
        };

        ActionMode.Callback actionModeCallBack = new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater = mode.getMenuInflater();
                menuInflater.inflate(R.menu.app_context_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.context_delete:
                        removeAt(getAdapterPosition());
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        };

        public IconsViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnLongClickListener((v) -> {
                v.startActionMode(actionModeCallBack);
                return true;
            });
            itemView.setOnClickListener(startItemListener);
        }

        public void bindIcon(Drawable icon) {
            ((SquareView)itemView).setIcon(icon);
        }
        public void bindName(String name) {
            ((SquareView)itemView).setTitle(name);
        }
    }

    public void removeAt(int position) {
        final Intent intent = new Intent("android.intent.action.DELETE");
        intent.setData(Uri.parse("package:" + appItemList.get(position).getPackageName()));
        context.startActivity(intent);
    }
    public void startAt(int position) {
        final Intent launchIntentForPackage = context
                .getPackageManager()
                .getLaunchIntentForPackage(appItemList.get(position).getPackageName());
        if (launchIntentForPackage != null) {
            context.startActivity(launchIntentForPackage);
        }
    }
}
