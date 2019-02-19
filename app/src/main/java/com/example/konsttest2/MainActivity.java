package com.example.konsttest2;

import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.example.konsttest2.listApp.ListAppFragment;
import com.example.konsttest2.netApp.NetAppFragment;
import io.fabric.sdk.android.Fabric;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

public class MainActivity extends BasicActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCenter.start(getApplication(), "450322b7-9374-4d01-bcaf-5abf9816b3cf",
                Analytics.class, Crashes.class);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());

        final SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        final boolean showWelcomePage =
                preferences.getBoolean("showWelcomePage", true);
        if (showWelcomePage) {
            final Intent intent = new Intent(this, WelcomeSlideActivity.class);
            startActivity(intent);
            return;
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            setNetFragment();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_net) {
            setNetFragment();
        } else if (id == R.id.nav_list) {
            setListFragment();
        } else if (id == R.id.nav_settings) {
            final Intent intent = new Intent();
            intent.setClass(this, SettingsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void clickAvatarHandler(View view) {
        final Intent intent = new Intent();
        intent.setClass(this, ProfileActivity.class);
        startActivity(intent);
    }



    public void setListFragment() {
        Fragment listFragment = new ListAppFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.main_content, listFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    public void setNetFragment() {
        Fragment netFragment = new NetAppFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.main_content, netFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
