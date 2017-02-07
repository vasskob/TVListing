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
import com.vasskob.tvchannels.model.TvCategory;
import com.vasskob.tvchannels.ui.adapter.Category_R_V_Adapter;

import java.util.List;


public class CategoryFragment extends Fragment {


    private RecyclerView rvCategory;

    private DbFunction dbFunction;


    public CategoryFragment() {
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


    private class MyTask extends AsyncTask<Void, Void, List<TvCategory>> {


        public MyTask() {
            super();
        }

        @Override
        protected List<TvCategory> doInBackground(Void... params) {

            return dbFunction.getChannelCategories();
        }

        @Override
        protected void onPostExecute(List<TvCategory> tvCategories) {
            Category_R_V_Adapter adapter = new Category_R_V_Adapter(tvCategories, getActivity());
            rvCategory.setAdapter(adapter);
        }
    }

    private void executeMyTask() {
        new MyTask().execute();
    }
}