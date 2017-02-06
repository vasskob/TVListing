package com.vasskob.tvchannels.ui.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vasskob.tvchannels.R;


public class Category_View_Holder extends RecyclerView.ViewHolder {


     CardView catCv;
     TextView catTitle;
     ImageView catLogo;


    Category_View_Holder(View itemView) {
        super(itemView);
        catCv = (CardView) itemView.findViewById(R.id.categories_card_view);
        catTitle = (TextView) itemView.findViewById(R.id.category_name);
        catLogo = (ImageView) itemView.findViewById(R.id.category_logo);

    }
}

