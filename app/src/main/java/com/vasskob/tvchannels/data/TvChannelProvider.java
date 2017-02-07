package com.vasskob.tvchannels.data;

// @formatter:off

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.vasskob.tvchannels.BuildConfig;
import com.vasskob.tvchannels.data.TvChannelContract.CategoryEntry;
import com.vasskob.tvchannels.data.TvChannelContract.ChannelEntry;
import com.vasskob.tvchannels.data.TvChannelContract.ListingEntry;
import com.vasskob.tvchannels.data.base.BaseContentProvider;

import java.util.Arrays;

public class TvChannelProvider extends BaseContentProvider {
    private static final String TAG = TvChannelProvider.class.getSimpleName();

    private static final boolean DEBUG = BuildConfig.DEBUG;

    private static final String TYPE_CURSOR_ITEM = "vnd.android.cursor.item/";
    private static final String TYPE_CURSOR_DIR = "vnd.android.cursor.dir/";

    private static final String AUTHORITY = "com.vasskob.tvchannels";
    // /public static final String CONTENT_URI_BASE = "content://" + AUTHORITY;

    private static final int URI_TYPE_CATEGORIES = 0;
    private static final int URI_TYPE_CATEGORIES_ID = 1;

    private static final int URI_TYPE_CHANNELS = 2;
    private static final int URI_TYPE_CHANNELS_ID = 3;

    private static final int URI_TYPE_LISTING = 4;
    private static final int URI_TYPE_LISTING_ID = 5;


    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, CategoryEntry.TABLE_NAME, URI_TYPE_CATEGORIES);
        URI_MATCHER.addURI(AUTHORITY, CategoryEntry.TABLE_NAME + "/#", URI_TYPE_CATEGORIES_ID);
        URI_MATCHER.addURI(AUTHORITY, ChannelEntry.TABLE_NAME, URI_TYPE_CHANNELS);
        URI_MATCHER.addURI(AUTHORITY, ChannelEntry.TABLE_NAME + "/#", URI_TYPE_CHANNELS_ID);
        URI_MATCHER.addURI(AUTHORITY, ListingEntry.TABLE_NAME, URI_TYPE_LISTING);
        URI_MATCHER.addURI(AUTHORITY, ListingEntry.TABLE_NAME + "/#", URI_TYPE_LISTING_ID);
    }

    @Override
    protected SQLiteOpenHelper createSqLiteOpenHelper() {
        return TvChannelDbHelper.getInstance(getContext());
    }

    @Override
    protected boolean hasDebug() {
        return DEBUG;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case URI_TYPE_CATEGORIES:
                return TYPE_CURSOR_DIR + CategoryEntry.TABLE_NAME;
            case URI_TYPE_CATEGORIES_ID:
                return TYPE_CURSOR_ITEM + CategoryEntry.TABLE_NAME;

            case URI_TYPE_CHANNELS:
                return TYPE_CURSOR_DIR + ChannelEntry.TABLE_NAME;
            case URI_TYPE_CHANNELS_ID:
                return TYPE_CURSOR_ITEM + ChannelEntry.TABLE_NAME;

            case URI_TYPE_LISTING:
                return TYPE_CURSOR_DIR + ListingEntry.TABLE_NAME;
            case URI_TYPE_LISTING_ID:
                return TYPE_CURSOR_ITEM + ListingEntry.TABLE_NAME;

        }
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        if (DEBUG) Log.d(TAG, "insert uri=" + uri + " values=" + values);
        return super.insert(uri, values);
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        if (DEBUG) Log.d(TAG, "bulkInsert uri=" + uri + " values.length=" + values.length);
        return super.bulkInsert(uri, values);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (DEBUG)
            Log.d(TAG, "update uri=" + uri + " values=" + values + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs));
        return super.update(uri, values, selection, selectionArgs);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        if (DEBUG)
            Log.d(TAG, "delete uri=" + uri + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs));
        return super.delete(uri, selection, selectionArgs);
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (DEBUG)
            Log.d(TAG, "query uri=" + uri + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs) + " sortOrder=" + sortOrder
                    + " groupBy=" + uri.getQueryParameter(QUERY_GROUP_BY) + " having=" + uri.getQueryParameter(QUERY_HAVING) + " limit=" + uri.getQueryParameter(QUERY_LIMIT));
        return super.query(uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    protected QueryParams getQueryParams(Uri uri, String selection, String[] projection) {
        QueryParams res = new QueryParams();
        String id = null;
        int matchedId = URI_MATCHER.match(uri);
        switch (matchedId) {
            case URI_TYPE_CATEGORIES:
            case URI_TYPE_CATEGORIES_ID:
                res.table = CategoryEntry.TABLE_NAME;
                res.idColumn = CategoryEntry.COLUMN_CATEGORY_ID;
                res.tablesWithJoins = CategoryEntry.TABLE_NAME;
           //     res.orderBy = CategoryEntry.DEFAULT_ORDER;
                break;

            case URI_TYPE_CHANNELS:
            case URI_TYPE_CHANNELS_ID:
                res.table = ChannelEntry.TABLE_NAME;
                res.idColumn = ChannelEntry.COLUMN_CHANNEL_ID;
                res.tablesWithJoins = ChannelEntry.TABLE_NAME;
            //    res.orderBy = ChannelEntry.DEFAULT_ORDER;
                break;

            case URI_TYPE_LISTING:
            case URI_TYPE_LISTING_ID:
                res.table = ListingEntry.TABLE_NAME;
                res.idColumn = ListingEntry.COLUMN_LISTING_CHANNEL_ID;
                res.tablesWithJoins = ListingEntry.TABLE_NAME;
             //   res.orderBy = ListingEntry.DEFAULT_ORDER;
                break;

            default:
                throw new IllegalArgumentException("The uri '" + uri + "' is not supported by this ContentProvider");
        }

        switch (matchedId) {
            case URI_TYPE_CATEGORIES_ID:
            case URI_TYPE_CHANNELS_ID:
            case URI_TYPE_LISTING_ID:
                id = uri.getLastPathSegment();
        }
        if (id != null) {
            if (selection != null) {
                res.selection = res.table + "." + res.idColumn + "=" + id + " and (" + selection + ")";
            } else {
                res.selection = res.table + "." + res.idColumn + "=" + id;
            }
        } else {
            res.selection = selection;
        }
        return res;
    }
}
