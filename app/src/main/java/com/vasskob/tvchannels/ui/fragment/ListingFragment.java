package com.vasskob.tvchannels.ui.fragment;

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
import com.vasskob.tvchannels.ui.adapter.Listing_R_V_Adapter;

import java.util.List;

import static com.vasskob.tvchannels.Constants.ARG_POSITION_NUMBER;
import static com.vasskob.tvchannels.Constants.IS_SORTED;
import static com.vasskob.tvchannels.Constants.PICKED_DATE;


public class ListingFragment extends Fragment {


    private List<TvListing> tvListing;
    private DbFunction dbFunction;
    private RecyclerView rv;

    public ListingFragment() {
    }

    private int position;
    private String picked_date;
    boolean isSorted;


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
        View rootView = inflater.inflate(R.layout.container_layout, container, false);

        rv = (RecyclerView) rootView.findViewById(R.id.container_rv);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        position = this.getArguments().getInt(ARG_POSITION_NUMBER);

        SharedPreferences sp = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        this.dbFunction = new DbFunction(getContext());
        this.picked_date = sp.getString(PICKED_DATE, null);
        executeMyTask();
        return rootView;
    }


    private class MyTask extends AsyncTask<Void, Void, List<TvListing>> {

        final String pickedDate;

        public MyTask(String pickedDate) {
            super();
            this.pickedDate = pickedDate;
        }

        @Override
        protected List<TvListing> doInBackground(Void... params) {

            tvListing = dbFunction.getChannelListing(String.valueOf(position), this.pickedDate);

            return tvListing;
        }

        @Override
        protected void onPostExecute(List<TvListing> tvListings) {
            Listing_R_V_Adapter adapter = new Listing_R_V_Adapter(tvListing, getActivity());
            rv.setAdapter(adapter);
        }
    }

    private void executeMyTask() {
        new MyTask(picked_date).execute();
    }
}