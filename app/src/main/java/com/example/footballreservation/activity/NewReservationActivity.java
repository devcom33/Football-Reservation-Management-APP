package com.example.footballreservation.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.footballreservation.R;
import com.example.footballreservation.adapter.FieldAdapter;
import com.example.footballreservation.adapter.OnReserveClickListener;
import com.example.footballreservation.data.DatabaseHelper;
import com.example.footballreservation.model.Field;
import com.example.footballreservation.model.Reservation;
import com.example.footballreservation.util.UserSession;

import java.util.List;

public class NewReservationActivity extends AppCompatActivity implements OnReserveClickListener {

    private RecyclerView fieldList;

    private DatabaseHelper dbHelper;
    private UserSession session;
    private FieldAdapter fieldAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reservation);

        dbHelper = new DatabaseHelper(this);
        session = new UserSession(this);

        fieldList = findViewById(R.id.fieldList);
        loadFields();
    }

    private void loadFields() {
        List<Field> fields = dbHelper.getAllFields();
        fieldAdapter = new FieldAdapter(this,fields, this);
        fieldList.setAdapter(fieldAdapter);
    }

    @Override
    public void onReserveClick(Field field) {
        // Open a new activity or dialog to select date and time
        Intent intent = new Intent(this, ReservationTimeActivity.class);
        intent.putExtra("FIELD_ID", field.getId());
        startActivity(intent);
    }
}