package com.nitram940.valid.martinprueba.model.callback;

import com.nitram940.valid.martinprueba.model.model_json.ArtistResponse;

import rx.Observable;



public interface LastFmService {

    Observable<ArtistResponse> getTopArtist(int page);

}
