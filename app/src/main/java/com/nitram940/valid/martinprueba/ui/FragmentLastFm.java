package com.nitram940.valid.martinprueba.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.nitram940.valid.martinprueba.model.callback.searchSubscribed;
import com.nitram940.valid.martinprueba.model.model_json.Artist;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;


public class FragmentLastFm extends FragmentBaseLastFm {

    @NonNull
    private static List<FragmentLastFm> fragmentArrayAdapter = new ArrayList<>();


    public static void newInstance(int sectionNumber) {

        FragmentLastFm fragment;
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);

        if (fragmentArrayAdapter.size() < 1) {
            fragment = new FragmentLastFm();
            fragment.setArguments(args);
            fragmentArrayAdapter.add(fragment);
        }
    }

    public static FragmentLastFm getInstance(int sectionNumber) {
        return fragmentArrayAdapter.get(sectionNumber);
    }

    @Subscribe
    public void onSearchQuery(@NonNull searchSubscribed searchSubscribed) {
        if (searchSubscribed.getEstate() == 1) {
            string_search = searchSubscribed.getQuery();
            initRefresh();
        }
    }


    @Override
    public void onDeliverArtist(Artist artist) {

    }
}
