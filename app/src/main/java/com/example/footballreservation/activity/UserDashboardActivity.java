// UserDashboardActivity.java
package com.example.footballreservation.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.footballreservation.R;
import com.example.footballreservation.adapter.ReservationAdapter;
import com.example.footballreservation.data.DatabaseHelper;
import com.example.footballreservation.model.Reservation;
import com.example.footballreservation.util.UserSession;

import java.util.List;

public class UserDashboardActivity extends AppCompatActivity {

    private RecyclerView rvReservations;
    private Button btnNewReservation;
    private TextView tvWelcome;
    private DatabaseHelper dbHelper;
    private UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        dbHelper = new DatabaseHelper(this);
        session = new UserSession(this);

        rvReservations = findViewById(R.id.rvReservations);
        btnNewReservation = findViewById(R.id.btnNewReservation);
        tvWelcome = findViewById(R.id.tvWelcome);

        tvWelcome.setText("Welcome, " + session.getUsername() + "!");

        loadReservations();

        btnNewReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDashboardActivity.this, NewReservationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReservations();
    }

    private void loadReservations() {
        try {
            int userId = Integer.parseInt(session.getUserId());
            List<Reservation> reservations = dbHelper.getReservationsByUser(userId);
            ReservationAdapter adapter = new ReservationAdapter(this, reservations);
            rvReservations.setLayoutManager(new LinearLayoutManager(this));
            rvReservations.setAdapter(adapter);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Error: Invalid user ID", Toast.LENGTH_SHORT).show();
            // Handle the error appropriately, maybe log out the user or show an error message
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        session.logoutUser();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(UserDashboardActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}