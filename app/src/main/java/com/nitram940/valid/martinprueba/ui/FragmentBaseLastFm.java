package com.nitram940.valid.martinprueba.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.nitram940.valid.martinprueba.R;
import com.nitram940.valid.martinprueba.controller.adapter.ArtistAdapter;
import com.nitram940.valid.martinprueba.di.DaggerApplication;
import com.nitram940.valid.martinprueba.model.callback.LastFmFetchListener;
import com.nitram940.valid.martinprueba.model.callback.LastFmService;
import com.nitram940.valid.martinprueba.model.callback.searchSubscribed;
import com.nitram940.valid.martinprueba.model.database.LastFmDatabase;
import com.nitram940.valid.martinprueba.model.helper.BusProvider;
import com.nitram940.valid.martinprueba.model.helper.NetworkCheckValidation;
import com.nitram940.valid.martinprueba.model.helper.Utils;
import com.nitram940.valid.martinprueba.model.helper.picture;
import com.nitram940.valid.martinprueba.model.model_json.Artist;
import com.nitram940.valid.martinprueba.model.model_json.ArtistResponse;
import com.squareup.otto.Bus;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/*  Fragment para seccion perfil */
public abstract class FragmentBaseLastFm extends Fragment implements LastFmFetchListener, NetworkCheckValidation {

    public static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = "FragmentBaseLastFm";
    public RecyclerView recycler;
    public Observable<ArtistResponse> observable;
    View rootView;
    Subscription subscription;
    @Inject
    LastFmDatabase mDatabase;
    @Inject
    LastFmService movieService;
    @Inject
    picture picture;
    @Inject
    Utils utils;


    AlertDialog alertDialog;
    String string_search = "";
    private ArtistAdapter moviesAdapter;
    private TextView txt_pagination;
    private int query;
    private int pageQuery = 1;
    private int maxPageQuery = 0;
    private boolean isOnline = false;
    private boolean lastState = isOnline;
    @Nullable
    private NumberPicker numberPicker = null;
    private Bus bus;
    private boolean categorizate = false;
    private SwipeRefreshLayout refreshLayout;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;


    public FragmentBaseLastFm() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        query = getArguments().getInt(ARG_SECTION_NUMBER);
        initPagination();

        if (query == 3) {
            this.bus = BusProvider.getBus();
            categorizate = true;
        }

        if (rootView == null) {
            super.onCreateView(inflater, container, savedInstanceState);
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ((DaggerApplication) getActivity().getApplication()).getAppComponent().inject(this);

            refreshLayout = rootView.findViewById(R.id.swipeContainer);
            initViews();
            setupSwitchOnRefresh();
            initRefresh();
        }

