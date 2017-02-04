package com.vasskob.tvchannels.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ldoublem.loadingviewlib.view.LVBlock;
import com.vasskob.tvchannels.Constant;
import com.vasskob.tvchannels.MainActivity;
import com.vasskob.tvchannels.R;
import com.vasskob.tvchannels.data.DbFunction;
import com.vasskob.tvchannels.model.TvCategory;
import com.vasskob.tvchannels.model.TvChannel;
import com.vasskob.tvchannels.model.TvListing;
import com.vasskob.tvchannels.service.RetrofitApiManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    private final static String TAG = "myLog";

    LVBlock lvBlock;
    List<TvChannel> tvChannels = new ArrayList<>();
    List<TvCategory> tvCategories = new ArrayList<>();
    List<TvListing> tvListings = new ArrayList<>();


    final DbFunction dbFunction = new DbFunction(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_activity);
        lvBlock = (LVBlock) (findViewById(R.id.lv_block));

        startAnim(lvBlock);

        if (isNetworkAvailable(this)) {
            Snackbar mSnackbar = Snackbar.make(findViewById(R.id.splash_container), "Please wait. Data is loading", Snackbar.LENGTH_LONG);
            View mView = mSnackbar.getView();
            TextView mTextView = (TextView) mView.findViewById(android.support.design.R.id.snackbar_text);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            else
                mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            mSnackbar.setDuration(10000000).show();

            getCategoriesDataFromAPI();
            getChannelDataFromAPI();
            getListingDataFromAPI();
//            AsyncTask asyncTask = new AsyncTask() {
//                @Override
//                protected Object doInBackground(Object[] objects) {
//
//                    return null;
//                }
//
//                @Override
//                protected void onPostExecute(Object o) {
//
//                }
//            };
//            asyncTask.execute();


        } else {
            Snackbar.make(findViewById(R.id.splash_container), "Please connect to Internet for loading data", Snackbar.LENGTH_LONG)
                    .show();
        }


    }

    public void startAnim(View v) {
        if (v.getId() == R.id.lv_block) {
            ((LVBlock) v).isShadow(true);
            ((LVBlock) v).startAnim();
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }

        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public void getCategoriesDataFromAPI() {

        RetrofitApiManager.getApiService(Constant.URL_CATEGORIES).getCategoryInfo().
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


    public void getChannelDataFromAPI() {
        RetrofitApiManager.getApiService(Constant.URL_CHANNELS).getChannelInfo().
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
                                        Toast.makeText(getApplicationContext(), "DATABASE IS NOT created", Toast.LENGTH_LONG).show();
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

    public void getListingDataFromAPI() {
        RetrofitApiManager.getApiService(Constant.URL_LISTINGS + System.currentTimeMillis() + "/").
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
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);

                                        finish();
                                    }
                                } else {
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
}
