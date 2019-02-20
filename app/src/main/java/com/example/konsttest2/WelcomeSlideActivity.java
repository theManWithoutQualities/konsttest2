package com.example.konsttest2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import static com.example.konsttest2.ChooseDensityFragment.DENSITY_HIGH;
import static com.example.konsttest2.ChooseDensityFragment.DENSITY_STANDARD;
import static com.example.konsttest2.ChooseDensityFragment.KEY_DENSITY;
import static com.example.konsttest2.ChooseThemeFragment.KEY_THEME;
import static com.example.konsttest2.ChooseThemeFragment.THEME_DARK;
import static com.example.konsttest2.ChooseThemeFragment.THEME_LIGHT;

public class WelcomeSlideActivity extends BasicActivity {

    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_slide);

        mPager = findViewById(R.id.pager);
        final List<Fragment> fragments =
                Arrays.asList(
                        new WelcomeFragment(),
                        new DescriptionFragment(),
                        new DescriptionSecondFragment(),
                        new ChooseDensityFragment(),
                        new ChooseThemeFragment(),
                        new Fragment()
                );
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), fragments);
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(i == mPager.getAdapter().getCount() - 1){
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                            .edit()
                            .putBoolean("showWelcomePage", false)
                            .apply();
                    final Intent intent = new Intent();
                    intent.setClass(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
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

    public void clickRadioButtonTheme(View view) {
        String theme;
        switch (view.getId()) {
            case R.id.light_theme:
                theme = THEME_LIGHT;
                break;
            case R.id.dark_theme:
                theme = THEME_DARK;
                break;
            default:
                theme = THEME_LIGHT;
                break;
        }
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putString(KEY_THEME, theme)
                .apply();
        recreate();
    }

    public void clickRadioButtonDensity(View view) {
        String density;
        switch (view.getId()) {
            case R.id.standard_density:
                density = DENSITY_STANDARD;
                break;
            case R.id.high_density:
                density = DENSITY_HIGH;
                break;
            default:
                density = DENSITY_STANDARD;
                break;
        }
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putString(KEY_DENSITY, density)
                .apply();
    }
}
