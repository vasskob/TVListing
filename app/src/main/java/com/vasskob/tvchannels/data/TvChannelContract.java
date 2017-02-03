package com.vasskob.tvchannels.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

public class TvChannelContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.vasskob.tvchannels";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_CATEGORY = "category";
    public static final String PATH_CHANNEL = "channel";
    public static final String PATH_LISTING = "listing";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

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

        /*
             Student: This is the buildWeatherLocation function you filled in.
          */
//        public static Uri buildWeatherLocation(String locationSetting) {
//            return CONTENT_URI.buildUpon().appendPath(locationSetting).build();
//        }
//
//        public static Uri buildWeatherLocationWithStartDate(
//                String locationSetting, long startDate) {
//            long normalizedDate = normalizeDate(startDate);
//            return CONTENT_URI.buildUpon().appendPath(locationSetting)
//                    .appendQueryParameter(COLUMN_CHANNEL_NAME, Long.toString(normalizedDate)).build();
//        }
//
//        public static Uri buildWeatherLocationWithDate(String locationSetting, long date) {
//            return CONTENT_URI.buildUpon().appendPath(locationSetting)
//                    .appendPath(Long.toString(normalizeDate(date))).build();
//        }
//
//        public static String getLocationSettingFromUri(Uri uri) {
//            return uri.getPathSegments().get(1);
//        }
//
//        public static long getDateFromUri(Uri uri) {
//            return Long.parseLong(uri.getPathSegments().get(2));
//        }
//
//        public static long getStartDateFromUri(Uri uri) {
//            String dateString = uri.getQueryParameter(COLUMN_CHANNEL_NAME);
//            if (null != dateString && dateString.length() > 0)
//                return Long.parseLong(dateString);
//            else
//                return 0;
//        }
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

        public static final String DEFAULT_ORDER = TABLE_NAME + "." + COLUMN_LISTING_CHANNEL_ID + ","
                + TABLE_NAME + "." + COLUMN_LISTING_DATE + ","
                + TABLE_NAME + "." + COLUMN_LISTING_TIME + ","
                + TABLE_NAME + "." + COLUMN_LISTING_TITLE + ","
                + TABLE_NAME + "." + COLUMN_LISTING_DESCRIPTION;

        public static Uri buildListingUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


}


