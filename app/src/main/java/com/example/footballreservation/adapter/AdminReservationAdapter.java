// AdminReservationAdapter.java
package com.example.footballreservation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.footballreservation.R;
import com.example.footballreservation.data.DatabaseHelper;
import com.example.footballreservation.model.Field;
import com.example.footballreservation.model.Reservation;
import java.util.List;

public class AdminReservationAdapter extends RecyclerView.Adapter<AdminReservationAdapter.ViewHolder> {

    private List<Reservation> reservations;
    private Context context;
    private OnReservationActionListener actionListener;
    private DatabaseHelper dbHelper;

    public interface OnReservationActionListener {
        void onValidate(Reservation reservation);
        void onCancel(Reservation reservation);
    }

    public AdminReservationAdapter(Context context, List<Reservation> reservations, OnReservationActionListener listener, DatabaseHelper dbHelper) {
        this.context = context;
        this.reservations = reservations;
        this.actionListener = listener;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_reservation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);
        Field field = dbHelper.getFieldById(reservation.getFieldId());
        String userName = dbHelper.getUserNameById(reservation.getUserId());
        holder.tvFieldName.setText("Field: "+ (field != null ? field.getName() : "Unknown"));
        holder.tvUserName.setText("User: " + userName);
        holder.tvDate.setText("Date: " + reservation.getDate());
        holder.tvTime.setText("Time: " + reservation.getStartTime() + " - " + reservation.getEndTime());
        holder.tvStatus.setText("Status: " + reservation.getStatus());

        holder.btnValidate.setOnClickListener(v -> actionListener.onValidate(reservation));
        holder.btnCancel.setOnClickListener(v -> actionListener.onCancel(reservation));

        // Disable buttons if the reservation is not pending
        boolean isPending = reservation.getStatus().equalsIgnoreCase("Pending");
        holder.btnValidate.setEnabled(isPending);
        holder.btnCancel.setEnabled(isPending);
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFieldName, tvDate, tvTime, tvStatus, tvUserName;
        Button btnValidate, btnCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFieldName = itemView.findViewById(R.id.tvFieldName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            btnValidate = itemView.findViewById(R.id.btnValidate);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }
}