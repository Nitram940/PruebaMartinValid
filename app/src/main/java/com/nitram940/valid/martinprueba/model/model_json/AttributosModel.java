package com.nitram940.valid.martinprueba.model.model_json;

import com.google.gson.annotations.SerializedName;


public class AttributosModel {
    @SerializedName("country")
    private String country;
    @SerializedName("page")
    private String page;
    @SerializedName("perPage")
    private String perPage;
    @SerializedName("totalPages")
    private Integer totalPages;
    @SerializedName("total")
    private String total;

    public AttributosModel() {

    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPerPage() {
        return perPage;
    }

    public void setPerPage(String perPage) {
        this.perPage = perPage;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}
