package com.rmproduct.rmprinter;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

class UserInfo {

    String name, roll, session, uid, email;

    public UserInfo() {

    }

    public UserInfo(String name, String roll, String session, String uid, String email) {
        this.name = name;
        this.roll = roll;
        this.session = session;
        this.uid = uid;
        this.email=email;
    }

    public String getName() {
        return name;
    }

    public String getRoll() {
        return roll;
    }

    public String getSession() {
        return session;
    }

    public String getUid() {
        return uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

class PrinterInfo {
    String roll, due, advance, uid, bill, pay, session;

    public PrinterInfo() {
    }

    public PrinterInfo(String roll, String due, String advance, String uid, String bill, String pay, String session) {
        this.roll=roll;
        this.due = due;
        this.advance = advance;
        this.uid = uid;
        this.bill = bill;
        this.pay=pay;
        this.session=session;
    }

    public String getRoll() {
        return roll;
    }

    public String getDue() {
        return due;
    }

    public String getAdvance() {
        return advance;
    }

    public String getUid() {
        return uid;
    }

    public String getBill() {
        return bill;
    }

    public String getPay() {
        return pay;
    }

    public String getSession() {
        return session;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public void setAdvance(String advance) {
        this.advance = advance;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }
}

public class SignUp extends Activity {

    private EditText email, password, password2, name, roll;
    private Spinner session;
    private Button signUp;
    private TextView logIn, error;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference, printerDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Sign Up");

        email = (EditText) findViewById(R.id.email2);
        password = (EditText) findViewById(R.id.password2);
        password2 = (EditText) findViewById(R.id.password4);
        name = (EditText) findViewById(R.id.name);
        roll = (EditText) findViewById(R.id.roll);
        session = (Spinner) findViewById(R.id.session);
        signUp = (Button) findViewById(R.id.signUp);
        logIn = findViewById(R.id.logIn);
        error = findViewById(R.id.error);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        printerDatabase = FirebaseDatabase.getInstance().getReference("Printer Info");
        firebaseAuth = FirebaseAuth.getInstance();
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String txt_email = email.getText().toString().trim();
                String txt_password = password.getText().toString().trim();
                String txt_password2 = password2.getText().toString().trim();
                final String txt_name = name.getText().toString().trim();
                final String txt_roll = roll.getText().toString().trim();
                final String txt_session = session.getSelectedItem().toString().trim();


                if (TextUtils.isEmpty(txt_email)) {
                    email.setError("You must set a valid email!");
                    email.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(txt_email).matches()) {
                    email.setError("Set a valid email!");
                    email.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(txt_password)) {
                    password.setError("You must set password!");
                    password.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(txt_password2)) {
                    password2.setError("You have to confirm password!");
                    password2.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(txt_name)) {
                    name.setError("Please Enter Your Name!");
                    name.requestFocus();
                    return;
                }
                if ((txt_roll.length()) < 6 || (txt_roll.length() > 6)) {
                    roll.setError("Please Enter Valid Roll No!");
                    roll.requestFocus();
                    return;
                }
                if (txt_session.equals("Select Session")) {
                    error.setText("Please Select Your Session!");
                    return;
                }
                if ((password.length() < 6)) {
                    password.setError("Your password must be at least 6 character!");
                    password.requestFocus();
                    return;
                } else {
                    if (txt_password.equals(txt_password2)) {

                        progressBar.setVisibility(View.VISIBLE);
                        firebaseAuth.createUserWithEmailAndPassword(txt_email, txt_password)
                                .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            final String uid = firebaseAuth.getUid();

                                            UserInfo userInfo = new UserInfo(txt_name, txt_roll, txt_session, uid, txt_email);

                                            final PrinterInfo printerInfo = new PrinterInfo(txt_roll, "0.0", "0.0", uid, "0.0", "0.0",txt_session);

                                            //String id = databaseReference.push().getKey();

                                            databaseReference.child(txt_session).child(uid).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    printerDatabase.child(uid).setValue(printerInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            sendEmailVerification();
                                                        }
                                                    });
                                                }
                                            });

                                            progressBar.setVisibility(View.GONE);

//                                            startActivity(new Intent(SignUp.this, LogIn.class));
//                                            Toast.makeText(SignUp.this, "Registration Complete!!", Toast.LENGTH_LONG).show();
                                        } else {
                                            progressBar.setVisibility(View.GONE);
                                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                                Toast.makeText(getApplicationContext(), "This email is already registered!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });
                    } else {
                        password2.setError("You have to set same password!");
                        password2.requestFocus();
                        return;
                    }
                }

            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, LogIn.class);
                startActivity(intent);
            }
        });
    }

    public void sendEmailVerification() {
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUp.this, "Registration Successful, Please verify your email!", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(SignUp.this, LogIn.class));
                    } else {
                        Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
