package com.vasskob.tvchannels.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vasskob.tvchannels.R;
import com.vasskob.tvchannels.model.TvListing;
import com.vasskob.tvchannels.ui.adapter.holder.Listing_View_Holder;

import java.util.Collections;
import java.util.List;


public class Listing_R_V_Adapter extends RecyclerView.Adapter<Listing_View_Holder> {

    private List<TvListing> list = Collections.emptyList();

    public Listing_R_V_Adapter(List<TvListing> list, Context context) {
        this.list = list;
        Context context1 = context;
    }


    @Override
    public Listing_View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listing_layout, parent, false);
        return new Listing_View_Holder(v);

    }

    @Override
    public void onBindViewHolder(Listing_View_Holder holder, int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.time.setText(list.get(position).getTime());
        holder.date.setText(list.get(position).getDate());
        holder.title.setText(list.get(position).getTitle());
        holder.description.setText(list.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }


}