package com.nitram940.valid.martinprueba.model.callback;

import com.nitram940.valid.martinprueba.model.model_json.Artist;

import java.util.List;


public interface LastFmFetchListener {
    void onDeliverAllArtist(List<Artist> artistList, int pages);

    void onDeliverArtist(Artist artist);

    void onHideDialog();
}
