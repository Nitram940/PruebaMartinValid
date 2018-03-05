package com.nitram940.valid.martinprueba.model.model_json;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Artist {
    @SerializedName("name")
    private String name_artist;
    @SerializedName("playcount")
    private String playcount;
    @SerializedName("mbid")
    private String mbid_artist;
    @SerializedName("streamable")
    private String streamable;
    @SerializedName("image")
    private List<ImageModel> imageModelList;



    private Boolean offline = false;

    public Artist() {

    }

    public Artist(String name_artist, String playcount, String mbid_artist, String streamable) {
        this.name_artist = name_artist;
        this.playcount = playcount;
        this.mbid_artist = mbid_artist;
        this.streamable = streamable;
    }


    public Boolean isOffline() {
        return offline;
    }

    public void setOffline(Boolean Offline) {
        this.offline = Offline;
    }

    public String getName_artist() {
        return name_artist;
    }

    public void setName_artist(String name_artist) {
        this.name_artist = name_artist;
    }

    public String getPlaycount() {
        return playcount;
    }

    public void setPlaycount(String playcount) {
        this.playcount = playcount;
    }

    public String getMbid_artist() {
        return mbid_artist;
    }

    public void setMbid_artist(String mbid_artist) {
        this.mbid_artist = mbid_artist;
    }

    public String getStreamable() {
        return streamable;
    }

    public void setStreamable(String streamable) {
        this.streamable = streamable;
    }

    public List<ImageModel> getImageModelList() {
        return imageModelList;
    }

    public void setImageModelList(List<ImageModel> imageModelList) {
        this.imageModelList = imageModelList;
    }
}
