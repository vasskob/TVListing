package com.vasskob.tvchannels;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
import com.vasskob.tvchannels.data.DbFunction;
import com.vasskob.tvchannels.model.TvListing;
import com.vasskob.tvchannels.ui.AboutActivity;
import com.vasskob.tvchannels.ui.ListingFragment;
import com.vasskob.tvchannels.ui.adapter.ViewPagerAdapter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    //
//    private ProgressDialog progressDialog;
//    final ListingFragment fragment = new ListingFragment();

    //save our header or result
    private Drawer result = null;
    private Toolbar toolbar;
    private SmartTabLayout tabLayout;
    private ViewPager viewPager;
    DatePickerDialog dpd;
    DbFunction dbFunction = new DbFunction(this);
    List<String> tabTitles;
    List<String> newTabTitles;
    List<TvListing> tvListing;
    ListingFragment fView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Calendar lastDay = Calendar.getInstance();
        lastDay.add(Calendar.DAY_OF_MONTH, lastDay.getActualMaximum(Calendar.DAY_OF_MONTH) - lastDay.get(Calendar.DAY_OF_MONTH));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View logo = View.inflate(this, R.layout.castom_toolbar, null);
        ImageView calendarView = (ImageView) logo.findViewById(R.id.custom_view);
        calendarView.setImageDrawable(getResources().getDrawable(getIconId()));
        calendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar now = Calendar.getInstance();
                dpd = DatePickerDialog.newInstance(
                        MainActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.showYearPickerFirst(false);
                dpd.setMinDate(now);
                dpd.setMaxDate(lastDay);
                dpd.setTitle("Select Date for Tv Listing");
                dpd.show(getFragmentManager(), "Datepickerdialog");

            }
        });
        toolbar.addView(logo);

        tabTitles = dbFunction.getChannelNames(null);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager, tabTitles);
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
                        new SecondaryDrawerItem().withName(R.string.sort).withIcon(FontAwesome.Icon.faw_sort).withIdentifier(6).withSelectable(true),
                        new SecondaryDrawerItem().withName(R.string.update).withIcon(FontAwesome.Icon.faw_download).withIdentifier(7).withSelectable(true),
                        new SecondaryDrawerItem().withName(R.string.app_github).withIcon(FontAwesome.Icon.faw_github).withIdentifier(8).withSelectable(true),
                        new SecondaryDrawerItem().withName(R.string.about).withIcon(FontAwesome.Icon.faw_question).withIdentifier(9).withSelectable(true)
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
                                rerunSortedFragment();
                            } else if (id == 7) {
                                intent = new Intent(MainActivity.this, AboutActivity.class);
                            } else if (id == 8) {
                                openGitHub();
                            } else if (id == 9) {
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


    }

    private void rerunSortedFragment() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
//        fView = null;
//        fView = new ListingFragment();
        newTabTitles = dbFunction.getChannelNames("name ASC");
        setupViewPager(viewPager, newTabTitles);
        tabLayout.setViewPager(viewPager);

//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listing_r_view);
////        for (int i = 0; i < newTabTitles.size(); i++) {
//
//            tvListing = dbFunction.getChannelListing(newTabTitles.get(0), mFormat.format(cal.getTime()));
//            Recycler_View_Adapter adapter = new Recycler_View_Adapter(tvListing, getApplicationContext());
//            recyclerView.setAdapter(adapter);
//            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

//            //cal.add();
//        }

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


    private void setupViewPager(ViewPager viewPager, List<String> titles) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        for (int i = 0; i < titles.size(); i++) {
            fView = new ListingFragment();
            adapter.addFrag(fView, titles.get(i));
        }


        viewPager.setAdapter(adapter);
        
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = "Selected Date : " + dayOfMonth + "-" + (monthOfYear + 1);

        Toast.makeText(MainActivity.this, date, Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        rerunSortedFragment();
    }
}

