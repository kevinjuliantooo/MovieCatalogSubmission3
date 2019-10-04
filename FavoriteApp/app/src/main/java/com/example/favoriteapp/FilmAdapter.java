package com.example.favoriteapp;

import android.content.Intent;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.ListViewHolder> {

    public static final String EXTRA_DETAIL = "extra_detail";
    private ArrayList<FilmItems> mData = new ArrayList<>();

    public void setData(ArrayList<FilmItems> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();


    }

    public FilmAdapter(){

    }

    public void addItem(final FilmItems item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void clearData() {
        mData.clear();
    }


    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ListViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, final int position) {
        holder.bind(mData.get(position));

        final FilmItems film = mData.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilmItems intentFilm = new FilmItems(Parcel.obtain());
                intentFilm.setTitle(film.getTitle());
                intentFilm.setDescription(film.getDescription());
                intentFilm.setPoster(film.getPoster());
                intentFilm.setDate(film.getDate());
                intentFilm.setId(film.getId());

                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_DETAIL, film);
                v.getContext().startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        ImageView poster;


        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            description = itemView.findViewById(R.id.tvDescription);
            poster = itemView.findViewById(R.id.tvPoster);

        }

        public void bind(FilmItems filmItems) {
            title.setText(filmItems.getTitle());
            description.setText(filmItems.getDescription());
            Picasso.get().load("https://image.tmdb.org/t/p/w1280" + filmItems.getPoster()).into(poster);
        }
    }
}
