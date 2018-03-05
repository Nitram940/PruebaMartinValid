package com.nitram940.valid.martinprueba.di;

import android.support.annotation.NonNull;

import com.nitram940.valid.martinprueba.model.callback.LastFmApisImpl;
import com.nitram940.valid.martinprueba.model.callback.LastFmService;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class LastFmModule {

    @NonNull
    @Binds
    public abstract LastFmService providesMovieService(LastFmApisImpl lastFmApis);
}
