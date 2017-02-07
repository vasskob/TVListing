package com.vasskob.tvchannels.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

class TvChannelContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    private static final String CONTENT_AUTHORITY = "com.vasskob.tvchannels";


    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String PATH_CATEGORY = "category";
    private static final String PATH_CHANNEL = "channel";
    private static final String PATH_LISTING = "listing";


    /* Inner class that defines the table contents of the location table */
    public static final class CategoryEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORY).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORY;



        public static final String TABLE_NAME = "category";

        public static final String COLUMN_CATEGORY_ID = "_id";


        public static final String COLUMN_CATEGORY_TITLE = "title";


        public static final String COLUMN_CATEGORY_PICTURE = "picture";

        public static final String DEFAULT_ORDER = TABLE_NAME + "." + COLUMN_CATEGORY_ID + ","
                + TABLE_NAME + "." + COLUMN_CATEGORY_TITLE + ","
                + TABLE_NAME + "." + COLUMN_CATEGORY_PICTURE;

        public static Uri buildCategoryUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String[] FULL_PROJECTION = new String[]{
                COLUMN_CATEGORY_ID,
                COLUMN_CATEGORY_TITLE,
                COLUMN_CATEGORY_PICTURE};
    }

    /* Inner class that defines the table contents of the channel table */
    public static final class ChannelEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CHANNEL).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CHANNEL;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CHANNEL;

        public static final String TABLE_NAME = "channel";

         public static final String COLUMN_CHANNEL_ID = "_id";

        public static final String COLUMN_CHANNEL_NAME = "name";

        public static final String COLUMN_CHANNEL_URL = "url";

       public static final String COLUMN_CHANNEL_PICTURE = "picture";

        public static final String COLUMN_CHANNEL_CATEGORY_ID = "category_id";

        public static final String COLUMN_CHANNEL_FAVORITE = "is_favorite";

        public static final String DEFAULT_ORDER = TABLE_NAME + "." + COLUMN_CHANNEL_ID + ","
                + TABLE_NAME + "." + COLUMN_CHANNEL_NAME + ","
                + TABLE_NAME + "." + COLUMN_CHANNEL_URL;

        public static Uri buildChannelUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String[] FULL_PROJECTION = new String[]{
                COLUMN_CHANNEL_ID,
                COLUMN_CHANNEL_NAME,
                COLUMN_CHANNEL_URL,
                COLUMN_CHANNEL_PICTURE,
                COLUMN_CHANNEL_CATEGORY_ID,
                COLUMN_CHANNEL_FAVORITE};
        public static final String[] NAME_PROJECTION = new String[]{
                COLUMN_CHANNEL_NAME};
    }

    /* Inner class that defines the table contents of the listing table */
    public static final class ListingEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LISTING).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LISTING;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LISTING;

        // Table name
        public static final String TABLE_NAME = "listing";

         public static final String COLUMN_LISTING_ID = "_id";

        public static final String COLUMN_LISTING_DATE = "date";

        public static final String COLUMN_LISTING_TIME = "time";

        public static final String COLUMN_LISTING_TITLE = "title";

        public static final String COLUMN_LISTING_CHANNEL_ID = "channel_id";

        public static final String COLUMN_LISTING_DESCRIPTION = "description";

//        public static final String DEFAULT_ORDER = TABLE_NAME + "." + COLUMN_LISTING_CHANNEL_ID + ","
//                + TABLE_NAME + "." + COLUMN_LISTING_DATE + ","
//                + TABLE_NAME + "." + COLUMN_LISTING_TIME + ","
//                + TABLE_NAME + "." + COLUMN_LISTING_TITLE + ","
//                + TABLE_NAME + "." + COLUMN_LISTING_DESCRIPTION;

        public static Uri buildListingUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String[] FULL_PROJECTION = new String[]{
                COLUMN_LISTING_ID,
                COLUMN_LISTING_DATE,
                COLUMN_LISTING_TIME,
                COLUMN_LISTING_TITLE,
                COLUMN_LISTING_CHANNEL_ID,
                COLUMN_LISTING_DESCRIPTION};
    }


}


