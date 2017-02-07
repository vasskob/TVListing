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
import com.vasskob.tvchannels.ui.adapter.Channel_R_V_adapter;

import java.util.List;

import static com.vasskob.tvchannels.Constants.ARGUMENTS_CATEGORY_ID;


public class ChannelFragment extends Fragment {

    private int categoryId;

    private RecyclerView rvCategory;
    private List<TvChannel> tvChannels;
    private DbFunction dbFunction;



    public ChannelFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.container_layout, container, false);
        rvCategory = (RecyclerView) rootView.findViewById(R.id.container_rv);
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
        dbFunction = new DbFunction(getContext());
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            categoryId = bundle.getInt(ARGUMENTS_CATEGORY_ID);
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
            } else {
                tvChannels = dbFunction.getChannelsOfCategory(params[0]);
            }

            return tvChannels;
        }

        @Override
        protected void onPostExecute(List<TvChannel> tvCategories) {
            Channel_R_V_adapter adapter = new Channel_R_V_adapter(tvChannels, getActivity());
            rvCategory.setAdapter(adapter);
        }
    }

    private void executeMyTask(int categoryId) {
        new MyTask().execute(categoryId);
    }
}
