package com.example.footballreservation.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.footballreservation.R;
import com.example.footballreservation.data.DatabaseHelper;
import com.example.footballreservation.model.Field;
import com.example.footballreservation.model.Reservation;
import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {

    private List<Reservation> reservations;
    private Context context;
    private DatabaseHelper dbHelper;

    public ReservationAdapter(Context context, List<Reservation> reservations, DatabaseHelper dbHelper) {
        this.context = context;
        this.reservations = reservations;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);
        Field field = dbHelper.getFieldById(reservation.getFieldId());
        holder.tvFieldName.setText("Field: " + (field != null ? field.getName() : "Unknown"));
        holder.tvDate.setText("Date: " + reservation.getDate());
        holder.tvTime.setText("Time: " + reservation.getStartTime() + " - " + reservation.getEndTime());
        updateStatus(holder.tvStatus, reservation.getStatus());
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    private void updateStatus(TextView tvStatus, String status) {
        GradientDrawable statusBackground = (GradientDrawable) tvStatus.getBackground();

        switch (status.toLowerCase()) {
            case "pending":
                statusBackground.setColor(ContextCompat.getColor(context, R.color.colorPending));
                tvStatus.setText("Pending");
                break;
            case "validated":
                statusBackground.setColor(ContextCompat.getColor(context, R.color.colorValidated));
                tvStatus.setText("Validated");
                break;
            case "cancelled":
                statusBackground.setColor(ContextCompat.getColor(context, R.color.colorCancelled));
                tvStatus.setText("Cancelled");
                break;
            default:
                statusBackground.setColor(ContextCompat.getColor(context, R.color.colorPending));
                tvStatus.setText(status);
                break;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFieldName, tvDate, tvTime, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFieldName = itemView.findViewById(R.id.tvFieldName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}