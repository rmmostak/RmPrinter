package com.rmproduct.rmprinter;

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

public class Billing extends AppCompatActivity {

    private ListView historyList;
    private List<DailyData> dailyDataList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        historyList=findViewById(R.id.historyList);
        databaseReference= FirebaseDatabase.getInstance().getReference("Daily Data");

        Intent intent=getIntent();
        String uid = intent.getStringExtra("uid");

        showHistory(uid);

        dailyDataList=new ArrayList<>();
    }

    private void showHistory(String id) {
        databaseReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dailySnapshot : dataSnapshot.getChildren()) {

                    DailyData dailyData=dailySnapshot.getValue(DailyData.class);
                    dailyDataList.add(dailyData);
                }

                BillList billList=new BillList(Billing.this, dailyDataList);
                historyList.setAdapter(billList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Billing.this, "Something went wrong, Please try again later!", Toast.LENGTH_LONG).show();

            }
        });
    }
}
