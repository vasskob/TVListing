package com.vasskob.tvchannels.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vasskob.tvchannels.model.TvCategory;
import com.vasskob.tvchannels.model.TvChannel;
import com.vasskob.tvchannels.model.TvListing;

import java.util.ArrayList;
import java.util.List;

import static com.vasskob.tvchannels.data.TvChannelContract.CategoryEntry.COLUMN_CATEGORY_ID;
import static com.vasskob.tvchannels.data.TvChannelContract.CategoryEntry.COLUMN_CATEGORY_PICTURE;
import static com.vasskob.tvchannels.data.TvChannelContract.CategoryEntry.COLUMN_CATEGORY_TITLE;
import static com.vasskob.tvchannels.data.TvChannelContract.ChannelEntry.COLUMN_CHANNEL_CATEGORY_ID;
import static com.vasskob.tvchannels.data.TvChannelContract.ChannelEntry.COLUMN_CHANNEL_FAVORITE;
import static com.vasskob.tvchannels.data.TvChannelContract.ChannelEntry.COLUMN_CHANNEL_ID;
import static com.vasskob.tvchannels.data.TvChannelContract.ChannelEntry.COLUMN_CHANNEL_NAME;
import static com.vasskob.tvchannels.data.TvChannelContract.ChannelEntry.COLUMN_CHANNEL_PICTURE;
import static com.vasskob.tvchannels.data.TvChannelContract.ChannelEntry.COLUMN_CHANNEL_URL;
import static com.vasskob.tvchannels.data.TvChannelContract.ListingEntry.COLUMN_LISTING_CHANNEL_ID;
import static com.vasskob.tvchannels.data.TvChannelContract.ListingEntry.COLUMN_LISTING_DATE;
import static com.vasskob.tvchannels.data.TvChannelContract.ListingEntry.COLUMN_LISTING_DESCRIPTION;
import static com.vasskob.tvchannels.data.TvChannelContract.ListingEntry.COLUMN_LISTING_TIME;
import static com.vasskob.tvchannels.data.TvChannelContract.ListingEntry.COLUMN_LISTING_TITLE;


@SuppressWarnings("CanBeFinal")
public class DbFunction {

    private final Context mContext;
    //private TvChannelDbHelper mDbHelper;

