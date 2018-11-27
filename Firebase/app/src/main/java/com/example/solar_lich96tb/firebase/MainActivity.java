package com.example.solar_lich96tb.firebase;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    String TAG = "LICH";
    RecyclerView recyclerView;
    AdapterContent adapterContent;
    List<User> list;
    private DatabaseReference mDatabase;
    EditText edtSen;
    LinearLayout btnCheck;
    TextView edtLink;
    private TextView txtCheckIsPlay, valueVolume,txtSelect1,txtSelect2;
    private boolean checkplay;
    private String url;
    private ImageView imgIsPlay;
    private LinearLayout layoutVolume, dialog_display, linearLayout;
    private SeekBar seekbarVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        nghe();
        volume();


    }

    private void volume() {
        seekbarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    valueVolume.setText(progress + " %");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mDatabase.child("Volume").setValue(seekBar.getProgress());
            }
        });
    }

    private void nghe() {
        mDatabase.child("chatroom").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                url = user.getLink();
                checkplay = user.isCheckplay();
                if (user.isCheckplay()) {
                    imgIsPlay.setImageResource(R.drawable.ic_pause);
                    txtCheckIsPlay.setText("Bài hát đang chạy trên máy con");
                } else {
                    txtCheckIsPlay.setText("Bài hát đang không chạy trên máy con");
                    imgIsPlay.setImageResource(R.drawable.ic_play);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void initView() {
        txtSelect1=findViewById(R.id.txtSelect1);
        txtSelect2=findViewById(R.id.txtSelect2);
        linearLayout = findViewById(R.id.linearLayout);
        dialog_display = findViewById(R.id.dialog_display);
        valueVolume = findViewById(R.id.valueVolume);
        seekbarVolume = findViewById(R.id.seekbarVolume);
        layoutVolume = findViewById(R.id.layoutVolume);
        imgIsPlay = findViewById(R.id.imgIsPlay);
        txtCheckIsPlay = findViewById(R.id.txtCheckIsPlay);
        edtLink = findViewById(R.id.edtLinkNhac);
        btnCheck = findViewById(R.id.btnCheck);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    public void btnCheckPlay(View view) {
        if (checkplay) {
            mDatabase.child("chatroom").setValue(new User(false, url, false));
        } else {
            mDatabase.child("chatroom").setValue(new User(true, url, false));
        }
    }

    public void PlaySongLink(View view) {
        url = edtLink.getText().toString().trim();
        mDatabase.child("chatroom").setValue(new User(true, url, true));
    }

    public void setVolume(View view) {
        if (layoutVolume.getVisibility() == View.VISIBLE) {
            layoutVolume.setVisibility(View.GONE);
            dialog_display.setVisibility(View.GONE);
            linearLayout.setBackgroundResource(R.color.colorNormal);
        } else {
            linearLayout.setBackgroundResource(R.color.colorActive);
            layoutVolume.setVisibility(View.VISIBLE);
            dialog_display.setVisibility(View.VISIBLE);
        }
    }

    public void clickChonbaihat(View view) {
txtSelect1.setVisibility(View.VISIBLE);
txtSelect2.setVisibility(View.VISIBLE);
    }

    public void clickSelect(View view) {
        switch (view.getId()) {
            case R.id.txtSelect1:
                edtLink.setText("https://www.ssaurel.com/tmp/mymusic.mp3");
                break;
            case R.id.txtSelect2:
                edtLink.setText("http://vprbbc.streamguys.net:80/vprbbc24.mp3");
                break;
        }
        txtSelect1.setVisibility(View.GONE);
        txtSelect2.setVisibility(View.GONE);
    }
}
