package com.vasskob.tvchannels.service;


import com.vasskob.tvchannels.model.TvCategory;
import com.vasskob.tvchannels.model.TvChannel;
import com.vasskob.tvchannels.model.TvListing;

import java.util.List;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class RetrofitApiService {

    public interface ApiService {

        @GET("chanels")
        Call<List<TvChannel>> getChannelInfo();

        @GET("categories")
        Call<List<TvCategory>> getCategoryInfo();

        @GET
        Call<List<TvListing>> getListingInfo(@Url String emptyUrl);
    }

    public static ApiService getApiService(String baseUrl, Dispatcher dispatcher) {
        OkHttpClient ok = new OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(ok)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ApiService.class);
    }

}
