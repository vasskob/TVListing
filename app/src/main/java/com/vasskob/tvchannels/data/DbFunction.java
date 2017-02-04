package com.vasskob.tvchannels.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.vasskob.tvchannels.model.TvCategory;
import com.vasskob.tvchannels.model.TvChannel;
import com.vasskob.tvchannels.model.TvListing;

import java.util.ArrayList;
import java.util.List;

import static com.vasskob.tvchannels.data.TvChannelContract.CategoryEntry.COLUMN_CATEGORY_ID;
import static com.vasskob.tvchannels.data.TvChannelContract.CategoryEntry.COLUMN_CATEGORY_PICTURE;
import static com.vasskob.tvchannels.data.TvChannelContract.CategoryEntry.COLUMN_CATEGORY_TITLE;
import static com.vasskob.tvchannels.data.TvChannelContract.ChannelEntry.COLUMN_CHANNEL_CATEGORY_ID;
import static com.vasskob.tvchannels.data.TvChannelContract.ChannelEntry.COLUMN_CHANNEL_ID;
import static com.vasskob.tvchannels.data.TvChannelContract.ChannelEntry.COLUMN_CHANNEL_NAME;
import static com.vasskob.tvchannels.data.TvChannelContract.ChannelEntry.COLUMN_CHANNEL_PICTURE;
import static com.vasskob.tvchannels.data.TvChannelContract.ChannelEntry.COLUMN_CHANNEL_URL;
import static com.vasskob.tvchannels.data.TvChannelContract.ListingEntry.COLUMN_LISTING_CHANNEL_ID;
import static com.vasskob.tvchannels.data.TvChannelContract.ListingEntry.COLUMN_LISTING_DATE;
import static com.vasskob.tvchannels.data.TvChannelContract.ListingEntry.COLUMN_LISTING_DESCRIPTION;
import static com.vasskob.tvchannels.data.TvChannelContract.ListingEntry.COLUMN_LISTING_TIME;
import static com.vasskob.tvchannels.data.TvChannelContract.ListingEntry.COLUMN_LISTING_TITLE;

/**
 * Created by anonymous on 03.02.17.
 */

public class DbFunction {

    Context mContext;

    public DbFunction(Context context) {
        this.mContext = context;
    }

    /**
     * Insert fetched data to channel table in DB by one bulk operation
     *
     * @param channels List of categories to upload
     */
    public void insertChannelsToDb(List<TvChannel> channels) {
        ContentValues[] valuesArray = new ContentValues[channels.size()];

        for (int i = 0; i < channels.size(); i++) {
            TvChannel channel = channels.get(i);
            ContentValues values = new ContentValues();
            values.put(COLUMN_CHANNEL_ID, channel.getId());
            values.put(COLUMN_CHANNEL_NAME, channel.getName());
            values.put(COLUMN_CHANNEL_URL, channel.getUrl());
            values.put(COLUMN_CHANNEL_PICTURE, channel.getPicture());
            values.put(COLUMN_CHANNEL_CATEGORY_ID, channel.getCategoryId());
            valuesArray[i] = values;
        }
        mContext.getContentResolver().bulkInsert(TvChannelContract.ChannelEntry.CONTENT_URI, valuesArray);

    }

    /**
     * Insert fetched data to category table in DB by one bulk operation
     *
     * @param categories List of categories to upload
     */
    public void insertCategoriesToDb(List<TvCategory> categories) {
        ContentValues[] valuesArray = new ContentValues[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            TvCategory category = categories.get(i);
            ContentValues values = new ContentValues();
            values.put(COLUMN_CATEGORY_ID, category.getId());
            values.put(COLUMN_CATEGORY_TITLE, category.getTitle());
            values.put(COLUMN_CATEGORY_PICTURE, category.getPicture());
            valuesArray[i] = values;
        }

        mContext.getContentResolver().bulkInsert(TvChannelContract.CategoryEntry.CONTENT_URI, valuesArray);

    }

