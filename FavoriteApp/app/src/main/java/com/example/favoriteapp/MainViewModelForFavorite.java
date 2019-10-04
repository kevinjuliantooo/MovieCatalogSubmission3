package com.example.favoriteapp;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

public class MainViewModelForFavorite extends ViewModel {
    private static final String API_KEY = "652ae94b7a67745bd01006a794b01893";


    void setFilm(String language, final String get_film, final Set<String> set){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<FilmItems> listItems = new ArrayList<>();

        String url = "https://api.themoviedb.org/3/" + get_film + "/popular?api_key=" + API_KEY + "&language=" + language + "&page=1";

        //Sebelum mengambil data, Anda bisa initiate loading = true disini
//        loading.setValue(true);

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
//                    lalu loading = flase disini karena data sudah dimuat
                    System.out.println(set);
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject film = list.getJSONObject(i);
                        FilmItems filmItems = new FilmItems(film, get_film);
                        System.out.println("Your ID" + filmItems.getId());
                        if (set.contains(String.valueOf(filmItems.getId()))) {
                            listItems.add(filmItems);
                        }


                    }

                    if (get_film.equals("movie")){
                        listMovie.postValue(listItems);
                    } else {
                        listTv.postValue(listItems);
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    LiveData<ArrayList<FilmItems>> getFilms(String get_current_film){
        System.out.println(get_current_film);
        if (get_current_film.equals("movie")) {
            return listMovie;
        } else {
            return listTv;
        }

    }

    private MutableLiveData<ArrayList<FilmItems>> listMovie = new MutableLiveData<>();
    private MutableLiveData<ArrayList<FilmItems>> listTv = new MutableLiveData<>();

}
