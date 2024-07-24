package com.example.footballreservation.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.footballreservation.R;
import com.example.footballreservation.util.UserSession;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 3000; // 3 seconds
    private ProgressBar progressBar;
    //private TextView tvAppName;
    private UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        //tvAppName = findViewById(R.id.tvAppName);
        userSession = new UserSession(this);

        // Start with the progress bar visible and the app name invisible
        progressBar.setVisibility(View.VISIBLE);
        //tvAppName.setVisibility(View.INVISIBLE);
        userSession.logoutUser();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Hide the progress bar and show the app name
                progressBar.setVisibility(View.INVISIBLE);
                //tvAppName.setVisibility(View.VISIBLE);

                // Determine which activity to start based on login status
                Intent intent;
                if (userSession.isLoggedIn()) {

                    if (userSession.isAdmin()) {
                        intent = new Intent(MainActivity.this, AdminDashboardActivity.class);
                    } else {
                        intent = new Intent(MainActivity.this, UserDashboardActivity.class);
                    }
                } else {
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                }

                startActivity(intent);
                finish(); // Close this activity
            }
        }, SPLASH_TIME_OUT);
    }
}