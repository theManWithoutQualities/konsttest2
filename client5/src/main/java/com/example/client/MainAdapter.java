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
import android.widget.Toast;

import static com.example.client.ViewState.ALL;

public class MainAdapter extends CursorAdapter {

    private ViewState viewState;
    LayoutInflater mInflater;
    private static final Uri editAppUri = Uri.parse("content://com.example.konsttest2.info/update");

    public MainAdapter(Context context, Cursor c) {
        super(context, c, 0);
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public MainAdapter setViewState(ViewState viewState) {
        this.viewState = viewState;
        return this;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.d("Konst", "newView");
        return mInflater.inflate(R.layout.one_app, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, Cursor cursor) {
        Log.d("Konst", "bindView");
        ((TextView)view.findViewById(R.id.name))
                .setText("Name: " + cursor.getString(cursor.getColumnIndex("name")));
        ((TextView)view.findViewById(R.id.package_name))
                .setText(
                        "Package Name: " +
                                cursor.getString(cursor.getColumnIndex("package_name"))
                );
        final String id = cursor.getString(cursor.getColumnIndex("_id"));
        final String count = cursor.getString(cursor.getColumnIndex("count"));
        ((TextView)view.findViewById(R.id.count))
                .setText("Count: " + count);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.edit_dialog);
                dialog.setCancelable(true);
                dialog.show();
                ((EditText)dialog.findViewById(R.id.edit_clicks)).setText(count);
                final Button button = dialog.findViewById(R.id.button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ContentValues contentValues = new ContentValues();
                        String newClicks = ((EditText) dialog.findViewById(R.id.edit_clicks))
                                .getText().toString();
                        contentValues.put("count", newClicks);
                        try {
                            context.getContentResolver().update(
                                    editAppUri,
                                    contentValues,
                                    "_id =?",
                                    new String[]{id}
                            );
                            if (context instanceof MainActivity) {
                                if (viewState.equals(ALL)) {
                                    ((MainActivity) context).showAllAps(view);
                                } else {
                                    ((MainActivity) context).showLastClicked(view);
                                }
                            }
                        } catch (SecurityException e) {
                            Log.d("Konst", "no access");
                            Toast.makeText(context, "No access!", Toast.LENGTH_LONG).show();
                        }
                        dialog.dismiss();
                    }
                });
                return true;
            }
        });
    }
}
