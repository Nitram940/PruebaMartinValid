package com.nitram940.valid.martinprueba.model.model_json;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class topArtist {
    @SerializedName("artist")
    private List<Artist> artists;
    @SerializedName("@attr")
    private AttributosModel attributosModel;

    public topArtist() {

    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public AttributosModel getAttributosModel() {
        return attributosModel;
    }

    public void setAttributosModel(AttributosModel attributosModel) {
        this.attributosModel = attributosModel;
    }
}
