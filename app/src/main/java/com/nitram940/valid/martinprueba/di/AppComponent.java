package com.nitram940.valid.martinprueba.di;

import com.nitram940.valid.martinprueba.controller.ApiRetrofit;
import com.nitram940.valid.martinprueba.model.database.LastFmDatabase;
import com.nitram940.valid.martinprueba.model.helper.BusProvider;
import com.nitram940.valid.martinprueba.model.helper.Utils;
import com.nitram940.valid.martinprueba.model.helper.picture;
import com.nitram940.valid.martinprueba.ui.FragmentBaseLastFm;
import com.nitram940.valid.martinprueba.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by nitra on 24/12/2017.
 */

@Singleton
@Component(modules={AppModule.class, LastFmModule.class, ApiRetrofit.class, picture.class, LastFmDatabase.class, BusProvider.class, Utils.class})
public interface AppComponent {

    void inject(MainActivity activity);

    void inject(FragmentBaseLastFm fragmentBaseLastFm);

    //void inject(FullscreenActivityDetailMovie fullscreenActivityDetailMovie);
}