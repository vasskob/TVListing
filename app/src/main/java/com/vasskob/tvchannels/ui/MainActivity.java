package com.vasskob.tvchannels.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.vasskob.tvchannels.R;
import com.vasskob.tvchannels.data.DataLoader;
import com.vasskob.tvchannels.data.DbFunction;
import com.vasskob.tvchannels.ui.adapter.ViewPagerAdapter;
import com.vasskob.tvchannels.ui.fragment.CategoryFragment;
import com.vasskob.tvchannels.ui.fragment.ChannelFragment;
import com.vasskob.tvchannels.ui.fragment.FavoriteChFragment;
import com.vasskob.tvchannels.ui.fragment.ListingFragment;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Drawer result = null;
    private SmartTabLayout tabLayout;
    private ViewPager viewPager;
    private DatePickerDialog dpd;
    @SuppressWarnings("WeakerAccess")
    private final
    DbFunction dbFunction = new DbFunction(this);
    private List<String> tabTitles;
    private SharedPreferences shPr;
    private ImageView calendarView;
    private TextView customTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Calendar currentDate = Calendar.getInstance();
        final Calendar lastDay = Calendar.getInstance();
        //int dayOfMonth = lastDay.get(Calendar.DAY_OF_MONTH);
        lastDay.add(Calendar.DAY_OF_MONTH, lastDay.getActualMaximum(Calendar.DAY_OF_MONTH) - lastDay.get(Calendar.DAY_OF_MONTH));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        String date = sdf.format(new Date());

        shPr = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        if (shPr.getString("picked_date", null) == null) {
            SharedPreferences.Editor editor = shPr.edit();
            editor.putString("picked_date", date);
            if (shPr.getInt("date_for_calendar_icon", 0) == 0)
                editor.putInt("date_for_calendar_icon", currentDate.get(Calendar.DAY_OF_MONTH));
            editor.apply();
        }


        // Custom toolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View customToolbar = View.inflate(this, R.layout.castom_toolbar, null);
        customTitle = (TextView) customToolbar.findViewById(R.id.custom_title_text);
        toolbar.addView(customToolbar);


        tabTitles = dbFunction.getChannelNames(null);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager, tabTitles);
        tabLayout = (SmartTabLayout) findViewById(R.id.tabs);
        tabLayout.setViewPager(viewPager);


        //Calendar picker
        calendarView = (ImageView) customToolbar.findViewById(R.id.custom_view);

        System.out.println(" CALENDAR ICON " + shPr.getInt("date_for_calendar_icon", 0));
        calendarView.setImageResource(getIconId(shPr.getInt("date_for_calendar_icon", 0)));
        //calendarView.setImageResource(getIconId(dayOfMonth));
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
                dpd.setFirstDayOfWeek(2);
                dpd.showYearPickerFirst(false);
                dpd.setMinDate(now);
                dpd.setMaxDate(lastDay);
                dpd.setTitle("Select Date for Tv Listing");
                dpd.show(getFragmentManager(), "Datepickerdialog");

            }
        });


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
                        new PrimaryDrawerItem().withName(R.string.channels).withIcon(FontAwesome.Icon.faw_television).withIdentifier(3).withSelectable(true),
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
                                runListingFragment();
                            } else if (id == 2) {
                                runCategoryFragment();
                            } else if (id == 3) {
                                runChannelFragment();
                            } else if (id == 4) {
                                runFavoriteChFragment();
                            } else if (id == 6) {
                                sortListing();
                            } else if (id == 7) {
                                DataLoader dataLoader = new DataLoader(getApplicationContext());
                                dataLoader.manualUpdate();
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

    private void sortListing() {

        List<String> newTabTitles = dbFunction.getChannelNames("name ASC");
        setupViewPager(viewPager, newTabTitles);
        tabLayout.setViewPager(viewPager);
    }


    private void setupViewPager(ViewPager viewPager, List<String> titles) {

        boolean isSorted = false;
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), this, isSorted);

        for (int i = 0; i < titles.size(); i++) {
            ListingFragment listingFragment = new ListingFragment();
            adapter.addFrag(listingFragment, titles.get(i));
        }
        viewPager.setAdapter(adapter);
    }

    private void runListingFragment() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // RUN Fragments
    private void runCategoryFragment() {
        CategoryFragment categoryFragment = new CategoryFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame_container, categoryFragment);
        transaction.commit();
        customTitle.setText(getResources().getString(R.string.category));

    }

    private void runChannelFragment() {
        ChannelFragment channelFragment = new ChannelFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame_container, channelFragment);
        transaction.commit();
        customTitle.setText(getResources().getString(R.string.channels));
    }

    private void runFavoriteChFragment() {
        FavoriteChFragment favoriteChFragment = new FavoriteChFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame_container, favoriteChFragment);
        transaction.commit();
        customTitle.setText(getResources().getString(R.string.favorites));
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date;
        String day;
        String month;
        // Need to convert date in /dd/MM/yyyy format, now we have /d/m-1/yyyy
        if (dayOfMonth < 10) {
            day = "0" + dayOfMonth;
        } else {
            day = String.valueOf(dayOfMonth);
        }
        if (monthOfYear < 9) {
            month = "0" + (monthOfYear + 1);
        } else {
            month = String.valueOf(monthOfYear+1);
        }

        date = day + "/" + month + "/" + year;
        calendarView.setImageResource(getIconId(dayOfMonth));

        SharedPreferences.Editor editor = shPr.edit();
        editor.putString("picked_date", date);
        editor.putInt("date_for_calendar_icon", dayOfMonth);
        editor.apply();

        setupViewPager(viewPager, tabTitles);
    }

    private void openGitHub() {
        Uri uri = Uri.parse(getString(R.string.app_github_url));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        this.startActivity(intent);
    }

    private int getIconId(int dayOfMonth) {

        return getResources().getIdentifier("calendar_" + dayOfMonth, "drawable", this.getPackageName());
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
            LoadingActivity.close = true;
            finish();
        }

    }


}

