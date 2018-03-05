package com.nitram940.valid.martinprueba.model.callback;

import android.support.annotation.NonNull;

import com.nitram940.valid.martinprueba.model.database.LastFmDatabase;
import com.nitram940.valid.martinprueba.model.helper.Constants;
import com.nitram940.valid.martinprueba.model.model_json.ArtistResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class LastFmApisImpl implements LastFmService {

    private final LastFmApis lastFmApis;

    public static String LANGUAGE_API="en";

    private LastFmDatabase mDatabase;

    @Inject
    LastFmApisImpl(LastFmApis lastFmApis, LastFmDatabase mDatabase) {
        this.lastFmApis = lastFmApis;
        this.mDatabase=mDatabase;

        //LANGUAGE_API= Locale.getDefault().toLanguageTag();
    }


    @NonNull
    @Override
    public Observable<ArtistResponse> getTopArtist(int page) {
        return lastFmApis.getTopArtist(Constants.API_KEY, LANGUAGE_API, page);
    }


}
