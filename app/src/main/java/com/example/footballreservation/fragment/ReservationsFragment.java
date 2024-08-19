package com.example.footballreservation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.footballreservation.R;
import com.example.footballreservation.adapter.AdminReservationAdapter;
import com.example.footballreservation.data.DatabaseHelper;
import com.example.footballreservation.model.Reservation;
import java.util.List;


public class ReservationsFragment extends Fragment implements AdminReservationAdapter.OnReservationActionListener {
    private RecyclerView recyclerView;
    private AdminReservationAdapter adapter;
    private DatabaseHelper dbHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_all_reservations, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewReservations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DatabaseHelper(getContext());
        loadReservations();

        return view;
    }
    private void loadReservations() {
        List<Reservation> reservations = dbHelper.getAllReservations();
        adapter = new AdminReservationAdapter(getContext(), reservations, this, dbHelper);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onValidate(Reservation reservation) {
        dbHelper.updateReservationStatus(reservation.getId(), "Validated");
        loadReservations(); // Refresh the list
    }

    @Override
    public void onCancel(Reservation reservation) {
        dbHelper.updateReservationStatus(reservation.getId(), "Cancelled");
        loadReservations(); // Refresh the list
    }
}