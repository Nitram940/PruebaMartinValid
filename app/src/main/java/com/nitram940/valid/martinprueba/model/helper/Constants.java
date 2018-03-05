package com.nitram940.valid.martinprueba.model.helper;

import android.support.annotation.NonNull;

public class Constants {

    @NonNull
    public static String API_KEY = "829751643419a7128b7ada50de590067";

    public static final class HTTP {
        public static final String BASE_URL = "http://ws.audioscrobbler.com/2.0/";
    }

    public static final class DATABASE {

        public static final String DB_NAME = "music";
        public static final int DB_VERSION = 1;
        public static final String TABLE_ARTISTS = "artists";
        public static final String TABLE_TRACKS = "tracks";
        public static final String TABLE_COUNTRY = "country";


        //COUNTRY
        @NonNull
        public static String ID_COUNTRY = "ID_COUNTRY";
        @NonNull
        public static String NAME_COUTRY = "NAME_COUTRY";


        //ARTIST
        public static final String MBID_ARTIST = "MBID_ARTIST"; //STRING PK
        public static final String NAME_ARTIST = "NAME_ARTIST"; //STRING


        //TRACKS
        public static final String ID_TRACKS = "ID_TRACKS"; //PK
        public static final String NAME_TRACKS = "NAME_TRACKS"; //STRING
        public static final String PLAYCOUNT = "PLAYCOUNT"; //STRING
        public static final String MBID = "MBID"; //STRING
        public static final String URL = "URL"; //STRING
        public static final String STREAMABLE = "STREAMABLE"; //STRING
        public static final String IMG_SMALL = "IMG_SMALL"; //STRING
        public static final String IMG_MEDIUM = "IMG_MEDIUM"; //STRING
        public static final String IMG_LARGE = "IMG_LARGE"; //STRING

        //DETAIL ITEMS
        public static final String TAGLINE = "TAGLINE"; //STRING

        public static final String DROP_TABLE_ARTISTS = "DROP TABLE IF EXISTS " + TABLE_ARTISTS;
        public static final String DROP_TABLE_COUNTRY = "DROP TABLE IF EXISTS " + TABLE_COUNTRY;
        public static final String DROP_TABLE_TRACKS = "DROP TABLE IF EXISTS " + TABLE_TRACKS;

        public static final String GET_QUERY_TOP_ARTISTS = "SELECT * FROM " + TABLE_ARTISTS + " ";
        public static final String GET_QUEY_TOP_TRACKS = "SELECT * FROM " + TABLE_TRACKS + " ";

        public static final String CREATE_TABLE_ARTISTS = "CREATE TABLE IF NOT EXISTS " + TABLE_ARTISTS + "" +
                "(" + MBID_ARTIST + " TEXT PRIMARY KEY not null," +
                NAME_ARTIST + " TEXT not null," +
                PLAYCOUNT + " TEXT not null," +
                URL + " TEXT not null," +
                STREAMABLE + " TEXT not null," +
                IMG_SMALL + " TEXT not null," +
                IMG_MEDIUM + " TEXT not null," +
                IMG_LARGE + " TEXT not null)";

        public static final String CREATE_TABLE_TRACKS = "CREATE TABLE IF NOT EXISTS " + TABLE_TRACKS + "" +
                "(" + MBID + " TEXT PRIMARY KEY not null," +
                MBID_ARTIST + " TEXT not null," +
                NAME_TRACKS + " TEXT not null," +
                PLAYCOUNT + " TEXT not null," +
                URL + " TEXT not null," +
                STREAMABLE + " TEXT not null," +
                IMG_SMALL + " TEXT not null," +
                IMG_MEDIUM + " TEXT not null," +
                IMG_LARGE + " TEXT not null)";    }


    public static final class REFERENCE {
        public static final String MOVIE = Config.PACKAGE_NAME + "movie_item";
    }

    static final class Config {
        static final String PACKAGE_NAME = "com.codingchallenge.nitra.codingchallenge.";
    }


}
