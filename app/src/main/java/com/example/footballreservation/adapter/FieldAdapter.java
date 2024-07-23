package com.example.footballreservation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.footballreservation.R;
import com.example.footballreservation.model.Field;
import java.util.List;

public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.ViewHolder> {

    private List<Field> fields;
    private Context context;

    public FieldAdapter(Context context, List<Field> fields) {
        this.context = context;
        this.fields = fields;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_field, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Field field = fields.get(position);
        holder.tvFieldName.setText(field.getName());
        holder.tvFieldType.setText("Description: " + field.getType()); // This line matches the XML
        holder.tvFieldPrice.setText("Price per hour: DH" + field.getPricePerHour());
    }


    @Override
    public int getItemCount() {
        return fields.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFieldName, tvFieldType, tvFieldPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFieldName = itemView.findViewById(R.id.tvFieldName);
            tvFieldType = itemView.findViewById(R.id.tvFieldType);
            tvFieldPrice = itemView.findViewById(R.id.tvFieldPrice);
        }
    }
}
