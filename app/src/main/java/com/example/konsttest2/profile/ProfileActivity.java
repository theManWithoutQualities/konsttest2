package com.example.konsttest2.profile;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.example.konsttest2.BasicActivity;
import com.example.konsttest2.R;
import com.example.konsttest2.main.MainActivity;
import com.example.konsttest2.metrica.MetricaUtils;
import com.yandex.metrica.YandexMetrica;

import static com.example.konsttest2.profile.ProfileUtils.FB;
import static com.example.konsttest2.profile.ProfileUtils.GIT;
import static com.example.konsttest2.profile.ProfileUtils.MAIL;
import static com.example.konsttest2.profile.ProfileUtils.MAP;
import static com.example.konsttest2.profile.ProfileUtils.PHONE;
import static com.example.konsttest2.profile.ProfileUtils.VK;

public class ProfileActivity extends BasicActivity {

    private ProfileBroadcastReceiver profileBroadcastReceiver;

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
                    Uri.parse(VK)
            );
            startActivity(intent);
        });

        findViewById(R.id.fb).setOnClickListener(v -> {
            YandexMetrica.reportEvent(MetricaUtils.CLICK_FB);
            final Intent intent =
                    new Intent(
                            "android.intent.action.VIEW",
                            Uri.parse(FB)
                    );
            startActivity(intent);
        });

        findViewById(R.id.git).setOnClickListener(v -> {
            YandexMetrica.reportEvent(MetricaUtils.CLICK_GIT);
            final Intent intent =
                    new Intent(
                            "android.intent.action.VIEW",
                            Uri.parse(GIT)
                    );
            startActivity(intent);
        });

        findViewById(R.id.map).setOnClickListener(v -> {
            YandexMetrica.reportEvent(MetricaUtils.CLICK_MAP);
            final Intent intent =
                    new Intent(
                            "android.intent.action.VIEW",
                            Uri.parse(MAP)
                    );
            startActivity(intent);
        });

        findViewById(R.id.mail).setOnClickListener(v -> {
            YandexMetrica.reportEvent(MetricaUtils.CLICK_MAIL);
            Intent emailIntent = new Intent(
                    Intent.ACTION_SENDTO,
                    Uri.fromParts("mailto", MAIL, null));
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        });

        findViewById(R.id.phone).setOnClickListener(v -> {
            YandexMetrica.reportEvent(MetricaUtils.CLICK_PHONE);
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + PHONE));
            startActivity(intent);
        });
        profileBroadcastReceiver = new ProfileBroadcastReceiver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindPushText();
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(
                        profileBroadcastReceiver,
                        new IntentFilter("BIND_PUSH_TEXT")
                );
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(profileBroadcastReceiver);
    }

    public void bindPushText() {
        final SharedPreferences defaultSharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        final String pushText = defaultSharedPreferences.getString("pushText", "");
        ((TextView)findViewById(R.id.profile_push)).setText(pushText);
        Log.d("Konst", "set text in profile: " + pushText);
    }
}
