package com.nitram940.valid.martinprueba.model.model_json;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ArtistResponse {
    @SerializedName("topartists")
    private List<Artist> topartists;
    @SerializedName("@attr")
    private AttributosModel attributosModel;



    //error
    @SerializedName("status_message")
    private int status_message;
    @SerializedName("success")
    private int success;
    @SerializedName("status_code")
    private int status_code;


    public List<Artist> getTopartists() {
        return topartists;
    }

    public void setTopartists(List<Artist> topartists) {
        this.topartists = topartists;
    }

    public AttributosModel getAttributosModel() {
        return attributosModel;
    }

    public void setAttributosModel(AttributosModel attributosModel) {
        this.attributosModel = attributosModel;
    }
}
