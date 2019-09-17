package com.rmproduct.rmprinteradmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {

    private ListView historyList;
    private List<DailyData> dailyDataList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyList = findViewById(R.id.historyList);

        dailyDataList = new ArrayList<>();

        Intent intent = getIntent();
        String id = intent.getStringExtra("uid");
        String title = intent.getStringExtra("roll");
        getSupportActionBar().setTitle("Roll: "+title);
        //Toast.makeText(getApplicationContext(), id, Toast.LENGTH_LONG).show();

        dataShow(id);
    }

    private void dataShow(String id) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Daily Data").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dailySnapshot : dataSnapshot.getChildren()) {

                    DailyData dailyData = dailySnapshot.getValue(DailyData.class);
                    dailyDataList.add(dailyData);
                }
                BillList billList = new BillList(History.this, dailyDataList);
                historyList.setAdapter(billList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "Something went wrong, Please try again later!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
