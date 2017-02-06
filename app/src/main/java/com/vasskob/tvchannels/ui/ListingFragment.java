package com.vasskob.tvchannels.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vasskob.tvchannels.R;
import com.vasskob.tvchannels.data.DbFunction;
import com.vasskob.tvchannels.model.TvListing;
import com.vasskob.tvchannels.ui.adapter.Recycler_View_Adapter;

import java.util.List;


public class ListingFragment extends Fragment {
    private static final String ARG_POSITION_NUMBER = "position_number";
    private static final String IS_SORTED = "is_sorted";
    private static final String PICKED_DATE = "picked_date";

    View rootView;
    List<TvListing> tvListing;
    DbFunction dbFunction;
    RecyclerView rv;

    public ListingFragment() {
    }

    int position;
    String picked_date;
    boolean isSorted;


    @Override
    public void onAttach(Context context) {
        SharedPreferences sp = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        this.dbFunction = new DbFunction(context);
        this.picked_date = sp.getString(PICKED_DATE, null);
        // Toast.makeText(context, " Chared PREFFS is " + picked_date, Toast.LENGTH_LONG).show();
        super.onAttach(context);
    }

    public static Fragment newInstance(int position, String picked_date, boolean isSorted) {
        ListingFragment fragment = new ListingFragment();

        Bundle args = new Bundle();
        args.putString(PICKED_DATE, picked_date);
        args.putInt(ARG_POSITION_NUMBER, position);
        args.putBoolean(IS_SORTED, isSorted);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.list_fragment, container, false);

        rv = (RecyclerView) rootView.findViewById(R.id.listing_r_view);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        position = this.getArguments().getInt(ARG_POSITION_NUMBER);

        executeMyTask();
        return rootView;
    }


    private class MyTask extends AsyncTask<Void, Void, List<TvListing>> {

        String pickedDate;

        public MyTask(String pickedDate) {
            super();
            this.pickedDate = pickedDate;
        }

        @Override
        protected List<TvListing> doInBackground(Void... params) {
            if (isSorted) {
                tvListing = dbFunction.getSortedListing(dbFunction.getChannelNames("name ASC").get(position),this.pickedDate);

            }
            else { tvListing = dbFunction.getChannelListing(String.valueOf(position), this.pickedDate);

            }
            System.out.println("????>???" + tvListing.size());
            for (int i = 0; i < tvListing.size(); i++) {
                System.out.println("????>???" + tvListing.get(i));
            }
            return tvListing;
        }

        @Override
        protected void onPostExecute(List<TvListing> tvListings) {
            System.out.println(">>>>>>>>>>>>>>>" + position + ">>>>>>>>>>" + this.pickedDate);
            Recycler_View_Adapter adapter = new Recycler_View_Adapter(tvListing, getActivity());
            rv.setAdapter(adapter);
        }
    }

    private void executeMyTask() {
        new MyTask(picked_date).execute();
    }
}