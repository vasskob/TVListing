package com.vasskob.tvchannels.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.vasskob.tvchannels.Constants;
import com.vasskob.tvchannels.model.TvCategory;
import com.vasskob.tvchannels.model.TvChannel;
import com.vasskob.tvchannels.model.TvListing;
import com.vasskob.tvchannels.service.RetrofitApiService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Dispatcher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataLoader {

    private List<TvChannel> tvChannels = new ArrayList<>();
    private List<TvCategory> tvCategories = new ArrayList<>();
    private List<TvListing> tvListings = new ArrayList<>();
    private Context context;
    private final DbFunction dbFunction;
    public boolean dataIsLoaded;
    long corection;
    public int days;
    private final AtomicInteger counter = new AtomicInteger(0);
    private final Dispatcher dispatcher;

    public DataLoader(Context context) {
        this.context = context;
        this.dbFunction = new DbFunction(context);
        this.dispatcher = new Dispatcher(Executors.newFixedThreadPool(4));
    }

    public void loadData() {
        corection = 86400000;
        Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        days = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH) -
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        System.out.println("CORRECTION" + (System.currentTimeMillis() + corection));
        SharedPreferences sP = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        if (sP.getString("picked_date", null) == null) {
//            loadCategoriesDataFromAPI();
//            loadChannelDataFromAPI();
//            // loadListingDataFromAPI(0);
//            for (int i = 0; i < 3; i++) {
//                loadListingDataFromAPI(corection * i);
//            }
//        } else {

            dbFunction.eraseDbTables();
            loadCategoriesDataFromAPI();
            loadChannelDataFromAPI();
            for (int i = 0; i < days; i++) {

                loadListing(corection * i, dispatcher).
                        enqueue(new Callback<List<TvListing>>() {
                            @Override
                            public void onResponse
                                    (Call<List<TvListing>> call, Response<List<TvListing>> response) {
                                counter.incrementAndGet();
                                if (response.body() != null) {
                                    List tvListings = new ArrayList(response.body());
//                                    tvListings.addAll(response.body());
                                    if (tvListings.size() > 0) {
                                        dbFunction.insertListingsToDb(tvListings);
                                        //  dataIsLoaded = true;

                                    }
                                } else {
//                                                dataIsLoaded = false;
                                }

                            }

                            @Override
                            public void onFailure(Call<List<TvListing>> call, Throwable t) {

                            }

                        });
            }
        }
    }

    private void loadCategoriesDataFromAPI() {

        RetrofitApiService.getApiService(Constants.URL_CATEGORIES,dispatcher).getCategoryInfo().
                enqueue(new Callback<List<TvCategory>>() {
                            @Override
                            public void onResponse
                                    (Call<List<TvCategory>> call, Response<List<TvCategory>> response) {
                                System.out.println("onRESPONSE!!!!");
                                if (response.body() != null) {
                                    tvCategories.addAll(response.body());
                                    if (tvCategories != null) {
                                        dbFunction.insertCategoriesToDb(tvCategories);
                                    }
                                } else {
                                    System.out.println("RESPONSE IS NULL " + response.errorBody().toString());
                                }
                            }

                            @Override
                            public void onFailure(Call<List<TvCategory>> call, Throwable t) {
                                System.out.println("onFAILUREEEEEEE!!!");
                            }

                        }

                );
    }


    private void loadChannelDataFromAPI() {
        RetrofitApiService.getApiService(Constants.URL_CHANNELS,dispatcher).getChannelInfo().
                enqueue(new Callback<List<TvChannel>>() {
                            @Override
                            public void onResponse
                                    (Call<List<TvChannel>> call, Response<List<TvChannel>> response) {
                                System.out.println("onRESPONSE!!!!");
                                if (response.body() != null) {
                                    tvChannels.addAll(response.body());

                                    if (tvChannels != null) {
                                        dbFunction.insertChannelsToDb(tvChannels);

                                    } else {
                                        Toast.makeText(context, "DATABASE IS NOT created", Toast.LENGTH_LONG).show();
                                    }

                                } else {
                                    System.out.println("RESPONSE IS NULL " + response.errorBody().toString());
                                }

                            }

                            @Override
                            public void onFailure(Call<List<TvChannel>> call, Throwable t) {
                                System.out.println("onFAILUREEEEEEE!!!");
                            }

                        }

                );
    }

    public int getCallCount() {
        return counter.intValue();
    }

    private Call<List<TvListing>> loadListing(long timestamp, Dispatcher dispatcher) {
        return RetrofitApiService.getApiService(Constants.URL_LISTINGS + (System.currentTimeMillis() + timestamp) + "/", dispatcher).
                getListingInfo("");
    }

}