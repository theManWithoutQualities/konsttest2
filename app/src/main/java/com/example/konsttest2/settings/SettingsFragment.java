package com.example.konsttest2.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.konsttest2.R;

import static com.example.konsttest2.settings.SettingsUtils.KEY_NEED_RECREATE;
import static com.example.konsttest2.settings.SettingsUtils.KEY_THEME;

public class SettingsFragment
        extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_THEME)) {
            getActivity().recreate();
            sharedPreferences.edit().putBoolean(KEY_NEED_RECREATE, true).apply();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager()
                .getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager()
                .getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
}