        return rootView;
    }

    public void initRefresh(){
        if (!refreshLayout.isRefreshing()) {
            refreshListener.onRefresh();
            refreshLayout.setRefreshing(true);
        }
    }

    private void initViews() {
        recycler = rootView.findViewById(R.id.movies_recycler_view);
        recycler.setItemViewCacheSize(5); // establecer las vistas en cache
        recycler.setNestedScrollingEnabled(false);

        txt_pagination = rootView.findViewById(R.id.txt_pagination);
        txt_pagination.setVisibility(View.GONE);

        txt_pagination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPagination();
            }
        });

        LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        recycler.setLayoutManager(mManager);
    }

    public void getMovies() {
        if (isOnline != lastState) {
            pageQuery = 1;
            assert numberPicker != null;
            numberPicker.setValue(pageQuery);
        }

        typeObservable(pageQuery);

        if (!(string_search.equals("") && query == 3)) {
            if (isOnline) {
                readWebService();
            } else {
                persistenciaMovies();
            }
        } else {
            Artist newArtist = new Artist();
            //newArtist.setName_artist(getString(R.string.do_search));
            //newArtist.setOverview(getString(R.string.doSearchInstruction));
            //newArtist.setInfoItem(true);
            //newArtist.setOffline(true);
            //newArtist.setGenreIds(new ArrayList<Integer>());

            inflateRecycler(null, newArtist);
        }

        lastState = isOnline;
    }

    private void setupSwitchOnRefresh() {
        refreshListener=new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkNetwork();
            }
        };

        refreshLayout.setOnRefreshListener(refreshListener);
    }

    private void checkNetwork(){
        utils.isNetworkAvailable(this);
    }

    private void typeObservable(int pageQuery) {

        switch (query) {
            case 0:
                observable = movieService.getTopArtist(pageQuery);
                break;
            case 1:
                //observable = movieService.getTopRatedMovies(pageQuery);
                break;
        }
    }

    private void persistenciaMovies() {
        mDatabase.fetchArtist(this, query, pageQuery - 1, string_search);
    }


    private void readWebService() {

        subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArtistResponse>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "complete");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        persistenciaMovies();
                    }

                    @Override
                    public void onNext(@NonNull ArtistResponse artistResponse) {
                        List<Artist> artistList = artistResponse.getTopartists();
                        mDatabase.addListArtist(artistList);
                        inflateRecycler(artistList, null);
                        max_pagination(artistResponse.getAttributosModel().getTotalPages());
                    }
                });
    }

    private void initPagination() {
        AlertDialog.Builder d = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.number_picker_dialog, null);

        d.setTitle(R.string.selectPage);
        d.setMessage(R.string.message);
        d.setView(dialogView);

        numberPicker = dialogView.findViewById(R.id.dialog_number_picker);


        numberPicker.setMaxValue(maxPageQuery);
        numberPicker.setMinValue(1);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setValue(pageQuery);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                Log.d(TAG, "onValueChange: ");
            }
        });
        d.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG, "onClick: " + numberPicker.getValue());

                if (pageQuery != numberPicker.getValue()) {
                    pageQuery = numberPicker.getValue();
                    initRefresh();
                    setPageMenu();
                }
            }
        });
        d.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                numberPicker.setValue(pageQuery);
            }
        });
        alertDialog = d.create();
        alertDialog.setCancelable(false);
    }

    private void selectPagination() {
        alertDialog.show();
    }

    private void max_pagination(int page) {
        if (page == 0) {
            rootView.findViewById(R.id.pagination).setVisibility(View.GONE);
        } else {
            rootView.findViewById(R.id.pagination).setVisibility(View.VISIBLE);

            if (page != maxPageQuery) {
                assert numberPicker != null;
                numberPicker.setMaxValue(page);
                maxPageQuery = page;
                alertDialog.setMessage(MessageFormat.format("{0} {1}", getString(R.string.maxim), maxPageQuery));
                pageQuery = 1;
            }

            setPageMenu();
        }
    }

    private void setPageMenu() {
        txt_pagination.setText(MessageFormat.format("{0} {1} {2} {3}", getString(R.string.pagine), pageQuery, getString(R.string.of), maxPageQuery));
    }

    private void inflateRecycler(@Nullable List<Artist> artistList, Artist artist) {
        List<String> stringTitles = new ArrayList<>();
        txt_pagination.setVisibility(View.VISIBLE);

        if (artistList == null) {
            recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            recycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }

        if (moviesAdapter == null) {
            if (artistList != null) {
                moviesAdapter = new ArtistAdapter(artistList, R.layout.list_item_movie, getActivity().getApplicationContext(), picture);
            } else {
                moviesAdapter = new ArtistAdapter(artist, R.layout.list_item_movie, getActivity().getApplicationContext(), picture);
            }
            recycler.setAdapter(moviesAdapter);
        } else {
            if (artistList != null) {
                moviesAdapter.setSearchResponse(artistList);
            } else {
                moviesAdapter.setSearchResponse(artist);
            }
        }

        if (query == 3) {
            searchSubscribed searchSubscribed = new searchSubscribed(null, 2);
            if (artistList != null) {
                for (Artist artist1 : artistList) {
                    stringTitles.remove(artist1.getName_artist());
                    stringTitles.add(artist1.getName_artist());
                }
                searchSubscribed.setTitlesMovies(stringTitles);
            }
            bus.post(searchSubscribed);
        }

        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onDeliverAllArtist(List<Artist> artistList, int pages) {
        max_pagination(pages);
        inflateRecycler(artistList, null);
    }

    @Override
    public void onHideDialog() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (query == 3) {
            bus.register(this);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (query == 3) {
            bus.unregister(this);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }


    }

    @Override
    public void onNetworkCheckValidation(Boolean bool){
        isOnline=bool;
        getMovies();
    }
}
