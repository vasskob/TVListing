package com.vasskob.tvchannels.ui.fragment;

import android.content.Context;
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
    private static final String ARG_POSITION_NUMBER = "position_number";
    private static final String PICKED_DATE = "picked_date";

    View rootView;
    RecyclerView rvCategory;

    private List<TvCategory> tvCategories;
    DbFunction dbFunction;
    Context context;


    public CategoryFragment() {
    }

    int position;
    String picked_date;


    @Override
    public void onAttach(Context context) {
      //  SharedPreferences sp = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        this.dbFunction = new DbFunction(context);
     //   this.picked_date = sp.getString(PICKED_DATE, null);
       // Toast.makeText(context, " Chared PREFFS is " + picked_date, Toast.LENGTH_LONG).show();
        super.onAttach(context);
    }

//    public static Fragment newInstance(int position, String picked_date) {
//        ListingFragment fragment = new ListingFragment();
//
//        Bundle args = new Bundle();
//     //   args.putString(PICKED_DATE, picked_date);
//      //  args.putInt(ARG_POSITION_NUMBER, position);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.container_layout, container, false);

        rvCategory = (RecyclerView) rootView.findViewById(R.id.container_rv);
      //  System.out.println(" RVcategory " + rvCategory + "!!!!!!!!!!!!");
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity()));


        executeMyTask();
        return rootView;
    }


    private class MyTask extends AsyncTask<Void, Void, List<TvCategory>> {


        public MyTask() {
            super();
        }

        @Override
        protected List<TvCategory> doInBackground(Void... params) {

            tvCategories = dbFunction.getChannelCategories();
          //  System.out.println("????>???" + tvCategories.size());
//            for (int i = 0; i < tvCategories.size(); i++) {
//                System.out.println("Category PICTURE " + tvCategories.get(i).getPicture() + " !!!!!!!!");
//            }
            return tvCategories;
        }

        @Override
        protected void onPostExecute(List<TvCategory> tvCategories) {
         //   System.out.println(">>>>>>>>>>>>>>>" + position + ">>>>>>>>>>");
            Category_R_V_Adapter adapter = new Category_R_V_Adapter(tvCategories, getActivity());
            rvCategory.setAdapter(adapter);
        }
    }

    private void executeMyTask() {
        new MyTask().execute();
    }
}