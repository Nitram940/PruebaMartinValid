package com.nitram940.valid.martinprueba.model.helper;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Singleton
@Module
public class BusProvider {


    private BusProvider() {
    }

    private static Bus bus;

    @Provides
    public synchronized static Bus getBus() {

        if (bus == null) {
            bus = new MainThreadBus();
        }

        return bus;
    }

    public static class MainThreadBus extends Bus {

        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void post(final Object event) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                super.post(event);
            } else {
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        post(event);
                    }
                });
            }
        }
    }
}
