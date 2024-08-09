package com.google.android.vending;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

// TODO: Move
public class GsfGServices {
    private static final Uri CONTENT_URI = Uri.parse("content://com.google.android.gsf.gservices");

    public static String getString(ContentResolver resolver,String key,String defaultValue){
        String result = defaultValue;
        Cursor cursor = resolver.query(CONTENT_URI, null, null, new String[]{key}, null);
        assert cursor != null;
        if (cursor.moveToNext()) {
            result = cursor.getString(1);
        }
        cursor.close();
        return result;
    }
}