    /**
     * Insert fetched data to listing table in DB by one bulk operation
     *
     * @param listings List of listings to upload
     */
    public void insertListingsToDb(List<TvListing> listings) {
        ContentValues[] valuesArray = new ContentValues[listings.size()];
        for (int i = 0; i < listings.size(); i++) {
            TvListing listing = listings.get(i);
            ContentValues values = new ContentValues();
            values.put(COLUMN_LISTING_DATE, listing.getDate());
            values.put(COLUMN_LISTING_TIME, listing.getTime());
            values.put(COLUMN_LISTING_TITLE, listing.getTitle());
            values.put(COLUMN_LISTING_DESCRIPTION, listing.getDescription());
            values.put(COLUMN_LISTING_CHANNEL_ID, listing.getChannelId());
            valuesArray[i] = values;
        }
        mContext.getContentResolver().bulkInsert(TvChannelContract.ListingEntry.CONTENT_URI, valuesArray);


    }

    /**
     * Erase all row in listing,channel,category tables in DB
     */
    public void clearTablesInDb() {
        mContext.getContentResolver().delete(TvChannelContract.ListingEntry.CONTENT_URI, null, null);
        mContext.getContentResolver().delete(TvChannelContract.ChannelEntry.CONTENT_URI, null, null);
        mContext.getContentResolver().delete(TvChannelContract.CategoryEntry.CONTENT_URI, null, null);
    }

    /**
     * Get all channel names for store in TabLayout
     *
     * @return Channel names from Db
     */

    public List<String> getChannelNames(String orderBy) {
        List<String> names = new ArrayList<>();
        Cursor c = mContext.getContentResolver().query(
                TvChannelContract.ChannelEntry.CONTENT_URI,
                TvChannelContract.ChannelEntry.NAME_PROJECTION,
                null,
                null,
                orderBy);
        if (c != null) {

            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                names.add(c.getString(c.getColumnIndex(COLUMN_CHANNEL_NAME)));
            }
            c.close();
        }
        return names;
    }

//    public List<TvListing> getChannelListing(

//    ) {
//        List<TvListing> tvListingList = new ArrayList<>();
//
//
//        Cursor c = mContext.getContentResolver().query(
//                TvChannelContract.ListingEntry.CONTENT_URI,
//                TvChannelContract.ListingEntry.FULL_PROJECTION,
//                null,
//                null,
//                null);
//
//              if (c != null) {
//            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
//                TvListing tvListing = new TvListing();
//
//                tvListing.setChannelId(c.getInt(c.getColumnIndexOrThrow("channel_id")));
//                tvListing.setDate(c.getString(c.getColumnIndexOrThrow("l_date")));
//                tvListing.setTime(c.getString(c.getColumnIndexOrThrow("l_time")));
//                tvListing.setTitle(c.getString(c.getColumnIndexOrThrow("title")));
//                tvListing.setDescription(c.getString(c.getColumnIndexOrThrow("description")));
//                tvListingList.add(tvListing);
//            }
//            c.close();
//        }
//        return tvListingList;
//    }

    public List<TvListing> getChannelListing(String channel_Id, String forDate) {
        List<TvListing> programs = new ArrayList<>();
        String[] selectionArgs = new String[]{String.valueOf(channel_Id), forDate};
        Cursor cursor = mContext.getContentResolver().query(
                TvChannelContract.ListingEntry.CONTENT_URI,
                TvChannelContract.ListingEntry.FULL_PROJECTION,
                COLUMN_LISTING_CHANNEL_ID + " = ? AND " + COLUMN_LISTING_DATE + " = ?",
                selectionArgs,
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {

                int dateColIndex = cursor.getColumnIndex(COLUMN_LISTING_DATE);
                int timeColIndex = cursor.getColumnIndex(COLUMN_LISTING_TIME);
                int titleColIndex = cursor.getColumnIndex(COLUMN_LISTING_TITLE);
                do {
                    TvListing listing = new TvListing();
                    listing.setTitle(cursor.getString(titleColIndex));
                    listing.setTime(cursor.getString(timeColIndex));
                    listing.setDate(cursor.getString(dateColIndex));
                    programs.add(listing);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return programs;
    }


}
