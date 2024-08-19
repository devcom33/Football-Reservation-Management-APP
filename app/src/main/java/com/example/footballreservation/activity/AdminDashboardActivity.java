package com.example.footballreservation.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.footballreservation.R;
import com.example.footballreservation.data.DatabaseHelper;
import com.example.footballreservation.fragment.ReservationsFragment;
import com.example.footballreservation.fragment.ManageFieldsFragment;
import com.example.footballreservation.fragment.StatisticsFragment;
import com.example.footballreservation.util.UserSession;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AdminDashboardActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        dbHelper = new DatabaseHelper(this);
        session = new UserSession(this);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);

        loadFragment(new StatisticsFragment());
    }

    private NavigationBarView.OnItemSelectedListener navListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    int itemId = item.getItemId();

                    if (itemId == R.id.nav_statistics) {
                        selectedFragment = new StatisticsFragment();
                    } else if (itemId == R.id.nav_manage_fields) {
                        selectedFragment = new ManageFieldsFragment();
                    } else if (itemId == R.id.nav_reservations) {
                        selectedFragment = new ReservationsFragment();
                    } else if (itemId == R.id.nav_logout) {
                        logoutUser();
                        return true;
                    }

                    if (selectedFragment != null) {
                        loadFragment(selectedFragment);
                    }

                    return true;
                }
            };

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void logoutUser() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        session.logoutUser();
                        Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}