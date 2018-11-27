package com.example.restatrunservice;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;

public class MsgPushService extends Service {
    private BroadcastReceiver broadcastReceiver;
    Handler handler = new Handler();
    MediaPlayer mp;
    String url = "http://vprbbc.streamguys.net:80/vprbbc24.mp3";

    @Override
    public void onCreate() {
        super.onCreate();
        mp = new MediaPlayer();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction() == "android.net.conn.CONNECTIVITY_CHANGE") {
                    mp.reset();
                    if (isOnline(context)) {
                        Toast.makeText(context, "co mang ", Toast.LENGTH_SHORT).show();
                        try {
                            mp.setDataSource(url);
                            mp.prepare();
                            Toast.makeText(getApplicationContext(), "thoi gian " + mp.getDuration(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mp.start();
                    } else {
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.duthenaodinua);
                        mp.start();
                        Toast.makeText(context, "mat mang ", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        };

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerReceiver(broadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        Toast.makeText(this, "Service Destroy", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

}
