package com.nitram940.valid.martinprueba.model.model_json;

import com.google.gson.annotations.SerializedName;


public class ArtistResponse {
    @SerializedName("topartists")
    private topArtist topartists;


    //error
    @SerializedName("status_message")
    private int status_message;
    @SerializedName("success")
    private int success;
    @SerializedName("status_code")
    private int status_code;


    public ArtistResponse() {
    }

    public topArtist getTopartists() {
        return topartists;
    }

    public void setTopartists(topArtist topartists) {
        this.topartists = topartists;
    }
}
