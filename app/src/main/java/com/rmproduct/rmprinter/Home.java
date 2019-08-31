package com.rmproduct.rmprinter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ForceUpdateChecker.OnUpdateNeededListener {

    private DatabaseReference printerDatabase, databaseReference;
    private TextView due, advance, advice, payment, bill;
    private Float dueBill, advanceBill, paybill, billPay;
    private String Due, Advance, Bill, Pay, Session;

    private long backPressedTime;
    private int counter = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ForceUpdateChecker.with(this).onUpdateNeeded(this).check();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!isConnected()) {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_warning)
                    .setTitle("No Internet Connection!")
                    .setMessage("Please Check Your Internet Connection and Try Again!")
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }

        due = findViewById(R.id.due);
        advance = findViewById(R.id.advance);
        advice = findViewById(R.id.advice);
        payment = findViewById(R.id.payment);
        bill = findViewById(R.id.bill);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        final String uid = FirebaseAuth.getInstance().getUid();
        printerDatabase = FirebaseDatabase.getInstance().getReference("Printer Info").child(uid);
        printerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PrinterInfo printerInfo = dataSnapshot.getValue(PrinterInfo.class);

                Due = printerInfo.getDue();
                Advance = printerInfo.getAdvance();
                Bill = printerInfo.getBill();
                Pay = printerInfo.getPay();
                Session = printerInfo.getSession();

                dueBill = Float.parseFloat(Due);
                advanceBill = Float.parseFloat(Advance);

                /*billPay=Float.parseFloat(Bill);
                paybill=Float.parseFloat(Pay);

                if (billPay>paybill) {

                }*/

                if (dueBill > 0.0) {
                    advice.setText("Please pay your due and get our service. Thank you.");
                } else {
                    advice.setText("");
                }
                due.setText(Due + " Tk.");
                advance.setText(Advance + " Tk.");
                payment.setText(Pay + " Tk.");
                bill.setText(Bill + " Tk.");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "Something went wrong, Please try again later!", Toast.LENGTH_LONG).show();
            }
        });

        /*databaseReference = FirebaseDatabase.getInstance().getReference().child(Session).child(Id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                String name = userInfo.getName();
                String email = userInfo.getEmail();

                studentName.setText(name);
                studentEmail.setText(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        UpdateInfo();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.billing) {

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Home.this, LogIn.class));

        } else if (id == R.id.nav_share) {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareBody = "https://rmproduct121.blogspot.com/2019/08/rm-printer-user-applicaion.html";
            String shareSubject = "Rm Printer Android App";

            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
            startActivity(Intent.createChooser(shareIntent, "Share App Using..."));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean isConnected() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
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

    public void UpdateInfo() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View infoView = navigationView.getHeaderView(0);
        final TextView Name = infoView.findViewById(R.id.Name);
        final TextView Email = infoView.findViewById(R.id.Email);

        final String uid = FirebaseAuth.getInstance().getUid();
        printerDatabase = FirebaseDatabase.getInstance().getReference("Printer Info").child(uid);
        printerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PrinterInfo printerInfo = dataSnapshot.getValue(PrinterInfo.class);

                Session = printerInfo.getSession();

                databaseReference = FirebaseDatabase.getInstance().getReference(Session).child(uid);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);

                        Name.setText(userInfo.getName());
                        Email.setText(userInfo.getEmail());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "Something went wrong, Please try again later!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onUpdateNeeded(final String updateUrl) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New version available")
                .setMessage("Please, update this app and enjoy more functions.")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                redirectStore(updateUrl);
                            }
                        }).setNegativeButton("No, thanks",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }).create();
        dialog.show();
    }

    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
