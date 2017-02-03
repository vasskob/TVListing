package com.vasskob.tvchannels;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.vasskob.tvchannels.data.TvChannelContract;
import com.vasskob.tvchannels.model.TvCategory;
import com.vasskob.tvchannels.model.TvChannel;
import com.vasskob.tvchannels.model.TvListing;
import com.vasskob.tvchannels.service.ApiManager;
import com.vasskob.tvchannels.ui.AboutActivity;
import com.vasskob.tvchannels.ui.ListingFragment;
import com.vasskob.tvchannels.ui.adapter.Recycler_View_Adapter;
import com.vasskob.tvchannels.ui.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

public class MainActivity extends AppCompatActivity {
    //
//    private ProgressDialog progressDialog;
//    final ListingFragment fragment = new ListingFragment();
    List<TvChannel> tvChannels = new ArrayList<>();
    List<TvCategory> tvCategories = new ArrayList<>();
    List<TvListing> tvListings = new ArrayList<>();

    //save our header or result
    private Drawer result = null;
//
//    private int mRange;
//    private List<Fragment> mFragmentList = new ArrayList<>();
//    TabLayout mTab;
//    ViewPager mVp;
//    private VpAdapter mVpAdapter;
//
//    // LinearLayout mLayout;
//    String[] tabsName = new String[10];

    private Toolbar toolbar;
    private SmartTabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View logo = View.inflate(this, R.layout.castom_toolbar, null);
        ImageView calendarView = (ImageView) logo.findViewById(R.id.custom_view);
        calendarView.setImageDrawable(getResources().getDrawable(getIconId()));
        calendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        toolbar.addView(logo);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (SmartTabLayout) findViewById(R.id.tabs);
        tabLayout.setViewPager(viewPager);




