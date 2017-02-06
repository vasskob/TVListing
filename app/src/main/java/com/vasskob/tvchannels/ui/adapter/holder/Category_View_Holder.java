package com.vasskob.tvchannels.ui.adapter.holder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vasskob.tvchannels.R;
import com.vasskob.tvchannels.ui.fragment.ChannelFragment;


public class Category_View_Holder extends RecyclerView.ViewHolder {


    private CardView catCv;
    public TextView catTitle;
    public ImageView catLogo;


    public Category_View_Holder(View itemView) {
        super(itemView);
        catCv = (CardView) itemView.findViewById(R.id.categories_card_view);
        catTitle = (TextView) itemView.findViewById(R.id.category_name);
        catLogo = (ImageView) itemView.findViewById(R.id.category_logo);

    }


}

