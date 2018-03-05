package com.nitram940.valid.martinprueba.model.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

@Singleton
@Module
public class Utils {


    private final String SHARED_PREFS_FILE = "RTBPrefs";
    private final String KEY_OFFLINE = "KEY_OFFLINE";
    private final String KEY_SEARCHED = "KEY_SEARCHED";
    private Context mContext;


    @Inject
    public Utils(Context context) {
        mContext = context;
    }

    private SharedPreferences getSettings() {
        return mContext.getSharedPreferences(SHARED_PREFS_FILE, 0);
    }

    public boolean getKEY_OFFLINE() {
        return getSettings().getBoolean(KEY_OFFLINE, false);
    }

    public void setKEY_OFFLINE(boolean value) {
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putBoolean(KEY_OFFLINE, value);
        editor.apply();
    }

    public void isNetworkAvailable(final NetworkCheckValidation networkCheckValidation){
        Observable<Boolean> observable = Observable.defer(new Func0<Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call() {
                return Observable.just(checkNetwork());
            }
        });


        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("isNetworkAvailable", "complete");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(@NonNull Boolean bool) {
                        networkCheckValidation.onNetworkCheckValidation(bool);
                    }
                });
    }

    private boolean checkNetwork() {
        boolean isConnected;
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected || getKEY_OFFLINE()) {
            isConnected= false;
        } else {
            isConnected= hostAvailable("www.google.com", 80);
        }

        return isConnected ;
    }

    private boolean hostAvailable(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 2000);
            return true;
        } catch (IOException e) {
            Log.e("hostAvailable", e.getMessage());
        }
        return false;
    }

    public String toNoAccents( String original )
    {
        original = original.replace("?", "c");
        original = original.replace("?", "c");
        original = original.replace("š ", "s");
        original = original.replace("?", "d");
        original = original.replace("ž", "z");

        original = original.replace("š", "s");
//french accents
        original = original.replace("à ", "a");
        original = original.replace("â ", "a");
        original = original.replace("æ", "ae");
        original = original.replace("ç", "c");
        original = original.replace("è ", "e");
        original = original.replace("è", "e");
        original = original.replace("é ", "e");
        original = original.replace("é", "e");
        original = original.replace("ê ", "e");
        original = original.replace("ë ", "e");
        original = original.replace("î ", "i");
        original = original.replace("ï ", "i");
        original = original.replace("ô", "o");
        original = original.replace("œ", "oe");
        original = original.replace("ù", "u");
        original = original.replace("û", "u");
        original = original.replace("ü", "u");
//poland
        original = original.replace("?", "a");
        original = original.replace("?", "e");
        original = original.replace("?", "l");
        original = original.replace("?", "n");
        original = original.replace("ó", "o");
        original = original.replace("? ", "s");
        original = original.replace("?", "z");
        original = original.replace("?", "z");
        original = original.replace("?", "s");
//czech republic
        original = original.replace("?", "e");
        original = original.replace("?", "r");
        original = original.replace("ý", "y");
        original = original.replace("á", "a");
        original = original.replace("í", "i");
        original = original.replace("ú", "u");
        original = original.replace("?", "u");
        original = original.replace("?", "d");
        original = original.replace("?", "t");
        original = original.replace("?", "n");
//for slovak language:
        original = original.replace("ä", "a");
        original = original.replace("?", "l");
        original = original.replace("?", "l");
        original = original.replace("?", "r");

        return original;
    }

    @Nullable
    public Set<String> getKEY_SEARCHED()  {
        return getSettings().getStringSet(KEY_SEARCHED, null);
    }

    public void setKEY_SEARCHED(Set<String> stringSet)  {
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putStringSet(KEY_SEARCHED, stringSet);
        editor.apply();
    }
}