package com.rmproduct.rmprinter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView, textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        imageView=findViewById(R.id.imageView);
        textView=findViewById(R.id.textView);
        textView2=findViewById(R.id.textView2);

        Animation animation= AnimationUtils.loadAnimation(this, R.anim.animation);
        imageView.startAnimation(animation);
        textView.startAnimation(animation);
        textView2.startAnimation(animation);

        final Intent intent=new Intent(SplashScreen.this, LogIn.class);
        Thread timer=new Thread() {
            public void run() {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(intent);
                    finish();
                }
            }
        };

        timer.start();
    }
}
