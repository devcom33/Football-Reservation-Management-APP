package com.example.footballreservation.activity;
import com.example.footballreservation.R;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.footballreservation.data.DatabaseHelper;
import com.example.footballreservation.fragment.StatisticsFragment;
import com.example.footballreservation.fragment.ManageFieldsFragment;
import com.example.footballreservation.fragment.ReservationsFragment;
import com.example.footballreservation.util.UserSession;
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

        // Load the default fragment
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_view_reservations) {
            // TODO: Implement view all reservations functionality
            return true;
        } else if (id == R.id.action_logout) {
            logoutUser();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        session.logoutUser();
        startActivity(new Intent(AdminDashboardActivity.this, LoginActivity.class));
        finish();
    }
}