package com.vasskob.tvchannels.service;


import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.vasskob.tvchannels.R;
import com.vasskob.tvchannels.data.DataLoader;

import java.util.Calendar;

import static com.vasskob.tvchannels.ui.LoadingActivity.isNetworkAvailable;

public class ListingForMonthService extends IntentService {

    long correction;
    int days;

    public ListingForMonthService() {
        super("LoadingMonthProgramsIntentService");
    }


    public static Intent newIntent(Context context) {
        return new Intent(context, ListingForMonthService.class);
    }

    public static void startService(Context context, boolean isOn) {
        Intent intent = ListingForMonthService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_DAY, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
        Log.i("myLog", "startService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("myLog", "will startService");

        DataLoader dataLoader=new DataLoader(getApplicationContext());

        correction= 86400000;
        Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        days=Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH) -
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        if (isNetworkAvailable(getApplicationContext())) return;
        for (int i = 1; i < days; i++) {
           dataLoader.loadListingDataFromAPI(correction*(i+1));

            //QueryPreferences.setCountLoadedDays(getApplicationContext(), i);
        }


        Notification.sendNotification(getApplicationContext(),
                getResources().getString(R.string.loading_data_complite,
                        String.valueOf(days)),0);

    }



}
