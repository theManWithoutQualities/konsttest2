package com.example.client;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    ProfileAdapter profileAdapter;
    private ListView listView;
    private static final Uri editAppUri = Uri.parse("content://com.example.konsttest2.profile/get");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileAdapter = new ProfileAdapter(this, null);
        listView = findViewById(R.id.content);
        listView.setAdapter(profileAdapter);
    }

    public void showProfile(View view) {
        try {
            final Cursor cursor = getContentResolver()
                    .query(editAppUri, null, null, null, null);
            profileAdapter.changeCursor(cursor);
        } catch (SecurityException e) {
            Log.d("Konst", "no access");
            Toast.makeText(this, "No access!", Toast.LENGTH_LONG).show();
        }

    }
}
