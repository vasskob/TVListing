package com.vasskob.tvchannels.data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.vasskob.tvchannels.Constants;
import com.vasskob.tvchannels.model.TvCategory;
import com.vasskob.tvchannels.model.TvChannel;
import com.vasskob.tvchannels.model.TvListing;
import com.vasskob.tvchannels.service.RetrofitApiService;
import com.vasskob.tvchannels.ui.LoadingActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Dispatcher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("ALL")
public class DataLoader {

    private List<TvChannel> tvChannels = new ArrayList<>();
    private List<TvCategory> tvCategories = new ArrayList<>();
    private List<TvListing> tvListings = new ArrayList<>();
    private Context context;
    private final DbFunction dbFunction;
    public boolean dataIsLoaded;
    private long corection;
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
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+1;

        System.out.println("CORRECTION" + days);
        SharedPreferences sP = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        if (sP.getString("picked_date", null) == null) {

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
                                    List<TvListing> tvListings = new ArrayList(response.body());
                                    if (tvListings.size() > 0) {
                                        dbFunction.insertListingsToDb(tvListings);
                                    }
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

        RetrofitApiService.getApiService(Constants.URL_CATEGORIES, dispatcher).getCategoryInfo().
                enqueue(new Callback<List<TvCategory>>() {
                            @Override
                            public void onResponse
                                    (Call<List<TvCategory>> call, Response<List<TvCategory>> response) {
                                if (response.body() != null) {
                                    tvCategories.addAll(response.body());
                                    if (tvCategories != null) {
                                        dbFunction.insertCategoriesToDb(tvCategories);
                                    }
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
        RetrofitApiService.getApiService(Constants.URL_CHANNELS, dispatcher).getChannelInfo().
                enqueue(new Callback<List<TvChannel>>() {
                            @Override
                            public void onResponse
                                    (Call<List<TvChannel>> call, Response<List<TvChannel>> response) {
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

    public void manualUpdate() {
        final Calendar curentDay = Calendar.getInstance();
        SharedPreferences sP = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();
        editor.putString("picked_date", null);
        editor.putInt("date_for_calendar_icon", curentDay.get(Calendar.DAY_OF_MONTH));
        editor.apply();
        Intent intent = new Intent(context, LoadingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


}