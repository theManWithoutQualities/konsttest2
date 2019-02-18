package com.example.konsttest2.listApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.konsttest2.R;

import java.util.List;

public class ListAppAdapter extends RecyclerView.Adapter<ListAppAdapter.ListHolder> {
    private final List<AppItem> appItemList;
    private final Context context;

    public ListAppAdapter(List<AppItem> appItemList, Context context) {
        this.appItemList = appItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_list_view, viewGroup, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder listHolder, int i) {
        listHolder.bindIcon(appItemList.get(i).getIcon());
        listHolder.bindName(appItemList.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return appItemList.size();
    }



    public class ListHolder extends RecyclerView.ViewHolder {

        View.OnClickListener startItemListener = (v) -> startAt(getAdapterPosition());
//        ActionMode.Callback actionModeCallBack = new ActionMode.Callback() {
//            @Override
//            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                MenuInflater menuInflater = mode.getMenuInflater();
//                menuInflater.inflate(R.menu.app_context_menu, menu);
//                return true;
//            }
//
//            @Override
//            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                return false;
//            }
//
//            @Override
//            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.context_delete:
//                        removeAt(getAdapterPosition());
//                        mode.finish();
//                        return true;
//                     default:
//                         return false;
//                }
//            }
//
//            @Override
//            public void onDestroyActionMode(ActionMode mode) {
//
//            }
//        };
        public ListHolder(@NonNull View itemView) {
            super(itemView);
//            itemView.setOnLongClickListener((v) -> {
//                v.startActionMode(actionModeCallBack);
//                return true;
//            });
            itemView.setOnClickListener(startItemListener);
        }

        public void bindIcon(Drawable icon) {
            ((ListView)itemView).setIcon(icon);
        }
        public void bindName(String name) {
            ((ListView)itemView).setTitle(name);
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
