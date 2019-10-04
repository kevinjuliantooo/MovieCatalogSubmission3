package com.example.favoriteapp;

import android.net.Uri;
import android.provider.BaseColumns;


public class DatabaseFavoriteContract {

    public static final String AUTHORITY = "com.example.moviecatalogsubmission3";
    private static final String SCHEME = "content";

    private DatabaseFavoriteContract(){}

    static String TABLE_NOTE = "favoritedb";

    static final class NoteColumns implements BaseColumns {
        static String FAVORITE_ID = "favorite_id";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NOTE)
                .build();
    }
}
