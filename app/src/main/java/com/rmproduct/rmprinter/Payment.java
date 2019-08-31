package com.rmproduct.rmprinter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

class ListAdapter extends ArrayAdapter<UserInfo> {
    private Activity context;
    private List<UserInfo> userInfoList;

    public ListAdapter(Activity context, List<UserInfo> userInfoList) {
        super(context, R.layout.pay_list_layout, userInfoList);
        this.context = context;
        this.userInfoList = userInfoList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.pay_list_layout, null, true);

        TextView rollNo = listViewItem.findViewById(R.id.studentId);
        TextView Name = listViewItem.findViewById(R.id.studentName);

        UserInfo userInfo = userInfoList.get(position);

        rollNo.setText(userInfo.getRoll());
        Name.setText(userInfo.getName());

        return listViewItem;
    }
}

public class Payment extends AppCompatActivity {

    private Button listBtn;
    private Spinner session;
    private DatabaseReference databaseReference;
    private String txt_session;
    private ListView userList;
    private List<UserInfo> userInfos;
    private String Adv, Due;
    private TextView due, advance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        listBtn = findViewById(R.id.listBtn);
        session = findViewById(R.id.session);
        userList = findViewById(R.id.userList);

        due = findViewById(R.id.due);
        advance = findViewById(R.id.advance);

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

        userList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long i) {

                UserInfo userInfo = userInfos.get(position);

                showUpdateDialog(userInfo.getUid(), userInfo.getRoll());

                return false;
            }
        });
    }

    void UserList(String session) {
        databaseReference = FirebaseDatabase.getInstance().getReference("User Info").child(session);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfos.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    UserInfo userInfo = userSnapshot.getValue(UserInfo.class);

                    userInfos.add(userInfo);
                }

                ListAdapter adapter = new ListAdapter(Payment.this, userInfos);
                userList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showUpdateDialog(final String id, String roll) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);

        dialogBuilder.setView(dialogView);

        final EditText _Bill = dialogView.findViewById(R.id.bill);
        final EditText _Pay = dialogView.findViewById(R.id.pay);
        final Button submit = dialogView.findViewById(R.id.submit);


        dialogBuilder.setTitle("Roll: " + roll);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String bill = _Bill.getText().toString().trim();
                final String pay = _Pay.getText().toString().trim();


                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Printer Info").child(id);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        PrinterInfo printerInfo = dataSnapshot.getValue(PrinterInfo.class);
                        Adv = printerInfo.getAdvance();
                        Due = printerInfo.getDue();

                        String Bill = "0.0", Pay = "0.0";

                        Float _bill, _pay, _adv, _due;

                        _bill = Float.parseFloat(bill);
                        _pay = Float.parseFloat(pay);
                        Float _preAdv = Float.parseFloat(Adv);
                        Float _preDue = Float.parseFloat(Due);

                        if (_bill > _pay) {

                            if (_preAdv > 0.0) {

                                _adv = (_preAdv - (_bill - _pay));

                                if (_adv >= 0.0) {

                                    Bill = String.valueOf(_adv);
                                    return;
                                }
                                if (_adv < 0.0) {

                                    Pay = String.valueOf(Math.abs(_adv));
                                    return;
                                }
                                return;
                            }
                            if (_preAdv == 0.0) {

                                Pay = String.valueOf(_bill - _pay);
                                return;
                            }
                            return;
                        }

                        if (_bill < _pay) {

                            if (_preDue > 0.0) {

                                _due = (_pay - _bill) - _preDue;

                                if (_due > 0.0) {

                                    Bill = String.valueOf(_due);
                                    return;
                                }

                                if (_due < 0.0) {

                                    Pay = String.valueOf(Math.abs(_due));
                                    return;
                                }
                                return;
                            }

                            if (_preDue == 0.0) {

                                Bill = String.valueOf(_pay - _bill);
                                return;
                            }
                        }

                        if (_bill == _pay) {

                            return;
                        }

                        Toast.makeText(getApplicationContext(), Bill.toString()+"\n"+Pay.toString(), Toast.LENGTH_LONG).show();

                        /*DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Printer Info").child(id);
                        databaseReference.child("advance").setValue(Bill);
                        databaseReference.child("due").setValue(Pay);*/


                        /*Due = Bill;
                        Adv = Pay;*/
                        //advance.setText(Pay);
                        /*updateData(id, Bill, Pay);*/

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(getApplicationContext(), "Something went wrong, Please try again later!", Toast.LENGTH_LONG).show();
                    }
                });

                /*due.setText(Due);
                advance.setText(Adv);*/
                //updateData(id, Due, Adv);
                alertDialog.dismiss();

            }
        });

    }

    /*private boolean updateData(String id, String bill, String pay) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Printer Info").child(id);
        databaseReference.child("advance").setValue(bill);
        databaseReference.child("due").setValue(pay);

        return true;
    }*/
}
