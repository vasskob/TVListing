package com.vasskob.tvchannels.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.vasskob.tvchannels.R;
import com.vasskob.tvchannels.model.TvCategory;
import com.vasskob.tvchannels.model.TvListing;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;


public class Category_R_V_Adapter extends RecyclerView.Adapter<Category_View_Holder> {

    List<TvCategory> tvCategoryList = Collections.emptyList();
    Context context;

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
    public void onBindViewHolder(Category_View_Holder holder, int position) {

        //  Bitmap logo = loadImageFromServer((tvCategoryList.get(position).getPicture()));
        //  holder.catLogo.setImageBitmap(logo);
        holder.catTitle.setText(tvCategoryList.get(position).getTitle());

        Picasso.with(context)
                .load((tvCategoryList.get(position).getPicture()))
                .fit()
                .centerCrop()
                .placeholder(R.drawable.muz_pic)
                .into(holder.catLogo);
        System.out.println("PICTURE URL " + tvCategoryList.get(position).getPicture() + "!@!!!!!!!!!");

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

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, TvCategory data) {
        tvCategoryList.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(TvListing data) {
        int position = tvCategoryList.indexOf(data);
        tvCategoryList.remove(position);
        notifyItemRemoved(position);
    }

    public static Bitmap loadImageFromServer(String url) {
        Bitmap bitmap = null;
        try {
            InputStream in = (InputStream) new URL(url).getContent();
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}