package com.example.moviecatalogsubmission3;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.UserDictionary;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.provider.BaseColumns._ID;
import static com.example.moviecatalogsubmission3.DatabaseContract.NoteColumns.FILMID;
import static com.example.moviecatalogsubmission3.DatabaseContract.TABLE_NOTE;
import static com.example.moviecatalogsubmission3.DatabaseFavoriteContract.NoteColumns.FAVORITE_ID;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_DETAIL = "extra_detail";
    private TextView tvTitle, tvDescription, tvDate;
    private ImageView tvPoster;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String KEY_FAV = "favorite";
    private String KEY_IMG = "image";
    private String film_img;
    private int film_id;
    private boolean favorite;
    private SQLiteDatabase db;
    private FavoriteHelper favoriteHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvDate = findViewById(R.id.tvDate);
        tvPoster = findViewById(R.id.tvPoster);


        favoriteHelper = FavoriteHelper.getInstance(getApplicationContext());
        favoriteHelper.open();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        editor = sharedPreferences.edit();

        FilmItems film = getIntent().getParcelableExtra(EXTRA_DETAIL);
        tvTitle.setText(film.getTitle());
        tvDate.setText(film.getDate());
        tvDescription.setText(film.getDescription());
        film_img = film.getPoster();
        Picasso.get().load("https://image.tmdb.org/t/p/w1280" + film_img).into(tvPoster);

        film_id = film.getId();
//        tvPoster.setImageResource(film.getPoster());


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favorite_film, menu);

//        Cursor cursor = favoriteHelper.queryAll();

        Cursor cursor = getContentResolver().query(DatabaseFavoriteContract.NoteColumns.CONTENT_URI,
                null, null, null, null);


        System.out.println(cursor);

        Set<String> filmIds = new HashSet<>();
        while(cursor.moveToNext()) {
            String filmId = String.valueOf(cursor.getLong(
                    cursor.getColumnIndexOrThrow(FAVORITE_ID)));
            filmIds.add(filmId);
        }
        cursor.close();




        System.out.println("Hasil: " + filmIds);
        System.out.println(filmIds.contains(String.valueOf(film_id)));

        if (filmIds.contains(String.valueOf(film_id))){
            favorite = true;
            menu.getItem(0).setIcon(R.drawable.ic_favorite_yes_24dp);
        } else {
            menu.getItem(0).setIcon(R.drawable.ic_favorite_no_24dp);
            favorite = false;
        }

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        ContentValues values = new ContentValues();

        if (!favorite){
//            set.add(String.valueOf(film_id));
//            set_img.add(film_img);
//            db.insert(TABLE_NAME, null, values);
//            getContentResolver().insert(DatabaseContract.NoteColumns.CONTENT_URI, values);

            values.put(FAVORITE_ID, String.valueOf(film_id));
            favoriteHelper.insert(values);

//            Cursor cursor = favoriteHelper.queryAll();
            Cursor cursor = getContentResolver().query(DatabaseFavoriteContract.NoteColumns.CONTENT_URI,
                    null, null, null, null);

            Set<String> filmIds = new HashSet<>();
            while(cursor.moveToNext()) {
                String filmId = String.valueOf(cursor.getLong(
                        cursor.getColumnIndexOrThrow(FAVORITE_ID)));
                filmIds.add(filmId);
            }

            cursor.close();


            System.out.println(filmIds);
            editor.putStringSet("favoriteList", filmIds);
            editor.apply();

            favorite = true;
            item.setIcon(R.drawable.ic_favorite_yes_24dp);



//            editor.putStringSet(KEY_FAV, set);
//            editor.putStringSet(KEY_IMG,set_img);
//            editor.apply();
//            System.out.println(set);
//            System.out.println(set_img);
//            Toast.makeText(getApplicationContext(), "Film " + tvTitle.getText() + "telah Ditambahkan di Favorite List", Toast.LENGTH_SHORT).show();
//
        } else {
//            Set<String> set = sharedPreferences.getStringSet(KEY, new HashSet<String>());
//            set.remove(String.valueOf(film_id));

            // Define 'where' part of query.
            String selection = FAVORITE_ID + " LIKE ?";
            // Specify arguments in placeholder order.
            String[] selectionArgs = { String.valueOf(film_id) };
            // Issue SQL statement.
            favoriteHelper.deleteByItem(selection, selectionArgs);
//            getContentResolver().delete(DatabaseContract.NoteColumns.CONTENT_URI, selection, selectionArgs);

//            Cursor cursor = favoriteHelper.queryAll();
            Cursor cursor = getContentResolver().query(DatabaseFavoriteContract.NoteColumns.CONTENT_URI,
                    null, null, null, null);

            Set<String> filmIds = new HashSet<>();
            while(cursor.moveToNext()) {
                String filmId = String.valueOf(cursor.getLong(
                        cursor.getColumnIndexOrThrow(FAVORITE_ID)));
                filmIds.add(filmId);
            }

            cursor.close();

            System.out.println(filmIds);
            editor.putStringSet("favoriteList", filmIds);
            editor.apply();
//            cursor.close();

//            editor.putStringSet(KEY_FAV, set);
//            editor.putStringSet(KEY_IMG,set_img);
//            editor.apply();
//            System.out.println(set);
//            System.out.println(set_img);
            favorite = false;
            item.setIcon(R.drawable.ic_favorite_no_24dp);
//            Toast.makeText(getApplicationContext(), "Film " + tvTitle.getText() + "dihapus dari Favorite List", Toast.LENGTH_SHORT).show();
//
        }


//        if (String.valueOf(item.getIcon()).equals(String.valueOf(R.drawable.ic_favorite_no_24dp))) {
//            Set<String> set = new HashSet<String>();
//            item.setIcon(R.drawable.ic_favorite_yes_24dp);
//            set.add(String.valueOf(film_id));
//            editor.putStringSet(KEY, set);
//            System.out.println(set);
//            item.setIcon(R.drawable.ic_favorite_yes_24dp);
//            Toast.makeText(getApplicationContext(), "Film " + tvTitle.getText() + "telah Ditambahkan di Favorite List", Toast.LENGTH_SHORT).show();
//        } else{
//            Set<String> set = sharedPreferences.getStringSet(KEY, null);
//            set.remove(String.valueOf(film_id));
//            System.out.println(set);
//            item.setIcon(R.drawable.ic_favorite_no_24dp);
//        }


        return true;

    }

    protected void onDestroy() {
        super.onDestroy();
        favoriteHelper.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        favoriteHelper.close();
    }
}
