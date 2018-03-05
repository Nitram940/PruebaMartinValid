package com.nitram940.valid.martinprueba.model.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nitram940.valid.martinprueba.model.callback.LastFmApisImpl;

import java.util.Locale;
import java.util.Objects;

public class LocaleChangedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), Intent.ACTION_LOCALE_CHANGED)) {
            LastFmApisImpl.LANGUAGE_API= Locale.getDefault().toLanguageTag();
            Log.v("LocaleChangedRecevier", Locale.getDefault().toLanguageTag());
        }

    }
}
