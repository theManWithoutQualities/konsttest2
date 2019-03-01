package com.example.konsttest2;

import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.yandex.metrica.YandexMetrica;

import static com.example.konsttest2.settings.SettingsUtils.KEY_THEME;
import static com.example.konsttest2.settings.SettingsUtils.THEME_LIGHT;

public class BasicActivity extends AppCompatActivity {

    @Override
    public Resources.Theme getTheme() {

        final String themeName = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(KEY_THEME, THEME_LIGHT);
        final Resources.Theme theme = super.getTheme();
        theme.applyStyle(THEME_LIGHT.equals(themeName) ? R.style.light : R.style.dark, true);
        return theme;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            YandexMetrica.resumeSession(this);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            YandexMetrica.pauseSession(this);
        }
    }
}
