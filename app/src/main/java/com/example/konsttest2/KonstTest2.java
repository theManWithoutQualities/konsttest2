package com.example.konsttest2;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

import static com.example.konsttest2.settings.SettingsUtils.KEY_CHANGE_WALLPAPER_NOW;

public class KonstTest2 extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Создание расширенной конфигурации библиотеки.
        YandexMetricaConfig config = YandexMetricaConfig
                .newConfigBuilder("4e0e3d84-abb8-4461-b47d-1955998421ad")
                .withLogs()
                .build();
        // Инициализация AppMetrica SDK.
        YandexMetrica.activate(getApplicationContext(), config);
        // Отслеживание активности пользователей.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            YandexMetrica.enableActivityAutoTracking(this);
        }
    }
}
