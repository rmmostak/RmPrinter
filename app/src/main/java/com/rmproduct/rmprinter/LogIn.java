package com.rmproduct.rmprinter;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class LogIn extends AppCompatActivity {

    private EditText email, password;
    private Button login;
    private TextView forgotPassword, signUp, loginInfo;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    private long backPressedTime;
    private int counter=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        email = findViewById(R.id.email2);
        password = findViewById(R.id.password2);
        forgotPassword = findViewById(R.id.forgotPassword);
        login = findViewById(R.id.logIn);
        signUp = findViewById(R.id.signUp);
        loginInfo=findViewById(R.id.loginInfo);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            startActivity(new Intent(LogIn.this, Home.class));
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString().trim();
                String txt_password = password.getText().toString().trim();

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
                if (password.length() < 6) {
                    password.setError("Please enter valid password!");
                    password.requestFocus();
                    return;
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(LogIn.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.GONE);
                                        checkEmailVerification();
                                        /*startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                                        Toast.makeText(LogIn.this, "Login Success", Toast.LENGTH_SHORT).show();*/
                                    } else {

                                        Toast.makeText(LogIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        counter--;
                                        loginInfo.setText("Attempts remaining: " + counter);
                                        progressBar.setVisibility(View.GONE);
                                        if(counter == 0){
                                            login.setEnabled(false);
                                        }
                                    }
                                }
                            });
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogIn.this, SignUp.class);
                startActivity(intent);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, Password.class));
            }
        });


        //FirebaseMessaging.getInstance().subscribeToTopic("rm");
    }

    public void checkEmailVerification() {
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        boolean userStatus = firebaseUser.isEmailVerified();

        if (userStatus) {
            //Toast.makeText(LogIn.this, "Login Successful!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LogIn.this, Home.class));
        } else {
            Toast.makeText(LogIn.this, "Please verify your email!", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
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

}