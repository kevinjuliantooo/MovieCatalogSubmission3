package com.example.moviecatalogsubmission3;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;


public class DatabaseContract {
    public static final String AUTHORITY = "com.example.moviecatalogsubmission3";
    private static final String SCHEME = "content";

    static String TABLE_NOTE = "favorite";
    static final class NoteColumns implements BaseColumns {
        static String FILMID = "filmid";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NOTE)
                .build();

        public static Uri buildTodoUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }

//        public static Uri buildFavoriteUriWithId(long id) {
//            return CONTENT_URI.buildUpon()
//                    .appendPath(Long.toString(id))
//                    .build();
//        }

    }



    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }
    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }
    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

}
