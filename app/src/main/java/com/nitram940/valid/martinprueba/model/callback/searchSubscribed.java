package com.nitram940.valid.martinprueba.model.callback;

import java.util.List;

/**
 * Created by nitra on 28/12/2017.
 */

public class searchSubscribed {

    private String query;
    private int estate;
    private List<String> titlesMovies;


    public searchSubscribed(String query, int estate) {
        this.query = query;
        this.estate = estate;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getEstate() {
        return estate;
    }

    public void setEstate(int estate) {
        this.estate = estate;
    }

    public List<String> getTitlesMovies() {
        return titlesMovies;
    }

    public void setTitlesMovies(List<String> titlesMovies) {
        this.titlesMovies = titlesMovies;
    }
}