        //Create the drawer
        result = new DrawerBuilder(this)
                //this layout have to contain child layouts
                .withRootView(R.id.drawer_container)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(false)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.app_name).withIcon(FontAwesome.Icon.faw_newspaper_o).withIdentifier(1).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.categories).withIcon(FontAwesome.Icon.faw_clone).withIdentifier(2).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.channels).withIcon(FontAwesome.Icon.faw_television).withIdentifier(3).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.favorites).withIcon(FontAwesome.Icon.faw_star_o).withIdentifier(4).withSelectable(true),
                        new SectionDrawerItem().withName(R.string.other).withIdentifier(5).withSelectable(false),
                        new SecondaryDrawerItem().withName(R.string.update).withIcon(FontAwesome.Icon.faw_download).withIdentifier(6).withSelectable(true),
                        new SecondaryDrawerItem().withName(R.string.app_github).withIcon(FontAwesome.Icon.faw_github).withIdentifier(7).withSelectable(true),
                        new SecondaryDrawerItem().withName(R.string.about).withIcon(FontAwesome.Icon.faw_question).withIdentifier(8).withSelectable(true)
                ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem != null) {
                            Intent intent = null;
                            long id = drawerItem.getIdentifier();
                            if (id == 1) {
                                intent = new Intent(MainActivity.this, AboutActivity.class);
                            } else if (id == 2) {
                                intent = new Intent(MainActivity.this, AboutActivity.class);
                            } else if (id == 3) {
                                intent = new Intent(MainActivity.this, AboutActivity.class);
                            } else if (id == 4) {
                                intent = new Intent(MainActivity.this, AboutActivity.class);
                            } else if (id == 6) {
                                intent = new Intent(MainActivity.this, AboutActivity.class);
                            } else if (id == 7) {
                                openGitHub();
                            } else if (id == 8) {
                                intent = new Intent(MainActivity.this, AboutActivity.class);
                            }
                            if (intent != null) {
                                MainActivity.this.startActivity(intent);
                            }
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();


        ApiManager.getApiService(Constant.URL_CATEGORIES).getCategoryInfo().
                enqueue(new Callback<List<TvCategory>>() {
                            @Override
                            public void onResponse
                                    (Call<List<TvCategory>> call, Response<List<TvCategory>> response) {
                                System.out.println("onRESPONSE!!!!");
                                if (response.body() != null) {
                                    tvCategories.addAll(response.body());
//                            for (int i = 0; i < tvCategories.size(); i++) {
//                                System.out.println(tvCategories.get(i));
//                            }
                                    if (tvCategories.size() > 0) {
                                        //  insertListCategories(tvCategories);
                                    }
                                } else {
                                    System.out.println("RESPONSE IS NULL " + response.errorBody().toString());
                                }

                                //  recyclerView.getAdapter().notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(Call<List<TvCategory>> call, Throwable t) {
                                System.out.println("onFAILUREEEEEEE!!!");
                            }

                        }

                );


        ApiManager.getApiService(Constant.URL_CHANNELS).getChannelInfo().
                enqueue(new Callback<List<TvChannel>>() {
                            @Override
                            public void onResponse
                                    (Call<List<TvChannel>> call, Response<List<TvChannel>> response) {
                                System.out.println("onRESPONSE!!!!");
                                if (response.body() != null) {
                                    tvChannels.addAll(response.body());

//                                    for (int i = 0; i < tvChannels.size(); i++) {
//                                        //System.out.println(tvChannels.get(i));
//
//                                        //tabsName[i] = tvChannels.get(i).getName();
//                                    }
                                    if (tvChannels.size() > 0) {
                                        //   insertListChannels(tvChannels);

                                    } else {
                                        Toast.makeText(getApplicationContext(), "DATABASE IS NOT created", Toast.LENGTH_LONG).show();
                                    }

                                } else {
                                    System.out.println("RESPONSE IS NULL " + response.errorBody().toString());
                                }

                                //  recyclerView.getAdapter().notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(Call<List<TvChannel>> call, Throwable t) {
                                System.out.println("onFAILUREEEEEEE!!!");
                            }

                        }

                );


        ApiManager.getApiService(Constant.URL_LISTINGS + System.currentTimeMillis() + "/").
                getListingInfo("").
                enqueue(new Callback<List<TvListing>>() {
                            @Override
                            public void onResponse
                                    (Call<List<TvListing>> call, Response<List<TvListing>> response) {
                                System.out.println("onRESPONSE!!!!");
                                if (response.body() != null) {
                                    tvListings.addAll(response.body());

                                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listing_r_view);
                                    Recycler_View_Adapter adapter = new Recycler_View_Adapter(tvListings, getApplication());
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

//                            for (int i = 0; i < tvListings.size(); i++) {
//                                System.out.println(tvListings.get(i));
//                            }
                                    if (tvListings.size() > 0) {
                                        //  insertListListings(tvListings);
                                    }
                                } else {
                                    System.out.println("RESPONSE IS NULL " + response.errorBody().toString());
                                }

                                //  recyclerView.getAdapter().notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(Call<List<TvListing>> call, Throwable t) {
                                System.out.println("onFAILUREEEEEEE!!!");
                            }

                        }

                );

    }

    public void insertListChannels(List<TvChannel> channels) {
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
        getContentResolver().bulkInsert(TvChannelContract.ChannelEntry.CONTENT_URI, valuesArray);

    }

    public void insertListCategories(List<TvCategory> categories) {
        ContentValues[] valuesArray = new ContentValues[categories.size()];
        for (int i = 0; i < categories.size(); i++) {
            TvCategory category = categories.get(i);
            ContentValues values = new ContentValues();
            values.put(COLUMN_CATEGORY_ID, category.getId());
            values.put(COLUMN_CATEGORY_TITLE, category.getTitle());
            values.put(COLUMN_CATEGORY_PICTURE, category.getPicture());
            valuesArray[i] = values;
        }
        getContentResolver().bulkInsert(TvChannelContract.CategoryEntry.CONTENT_URI, valuesArray);

    }

    public void insertListListings(List<TvListing> listings) {
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
        getContentResolver().bulkInsert(TvChannelContract.ListingEntry.CONTENT_URI, valuesArray);

    }


    private int getIconId() {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        return getResources().getIdentifier("calendar_" + day, "drawable", this.getPackageName());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }


    private void openGitHub() {
        Uri uri = Uri.parse(getString(R.string.app_github_url));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        this.startActivity(intent);
    }

//    private void setFragment(Fragment fragment) {
//        FragmentManager mFragmentManager = getSupportFragmentManager();
//        Fragment mFragment = mFragmentManager.findFragmentById(R.id.fragment_container);
//        if (mFragment == null) {
//            mFragmentManager.beginTransaction()
//                    .add(R.id.fragment_container, fragment)
//                    .commit();
//        } else {
//            mFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, fragment)
//                    .commit();
//        }
//    }


    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        int count = 10;
        for (int i = 0; i < count; i++) {

            ListingFragment fView = new ListingFragment();
            adapter.addFrag(fView, "TAB " + i);
        }


        viewPager.setAdapter(adapter);
    }

}

