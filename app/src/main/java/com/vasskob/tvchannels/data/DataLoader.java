package com.vasskob.tvchannels.data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.vasskob.tvchannels.Constants;
import com.vasskob.tvchannels.MainActivity;
import com.vasskob.tvchannels.model.TvCategory;
import com.vasskob.tvchannels.model.TvChannel;
import com.vasskob.tvchannels.model.TvListing;
import com.vasskob.tvchannels.service.RetrofitApiManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    public DataLoader(Context context) {
        this.context = context;
        dbFunction = new DbFunction(context);
    }

    public void loadData() {
        corection = 86400000;
        Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        days = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH) -
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        System.out.println("CORRECTION" + (System.currentTimeMillis() + corection));
        SharedPreferences sP = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        if (sP.getString("picked_date", null) == null) {
            loadCategoriesDataFromAPI();
            loadChannelDataFromAPI();
            // loadListingDataFromAPI(0);
            for (int i = 0; i < 2; i++) {
                loadListingDataFromAPI(corection * i);
            }
        } else {

            dbFunction.eraseDbTables();
            loadCategoriesDataFromAPI();
            loadChannelDataFromAPI();
            //  loadListingDataFromAPI(0);
            for (int i = 0; i < 2; i++) {
                loadListingDataFromAPI(corection * i);
            }
        }
    }


    private void loadCategoriesDataFromAPI() {

        RetrofitApiManager.getApiService(Constants.URL_CATEGORIES).getCategoryInfo().
                enqueue(new Callback<List<TvCategory>>() {
                            @Override
                            public void onResponse
                                    (Call<List<TvCategory>> call, Response<List<TvCategory>> response) {
                                System.out.println("onRESPONSE!!!!");
                                if (response.body() != null) {
                                    tvCategories.addAll(response.body());
//                            for (int i = 0; i < tvCategories.size(); i++) {
//                                System.out.println(tvCategories.get(i));
//                            }
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
        RetrofitApiManager.getApiService(Constants.URL_CHANNELS).getChannelInfo().
                enqueue(new Callback<List<TvChannel>>() {
                            @Override
                            public void onResponse
                                    (Call<List<TvChannel>> call, Response<List<TvChannel>> response) {
                                System.out.println("onRESPONSE!!!!");
                                if (response.body() != null) {
                                    tvChannels.addAll(response.body());

//                                    for (int i = 0; i < tvChannels.size(); i++) {
//                                        //System.out.println(tvChannels.get(i));
//
//                                        //tabsName[i] = tvChannels.get(i).getName();
//                                    }
                                    if (tvChannels != null) {
                                        dbFunction.insertChannelsToDb(tvChannels);
//


                                    } else {
                                        Toast.makeText(context, "DATABASE IS NOT created", Toast.LENGTH_LONG).show();
                                    }

                                } else {
                                    System.out.println("RESPONSE IS NULL " + response.errorBody().toString());
                                }

                                //  recyclerView.getAdapter().notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(Call<List<TvChannel>> call, Throwable t) {
                                System.out.println("onFAILUREEEEEEE!!!");
                            }

                        }

                );

    }

    public void loadListingDataFromAPI(long corection) {
        RetrofitApiManager.getApiService(Constants.URL_LISTINGS + (System.currentTimeMillis() + corection) + "/").
                getListingInfo("").
                enqueue(new Callback<List<TvListing>>() {
                            @Override
                            public void onResponse
                                    (Call<List<TvListing>> call, Response<List<TvListing>> response) {
                                System.out.println("onRESPONSE!!!!");
                                if (response.body() != null) {
                                    tvListings.addAll(response.body());
//                            for (int i = 0; i < tvListings.size(); i++) {
//                                System.out.println(tvListings.get(i));
//                            }
                                    if (tvListings.size() > 0) {
                                        dbFunction.insertListingsToDb(tvListings);
                                        Intent intent = new Intent(context, MainActivity.class);
                                        context.startActivity(intent);

                                        dataIsLoaded = true;
                                    }
                                } else {
                                    dataIsLoaded = false;
                                    System.out.println("RESPONSE IS NULL " + response.errorBody().toString());
                                }


                                //  recyclerView.getAdapter().notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(Call<List<TvListing>> call, Throwable t) {
                                System.out.println("onFAILUREEEEEEE!!!");
                            }

                        }

                );
    }

//    private class MyTask extends AsyncTask<Integer, Void, Void> {
//
//
//        @Override
//        protected Void doInBackground(Integer... integers) {
//            for (int i = ); i<days; i++) {
//
//                loadData();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            //  ListingForMonthService.startService(getApplicationContext(), true);
//        }
//    }


}
