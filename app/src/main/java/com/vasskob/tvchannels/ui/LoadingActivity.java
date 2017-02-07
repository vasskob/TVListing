package com.vasskob.tvchannels.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ldoublem.loadingviewlib.view.LVBlock;
import com.vasskob.tvchannels.R;
import com.vasskob.tvchannels.data.DataLoader;

public class LoadingActivity extends AppCompatActivity {

    public static boolean close = false;
    private DataLoader dataLoader;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_load);
        LVBlock lvBlock = (LVBlock) (findViewById(R.id.lv_block));
        lvBlock.isShadow(true);
        lvBlock.startAnim();

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

                dataLoader.loadData();
                handler.postDelayed(handlerRunnable, 1000);

            } else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }


        } else {
            Snackbar.make(findViewById(R.id.splash_container), "Please connect to Internet for loading data", Snackbar.LENGTH_LONG)
                    .show();
        }
    }


    private final Runnable handlerRunnable = new Runnable() {
        @Override
        public void run() {
            if (dataLoader.getCallCount() != dataLoader.days) {
                handler.postDelayed(this, 1000);
            } else {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();

            }
        }
    };

    private static boolean isNetworkAvailable(Context context) {
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
}
