package com.vasskob.tvchannels.ui.fragment;


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
import com.vasskob.tvchannels.ui.adapter.Fav_Channel_R_V_adapter;

import java.util.List;

public class FavoriteChFragment extends Fragment {

    private RecyclerView rvCategory;
    private List<TvChannel> favoriteTvChannels;
    private DbFunction dbFunction;

    public FavoriteChFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.container_layout, container, false);

        rvCategory = (RecyclerView) rootView.findViewById(R.id.container_rv);
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
        dbFunction = new DbFunction(getContext());
        executeMyTask();
        return rootView;
    }


    private class MyTask extends AsyncTask<Void, Void, List<TvChannel>> {


        public MyTask() {
            super();
        }

        @Override
        protected List<TvChannel> doInBackground(Void... params) {
            favoriteTvChannels = dbFunction.getFavoriteChannels();
            return favoriteTvChannels;
        }

        @Override
        protected void onPostExecute(List<TvChannel> tvChannels) {
            Fav_Channel_R_V_adapter adapter = new Fav_Channel_R_V_adapter(favoriteTvChannels, getActivity());
            rvCategory.setAdapter(adapter);

        }
    }

    private void executeMyTask() {
        new MyTask().execute();
    }
}

