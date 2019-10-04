package com.example.favoriteapp;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.HashSet;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private int tabPosition = 0;
    public static final int NOTIFICATION_ID = 1;
    public static String CHANNEL_ID = "channel_01";
    public static CharSequence CHANNEL_NAME = "dicoding channel";
    private AsyncHttpClient client;
    public ArrayList<String> list;


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    NotificationCompat.Builder mBuilder;
    NotificationManager mNotificationManager;

//    private static final String API_KEY = "652ae94b7a67745bd01006a794b01893";

    private TabLayout.OnTabSelectedListener mOnTabSelectedListener = new TabLayout.OnTabSelectedListener() {

        Fragment fragment;

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            tabPosition = tab.getPosition();
            switch (tab.getPosition()){
                case 0:
                    fragment = new FavoriteMovieFragment();
                    break;
                case 1:
                    fragment = new FavoriteTVFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, fragment, fragment.getClass().getSimpleName())
                    .commit();

        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        tabLayout = findViewById(R.id.tab);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigation.setVisibility(View.GONE);

        tabLayout.addOnTabSelectedListener(mOnTabSelectedListener);

        if (savedInstanceState != null) {
            System.out.println(savedInstanceState.getInt("tabPosition"));
            tabLayout.setScrollPosition(savedInstanceState.getInt("tabPosition"), 0, true);
            tabPosition = savedInstanceState.getInt("tabPosition");
        }

        if (savedInstanceState == null) {
            Fragment defaultFragment = new FavoriteMovieFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, defaultFragment, defaultFragment.getClass().getSimpleName())
                    .commit();
        }


        System.out.println("Ini" + sharedPreferences.getStringSet("newFilm", new HashSet<String>()));

        boolean release_status = sharedPreferences.getBoolean("release_status", false);
        boolean daily_status = sharedPreferences.getBoolean("daily_status", false);

        list = new ArrayList<>();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.language:
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);
                return true;
            default:
                return true;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tabPosition", tabPosition);
    }

}
