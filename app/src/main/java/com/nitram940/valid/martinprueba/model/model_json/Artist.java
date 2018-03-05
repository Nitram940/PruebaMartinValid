package com.nitram940.valid.martinprueba.model.model_json;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Artist {
    @SerializedName("name")
    private String name_artist;
    @SerializedName("listeners")
    private String listeners;
    @SerializedName("url")
    private String url;
    @SerializedName("mbid")
    private String mbid_artist;
    @SerializedName("streamable")
    private String streamable;
    @SerializedName("image")
    private List<ImageModel> imageModelList;



    private Boolean offline = false;

    public Artist() {

    }

    public Artist(String name_artist, String listeners, String mbid_artist, String streamable) {
        this.name_artist = name_artist;
        this.listeners = listeners;
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

    public String getListeners() {
        return listeners;
    }

    public void setListeners(String listeners) {
        this.listeners = listeners;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
