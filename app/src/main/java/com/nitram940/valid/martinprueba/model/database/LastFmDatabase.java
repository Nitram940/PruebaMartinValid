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
import com.nitram940.valid.martinprueba.model.model_json.ImageModel;

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

        values.put(Constants.DATABASE.MBID_ARTIST, artist.getMbid_artist());
        values.put(Constants.DATABASE.NAME_ARTIST, artist.getName_artist());
        values.put(Constants.DATABASE.LISTENERS, artist.getListeners());
        values.put(Constants.DATABASE.STREAMABLE, artist.getStreamable());
        values.put(Constants.DATABASE.URL, artist.getUrl());
        values.put(Constants.DATABASE.IMG_SMALL, artist.getImageModelList().get(0).getUrl());
        values.put(Constants.DATABASE.IMG_MEDIUM, artist.getImageModelList().get(1).getUrl());
        values.put(Constants.DATABASE.IMG_LARGE, artist.getImageModelList().get(2).getUrl());
        values.put(Constants.DATABASE.IMG_EXTRALARGE, artist.getImageModelList().get(3).getUrl());
        values.put(Constants.DATABASE.IMG_MEGA, artist.getImageModelList().get(4).getUrl());

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

        @Override
        public void run() {
            Cursor cursor = null;
            Cursor cursorSize;
            String pagination = "";

            if (idMovie == 0) {

                cursorSize = mDb.rawQuery(Constants.DATABASE.GET_ALL_ARTIST_COUNT, null);

                cursorSize.moveToFirst();
                pageSize = (int) Math.ceil((double) cursorSize.getInt(0) / 50);

                if (pageQuery > pageSize) {
                    pageQuery = 0;
                }

                pagination = " LIMIT " + 50 + " OFFSET  " + ((pageQuery) * 50);
                cursorSize.close();
            }



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
                        artist.setListeners(cursor.getString(cursor.getColumnIndex(Constants.DATABASE.LISTENERS)));
                        artist.setName_artist(cursor.getString(cursor.getColumnIndex(Constants.DATABASE.NAME_ARTIST)));
                        artist.setMbid_artist(cursor.getString(cursor.getColumnIndex(Constants.DATABASE.MBID_ARTIST)));
                        artist.setStreamable(cursor.getString(cursor.getColumnIndex(Constants.DATABASE.STREAMABLE)));
                        artist.setUrl(cursor.getString(cursor.getColumnIndex(Constants.DATABASE.URL)));


                        List<ImageModel> imageModel = new ArrayList<ImageModel>();

                        imageModel.add(new ImageModel(cursor.getString(cursor.getColumnIndex(Constants.DATABASE.IMG_SMALL))));
                        imageModel.add(new ImageModel(cursor.getString(cursor.getColumnIndex(Constants.DATABASE.IMG_MEDIUM))));
                        imageModel.add(new ImageModel(cursor.getString(cursor.getColumnIndex(Constants.DATABASE.IMG_LARGE))));
                        imageModel.add(new ImageModel(cursor.getString(cursor.getColumnIndex(Constants.DATABASE.IMG_EXTRALARGE))));
                        imageModel.add(new ImageModel(cursor.getString(cursor.getColumnIndex(Constants.DATABASE.IMG_MEGA))));

                        artist.setImageModelList(imageModel);

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
