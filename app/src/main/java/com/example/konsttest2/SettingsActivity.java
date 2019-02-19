package com.example.konsttest2;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class SettingsActivity extends BasicActivity {

    public static final String KEY_SORT = "sort";
    public static final String SORT_ALPHABETIC = "alphabet";
    public static final String SORT_ALPHABETIC_REVERSE = "alphabetRev";
    public static final String SORT_FREQUENCY = "frequency";
    public static final String SORT_DATE = "date";
    public static final String SORT_NO = "noSort";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, new SettingsFragment())
                .commit();
    }
}
