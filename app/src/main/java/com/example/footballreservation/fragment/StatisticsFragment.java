package com.example.footballreservation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.footballreservation.R;
import com.example.footballreservation.data.DatabaseHelper;
import com.example.footballreservation.model.Reservation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StatisticsFragment extends Fragment {

    private TextView tvTotalFields;
    private TextView tvTotalUsers;
    private TextView tvTotalRevenue;
    private TextView tvTodayReservations;
    private Button btnGenerateReport;

    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        dbHelper = new DatabaseHelper(getContext());

        tvTotalFields = view.findViewById(R.id.tvTotalFields);
        tvTotalUsers = view.findViewById(R.id.tvTotalUsers);
        tvTotalRevenue = view.findViewById(R.id.tvTotalRevenue);
        tvTodayReservations = view.findViewById(R.id.tvTodayReservations);

        updateDashboard();


        return view;
    }

    private void updateDashboard() {
        int totalFields = dbHelper.getAllFields().size();
        //int totalUsers = dbHelper.getAllUsers().size();
        double totalRevenue = calculateTotalRevenue();
        int todayReservations = getTodayReservationsCount();

        tvTotalFields.setText(String.valueOf(totalFields));
        tvTotalUsers.setText(String.valueOf(dbHelper.getNonAdminUserCount()));
        tvTotalRevenue.setText(String.format(Locale.getDefault(), "DH %.1f", totalRevenue));
        tvTodayReservations.setText(String.valueOf(todayReservations));
    }

    private double calculateTotalRevenue() {
        List<Reservation> allReservations = dbHelper.getAllReservations();
        double totalRevenue = 0;
        for (Reservation reservation : allReservations) {
            if ("validated".equalsIgnoreCase(reservation.getStatus())) {
                totalRevenue += reservation.getTotalPrice();
            }
        }
        return totalRevenue;
    }

    private int getTodayReservationsCount() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = sdf.format(new Date());
        List<Reservation> allReservations = dbHelper.getAllReservations();
        int count = 0;
        for (Reservation reservation : allReservations) {
            if (reservation.getDate().equals(today)) {
                count++;
            }
        }
        return count;
    }
}