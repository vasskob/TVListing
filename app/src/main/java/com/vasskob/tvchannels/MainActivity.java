package com.vasskob.tvchannels;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.vasskob.tvchannels.model.TvCategory;
import com.vasskob.tvchannels.model.TvChannel;
import com.vasskob.tvchannels.model.TvListing;
import com.vasskob.tvchannels.service.ApiManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private TextView txtResult;
    private RecyclerView recyclerView;

    List<TvChannel> tvChannels=new ArrayList<>();
    List<TvCategory> tvCategories=new ArrayList<>();
    List<TvListing> tvListings=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtResult = (TextView)findViewById(R.id.text);
        // APIManager.getApiService().getChannelInfo();

//        txtResult = (TextView)findViewById(R.id.txt_result);
//
//        progressDialog = new ProgressDialog(MainActivity.this);
//        progressDialog.setMessage("Please wait...");
//
//        requestAsync();

//        tvChannel = new ArrayList<>();
//
//        recyclerView = (RecyclerView) findViewById(R.id.posts_recycle_view);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        PostsAdapter adapter = new PostsAdapter(tvChannel);
//        recyclerView.setAdapter(adapter);



        ApiManager.getApiService(Constant.URL_CHANNELS).getChannelInfo()
                .enqueue(new Callback<ArrayList<TvChannel>>() {
            @Override
            public void onResponse(Call<ArrayList<TvChannel>> call, Response<ArrayList<TvChannel>> response) {
                System.out.println("onRESPONSE!!!!");
                if (response.body() != null) {
                    tvChannels.addAll(response.body());
                    for (int i=0; i< tvChannels.size(); i++)
                    {System.out.println(tvChannels.get(i));
                        txtResult.append(tvChannels.get(i).getName()+"\n");
                    }
                }else {
                    System.out.println("RESPONSE IS NULL " + response.errorBody().toString() );
                }

              //  recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<TvChannel>> call, Throwable t) {
                System.out.println("onFAILUREEEEEEE!!!");
            }

        });


        ApiManager.getApiService(Constant.URL_CATEGORIES).getCategoryInfo()
                .enqueue(new Callback<ArrayList<TvCategory>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TvCategory>> call, Response<ArrayList<TvCategory>> response) {
                        System.out.println("onRESPONSE!!!!");
                        if (response.body() != null) {
                            tvCategories.addAll(response.body());
                            for (int i=0; i< tvCategories.size(); i++)
                            {System.out.println(tvCategories.get(i));
                            }
                        }else {
                            System.out.println("RESPONSE IS NULL " + response.errorBody().toString() );
                        }

                        //  recyclerView.getAdapter().notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TvCategory>> call, Throwable t) {
                        System.out.println("onFAILUREEEEEEE!!!");
                    }

                });


        ApiManager.getApiService(Constant.URL_LISTINGS + System.currentTimeMillis()+"/").getListingInfo("")
                .enqueue(new Callback<ArrayList<TvListing>>() {
                    @Override
                    public void onResponse(Call<ArrayList<TvListing>> call, Response<ArrayList<TvListing>> response) {
                        System.out.println("onRESPONSE!!!!");
                        if (response.body() != null) {
                            tvListings.addAll(response.body());
                            for (int i=0; i< tvListings.size(); i++)
                            {System.out.println(tvListings.get(i));
                            }
                        }else {
                            System.out.println("RESPONSE IS NULL " + response.errorBody().toString() );
                        }

                        //  recyclerView.getAdapter().notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<TvListing>> call, Throwable t) {
                        System.out.println("onFAILUREEEEEEE!!!");
                    }

                });
    }


}
