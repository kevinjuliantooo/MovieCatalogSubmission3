package com.example.moviecatalogsubmission3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.provider.BaseColumns._ID;
import static com.example.moviecatalogsubmission3.DatabaseFavoriteContract.NoteColumns.FAVORITE_ID;
//import static com.example.moviecatalogsubmission3.FeedReaderContract.FeedEntry.COLUMN_FILM_ID;
//import static com.example.moviecatalogsubmission3.FeedReaderContract.FeedEntry.TABLE_NAME;

public class StackRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private final List<Bitmap> mWidgetItems = new ArrayList<>();
    private final Context mContext;
//    private FeedReaderDbHelper dbHelper;
    private SQLiteDatabase db;
    private SharedPreferences sharedPreferences;

    public StackRemoteViewFactory(Context context) {
        mContext = context;
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

//        dbHelper = new FeedReaderDbHelper(mContext);
//        db = dbHelper.getWritableDatabase();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        Set<String> sets = sharedPreferences.getStringSet("favoriteList", new HashSet<String>());
        System.out.println("Isi " + sets);

//        Cursor cursor = mContext.getContentResolver().query(DatabaseFavoriteContract.NoteColumns.CONTENT_URI,
//                null, null, null, null);

//        while(cursor.moveToNext()) {
//            String filmId = String.valueOf(cursor.getLong(
//                    cursor.getColumnIndexOrThrow(FAVORITE_ID)));
//            filmIds.add(filmId);
//        }

        for (String s: sets){
            String src = "https://image.tmdb.org/t/p/w1280/" + s;
            try {
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                mWidgetItems.add(myBitmap);
            } catch (IOException e) {
                // Log exception
            }
        }

//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.venue_18));
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.venue_18));
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.venue_18));
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.venue_18));
//        mWidgetItems.add(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.venue_18));

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems.get(position));
        Bundle extras = new Bundle();
        extras.putInt(ImageBannerWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
