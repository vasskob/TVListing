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

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
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


        // Table name
        public static final String TABLE_NAME = "category";

        // The location setting string is what will be sent to openweathermap
        // as the location query.
        public static final String COLUMN_CATEGORY_ID = "_id";

        // Human readable location string, provided by the API.  Because for styling,
        // "Mountain View" is more recognizable than 94043.
        public static final String COLUMN_CATEGORY_TITLE = "title";

        // In order to uniquely pinpoint the location on the map when we launch the
        // map intent, we store the latitude and longitude as returned by openweathermap.
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

    /* Inner class that defines the table contents of the weather table */
    public static final class ChannelEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CHANNEL).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CHANNEL;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CHANNEL;

        public static final String TABLE_NAME = "channel";

        // Column with the foreign key into the location table.
        public static final String COLUMN_CHANNEL_ID = "_id";
        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_CHANNEL_NAME = "name";
        // Weather id as returned by API, to identify the icon to be used
        public static final String COLUMN_CHANNEL_URL = "url";

        // Short description and long description of the weather, as provided by API.
        public static final String COLUMN_CHANNEL_PICTURE = "picture";

        // Min and max temperatures for the day (stored as floats)
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

    /* Inner class that defines the table contents of the location table */
    public static final class ListingEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LISTING).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LISTING;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LISTING;

        // Table name
        public static final String TABLE_NAME = "listing";

        // The location setting string is what will be sent to openweathermap
        // as the location query.
        public static final String COLUMN_LISTING_ID = "_id";

        // Human readable location string, provided by the API.  Because for styling,
        // "Mountain View" is more recognizable than 94043.
        public static final String COLUMN_LISTING_DATE = "date";
        // Human readable location string, provided by the API.  Because for styling,
        // "Mountain View" is more recognizable than 94043.
        public static final String COLUMN_LISTING_TIME = "time";
        // Human readable location string, provided by the API.  Because for styling,
        // "Mountain View" is more recognizable than 94043.
        public static final String COLUMN_LISTING_TITLE = "title";

        public static final String COLUMN_LISTING_CHANNEL_ID = "channel_id";

        // In order to uniquely pinpoint the location on the map when we launch the
        // map intent, we store the latitude and longitude as returned by openweathermap.
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


