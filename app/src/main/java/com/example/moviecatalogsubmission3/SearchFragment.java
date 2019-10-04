package com.example.moviecatalogsubmission3;


import android.os.Bundle;
import android.view.LayoutInflater;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private FilmAdapter filmAdapter;
    public static final String EXTRA_DETAIL = "extra_detail";
    public static String GET_THIS = null;
    private RecyclerView recyclerView;
    private MainViewModel mainViewModel;
    private ProgressBar progressBar;
    private String current_language;


    public SearchFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_film, container, false);
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
        mainViewModel.getFilms("movie").observe(this, getFilm);



        filmAdapter =  new FilmAdapter();
        filmAdapter.notifyDataSetChanged();

        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(filmAdapter);

    }

    private void setFilmItems() {
        mainViewModel.setFilm(current_language, "movie");
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



}
