package com.example.konsttest2.main;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.example.konsttest2.BasicActivity;
import com.example.konsttest2.R;
import com.example.konsttest2.backgroundload.BackgroundLoadService;
import com.example.konsttest2.backgroundload.RestartBackgroundLoadServiceBroadcastReceiver;
import com.example.konsttest2.backgroundload.UpdateBackgroundBroadcastReceiver;
import com.example.konsttest2.launcher.desktop.DesktopFragment;
import com.example.konsttest2.launcher.grid.GridFragment;
import com.example.konsttest2.launcher.list.ListFragment;
import com.example.konsttest2.profile.ProfileActivity;
import com.example.konsttest2.welcome.WelcomeSlideActivity;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.microsoft.appcenter.distribute.Distribute;

import java.util.Arrays;
import java.util.List;

import io.fabric.sdk.android.Fabric;

import static com.example.konsttest2.KonstTest2.TAG;
import static com.example.konsttest2.settings.SettingsUtils.KEY_CHANGE_WALLPAPER_NOW;
import static com.example.konsttest2.settings.SettingsUtils.KEY_NEED_RECREATE;
import static com.example.konsttest2.settings.SettingsUtils.SHOW_WELCOME_PAGE_KEY;

public class MainActivity extends BasicActivity {

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private UpdateBackgroundBroadcastReceiver updateBackgroundBroadcastReceiver;
    private RestartBackgroundLoadServiceBroadcastReceiver restartBackgroundLoadServiceBroadcastReceiver;
    private boolean registeredReceivers = false;

    public static final String RESTART_IMAGE_SERVICE = "RESTART_IMAGE_SERVICE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Main onCreate");
        super.onCreate(savedInstanceState);
        AppCenter.start(getApplication(), "450322b7-9374-4d01-bcaf-5abf9816b3cf",
                Analytics.class, Crashes.class, Distribute.class);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        final SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        final boolean needRecreate = preferences.getBoolean(KEY_NEED_RECREATE, false);
        final boolean showWelcomePage = preferences.getBoolean(SHOW_WELCOME_PAGE_KEY, true);
        if (needRecreate) {
            preferences.edit().putBoolean(KEY_NEED_RECREATE, false).apply();
        } else if (showWelcomePage) {
            final Intent intent = new Intent(this, WelcomeSlideActivity.class);
            finish();
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
        navigationView.setNavigationItemSelectedListener(
                new MainNavigationItemSelectedListener(this)
        );
        findViewById(R.id.simple_push).setOnClickListener(
                new NotificationButtonListener(false, this)
        );
        findViewById(R.id.pretty_push).setOnClickListener(
                new NotificationButtonListener(true, this)
        );
        mPager = findViewById(R.id.main_content);
        final List<Fragment> fragments =
                Arrays.asList(
                        new DesktopFragment(),
                        new GridFragment(),
                        new ListFragment()
                );
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), fragments);
        mPager.setAdapter(mPagerAdapter);
        new AlarmClockConfigTask(this).execute();
        restartBackgroundLoadServiceBroadcastReceiver = new RestartBackgroundLoadServiceBroadcastReceiver();
        updateBackgroundBroadcastReceiver = new UpdateBackgroundBroadcastReceiver(this);
        registerReceiver(updateBackgroundBroadcastReceiver,
                new IntentFilter(BackgroundLoadService.BROADCAST_ACTION_UPDATE_IMAGE));
        registerReceiver(restartBackgroundLoadServiceBroadcastReceiver,
                new IntentFilter(RESTART_IMAGE_SERVICE));
        registeredReceivers = true;
        if (savedInstanceState == null) {
            setDesktopFragment();
            restartBackgroundLoading();
        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "Main onResume");
        super.onResume();
        if (!registeredReceivers) {
            registerReceiver(updateBackgroundBroadcastReceiver,
                    new IntentFilter(BackgroundLoadService.BROADCAST_ACTION_UPDATE_IMAGE));
            registerReceiver(restartBackgroundLoadServiceBroadcastReceiver,
                    new IntentFilter(RESTART_IMAGE_SERVICE));
        }
        final SharedPreferences defaultSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        final boolean needRecreate = defaultSharedPreferences
                .getBoolean(KEY_NEED_RECREATE, false);
        final boolean changeWallpaperNow = defaultSharedPreferences
                .getBoolean(KEY_CHANGE_WALLPAPER_NOW, true);
        if (needRecreate) {
            recreate();
        } else if(changeWallpaperNow) {
            restartBackgroundLoading();
        } else {
            setBackground();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(updateBackgroundBroadcastReceiver);
            unregisterReceiver(restartBackgroundLoadServiceBroadcastReceiver);
            registeredReceivers = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        hideDrawer();
    }

    public void clickAvatarHandler(View view) {
        hideDrawer();
        final Intent intent = new Intent();
        intent.setClass(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void setDesktopFragment() {
        mPager.setCurrentItem(0, true);
    }

    public void setGridFragment() {
        mPager.setCurrentItem(1, true);
    }

    public void setListFragment() {
        mPager.setCurrentItem(2, true);
    }

    public void setBackground() {
        new SetBackgroundTask(this).execute();
    }

    private void restartBackgroundLoading() {
        final Intent intent = new Intent(RESTART_IMAGE_SERVICE);
        sendBroadcast(intent);
        Log.d(TAG, "send intent: restart background loading");
    }

    public void hideDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }
}
