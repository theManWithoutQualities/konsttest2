package com.example.konsttest2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.example.konsttest2.imageload.BackgroundLoadService;
import com.example.konsttest2.imageload.CacheBackgroundHandler;
import com.example.konsttest2.imageload.RestartBackgroundLoadServiceBroadcastReceiver;
import com.example.konsttest2.launcher.desktop.DesktopFragment;
import com.example.konsttest2.launcher.list.ListFragment;
import com.example.konsttest2.launcher.grid.GridFragment;
import io.fabric.sdk.android.Fabric;

import com.example.konsttest2.metrica.MetricaUtils;
import com.example.konsttest2.profile.ProfileActivity;
import com.example.konsttest2.settings.SettingsActivity;
import com.example.konsttest2.welcome.WelcomeSlideActivity;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.microsoft.appcenter.distribute.Distribute;
import com.yandex.metrica.YandexMetrica;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static com.example.konsttest2.imageload.BackgroundLoadService.BACKGROUND_IMAGE_NAME;
import static com.example.konsttest2.settings.SettingsUtils.KEY_CHANGE_WALLPAPER_NOW;
import static com.example.konsttest2.settings.SettingsUtils.SHOW_WELCOME_PAGE_KEY;

public class MainActivity extends BasicActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private UpdateBackgroundBroadcastReceiver updateBackgroundBroadcastReceiver;
    private RestartBackgroundLoadServiceBroadcastReceiver restartBackgroundLoadServiceBroadcastReceiver;

    public static final String RESTART_IMAGE_SERVICE = "RESTART_IMAGE_SERVICE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCenter.start(getApplication(), "450322b7-9374-4d01-bcaf-5abf9816b3cf",
                Analytics.class, Crashes.class, Distribute.class);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());

        final SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        final boolean showWelcomePage =
                preferences.getBoolean(SHOW_WELCOME_PAGE_KEY, true);
        if (showWelcomePage) {
            final Intent intent = new Intent(this, WelcomeSlideActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

        mPager = findViewById(R.id.main_content);
        final List<Fragment> fragments =
                Arrays.asList(
                        new DesktopFragment(),
                        new GridFragment(),
                        new ListFragment()
                );
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), fragments);
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        if (savedInstanceState == null) {
            setDesktopFragment();
        }

        restartBackgroundLoadServiceBroadcastReceiver = new RestartBackgroundLoadServiceBroadcastReceiver();
        updateBackgroundBroadcastReceiver = new UpdateBackgroundBroadcastReceiver();
        registerReceiver(updateBackgroundBroadcastReceiver,
                new IntentFilter(BackgroundLoadService.BROADCAST_ACTION_UPDATE_IMAGE));
        registerReceiver(restartBackgroundLoadServiceBroadcastReceiver,
                new IntentFilter(RESTART_IMAGE_SERVICE));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 12) {
            calendar.add(Calendar.DATE, 1);
        }
        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(
                        this,
                        0,
                        new Intent(
                                this,
                                RestartBackgroundLoadServiceBroadcastReceiver.class
                        ).setAction(RESTART_IMAGE_SERVICE),
                        0
                );
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );
        Log.d("Konst", "Alarm manager is configured");
        final boolean changeWallpaperNow = preferences
                .getBoolean(KEY_CHANGE_WALLPAPER_NOW, true);
        if(changeWallpaperNow) {
            restartBackgroundLoading();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBackground();
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

        if (id == R.id.nav_desktop) {
            setDesktopFragment();
            YandexMetrica.reportEvent(MetricaUtils.CHOOSE_DESKTOP);
        } else if (id == R.id.nav_net) {
            setGridFragment();
            YandexMetrica.reportEvent(MetricaUtils.CHOOSE_GRID);
        } else if (id == R.id.nav_list) {
            setListFragment();
            YandexMetrica.reportEvent(MetricaUtils.CHOOSE_LIST);
        } else if (id == R.id.nav_settings) {
            YandexMetrica.reportEvent(MetricaUtils.CHOOSE_SETTINGS);
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

    public void setDesktopFragment() {
        mPager.setCurrentItem(0, true);
    }

    public void setGridFragment() {
        mPager.setCurrentItem(1, true);
    }

    public void setListFragment() {
        mPager.setCurrentItem(2, true);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> fragments;
        public ScreenSlidePagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    private class UpdateBackgroundBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            String action = intent.getAction();
            if (BackgroundLoadService.BROADCAST_ACTION_UPDATE_IMAGE.equals(action)) {
                final String imageName = intent.getStringExtra(BackgroundLoadService.BROADCAST_EXTRA_IMAGE_NAME);
                if (TextUtils.isEmpty(imageName) == false) {
                    setBackground();
                }
            }
        }
    }

    private void setBackground() {
        final Bitmap bitmap = CacheBackgroundHandler
                .getInstance()
                .loadImage(getApplicationContext(), BACKGROUND_IMAGE_NAME);
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        int heightPixels = getResources().getDisplayMetrics().heightPixels;
        if (bitmap != null) {
            getWindow()
                    .setBackgroundDrawable(
                            new BitmapDrawable(getResources(),
                                    Bitmap.createScaledBitmap(bitmap, widthPixels, heightPixels, false)
                            )
                    );
        }
    }

    private void restartBackgroundLoading() {
        final Intent intent = new Intent(RESTART_IMAGE_SERVICE);
        sendBroadcast(intent);
        Log.d("Konst", "send intent: restart background loading");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(updateBackgroundBroadcastReceiver);
            unregisterReceiver(restartBackgroundLoadServiceBroadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
