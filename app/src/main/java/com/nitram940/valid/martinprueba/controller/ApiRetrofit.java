package com.nitram940.valid.martinprueba.controller;


import com.nitram940.valid.martinprueba.model.callback.LastFmApis;
import com.nitram940.valid.martinprueba.model.helper.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiRetrofit {

    @Provides
    @Singleton
    LastFmApis movieApis() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.HTTP.BASE_URL)
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        return retrofit.create(LastFmApis.class);
    }


}
