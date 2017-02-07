package com.vasskob.tvchannels.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vasskob.tvchannels.ui.fragment.ListingFragment;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final Context context;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private final boolean isSorted;

    public ViewPagerAdapter(FragmentManager manager, Context context, boolean isSorted) {
        super(manager);
        this.context = context;
        this.isSorted = isSorted;
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return ListingFragment.newInstance(position + 1, prefs.getString("picked_date", null), isSorted);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

}