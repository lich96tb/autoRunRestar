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
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MsgPushService extends Service {
    private BroadcastReceiver broadcastReceiver;
    Handler handler = new Handler();
    MediaPlayer mp;
    private String urlStream = "";

    private AudioManager audio;
    private int maxVolume;

    private DatabaseReference mDatabase;
    int i = 0;

    // Nếu link stream thay đổi thì gán lại
    private boolean isChangeLink = false;
    private int volumeStrem;

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
                        mp = new MediaPlayer();

//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                mp.pause();
//                                Toast.makeText(MsgPushService.this, "paus", Toast.LENGTH_SHORT).show();
//                            }
//                        },15000);

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


    //    private void getStreamState() {
//        final String urlRequest = "http://192.168.0.111:9000/api/Ping?id=5";
//
//        mp = new MediaPlayer();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Get ping
//                // Creating a string request
//
//                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlRequest,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(final String response) {
//
//                                if (response == null || response.isEmpty()) {
//                                    return;
//                                }
//
//                                DataResponseModel reponseModel = new Gson()
//                                        .fromJson(response, DataResponseModel.class);
//                                // Nếu trạng thái là play và link stream khác rỗng thì xử lý play
//                                if(reponseModel.getPlayStatus() == 0 && !TextUtils.isEmpty(reponseModel.getLink())){
//                                    // Nếu link Stream không thay đổi thì bỏ qua không cần cập nhật lại
//                                    if(urlStream.equalsIgnoreCase(reponseModel.getLink())){
//                                        return;
//                                    }
//                                    // Nếu link có thay đổi thì play lại media player với mức volume server trả về
//                                    urlStream = reponseModel.getLink();
//                                    try {
//                                        mp.setDataSource(urlStream);
//                                        mp.prepare();
//                                        mp.start(); }
//                                    catch (IOException e) {
//                                        e.printStackTrace(); }
//                                  //  audio.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume * valueVolume / 100, 0);
//                                } else {
//                                    // páuse stream
//                                    mp.pause();
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
//    }
    private void getStreamState() {
        final String urlRequest = "http://192.168.0.111:9000/api/Ping?id=5";


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlRequest, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // kiem tra volume xem co thay doi ko neu co thay doi thi set lai
                            if (response.getInt("Volume") != volumeStrem) {
                                volumeStrem = response.getInt("Volume");
                                audio.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume * response.getInt("Volume") / 100, 0);

                            }
                            if (response.getInt("PlayStatus") == 0 && !TextUtils.isEmpty(response.getString("Link"))) {
                                // Nếu link Stream không thay đổi thì bỏ qua không cần cập nhật lại
                                if (response.getInt("Volume") == 0) {
                                    if (mp.isPlaying()) {
                                        mp.pause();
                                    }
                                    return;
                                } else {
                                        mp.start();
                                }


                                if (urlStream.equalsIgnoreCase(response.getString("Link"))) {
                                    return;
                                }
                                // Nếu link có thay đổi thì play lại media player với mức volume server trả về
                                urlStream = response.getString("Link");
                                try {
                                    Log.e("ACSddddsDFDS ", " j  " + response.getString("Volume"));
                                    mp.setDataSource(urlStream);
                                    mp.prepare();
                                    mp.start();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                // páuse stream
                                mp.pause();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(jsonObjectRequest);

                // Ping re-try
                getStreamState();
            }
        }, 2000);
    }
}
