package com.example.konsttest2;

import android.app.Application;
import android.os.Build;

import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;
import com.yandex.metrica.push.YandexMetricaPush;

public class KonstTest2 extends Application {

    public static final String TAG = "Konst";

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
        YandexMetricaPush.init(getApplicationContext());
    }
}
