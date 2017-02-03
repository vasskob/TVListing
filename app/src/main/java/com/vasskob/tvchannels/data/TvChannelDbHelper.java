package com.vasskob.tvchannels.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vasskob.tvchannels.data.TvChannelContract.CategoryEntry;
import com.vasskob.tvchannels.data.TvChannelContract.ChannelEntry;
import com.vasskob.tvchannels.data.TvChannelContract.ListingEntry;

public class TvChannelDbHelper extends SQLiteOpenHelper {
    private static TvChannelDbHelper instance;
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "tvchannel.db";

    private TvChannelDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static TvChannelDbHelper getInstance(Context context) {

        if (instance == null) {
            instance = new TvChannelDbHelper(context.getApplicationContext());
        }
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE " + CategoryEntry.TABLE_NAME + " (" +
                CategoryEntry.COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY," +
                CategoryEntry.COLUMN_CATEGORY_TITLE + " TEXT NOT NULL, " +
                CategoryEntry.COLUMN_CATEGORY_PICTURE + " TEXT NOT NULL" +
                " );";

        final String SQL_CREATE_CHANNEL_TABLE = "CREATE TABLE " + ChannelEntry.TABLE_NAME + " (" +

                ChannelEntry.COLUMN_CHANNEL_ID + " INTEGER PRIMARY KEY," +
                ChannelEntry.COLUMN_CHANNEL_NAME + " TEXT NOT NULL, " +
                ChannelEntry.COLUMN_CHANNEL_URL + " TEXT NOT NULL, " +
                ChannelEntry.COLUMN_CHANNEL_PICTURE + " TEXT NOT NULL, " +
                ChannelEntry.COLUMN_CHANNEL_CATEGORY_ID + " INTEGER NOT NULL, " +
                ChannelEntry.COLUMN_CHANNEL_FAVORITE + " INTEGER NOT NULL DEFAULT 0 CHECK (is_favorite IN (0,1)), " +
                "FOREIGN KEY (" + ChannelEntry.COLUMN_CHANNEL_CATEGORY_ID + ") REFERENCES " +
                CategoryEntry.TABLE_NAME + "(" + CategoryEntry.COLUMN_CATEGORY_ID + "));";


        final String SQL_CREATE_LISTING_TABLE = "CREATE TABLE " + ListingEntry.TABLE_NAME + " (" +
                ListingEntry.COLUMN_LISTING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ListingEntry.COLUMN_LISTING_DATE + " TEXT NOT NULL, " +
                ListingEntry.COLUMN_LISTING_TIME + " TEXT NOT NULL, " +
                ListingEntry.COLUMN_LISTING_TITLE + " TEXT NOT NULL, " +
                ListingEntry.COLUMN_LISTING_DESCRIPTION + " TEXT NOT NULL, " +
                ListingEntry.COLUMN_LISTING_CHANNEL_ID + " INTEGER NOT NULL, "+
                "FOREIGN KEY (" + ListingEntry.COLUMN_LISTING_CHANNEL_ID + ") REFERENCES " +
                ChannelEntry.TABLE_NAME + "(" + ChannelEntry.COLUMN_CHANNEL_ID + "));";


        sqLiteDatabase.execSQL(SQL_CREATE_CATEGORY_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CHANNEL_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_LISTING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ChannelEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ListingEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}