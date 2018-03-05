package com.nitram940.valid.martinprueba.model.model_json;

import com.google.gson.annotations.SerializedName;


public class ImageModel {
    @SerializedName("#text")
    private String url;
    @SerializedName("size")
    private String size;

    public ImageModel() {

    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
