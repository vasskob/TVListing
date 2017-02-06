package com.vasskob.tvchannels.ui.adapter.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.vasskob.tvchannels.R;


public class Channel_View_Holder extends RecyclerView.ViewHolder {


    private CardView channelCv;
    public TextView channelName;
    public TextView channelCategory;
    public TextView channelUrl;
    public ImageView channelLogo;
    public MaterialFavoriteButton favoriteButton;

    public Channel_View_Holder(View itemView) {
        super(itemView);
        channelCv = (CardView) itemView.findViewById(R.id.channel_card_view);
        channelName = (TextView) itemView.findViewById(R.id.channel_name);
        channelCategory = (TextView) itemView.findViewById(R.id.channel_category_name);
        channelUrl = (TextView) itemView.findViewById(R.id.channel_url);
        channelLogo = (ImageView) itemView.findViewById(R.id.category_logo);
        favoriteButton = (MaterialFavoriteButton) itemView.findViewById(R.id.favoriteButton);

    }


}