package com.vasskob.tvchannels.ui.adapter.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vasskob.tvchannels.R;


public class Listing_View_Holder extends RecyclerView.ViewHolder {

    private CardView cv;
    public TextView date;
    public TextView time;
    public  TextView title;
    public  TextView description;
//    CardView catCv;
//    TextView catTitle;
//    TextView catPicture;


    public Listing_View_Holder(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cardView);
        date = (TextView) itemView.findViewById(R.id.listing_date);
        time = (TextView) itemView.findViewById(R.id.listing_time);
        title = (TextView) itemView.findViewById(R.id.listing_title);
        description = (TextView) itemView.findViewById(R.id.listing_description);

//        cv = (CardView) itemView.findViewById(R.id.cardView);
//        date = (TextView) itemView.findViewById(R.id.listing_date);
//        time = (TextView) itemView.findViewById(R.id.listing_time);

    }
}
