package com.example.moviecatalogsubmission3;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private int tabPosition = 0;
    public static final int NOTIFICATION_ID = 1;
    public static String CHANNEL_ID = "channel_01";
    public static CharSequence CHANNEL_NAME = "dicoding channel";
    private AsyncHttpClient client;
    public AlarmReceiverDaily alarmReceiverDaily;
    public AlarmReceiverRelease alarmReceiverRelease;
    public MainViewModelForNotification mainViewModelForNotification;
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;

            switch (item.getItemId()) {
                case R.id.navigation_movie:
                    tabLayout.setVisibility(View.GONE);
                    fragment = new MovieFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;
                case R.id.navigation_tv:
                    tabLayout.setVisibility(View.GONE);
                    fragment = new TvFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;
                case R.id.navigation_favorite:
                    tabLayout.setVisibility(View.VISIBLE);
                    fragment = new FavoriteMovieFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, fragment, fragment.getClass().getSimpleName())
                            .commit();
                    return true;
            }
            return false;
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
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            Fragment defaultFragment = new MovieFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, defaultFragment, defaultFragment.getClass().getSimpleName())
                    .commit();
            navigation.setSelectedItemId(R.id.navigation_movie);
        }

        tabLayout.addOnTabSelectedListener(mOnTabSelectedListener);

        if (savedInstanceState != null) {
            System.out.println(savedInstanceState.getInt("tabPosition"));
            tabLayout.setScrollPosition(savedInstanceState.getInt("tabPosition"), 0, true);
            tabPosition = savedInstanceState.getInt("tabPosition");
        }


        alarmReceiverDaily = new AlarmReceiverDaily();
        alarmReceiverRelease = new AlarmReceiverRelease();

        System.out.println("Ini" + sharedPreferences.getStringSet("newFilm", new HashSet<String>()));

        boolean release_status = sharedPreferences.getBoolean("release_status", false);
        boolean daily_status = sharedPreferences.getBoolean("daily_status", false);

        list = new ArrayList<>();

        if (release_status){
            System.out.println("Release Notification Active");
            alarmReceiverRelease.setRepeatingAlarm(this, AlarmReceiverDaily.TYPE_REPEATING,
                    "8:00");

//            AsyncHttpClient client = new AsyncHttpClient();
//
//            Date c = Calendar.getInstance().getTime();
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//
//            String today = df.format(c);
//            String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&primary_release_date.gte=" + today + "&primary_release_date.lte=" + today;
//
//            client.get(url, new AsyncHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                    try {
//                        String result = new String(responseBody);
//                        JSONObject responseObject = new JSONObject(result);
//                        JSONArray list = responseObject.getJSONArray("results");
//                        for (int i = 0; i < list.length(); i++) {
//                            JSONObject film = list.getJSONObject(i);
//                            String filmName = film.getString("title");
//                            System.out.println(filmName);
////                            alarmReceiverRelease.setRepeatingAlarm(getApplicationContext(), AlarmReceiverRelease.TYPE_REPEATING, "*8:00", filmName);
////***************************************************************//
////                            String CHANNEL_ID = "Channel_1";
////                            String CHANNEL_NAME = "AlarmManager channel";
////                            NotificationManager notificationManagerCompat = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
////                            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
////                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
////                                    .setSmallIcon(R.drawable.ic_notifications_white_24dp)
////                                    .setContentTitle(filmName)
////                                    .setContentText(filmName + "has been released")
////                                    .setColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent))
////                                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
////                                    .setSound(alarmSound);
////                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////                                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
////                                        CHANNEL_NAME,
////                                        NotificationManager.IMPORTANCE_DEFAULT);
////                                channel.enableVibration(true);
////                                channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
////                                builder.setChannelId(CHANNEL_ID);
////                                if (notificationManagerCompat != null) {
////                                    notificationManagerCompat.createNotificationChannel(channel);
////                                }
////                            }
////                            Notification notification = builder.build();
////                            if (notificationManagerCompat != null) {
////                                Random rand = new Random();
////                                notificationManagerCompat.notify(201 + rand.nextInt(1000), notification);
////                            }
//////
////
//////***************************************************************//
//                        }
//
//
//                    } catch (Exception e) {
//                        Log.d("Exception", e.getMessage());
//                    }
//
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//                }
//
//            });
//
//

        }

        if (daily_status) {
            System.out.println("Daily Notification Active");
            alarmReceiverDaily.setRepeatingAlarm(this, AlarmReceiverDaily.TYPE_REPEATING,
                    "7:00");
        }


    }







//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        MenuInflater inflater = getMenuInflater();
////        inflater.inflate(R.menu.language_setting, menu);
//
////        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
////        if (searchManager != null) {
////            SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
////            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
////            searchView.setQueryHint(getResources().getString(R.string.search_hint));
////
////
////            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
////                @Override
////                public boolean onQueryTextSubmit(String query) {
////                    Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
////                    return true;
////                }
////                @Override
////                public boolean onQueryTextChange(String newText) {
////
//////                    Fragment fragment = new SearchFragment();
//////                    getSupportFragmentManager().beginTransaction()
//////                            .replace(R.id.frameLayout, fragment, fragment.getClass().getSimpleName())
//////                            .commit();
//////
//////
////
////
////                    return false;
////                }
////            });
////        }
////
////        return true;
//        return true;
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.language:
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);
                return true;
            case R.id.setting:
                Intent nIntent = new Intent(this, ReminderSettingActivity.class);
                startActivity(nIntent);
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
