package com.example.solar_lich96tb.firebase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    Button btnCheck;
    EditText edtLink;
    TextView txtCheckIsPlay;
    private boolean checkplay;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        nghe();


    }

    private void nghe() {
        mDatabase.child("chatroom").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                url = user.getLink();
                checkplay = user.isCheckplay();
                if (user.isCheckplay()) {
                    btnCheck.setText("tạm dừng");
                    txtCheckIsPlay.setText("Bài hát đang chạy trên máy con");
                } else {
                    btnCheck.setText("tiếp tục");
                    txtCheckIsPlay.setText("Bài hát đang không chạy trên máy con");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void initView() {
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
}