    public DbFunction(Context context) {
        this.mContext = context;
        //  mDbHelper = TvChannelDbHelper.getInstance(context);
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

    /**
     * Return List of TvListing object for
     * channel with ch_id for date
     *
     * @param channel_Id - id of channel listing for
     * @param forDate    -   date of channel listing
     * @return teturn List of TvListing for channel for date
     */
    public List<TvListing> getChannelListing(String channel_Id, String forDate) {
        List<TvListing> listings = new ArrayList<>();
        String[] selectionArgs = new String[]{channel_Id, forDate};
        Cursor cursor = mContext.getContentResolver().query(
                TvChannelContract.ListingEntry.CONTENT_URI,
                TvChannelContract.ListingEntry.FULL_PROJECTION,
                COLUMN_LISTING_CHANNEL_ID + " = ? AND " + COLUMN_LISTING_DATE + " = ?",
                selectionArgs,
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {


                int idColIndex = cursor.getColumnIndex(COLUMN_LISTING_CHANNEL_ID);
                int dateColIndex = cursor.getColumnIndex(COLUMN_LISTING_DATE);
                int timeColIndex = cursor.getColumnIndex(COLUMN_LISTING_TIME);
                int titleColIndex = cursor.getColumnIndex(COLUMN_LISTING_TITLE);
                int chnIdIndex = cursor.getColumnIndex(COLUMN_LISTING_DESCRIPTION);

                do {
                    TvListing listing = new TvListing();
                    listing.setTitle(cursor.getString(titleColIndex));
                    listing.setTime(cursor.getString(timeColIndex));
                    listing.setDate(cursor.getString(dateColIndex));
//                    listing.setChannelId(cursor.getInt(idColIndex));
                    listing.setDescription(cursor.getString(chnIdIndex));
                    listings.add(listing);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return listings;
    }

    /**
     * Erace all data from all Tables in DB
     */
    public void eraseDbTables() {

        mContext.getContentResolver().delete(TvChannelContract.ChannelEntry.CONTENT_URI, null, null);
        mContext.getContentResolver().delete(TvChannelContract.CategoryEntry.CONTENT_URI, null, null);
        mContext.getContentResolver().delete(TvChannelContract.ListingEntry.CONTENT_URI, null, null);

    }

    /**
     * Return List of TvChannel categories
     *
     * @return categories
     */
    public List<TvCategory> getChannelCategories() {

        List<TvCategory> categories = new ArrayList<>();

        Cursor cursor = mContext.getContentResolver().query(
                TvChannelContract.CategoryEntry.CONTENT_URI,
                TvChannelContract.CategoryEntry.FULL_PROJECTION,
                null,
                null,
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    TvCategory category = new TvCategory();
                    category.setId(cursor.getInt(cursor.getColumnIndex(TvChannelContract.CategoryEntry.COLUMN_CATEGORY_ID)));
                    category.setTitle(cursor.getString(cursor.getColumnIndex(TvChannelContract.CategoryEntry.COLUMN_CATEGORY_TITLE)));
                    category.setPicture(cursor.getString(cursor.getColumnIndex(TvChannelContract.CategoryEntry.COLUMN_CATEGORY_PICTURE)));
                    categories.add(category);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        System.out.println(" Category Picture from DBFunc " + categories.get(0).getTitle() + " !!!");
        return categories;
    }

    /**
     * Return List of all Channels
     *
     * @return List of channels
     */
    public List<TvChannel> getChannels() {
        List<TvChannel> channels = new ArrayList<>();
        Cursor cursor = mContext.getContentResolver().query(
                TvChannelContract.ChannelEntry.CONTENT_URI,
                TvChannelContract.ChannelEntry.FULL_PROJECTION,
                null,
                null,
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    TvChannel channel = new TvChannel();
                    channel.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CHANNEL_NAME)));
                    channel.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_CHANNEL_ID)));
                    channel.setIsFavorite(cursor.getInt(cursor.getColumnIndex(COLUMN_CHANNEL_FAVORITE)));
                    channel.setPicture(cursor.getString(cursor.getColumnIndex(COLUMN_CHANNEL_PICTURE)));
                    channel.setUrl(cursor.getString(cursor.getColumnIndex(COLUMN_CHANNEL_URL)));
                    String category = getCategoryForChannel(cursor.getInt(cursor.getColumnIndex(COLUMN_CHANNEL_CATEGORY_ID)));
                    channel.setCategory_name(category);

                    channels.add(channel);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        System.out.println(" Category Picture from DBFunc " + channels.get(0).getName() + " !!!");
        return channels;

    }

