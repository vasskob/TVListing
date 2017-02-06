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
import com.vasskob.tvchannels.model.TvChannel;
import com.vasskob.tvchannels.ui.adapter.Channel_R_V_adapter;

import java.util.List;

import static com.vasskob.tvchannels.ui.adapter.Category_R_V_Adapter.ARG_CATEGORY_ID;


public class ChannelFragment extends Fragment {
    private static final String ARG_POSITION_NUMBER = "position_number";
    private static final String PICKED_DATE = "picked_date";
    private int categoryId;
    private Bundle bundle;

    View rootView;
    RecyclerView rvCategory;

    private List<TvChannel> tvChannels;

    DbFunction dbFunction;
    Context context;


    public ChannelFragment() {
    }

    int position;
    String picked_date;


    @Override
    public void onAttach(Context context) {
        SharedPreferences sp = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        this.dbFunction = new DbFunction(context);
        this.picked_date = sp.getString(PICKED_DATE, null);
       // Toast.makeText(context, " Chared PREFFS is " + picked_date, Toast.LENGTH_LONG).show();
        super.onAttach(context);
    }

    public static Fragment newInstance(int position, String picked_date) {
        ListingFragment fragment = new ListingFragment();

        Bundle args = new Bundle();
        args.putString(PICKED_DATE, picked_date);
        args.putInt(ARG_POSITION_NUMBER, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.container_layout, container, false);

        rvCategory = (RecyclerView) rootView.findViewById(R.id.container_rv);
        System.out.println(" RVcategory " + rvCategory + "!!!!!!!!!!!!");
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity()));

        bundle = this.getArguments();
        if (bundle != null) {
            categoryId = bundle.getInt(ARG_CATEGORY_ID);
        }
        executeMyTask(categoryId);
        return rootView;
    }


    private class MyTask extends AsyncTask<Integer, Void, List<TvChannel>> {


        public MyTask() {
            super();
        }

        @Override
        protected List<TvChannel> doInBackground(Integer... params) {

            if (params[0] == 0) {
                tvChannels = dbFunction.getChannels();
            }
            else {
                tvChannels = dbFunction.getChannelsOfCategory(params[0]);
            }

            System.out.println("????>???" + tvChannels.size());
            for (int i = 0; i < tvChannels.size(); i++) {
                System.out.println("Channel PICTURE " + tvChannels.get(i).getPicture() + " !!!!!!!!");
            }
            return tvChannels;
        }

        @Override
        protected void onPostExecute(List<TvChannel> tvCategories) {
            System.out.println(">>>>>>>>>>>>>>>" + position + ">>>>>>>>>>");
            Channel_R_V_adapter adapter = new Channel_R_V_adapter(tvChannels, getActivity());
            rvCategory.setAdapter(adapter);
        }
    }

    private void executeMyTask(int categoryId) {
        new MyTask().execute(categoryId);
    }
}