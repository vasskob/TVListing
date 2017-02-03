package com.vasskob.tvchannels.ui.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.vasskob.tvchannels.R;


public class View_Holder extends RecyclerView.ViewHolder {

    CardView cv;
    TextView date;
    TextView time;
    TextView title;
    TextView description;


    View_Holder(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cardView);
        date = (TextView) itemView.findViewById(R.id.listing_date);
        time = (TextView) itemView.findViewById(R.id.listing_time);
        title = (TextView) itemView.findViewById(R.id.listing_title);
        description = (TextView) itemView.findViewById(R.id.listing_description);

    }
}
