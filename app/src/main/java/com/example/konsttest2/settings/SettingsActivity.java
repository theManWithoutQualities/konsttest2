package com.example.konsttest2.settings;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.konsttest2.BasicActivity;
import com.example.konsttest2.R;

public class SettingsActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, new SettingsFragment())
                    .commit();
        }
    }
}
