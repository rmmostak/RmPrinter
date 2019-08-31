package com.rmproduct.rmprinteradmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private long backPressedTime;

    private Button listBtn;
    private Spinner session;
    private DatabaseReference databaseReference;
    private String txt_session;
    private ListView userList;
    private List<UserInfo> userInfos;
    private Float Adv, Due, fl_Bill, fl_Pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        listBtn = findViewById(R.id.listBtn);
        session = findViewById(R.id.session);
        userList = findViewById(R.id.userList);

        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                txt_session = session.getSelectedItem().toString().trim();
                if (txt_session.equals("Select Session")) {
                    return;
                }

                if (txt_session.equals("2015-16")) {
                    UserList(txt_session);
                    return;
                }

                if (txt_session.equals("2016-17")) {
                    UserList(txt_session);
                    return;
                }

                if (txt_session.equals("2017-18")) {
                    UserList(txt_session);
                    return;
                }
            }
        });

        userInfos = new ArrayList<>();

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                UserInfo userInfo = userInfos.get(i);
                showUpdateDialog(userInfo.getUid(), userInfo.getRoll(), userInfo.getSession());

            }
        });

    }

    private void UserList(String session) {
        databaseReference = FirebaseDatabase.getInstance().getReference(session);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userInfos.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    UserInfo userInfo = userSnapshot.getValue(UserInfo.class);
                    userInfos.add(userInfo);

                }

                ListAdapter adapter = new ListAdapter(Home.this, userInfos);
                userList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }*/

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getApplicationContext(), "Press Again to Exit!", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();

    }


    private void showUpdateDialog(final String id, String roll, String session) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);

        dialogBuilder.setView(dialogView);

        final EditText bill = dialogView.findViewById(R.id.bill);
        final EditText pay = dialogView.findViewById(R.id.pay);
        final TextView advance = dialogView.findViewById(R.id.advance);
        final TextView due = dialogView.findViewById(R.id.due);
        final Button submit = dialogView.findViewById(R.id.submit);

        databaseReference = FirebaseDatabase.getInstance().getReference("Printer Info").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                PrinterInfo printerInfo = dataSnapshot.getValue(PrinterInfo.class);
                String st_advance = printerInfo.getAdvance();
                String st_due = printerInfo.getDue();
                String st_bill = printerInfo.getBill();
                String st_pay = printerInfo.getPay();

                advance.setText(printerInfo.getAdvance() + " TK.");
                due.setText(printerInfo.getDue() + " TK.");

                Adv = Float.parseFloat(st_advance);
                Due = Float.parseFloat(st_due);
                fl_Bill = Float.parseFloat(st_bill);
                fl_Pay = Float.parseFloat(st_pay);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dialogBuilder.setTitle("Roll: " + roll);
        dialogBuilder.setIcon(R.drawable.rm_printer);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Bill = "0.0";
                Bill = bill.getText().toString().trim();
                String Pay = "0.0";
                Pay = pay.getText().toString().trim();
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

                updateData(id, String.valueOf(inner_adv), String.valueOf(inner_due), String.valueOf(pre_bill), String.valueOf(pre_pay));
                alertDialog.dismiss();

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
}
