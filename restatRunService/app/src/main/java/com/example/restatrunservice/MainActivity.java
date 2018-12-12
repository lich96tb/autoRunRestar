package com.example.restatrunservice;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    int i = 0;
    MediaPlayer mp;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(getBaseContext(), MsgPushService.class));
        // mDatabase = FirebaseDatabase.getInstance().getReference();
        // ListenerSen();
//        mp = new MediaPlayer();
//        try {
//            mp.setDataSource("http://192.168.0.111:50004/stream/swyh.mp3");
//            mp.prepare();
//            mp.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // getStreamState();
    }

    private void getStreamState() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Get ping
                // Creating a string request
                url = "http://192.168.0.111:9000/api/Ping?id=5";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(final String response) {
                                if (response == null || response.isEmpty()) {
                                    return;
                                }


                                DataResponseModel reponseModel = new Gson()
                                        .fromJson(response, DataResponseModel.class);
                                if (reponseModel.getVolume() > 0) {
                                    //start services voi link
                                    Toast.makeText(MainActivity.this, "123", Toast.LENGTH_SHORT).show();

                                } else {
                                    // páuse stream

                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Thông báo lỗi
                    }
                });
                // Adding the string request to the queue
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                requestQueue.add(stringRequest);


                // Ping re-try
                getStreamState();
            }
        }, 2000);
    }
}
