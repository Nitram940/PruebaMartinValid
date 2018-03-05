package com.nitram940.valid.martinprueba.ui;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.nitram940.valid.martinprueba.R;
import com.nitram940.valid.martinprueba.di.DaggerApplication;
import com.nitram940.valid.martinprueba.model.callback.LastFmService;
import com.nitram940.valid.martinprueba.model.callback.searchSubscribed;
import com.nitram940.valid.martinprueba.model.database.LastFmDatabase;
import com.nitram940.valid.martinprueba.model.helper.BusProvider;
import com.nitram940.valid.martinprueba.model.helper.NetworkCheckValidation;
import com.nitram940.valid.martinprueba.model.helper.Utils;
import com.nitram940.valid.martinprueba.model.helper.picture;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NetworkCheckValidation {

    //search
    private static final long MSG_UPDATE_DELAY = 500;
    private static final String TAG = MainActivity.class.getSimpleName();
    @Inject
    LastFmDatabase mDatabase;
    @Inject
    picture picture;
    @Inject
    LastFmService movieService;
    @Inject
    Utils utils;

    private SearchView searchView;
    private Subscription subscription;

    private Set<String> prefsString;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */

    private ArrayAdapter<String> adapterSearches;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private Bus bus;
    private SearchView.SearchAutoComplete searchAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bus = BusProvider.getBus();

        setContentView(R.layout.activity_main);
        ((DaggerApplication) getApplication()).getAppComponent().inject(this);

        initViews();
        setupViewPager();

        prefsString = utils.getKEY_SEARCHED();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View view) {

                String text;
                if (utils.getKEY_OFFLINE()) {
                    text = getString(R.string.text_offline_movies);
                } else {
                    text = getString(R.string.text_online_movies);
                }

                Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                        .setAction(getString(android.R.string.yes), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (utils.getKEY_OFFLINE()) {
                                    utils.setKEY_OFFLINE(false);
                                } else {
                                    utils.setKEY_OFFLINE(true);
                                }
                            }
                        }).show();


            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mViewPager = findViewById(R.id.container);
        tabLayout = findViewById(R.id.tabs);
        //mViewPager.setNestedScrollingEnabled(false);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    private void setupViewPager() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        tabLayout.getTabAt(0).setText(R.string.TopArtist);
        mSectionsPagerAdapter.addFragment(0);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        initSearchView(menu);
        return true;
    }


    /**
	 *
	 * @param menu
	 */
	@SuppressLint("RestrictedApi")
    private void initSearchView(@NonNull Menu menu) {
        MenuItem item = menu.findItem(R.id.search);
        searchView = (SearchView) item.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        SearchView searchView = (SearchView) item.getActionView();
        LinearLayout searchBar = searchView.findViewById(R.id.search_bar);

        searchAutoComplete = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.white);
        searchAutoComplete.setDropDownAnchor(R.id.search);
        searchAutoComplete.setThreshold(0);
        //searchAutoComplete.setTextColor(Color.WHITE);
        loadCacheSearch();

        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull AdapterView<?> parent, View view, int position,
                                    long id) {
                String searchString = (String) parent.getItemAtPosition(position);
                searchAutoComplete.setText(searchString);
            }
        });

        //SearchManager searchManager =(SearchManager) getSystemService(SEARCH_SERVICE);
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchBar.setLayoutTransition(new LayoutTransition());

        subscriptionSearchView();
    }

    private void loadCacheSearch() {
        List<String> itemArrayList = new ArrayList<>();

        if (prefsString == null) {
            prefsString = new HashSet<>();
        } else {
            itemArrayList.addAll(prefsString);
        }

        adapterSearches = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, itemArrayList);
        searchAutoComplete.setAdapter(adapterSearches);
    }

    private void subscriptionSearchView() {
        subscription = RxSearchView.queryTextChanges(searchView).filter(new Func1<CharSequence, Boolean>() {
            @NonNull
            @Override
            public Boolean call(@NonNull CharSequence charSequence) {
                return charSequence.length() > 3;
            }
        }).debounce(MSG_UPDATE_DELAY, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<CharSequence>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "complete");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(@NonNull CharSequence charSequence) {
                        Log.d(TAG, "call: " + charSequence);
                        bus.post(new searchSubscribed(charSequence.toString(), 1));
                    }
                });
    }


    /**
	 *
	 * @param item
	 */
	@SuppressWarnings("StatementWithEmptyBody")
	@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_movie) {
            // Handle the camera action
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        utils.setKEY_SEARCHED(prefsString);

        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Subscribe
    public void onSearchQuery(@NonNull searchSubscribed searchSubscribed) {
        if (searchSubscribed.getEstate() == 1) {
            Snackbar.make(mViewPager, "Search: " + searchSubscribed.getQuery(), Snackbar.LENGTH_INDEFINITE).show();
            mViewPager.setCurrentItem(3, true);
        } else {
            if (searchSubscribed.getTitlesMovies() != null) {
                for (String string : searchSubscribed.getTitlesMovies()) {
                    string = utils.toNoAccents(string.toLowerCase());
                    adapterSearches.remove(string);
                    adapterSearches.add(string);
                    prefsString.remove(string);
                    prefsString.add(string);
                }
                searchAutoComplete.showDropDown();
            }
            Snackbar.make(mViewPager, "Loaded... ", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNetworkCheckValidation(Boolean bool) {

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        void addFragment(int instance) {
            FragmentLastFm.newInstance(instance);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return FragmentLastFm.getInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }
    }
}
