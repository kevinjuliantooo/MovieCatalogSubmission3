package com.example.moviecatalogsubmission3;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TvFragment extends Fragment {

    private FilmAdapter filmAdapter;
    public static final String EXTRA_DETAIL = "extra_detail";
    public static String GET_THIS = null;
    private RecyclerView recyclerView;
    private MainViewModel mainViewModel;
    private ProgressBar progressBar;
    private String current_language;
    private MainViewModelForSearch mainViewModelForSearch;


    public TvFragment() {
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

        if (current_language.equals("in_ID")){
            current_language = "id";
        }

        System.out.println(current_language);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        if (getActivity() != null) {
            mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        }

        setFilmItems();
        mainViewModel.getFilms("tv").observe(this, getFilm);



        filmAdapter =  new FilmAdapter();
        filmAdapter.notifyDataSetChanged();

        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(filmAdapter);

    }

    private void setFilmItems() {
        mainViewModel.setFilm(current_language, "tv");
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
        super.onCreateOptionsMenu(menu, inflater);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint));


            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
//                    Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                    return true;
                }
                @Override
                public boolean onQueryTextChange(String newText) {

                        if (getActivity() != null) {
                            mainViewModelForSearch = ViewModelProviders.of(getActivity()).get(MainViewModelForSearch.class);
                            if (newText.isEmpty()) {
                                setFilmItems();
                                mainViewModelForSearch.getFilms("tv").observe(getActivity(), getFilm);
                            } else {
                                mainViewModelForSearch.setFilm(current_language, "tv", newText);
                                mainViewModelForSearch.getFilms("tv").observe(getActivity(), getFilm);


                            }


                    }



                    return false;
                }
            });

            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    setFilmItems();
                    mainViewModelForSearch.getFilms("tv").observe(getActivity(), getFilm);

                    return false;
                }
            });
        }

    }



}
