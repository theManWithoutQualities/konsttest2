package com.example.konsttest2.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.konsttest2.BasicActivity;
import com.example.konsttest2.MainActivity;
import com.example.konsttest2.R;
import com.example.konsttest2.metrica.MetricaUtils;
import com.yandex.metrica.YandexMetrica;

public class ProfileActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.vk).setOnClickListener(v -> {
            YandexMetrica.reportEvent(MetricaUtils.CLICK_VK);
            final Intent intent = new Intent(
                    "android.intent.action.VIEW",
                    Uri.parse("http://vk.com")
            );
            startActivity(intent);
        });

        findViewById(R.id.fb).setOnClickListener(v -> {
            YandexMetrica.reportEvent(MetricaUtils.CLICK_FB);
            final Intent intent =
                    new Intent(
                            "android.intent.action.VIEW",
                            Uri.parse("http://www.facebook.com")
                    );
            startActivity(intent);
        });

        findViewById(R.id.git).setOnClickListener(v -> {
            YandexMetrica.reportEvent(MetricaUtils.CLICK_GIT);
            final Intent intent =
                    new Intent(
                            "android.intent.action.VIEW",
                            Uri.parse("https://github.com/theManWithoutQualities")
                    );
            startActivity(intent);
        });

        findViewById(R.id.map).setOnClickListener(v -> {
            YandexMetrica.reportEvent(MetricaUtils.CLICK_MAP);
            final Intent intent =
                    new Intent(
                            "android.intent.action.VIEW",
                            Uri.parse("https://www.google.com/maps/@53.8730496,27.5177472,12z")
                    );
            startActivity(intent);
        });

        findViewById(R.id.mail).setOnClickListener(v -> {
            YandexMetrica.reportEvent(MetricaUtils.CLICK_MAIL);
            Intent emailIntent = new Intent(
                    Intent.ACTION_SENDTO,
                    Uri.fromParts("mailto","konst007@tut.by", null));
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        });

        findViewById(R.id.phone).setOnClickListener(v -> {
            YandexMetrica.reportEvent(MetricaUtils.CLICK_PHONE);
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+375291939666"));
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        YandexMetrica.reportEvent(MetricaUtils.BACKPRESS_PROFILE);
        final Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
    }
}
