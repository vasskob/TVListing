package com.vasskob.tvchannels.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.squareup.picasso.Picasso;
import com.vasskob.tvchannels.R;
import com.vasskob.tvchannels.data.DbFunction;
import com.vasskob.tvchannels.model.TvChannel;
import com.vasskob.tvchannels.ui.adapter.holder.Channel_View_Holder;

import java.util.Collections;
import java.util.List;


public class Fav_Channel_R_V_adapter extends RecyclerView.Adapter<Channel_View_Holder> {

    List<TvChannel> tvChannels = Collections.emptyList();
    Context context;
    DbFunction dbFunction;
    boolean isFavorite;

    boolean[] checkBoxState;


    public Fav_Channel_R_V_adapter(List<TvChannel> tvChannels, Context context) {
        this.tvChannels = tvChannels;
        this.context = context;
        checkBoxState = new boolean[tvChannels.size()];
        this.dbFunction = new DbFunction(context);
    }


    @Override
    public Channel_View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_layout, parent, false);
        return new Channel_View_Holder(v);

    }

    @Override
    public void onBindViewHolder(final Channel_View_Holder holder, final int position) {
//
//  if (tvChannels.isEmpty()) {
//            Toast.makeText(context, " Favorite list is empty" + tvChannels.toString(), Toast.LENGTH_LONG).show();
//        }

        holder.channelName.setText(tvChannels.get(position).getName());
        holder.channelCategory.setText(tvChannels.get(position).getCategory_name());
        holder.channelUrl.setText(tvChannels.get(position).getUrl());
//
        if (tvChannels.get(position).getIsFavorite() == 1) {
            isFavorite = true;
        } else {
            isFavorite = false;
        }


        holder.favoriteButton.setFavorite(isFavorite, false);

        Picasso.with(context)
                .load((tvChannels.get(position).getPicture()))
                .placeholder(R.drawable.channel_pic)
                .into(holder.channelLogo);
        System.out.println("PICTURE URL " + tvChannels.get(position).getPicture() + "!@!!!!!!!!!");

        holder.favoriteButton.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                if (favorite) {
                    dbFunction.markAsFavoriteInDb(tvChannels.get(position).getName(), 1);

                 } else {
//
                    dbFunction.markAsFavoriteInDb(tvChannels.get(position).getName(), 0);
                    remove(tvChannels.get(holder.getAdapterPosition()));
                    notifyItemRemoved(holder.getAdapterPosition());
                    notifyItemRangeChanged(holder.getAdapterPosition(), tvChannels.size());

                }

            }
        });
    }


    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return tvChannels.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(TvChannel data) {
        int position = tvChannels.indexOf(data);
        tvChannels.remove(position);
        notifyItemRemoved(position);
    }

}