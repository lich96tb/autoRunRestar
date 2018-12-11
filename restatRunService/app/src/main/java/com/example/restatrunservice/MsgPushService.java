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
    // String url = "http://vprbbc.streamguys.net:80/vprbbc24.mp3";
    private String url, checkUrl = "";
    private DatabaseReference mDatabase;
    int i = 0;
    private AudioManager audio;
    private int maxVolume;

    @Override
    public void onCreate() {
        super.onCreate();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mp = new MediaPlayer();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction() == "android.net.conn.CONNECTIVITY_CHANGE") {
                    mp.reset();
                    if (isOnline(context)) {
                        getStreamState();


                    } else {
                        mp = MediaPlayer.create(getApplicationContext(), R.raw.duthenaodinua);
                        mp.start();
                        Toast.makeText(context, "mat mang ", Toast.LENGTH_SHORT).show();

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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Get ping
                // Creating a string request
                //  String url = "http://192.168.1.111:9000/api/Ping?id=5";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(final String response) {
                                if (response == null || response.isEmpty()) {
                                    return;
                                }
                                DataResponseModel reponseModel = new Gson()
                                        .fromJson(response, DataResponseModel.class);
                                Log.e("avc ", "Link" + reponseModel.getLink());
                                Log.e("avc ", "State" + reponseModel.isCheckplay());
                                Log.e("avc ", "Volume" + reponseModel.getVolumeConfig());
                                url = reponseModel.getLink();
                                if (reponseModel.isCheckplay()) {
                                    Toast.makeText(getApplicationContext(), "co mang ", Toast.LENGTH_SHORT).show();
                                    try {
                                        if (!checkUrl.equals(reponseModel.getLink())) {
                                            mp.setDataSource(url);
                                            mp.prepare();
                                            Toast.makeText(getApplicationContext(), "phat nhac " + mp.getDuration(), Toast.LENGTH_SHORT).show();
                                        }
                                        mp.start();

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }


                                    //start services voi link

                                } else {
                                    if (mp != null){
                                        mp.pause();
                                        // páuse stream
                                    }


                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Thông báo lỗi
                    }
                });
                // Adding the string request to the queue
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);


                // Ping re-try
                getStreamState();
            }
        }, 2000);
    }

}
