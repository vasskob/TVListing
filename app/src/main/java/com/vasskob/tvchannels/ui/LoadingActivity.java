package com.vasskob.tvchannels.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ldoublem.loadingviewlib.view.LVBlock;
import com.vasskob.tvchannels.MainActivity;
import com.vasskob.tvchannels.R;
import com.vasskob.tvchannels.data.DataLoader;

public class LoadingActivity extends AppCompatActivity {
    private final static String TAG = "myLog";

    LVBlock lvBlock;
    public static boolean close = false;
    DataLoader dataLoader;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_load);
        lvBlock = (LVBlock) (findViewById(R.id.lv_block));
        startAnim(lvBlock);

        dataLoader = new DataLoader(this);


        if (isNetworkAvailable(this)) {
            Snackbar mSnackbar = Snackbar.make(findViewById(R.id.splash_container), "Please wait. Data is loading", Snackbar.LENGTH_LONG);
            View mView = mSnackbar.getView();
            TextView mTextView = (TextView) mView.findViewById(android.support.design.R.id.snackbar_text);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            else
                mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            mSnackbar.setDuration(10000000).show();

            SharedPreferences sP = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            if (sP.getString("picked_date", null) == null) {
//                dataLoader.loadData();
//                dataLoader.loadData();
//                ListingForMonthService.startService(getApplicationContext(),true);
                runMyTask();

                handler.postDelayed(handlerRunnable, 1000);

//                finish();
//
//                while (true) {
//                    if (dataLoader.getCallCount() == 10)
//                        finish();
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
            } else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                System.out.println(" SplashActivity is Finished shared prefs in not null");
            }


        } else {
            System.out.println(" Internet is OFF  !!! ");
            Snackbar.make(findViewById(R.id.splash_container), "Please connect to Internet for loading data", Snackbar.LENGTH_LONG)
                    .show();
        }
    }


    private Runnable handlerRunnable = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getApplication().getBaseContext(),"WHAT YOU WANT TO WRITE? "+dataLoader.getCallCount() ,Toast.LENGTH_LONG).show();
            if (dataLoader.getCallCount() != dataLoader.days){
                handler.postDelayed(this, 1000);
            } else{
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

            }
        }
    };

    private void runMyTask() {
        new MyTask().execute();
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

    @Override
    public void onResume() {
        super.onResume();
        if (close) {
            finish();
        }
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            dataLoader.loadData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            System.out.println(">>>>>>>>>>>>>>>>>>>>>" + dataLoader.getCallCount());
         //  ListingForMonthService.startService(getApplicationContext(), true);
        }
    }
}
