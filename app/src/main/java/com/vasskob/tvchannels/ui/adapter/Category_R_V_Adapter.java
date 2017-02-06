package com.vasskob.tvchannels.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.vasskob.tvchannels.R;
import com.vasskob.tvchannels.model.TvCategory;
import com.vasskob.tvchannels.ui.adapter.holder.Category_View_Holder;
import com.vasskob.tvchannels.ui.fragment.ChannelFragment;

import java.util.Collections;
import java.util.List;


public class Category_R_V_Adapter extends RecyclerView.Adapter<Category_View_Holder> {

    List<TvCategory> tvCategoryList = Collections.emptyList();
    Context context;

    public static final String ARG_CATEGORY_ID = "categoryId";

    public Category_R_V_Adapter(List<TvCategory> tvCategoryList, Context context) {
        this.tvCategoryList = tvCategoryList;
        this.context = context;
    }


    @Override
    public Category_View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout, parent, false);
        return new Category_View_Holder(v);

    }

    @Override
    public void onBindViewHolder(Category_View_Holder holder, final int position) {

        holder.catTitle.setText(tvCategoryList.get(position).getTitle());
        Picasso.with(context)
                .load((tvCategoryList.get(position).getPicture()))
                .fit()
                .centerCrop()
                .placeholder(R.drawable.muz_pic)
                .into(holder.catLogo);
        System.out.println("PICTURE URL " + tvCategoryList.get(position).getPicture() + " !@!!!!!!!!!");

        holder.catTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ChannelFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(ARG_CATEGORY_ID, tvCategoryList.get(position).getId());
                fragment.setArguments(bundle);


                FragmentManager mFragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                mFragmentManager.popBackStack();
                Fragment mFragment = mFragmentManager.findFragmentById(R.id.frame_container);
                if (mFragment == null) {
                    mFragmentManager.beginTransaction()
                            .add(R.id.frame_container, fragment)
                            .commit();
                } else {
                    mFragmentManager.beginTransaction()
                            .replace(R.id.frame_container, fragment)
                            .commit();

                }
            }
        });

    }


    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return tvCategoryList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}