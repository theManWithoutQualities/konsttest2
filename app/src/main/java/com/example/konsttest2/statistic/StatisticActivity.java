package com.example.konsttest2.statistic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.konsttest2.R;

public class StatisticActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        ((TextView)findViewById(R.id.count))
                .setText(Integer.toString(getIntent().getIntExtra("count", 0)));
    }
}
