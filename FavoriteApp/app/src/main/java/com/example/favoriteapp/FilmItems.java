package com.example.favoriteapp;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class FilmItems implements Parcelable{

    //Untuk Movie
    private int id;
    private String title, description, date, poster;

    //Untuk TV Show
    private String name, first_air_date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public void setFirst_air_date(String first_air_date) {
        this.first_air_date = first_air_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public FilmItems(JSONObject object, String get_this) {
        try{
            int id;
            String title, description, date, poster;

            switch (get_this){
                case "movie":
                    id = object.getInt("id");
                    title = object.getString("title");
                    description = object.getString("overview");
                    date = object.getString("release_date");
                    poster = object.getString("poster_path");
                    System.out.println(poster);

                    this.id = id;
                    this.title = title;
                    this.description = description;
                    this.date = date;
                    this.poster = poster;
                    break;
                case "tv":
                    id = object.getInt("id");
                    title = object.getString("name");
                    description = object.getString("overview");
                    date = object.getString("first_air_date");
                    poster = object.getString("poster_path");

                    this.id = id;
                    this.title = title;
                    this.description = description;
                    this.date = date;
                    this.poster = poster;
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.date);
        dest.writeString(this.poster);
    }

    protected FilmItems(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.date = in.readString();
        this.poster = in.readString();
    }

    public static final Creator<FilmItems> CREATOR = new Creator<FilmItems>() {
        @Override
        public FilmItems createFromParcel(Parcel source) {
            return new FilmItems(source);
        }

        @Override
        public FilmItems[] newArray(int size) {
            return new FilmItems[size];
        }
    };
}
