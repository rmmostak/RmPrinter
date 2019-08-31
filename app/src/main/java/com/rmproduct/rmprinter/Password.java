package com.rmproduct.rmprinter;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Password extends AppCompatActivity {

    private Button resetPassword;
    private EditText email;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        resetPassword=findViewById(R.id.resetPassword);
        email=findViewById(R.id.email);
        firebaseAuth=FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email=email.getText().toString().trim();
                if (!TextUtils.isEmpty(txt_email)) {

                    firebaseAuth.sendPasswordResetEmail(txt_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                Toast.makeText(Password.this, "Password reset link has been sent to your email. Please check!", Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(Password.this, LogIn.class));
                            } else {

                                Toast.makeText(Password.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    email.setError("Please enter valid email");
                    email.requestFocus();
                }
            }
        });
    }
}
