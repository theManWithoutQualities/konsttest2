package com.example.client;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class ProfileAdapter extends CursorAdapter {

    LayoutInflater mInflater;

    public ProfileAdapter(Context context, Cursor c) {
        super(context, c, 0);
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.d("Konst", "newView");
        return mInflater.inflate(R.layout.profile, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        Log.d("Konst", "bindView");
        ((TextView)view.findViewById(R.id.name))
                .setText("Name: " + cursor.getString(cursor.getColumnIndex("name")));
        ((TextView)view.findViewById(R.id.phone))
                .setText(cursor.getString(cursor.getColumnIndex("phone")));
        ((TextView)view.findViewById(R.id.mail))
                .setText(cursor.getString(cursor.getColumnIndex("mail")));
        ((TextView)view.findViewById(R.id.vk))
                .setText(cursor.getString(cursor.getColumnIndex("vk")));
        ((TextView)view.findViewById(R.id.fb))
                .setText(cursor.getString(cursor.getColumnIndex("fb")));
        ((TextView)view.findViewById(R.id.git))
                .setText(cursor.getString(cursor.getColumnIndex("git")));
        ((TextView)view.findViewById(R.id.map))
                .setText(cursor.getString(cursor.getColumnIndex("location")));
    }
}
