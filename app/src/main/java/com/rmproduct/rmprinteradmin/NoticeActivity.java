package com.rmproduct.rmprinteradmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NoticeActivity extends AppCompatActivity {

    private EditText notice;
    private Button noticeBtn;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        noticeBtn = findViewById(R.id.noticeBtn);
        notice = findViewById(R.id.notice);
        reference = FirebaseDatabase.getInstance().getReference("Notices");


        reference.child("Latest Offer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String st_notice = dataSnapshot.getValue().toString().trim();
                notice.setText(st_notice);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        noticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String txt_notice = notice.getText().toString().trim();

                reference.child("Latest Offer").setValue(txt_notice);
                notice.setText("");
            }
        });
    }
}
