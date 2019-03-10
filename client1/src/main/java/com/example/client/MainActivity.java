package com.example.client;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private MainAdapter mainAdapter;
    private ListView listView;
    private static final Uri allAppsUri = Uri.parse("content://com.example.konsttest2.info/appsinfo");
    private static final Uri lastAppUri = Uri.parse("content://com.example.konsttest2.info/lastappinfo");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainAdapter = new MainAdapter(this, null);
        listView = findViewById(R.id.content);
        listView.setAdapter(mainAdapter);
    }

    public void showLastClicked(View view) {
        try {
            final Cursor cursor = getContentResolver()
                    .query(lastAppUri, null, null, null, null);
            mainAdapter.setViewState(ViewState.LAST);
            mainAdapter.changeCursor(cursor);
        } catch (SecurityException e) {
            Log.d("Konst", "no access");
            Toast.makeText(this, "No access!", Toast.LENGTH_LONG).show();
        }
    }

    public void showAllAps(View view) {
        try {
            final Cursor cursor = getContentResolver()
                    .query(allAppsUri, null, null, null, null);
            mainAdapter.setViewState(ViewState.ALL);
            mainAdapter.changeCursor(cursor);
        } catch (SecurityException e) {
            Log.d("Konst", "no access");
            Toast.makeText(this, "No access!", Toast.LENGTH_LONG).show();
        }

    }

    public void goToProfile(View view) {
        final Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}