    /**
     * Return Category name by id
     *
     * @param id of category to
     * @return category name
     */
    private String getCategoryForChannel(int id) {

        String categoryName = "category";
        Cursor cursor = mContext.getContentResolver().query(
                TvChannelContract.CategoryEntry.CONTENT_URI,
                TvChannelContract.CategoryEntry.FULL_PROJECTION,
                COLUMN_CATEGORY_ID + " = ? ",
                new String[]{String.valueOf(id)},
                null);
        System.out.println();
        if (cursor != null && cursor.moveToFirst()) {
            categoryName = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_TITLE));
            cursor.close();
        }
        return categoryName;

    }

    /** Mark Channel as favorite
     * @param channelName by name
     * @param checked by tag (0 or 1)
     */
    public void markAsFavoriteInDb(String channelName, int checked) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CHANNEL_FAVORITE, checked);
        mContext.getContentResolver().update(TvChannelContract.ChannelEntry.CONTENT_URI,
                values,
                // COLUMN_CHANNEL_ID + " = ?",
                COLUMN_CHANNEL_NAME + " = ?",
                new String[]{channelName});
        System.out.println("Channel with position " + channelName + "is Checked" + checked);
    }

    public List<TvChannel> getFavoriteChannels() {
        List<TvChannel> channels = new ArrayList<>();
        Cursor cursor = mContext.getContentResolver().query(
                TvChannelContract.ChannelEntry.CONTENT_URI,
                TvChannelContract.ChannelEntry.FULL_PROJECTION,
                COLUMN_CHANNEL_FAVORITE + "!=?",
                new String[]{String.valueOf(0)},
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    TvChannel channel = new TvChannel();
                    channel.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CHANNEL_NAME)));
                    channel.setUrl(cursor.getString(cursor.getColumnIndex(COLUMN_CHANNEL_URL)));
                    channel.setPicture(cursor.getString(cursor.getColumnIndex(COLUMN_CHANNEL_PICTURE)));
                    channel.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_CHANNEL_ID)));
                    channel.setIsFavorite(cursor.getInt(cursor.getColumnIndex(COLUMN_CHANNEL_FAVORITE)));
                    channel.setCategory_name(getCategoryForChannel(cursor.getColumnIndex(COLUMN_CHANNEL_CATEGORY_ID)));
                    channels.add(channel);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return channels;
    }

    public List<TvListing> getSortedListing(String channel_name, String forDate) {
        List<TvListing> listings = new ArrayList<>();
        TvChannelDbHelper tvDb = TvChannelDbHelper.getInstance(mContext);
        SQLiteDatabase db = tvDb.getWritableDatabase();
        String sqlQuery = "select time, date, title, " +
                "description, channel.name from listing  " +
                "join  channel on listing.channel_id=" +
                "channel._id where date='?' and  " +
                "channel.name='?'";
        Cursor cursor = db.rawQuery(sqlQuery, new String[]{forDate, channel_name});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    TvListing listing = new TvListing();
                    listing.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_LISTING_DATE)));
                    listing.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_LISTING_TIME)));
                    listing.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_LISTING_TITLE)));
                    listing.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_LISTING_DESCRIPTION)));
                    listing.setChannelId(cursor.getInt(cursor.getColumnIndex(COLUMN_LISTING_CHANNEL_ID)));
                    listings.add(listing);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        return listings;
    }

    public List<TvChannel> getChannelsOfCategory(Integer categoryId) {
        List<TvChannel> tvChannels = new ArrayList<>();
        Log.d("myLog", "getChannelsForCategory" + categoryId);
        Cursor cursor = mContext.getContentResolver().query(
                TvChannelContract.ChannelEntry.CONTENT_URI,
                TvChannelContract.ChannelEntry.FULL_PROJECTION,
                COLUMN_CHANNEL_CATEGORY_ID + "=?",
                new String[]{String.valueOf(categoryId)},
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    TvChannel tvChannel = new TvChannel();
                    tvChannel.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CHANNEL_NAME)));
                    tvChannel.setPicture(cursor.getString(cursor.getColumnIndex(COLUMN_CHANNEL_PICTURE)));
                    tvChannel.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_CHANNEL_ID)));
                    tvChannel.setIsFavorite(cursor.getInt(cursor.getColumnIndex(COLUMN_CHANNEL_FAVORITE)));
                    tvChannel.setUrl(cursor.getString(cursor.getColumnIndex(COLUMN_CHANNEL_URL)));
                    String categoryName = getCategoryForChannel(cursor.getInt(cursor.getColumnIndex(COLUMN_CHANNEL_CATEGORY_ID)));
                    tvChannel.setCategory_name(categoryName);
                    tvChannels.add(tvChannel);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        Log.d("myLog", "getChannelsForCategory");
        return tvChannels;
    }
}

//
//    public List<TvListing> getSortedListing2(String channel_name, String forDate) {
//        List<TvListing> listings = new ArrayList<>();
//        TvChannelDbHelper tvDb = TvChannelDbHelper.getInstance(mContext);
//        SQLiteDatabase db = tvDb.getWritableDatabase();
//        String sqlQuery = "select time, date, title, " +
//                "description, channel.name from listing  " +
//                "join  channel on listing.channel_id=" +
//                "channel._id where date='?' and  " +
//                "channel.name='?'";
//        Cursor cursor = db.rawQuery(sqlQuery, new String[]{forDate,channel_name});
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                do {
//                    TvListing listing = new TvListing();
//                    listing.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_LISTING_DATE)));
//                    listing.setTime(cursor.getString(cursor.getColumnIndex(COLUMN_LISTING_TIME)));
//                    listing.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_LISTING_TITLE)));
//                    listing.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_LISTING_DESCRIPTION)));
//                    listing.setChannelId(cursor.getInt(cursor.getColumnIndex(COLUMN_LISTING_CHANNEL_ID)));
//
//                    String channelName = getChannelNames(cursor.getString(cursor.getColumnIndex(COLUMN_CHANNEL_CATEGORY_ID)));
//
//                    listings.add(listing);
//                } while (cursor.moveToNext());
//            }
//            cursor.close();
//            db.close();
//        }
//        return listings;
//    }
//}
