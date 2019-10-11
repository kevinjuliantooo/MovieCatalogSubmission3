package com.example.moviecatalogsubmission3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.example.moviecatalogsubmission3.DatabaseFavoriteContract.NoteColumns.FAVORITE_ID;

public class FavoriteMovieFragment extends Fragment {

    private FilmAdapter filmAdapter;
    public static final String EXTRA_DETAIL = "extra_detail";
    public static String GET_THIS = null;
    private RecyclerView recyclerView;
    private MainViewModelForFavorite mainViewModel;
    private ProgressBar progressBar;
    private String current_language;
//    private Set<String> set;
    private Set<String> filmIds;
    private FavoriteHelper favoriteHelper;


    public FavoriteMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_film, container, false);
        setHasOptionsMenu(true);
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        current_language = String.valueOf(getResources().getConfiguration().locale);


        favoriteHelper = FavoriteHelper.getInstance(getContext());
        favoriteHelper.open();

        Cursor cursor = getContext().getContentResolver().query(DatabaseFavoriteContract.NoteColumns.CONTENT_URI,
                null, null, null, null);

//        Cursor cursor = getContentResolver().query(DatabaseFavoriteContract.NoteColumns.CONTENT_URI,
//                null, null, null, null);



        filmIds = new HashSet<>();
        while(cursor.moveToNext()) {
            String filmId = String.valueOf(cursor.getLong(
                    cursor.getColumnIndexOrThrow(FAVORITE_ID)));
            filmIds.add(filmId);
        }

        cursor.close();





        if (current_language.equals("in_ID")){
            current_language = "id";
        }

        System.out.println(current_language);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        if (getActivity() != null) {
            mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModelForFavorite.class);

        }

        setFilmItems();
        mainViewModel.getFilms("movie").observe(this, getFilm);



        filmAdapter =  new FilmAdapter();
        filmAdapter.notifyDataSetChanged();

        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(filmAdapter);

    }

    private void setFilmItems() {
        System.out.println(filmIds);
        mainViewModel.setFilm(current_language, "movie", filmIds);
    }

    private Observer<ArrayList<FilmItems>> getFilm = new Observer<ArrayList<FilmItems>>() {
        @Override
        public void onChanged(ArrayList<FilmItems> filmItems) {
            if (filmItems != null) {
                filmAdapter.setData(filmItems);
            }
            showLoading(false);
        }
    };

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.language:
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);
                return true;
            case R.id.setting:
                Intent nIntent = new Intent(getActivity(), ReminderSettingActivity.class);
                startActivity(nIntent);
            default:
                return true;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.language_setting, menu);
        menu.findItem(R.id.search).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }



}

