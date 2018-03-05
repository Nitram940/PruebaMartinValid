package com.nitram940.valid.martinprueba.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.nitram940.valid.martinprueba.model.callback.LastFmFetchListener;
import com.nitram940.valid.martinprueba.model.helper.Constants;
import com.nitram940.valid.martinprueba.model.model_json.Artist;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;

@Singleton
@Module
public class LastFmDatabase extends SQLiteOpenHelper {

    private static final String TAG = LastFmDatabase.class.getSimpleName();

    @Inject
    public LastFmDatabase(Context context) {
        super(context, Constants.DATABASE.DB_NAME, null, Constants.DATABASE.DB_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        try {
            db.execSQL(Constants.DATABASE.CREATE_TABLE_ARTISTS);
            db.execSQL(Constants.DATABASE.CREATE_TABLE_TRACKS);
        } catch (SQLException ex) {
            Log.d(TAG, ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Constants.DATABASE.DROP_TABLE_ARTISTS);
        db.execSQL(Constants.DATABASE.DROP_TABLE_COUNTRY);
        db.execSQL(Constants.DATABASE.DROP_TABLE_TRACKS);
        this.onCreate(db);
    }

    public void addListArtist(@NonNull List<Artist> artistList) {
        for (Artist artist: artistList) {
            addArtist(artist);
        }
    }

    public void addArtist(@NonNull Artist artist) {

        //Log.d(TAG, "Values Got " + Artist.getTitle());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        ContentValues valuesGenresMovie = new ContentValues();

        values.put(Constants.DATABASE.MBID_ARTIST, artist.getMbid_artist());
        values.put(Constants.DATABASE.NAME_ARTIST, artist.getName_artist());

        try {
            db.replaceOrThrow(Constants.DATABASE.TABLE_ARTISTS, null, values);
        } catch (Exception e) {
            Log.e(TAG, "ERR MOVIE" + e.getMessage());
        }

        db.close();
    }

    public void fetchArtist(LastFmFetchListener listener, int query, int pageQuery, String query_s) {
        LastFmFetcher fetcher = new LastFmFetcher(listener, this.getWritableDatabase(), query, pageQuery, query_s);
        fetcher.start();
    }

    public void fetchArtist(LastFmFetchListener listener, int idMovie) {
        LastFmFetcher fetcher = new LastFmFetcher(listener, this.getWritableDatabase(), idMovie);
        fetcher.start();
    }


    public class LastFmFetcher extends Thread {

        private final LastFmFetchListener mListener;
        private final SQLiteDatabase mDb;
        private int query;
        private String query_s = "";
        private int pageQuery;
        private int pageSize = 0;
        private int idMovie = 0;
        private boolean genres = false;


        LastFmFetcher(LastFmFetchListener listener, SQLiteDatabase db, int query, int pageQuery, String query_s) {
            this.query = query;
            this.pageQuery = pageQuery;
            this.query_s = query_s;
            mListener = listener;
            mDb = db;
        }

        LastFmFetcher(LastFmFetchListener listener, SQLiteDatabase db, int idMovie) {
            this.query = 9;
            mListener = listener;
            mDb = db;
            this.idMovie = idMovie;
        }

        public LastFmFetcher(LastFmFetchListener listener, SQLiteDatabase db, boolean genres) {
            this.genres = genres;
            mListener = listener;
            mDb = db;
        }

        @Override
        public void run() {
            Cursor cursor = null;
            Cursor cursor_cast = null;
            Cursor cursorSize;
            String pagination = "";


            switch (query) {
                case 0:
                    cursor = mDb.rawQuery(Constants.DATABASE.GET_QUERY_TOP_ARTISTS + pagination, null);
                    break;
                case 1:
                    cursor = mDb.rawQuery(Constants.DATABASE.GET_QUEY_TOP_TRACKS + pagination, null);
                    break;
            }

            final List<Artist> artistList = new ArrayList<>();

            if (cursor.getCount() > 0) {

                if (cursor.moveToFirst()) {
                    do {
                        Artist artist = new Artist();
                        artist.setOffline(true);
                        artist.setPlaycount(cursor.getString(cursor.getColumnIndex(Constants.DATABASE.PLAYCOUNT)));
                        //pendiente

                        artistList.add(artist);
                        publishMovie(artist);

                    } while (cursor.moveToNext());
                }
            }

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onDeliverAllArtist(artistList, pageSize);
                    mListener.onHideDialog();
                }
            });


            cursor.close();
        }

        void publishMovie(final Artist artist) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mListener.onDeliverArtist(artist);
                }
            });
        }
    }
}
