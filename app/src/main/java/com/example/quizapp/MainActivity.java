package com.example.quizapp;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Toolbar
      //  Toolbar toolbar = findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
      //  getSupportActionBar().setDisplayShowTitleEnabled(false);  // Set title


        progressBar = findViewById(R.id.progressBar);
        TextView loadingText = findViewById(R.id.txtLoading);

        // Set Custom Font for Loading Text
        loadingText.setTypeface(ResourcesCompat.getFont(this, R.font.fascinate_inline));

        // Start Progress Animation
        new Thread(() -> {
            while (progressStatus < 100) {
                progressStatus += 2;
                handler.post(() -> progressBar.setProgress(progressStatus));
                try {
                    Thread.sleep(50); // Adjust loading speed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Move to Next Activity after Loading
            startActivity(new Intent(MainActivity.this, RoleActivity.class));
            finish();
        }).start();
    }
}