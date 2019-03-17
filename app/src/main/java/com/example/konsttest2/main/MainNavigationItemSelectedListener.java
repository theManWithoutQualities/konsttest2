package com.example.konsttest2.main;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import com.example.konsttest2.R;
import com.example.konsttest2.metrica.MetricaUtils;
import com.example.konsttest2.settings.SettingsActivity;
import com.yandex.metrica.YandexMetrica;

public class MainNavigationItemSelectedListener
        implements NavigationView.OnNavigationItemSelectedListener {

    private MainActivity mainActivity;

    public MainNavigationItemSelectedListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mainActivity.hideDrawer();
        int id = item.getItemId();
        if (id == R.id.nav_desktop) {
            mainActivity.setDesktopFragment();
            YandexMetrica.reportEvent(MetricaUtils.CHOOSE_DESKTOP);
        } else if (id == R.id.nav_net) {
            mainActivity.setGridFragment();
            YandexMetrica.reportEvent(MetricaUtils.CHOOSE_GRID);
        } else if (id == R.id.nav_list) {
            mainActivity.setListFragment();
            YandexMetrica.reportEvent(MetricaUtils.CHOOSE_LIST);
        } else if (id == R.id.nav_settings) {
            YandexMetrica.reportEvent(MetricaUtils.CHOOSE_SETTINGS);
            final Intent intent = new Intent();
            intent.setClass(mainActivity, SettingsActivity.class);
            mainActivity.startActivity(intent);
        }
        return false;
    }
}
