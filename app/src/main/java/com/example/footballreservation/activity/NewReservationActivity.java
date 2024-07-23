package com.example.footballreservation.activity;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.footballreservation.R;
import com.example.footballreservation.data.DatabaseHelper;
import com.example.footballreservation.model.Field;
import com.example.footballreservation.model.Reservation;
import com.example.footballreservation.util.UserSession;

import java.util.List;

public class NewReservationActivity extends AppCompatActivity {

    private Spinner spinnerField;
    private DatePicker datePicker;
    private TimePicker startTimePicker, endTimePicker;
    private Button btnReserve;
    private DatabaseHelper dbHelper;
    private UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reservation);

        dbHelper = new DatabaseHelper(this);
        session = new UserSession(this);

        spinnerField = findViewById(R.id.spinnerField);
        datePicker = findViewById(R.id.datePicker);
        startTimePicker = findViewById(R.id.startTimePicker);
        endTimePicker = findViewById(R.id.endTimePicker);
        btnReserve = findViewById(R.id.btnReserve);

        loadFields();

        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createReservation();
            }
        });
    }

    private void loadFields() {
        List<Field> fields = dbHelper.getAllFields();
        ArrayAdapter<Field> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fields);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerField.setAdapter(adapter);
    }

    private void createReservation() {
        Field selectedField = (Field) spinnerField.getSelectedItem();
        String date = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
        String startTime = startTimePicker.getHour() + ":" + startTimePicker.getMinute();
        String endTime = endTimePicker.getHour() + ":" + endTimePicker.getMinute();

        // Calculate total price (assuming 1-hour increments)
        int hours = endTimePicker.getHour() - startTimePicker.getHour();
        double totalPrice = hours * selectedField.getPricePerHour();
        int userId = Integer.parseInt(session.getUserId());
        Reservation reservation = new Reservation(
                1, // ID will be set by the database
                userId,
                selectedField.getId(),
                date,
                startTime,
                endTime,
                totalPrice,
                "Pending"
        );

        long result = dbHelper.addReservation(reservation);
        if (result != -1) {
            Toast.makeText(this, "Reservation created successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to create reservation", Toast.LENGTH_SHORT).show();
        }
    }
}