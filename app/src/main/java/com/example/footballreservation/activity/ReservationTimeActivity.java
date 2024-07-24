package com.example.footballreservation.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.footballreservation.R;
import com.example.footballreservation.data.DatabaseHelper;
import com.example.footballreservation.model.Field;
import com.example.footballreservation.model.Reservation;
import com.example.footballreservation.util.UserSession;

import java.util.Calendar;
import java.util.List;

public class ReservationTimeActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private TimePicker startTimePicker, endTimePicker;
    private Button btnConfirmReservation;
    private int fieldId;
    private DatabaseHelper dbHelper;
    private UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_time);

        fieldId = getIntent().getIntExtra("FIELD_ID", -1);
        if (fieldId == -1) {
            Toast.makeText(this, "Error: Invalid field", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dbHelper = new DatabaseHelper(this);
        userSession = new UserSession(this);

        datePicker = findViewById(R.id.datePicker);
        startTimePicker = findViewById(R.id.startTimePicker);
        endTimePicker = findViewById(R.id.endTimePicker);
        btnConfirmReservation = findViewById(R.id.btnConfirmReservation);
        btnConfirmReservation.setOnClickListener(v -> confirmReservation());
    }

    private void confirmReservation() {
        if (!validateInputs()) {
            return;
        }

        String date = String.format("%d-%02d-%02d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth());
        String startTime = String.format("%02d:%02d", startTimePicker.getHour(), startTimePicker.getMinute());
        String endTime = String.format("%02d:%02d", endTimePicker.getHour(), endTimePicker.getMinute());

        if (dbHelper.checkConflictingReservation(fieldId, date, startTime, endTime)) {
            Toast.makeText(this, "This time slot is already reserved", Toast.LENGTH_SHORT).show();
            return;
        }

        Reservation reservation = createReservation(date, startTime, endTime);
        long result = dbHelper.addReservation(reservation);

        if (result != -1) {
            Toast.makeText(this, "Reservation confirmed!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to create reservation", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs() {
        Calendar now = Calendar.getInstance();
        Calendar selectedDate = Calendar.getInstance();

        int startHour = startTimePicker.getHour();
        int endHour = endTimePicker.getHour();
        int startMinute = startTimePicker.getMinute();
        int endMinute = endTimePicker.getMinute();
        selectedDate.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),startHour,startMinute);
        if (selectedDate.before(now)) {
            Toast.makeText(this, "Please select a future date", Toast.LENGTH_SHORT).show();
            return false;
        }


        //System.out.println("[+++++++++++++++]  "+startHour);
        if (startHour > endHour || (startHour == endHour && startMinute >= endMinute)) {
            Toast.makeText(this, "End time must be after start time", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private Reservation createReservation(String date, String startTime, String endTime) {
        List<Field> fields = dbHelper.getAllFields();
        Field selectedField = null;
        for (Field field : fields) {
            if (field.getId() == fieldId) {
                selectedField = field;
                break;
            }
        }

        if (selectedField == null) {
            throw new IllegalStateException("Selected field not found");
        }

        // Calculate total price (assuming 1-hour increments)
        int startHour = Integer.parseInt(startTime.split(":")[0]);
        int endHour = Integer.parseInt(endTime.split(":")[0]);
        int hours = endHour - startHour;
        double totalPrice = hours * selectedField.getPricePerHour();

        int userId = Integer.parseInt(userSession.getUserId());

        return new Reservation(
                0, // ID will be set by the database
                userId,
                fieldId,
                date,
                startTime,
                endTime,
                totalPrice,
                "Pending"
        );
    }
}