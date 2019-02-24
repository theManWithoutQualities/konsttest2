package com.example.konsttest2;

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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.example.konsttest2.launcher.desktop.DesktopFragment;
import com.example.konsttest2.launcher.list.ListFragment;
import com.example.konsttest2.launcher.grid.GridFragment;
import io.fabric.sdk.android.Fabric;

import com.example.konsttest2.profile.ProfileActivity;
import com.example.konsttest2.settings.SettingsActivity;
import com.example.konsttest2.welcome.WelcomeSlideActivity;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.microsoft.appcenter.distribute.Distribute;

import java.util.Arrays;
import java.util.List;

import static com.example.konsttest2.settings.SettingsUtils.SHOW_WELCOME_PAGE_KEY;

public class MainActivity extends BasicActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;

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
        } else if (id == R.id.nav_net) {
            setGridFragment();
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

}
