package com.example.moviecatalogsubmission3;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainViewModelForNotification extends ViewModel {
    private static final String API_KEY = "652ae94b7a67745bd01006a794b01893";


    void setFilm(){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<String> releaseMovieTitle = new ArrayList<>();

        String today = "2019-09-20";
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&primary_release_date.gte=" + today + "&primary_release_date.lte=" + today;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject film = list.getJSONObject(i);
                        String filmName = film.getString("title");
                        releaseMovieTitle.add(filmName);
                    }

                    listTitle.postValue(releaseMovieTitle);
                    System.out.println(releaseMovieTitle);



                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }

                onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }

        });
    }

    LiveData<ArrayList<String>> getFilms(String get_current_film){
        System.out.println(get_current_film);
        return listTitle;

    }

    private MutableLiveData<ArrayList<String>> listTitle = new MutableLiveData<>();

}
