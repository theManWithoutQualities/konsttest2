package com.example.konsttest2.launcher;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.konsttest2.R;
import com.example.konsttest2.data.LauncherDbHelper;
import com.example.konsttest2.metrica.MetricaUtils;
import com.example.konsttest2.statistic.StatisticActivity;
import com.yandex.metrica.YandexMetrica;

import java.util.List;

import static com.example.konsttest2.launcher.LauncherFragment.TOP_FREQUENT_COUNT;

public abstract class LauncherAdapter extends RecyclerView.Adapter {

    private static final String COUNT = "count";
    private static final String ACCENT_COLOR = "#43A047";

    protected final List<AppItem> appItemList;
    protected final Context context;
    protected final LauncherDbHelper dbHelper;

    public LauncherAdapter(List<AppItem> appItemList, Context context) {
        this.appItemList = appItemList;
        this.context = context;
        dbHelper = new LauncherDbHelper(context);
    }

    public LauncherDbHelper getDbHelper() {
        return dbHelper;
    }

    @Override
    public int getItemCount() {
        return appItemList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((LauncherHolder)viewHolder).bindIcon(appItemList.get(i).getIcon());
        ((LauncherHolder)viewHolder).bindName(appItemList.get(i).getName());
        if (i < TOP_FREQUENT_COUNT) {
            ((LauncherHolder)viewHolder).bindBackgroundColor(Color.parseColor(ACCENT_COLOR));
        } else {
            ((LauncherHolder)viewHolder).bindBackgroundColor(Color.TRANSPARENT);
        }
    }


    public class LauncherHolder extends RecyclerView.ViewHolder {

        View.OnClickListener startItemListener = (v) -> {
            YandexMetrica.reportEvent(MetricaUtils.START_APP);
            appItemList
                    .get(getAdapterPosition())
                    .setCount(appItemList.get(getAdapterPosition()).getCount() + 1);
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
                        YandexMetrica.reportEvent(MetricaUtils.CONTEXT_DELETE_APP);
                        return true;
                    case R.id.frequency:
                        final Intent intent = new Intent();
                        intent.setClass(context, StatisticActivity.class);
                        final Integer count = appItemList.get(getAdapterPosition()).getCount();
                        intent.putExtra(COUNT, count == null ? 0 : count);
                        context.startActivity(intent);
                        mode.finish();
                        YandexMetrica.reportEvent(MetricaUtils.CONTEXT_FREQUENCY);
                        return true;
                    case R.id.info:
                        YandexMetrica.reportEvent(MetricaUtils.CONTEXT_INFO);
                        Intent settingsIntent =
                                new Intent(
                                        android.provider.Settings
                                                .ACTION_APPLICATION_DETAILS_SETTINGS
                                );
                        settingsIntent.setData(
                                Uri.parse(
                                        "package:" + appItemList
                                                .get(getAdapterPosition())
                                                .getPackageName()
                                )
                        );
                        context.startActivity(settingsIntent);
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
        public LauncherHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnLongClickListener((v) -> {
                YandexMetrica.reportEvent(MetricaUtils.LONGCLICK_APP);
                v.startActionMode(actionModeCallBack);
                return true;
            });
            itemView.setOnClickListener(startItemListener);
        }

        public void bindIcon(Drawable icon) {
            ((LauncherView)itemView).setIcon(icon);
        }
        public void bindName(String name) {
            ((LauncherView)itemView).setTitle(name);
        }
        public void bindBackgroundColor(int color) {
            ((LauncherView)itemView).setBackgroundCol(color);
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
