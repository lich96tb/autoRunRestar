package com.example.restatrunservice;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;

public class MsgPushService extends Service {
    private BroadcastReceiver broadcastReceiver;
    Handler handler = new Handler();
    MediaPlayer mp;
    private String urlStream = "http://192.168.1.31:52319/stream/swyh.mp3";

    String url;
    //String url = "http://vprbbc.streamguys.net:80/vprbbc24.mp3";
    // String url = "http://192.168.0.111:50004/stream/swyh.mp3";
    private DatabaseReference mDatabase;
    int i = 0;
    private AudioManager audio;
    private int maxVolume;

    @Override
    public void onCreate() {
        super.onCreate();

        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mp = new MediaPlayer();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(getApplicationContext(), "co  check masng", Toast.LENGTH_SHORT).show();
                if (intent.getAction() == "android.net.conn.CONNECTIVITY_CHANGE") {
                 //   mp.reset();
                    if (isOnline(context)) {
                        Toast.makeText(context, "co mang ", Toast.LENGTH_SHORT).show();
                        getStreamState();


                    }
                }
            }
        };

        // ListenerSen();
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

    private void ListenerSen() {
        mDatabase.child("chatroom").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.isCheckSenLink() && user.getLink() != null) {
                    try {
                        mp.reset();
                        mp.setDataSource(user.getLink());
                        mp.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                if (user.isCheckplay()) {
                    mp.start();
                } else {
                    mp.pause();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("Volume").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int value = dataSnapshot.getValue(Integer.class);
                audio.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume * value / 100, 0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getStreamState() {
        try {
            mp = new MediaPlayer();
            mp.setDataSource(urlStream);
            mp.prepare();
           mp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Get ping
//                // Creating a string request
//                url = "http://192.168.0.111:9000/api/Ping?id=5";
//                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(final String response) {
//                                if (response == null || response.isEmpty()) {
//                                    return;
//                                }
//
//
//                                DataResponseModel reponseModel = new Gson()
//                                        .fromJson(response, DataResponseModel.class);
//                                if(reponseModel.getVolume() > 0){
//                                    //start services voi link
//                                    Toast.makeText(getApplicationContext(), "123", Toast.LENGTH_SHORT).show();
//
//                                } else {
//                                    // páuse stream
//
//                                }
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Thông báo lỗi
//                    }
//                });
//                // Adding the string request to the queue
//                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//                requestQueue.add(stringRequest);
//
//
//                // Ping re-try
//                getStreamState();
//            }
//        }, 2000);
    }
}
