package com.rmproduct.rmprinteradmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BillMaking extends AppCompatActivity {

    private TextView userRoll, userName, userAdv, userDue, userTotalBill, userTotalPay;
    private EditText topic, bill, pay;
    private Float Adv, Due, fl_Bill, fl_Pay;
    private Button submit, history;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_making);

        userRoll = findViewById(R.id.userRoll);
        userName = findViewById(R.id.userName);
        userAdv = findViewById(R.id.userAdv);
        userDue = findViewById(R.id.userDue);
        userTotalBill = findViewById(R.id.userTotalBill);
        userTotalPay = findViewById(R.id.userTotalPay);
        history = findViewById(R.id.history);
        submit = findViewById(R.id.submit);

        topic = findViewById(R.id.topic);
        bill = findViewById(R.id.bill);
        pay = findViewById(R.id.pay);

        Intent intent = getIntent();
        final String id = intent.getStringExtra("uid");
        final String roll = intent.getStringExtra("roll");
        String name = intent.getStringExtra("name");

        userRoll.setText(roll);
        userName.setText(name);

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(BillMaking.this, History.class);
                intent1.putExtra("uid", id);
                intent1.putExtra("roll", roll);
                startActivity(intent1);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Printer Info").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                PrinterInfo printerInfo = dataSnapshot.getValue(PrinterInfo.class);
                String st_advance = printerInfo.getAdvance();
                String st_due = printerInfo.getDue();
                String st_bill = printerInfo.getBill();
                String st_pay = printerInfo.getPay();

                userAdv.setText("Advance: " + printerInfo.getAdvance() + " TK.");
                userDue.setText("Due: " + printerInfo.getDue() + " TK.");
                userTotalBill.setText("Total Bill: " + st_bill + " TK.");
                userTotalPay.setText("Total Pay: " + st_pay + " TK.");

                Adv = Float.parseFloat(st_advance);
                Due = Float.parseFloat(st_due);
                fl_Bill = Float.parseFloat(st_bill);
                fl_Pay = Float.parseFloat(st_pay);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "Something went wrong, Please try again later!", Toast.LENGTH_LONG).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Bill = "0.0";
                Bill = bill.getText().toString().trim();
                String Pay = "0.0";
                Pay = pay.getText().toString().trim();
                bill.setText("");
                pay.setText("");
                Float bill, pay, adv, due;

                bill = pay = adv = due = 0.0f;

                if (Bill.equals("")) {
                    bill = 0.0f;
                } else {
                    bill = Float.parseFloat(Bill);
                }

                if (Pay.equals("")) {
                    pay = 0.0f;
                } else {
                    pay = Float.parseFloat(Pay);
                }

                String st_advance = String.valueOf(Adv);
                String st_due = String.valueOf(Due);

                adv = Adv;
                due = Due;

                Float pre_adv = 0.0f, pre_due = 0.0f, inner_adv = 0.0f, inner_due = 0.0f;

                if (bill > pay) {

                    if (adv > 0.0) {

                        pre_adv = (adv - (bill - pay));

                        if (pre_adv >= 0.0) {

                            inner_adv = pre_adv;

                        } else if (pre_adv < 0.0) {

                            inner_due = Math.abs(pre_adv);
                            inner_adv = 0.0f;

                        } else if (pre_adv == 0.0) {

                            inner_adv = 0.0f;
                        }
                    } else if (due > 0.0) {

                        inner_due = (due + (bill - pay));

                    } else if (adv == 0.0 && due == 0.0) {

                        inner_due = (bill - pay);
                    }
                }

                if (bill < pay) {

                    if (due > 0.0) {

                        pre_due = (pay - bill) - due;

                        if (pre_due > 0.0) {

                            inner_adv = pre_due;

                        } else if (pre_due < 0.0) {

                            inner_due = Math.abs(pre_due);

                        } else if (pre_due == 0.0) {

                        }
                    } else if (adv > 0.0) {

                        pre_adv = (pay - bill) + adv;
                        inner_adv = pre_adv;

                    } else if (adv == 0.0 && due == 0.0) {

                        inner_adv = (pay - bill);

                    }
                }

                if ((bill - pay) == 0) {

                    inner_adv = Adv;
                    inner_due = Due;

                }

                Float pre_bill, pre_pay;
                pre_bill = (bill + fl_Bill);
                pre_pay = (pay + fl_Pay);

                String Topic = topic.getText().toString().trim();
                if (Topic.equals("")) {
                    topic.setError("Please set a topic!");
                    topic.requestFocus();
                } else {
                    topic.setText("");

                    String date = pickDate();
                    saveData(id, date, Topic, Bill, Pay, st_advance, st_due);
                }

                updateData(id, String.valueOf(inner_adv), String.valueOf(inner_due), String.valueOf(pre_bill), String.valueOf(pre_pay));
            }
        });

    }

    private boolean updateData(String id, String adv, String due, String bill, String pay) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Printer Info").child(id);

        databaseReference.child("advance").setValue(adv);
        databaseReference.child("due").setValue(due);
        databaseReference.child("bill").setValue(bill);
        databaseReference.child("pay").setValue(pay);

        return true;
    }

    private String pickDate() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd.MM.yyyy ");
        String strDate = mdformat.format(calendar.getTime());

        return strDate;
    }

    private void saveData(String id, String date, String Topic, String Bill, String Pay, String Adv, String Due) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Daily Data");
        String key = databaseReference.push().getKey();
        DailyData dailyData = new DailyData(Topic, date, Adv, Due, Bill, Pay);
        databaseReference.child(id).child(key).setValue(dailyData);
    }
}
