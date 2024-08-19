package com.example.footballreservation.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.footballreservation.R;
import com.example.footballreservation.model.Field;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.Locale;

public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.FieldViewHolder> {

    private List<Field> fields;
    private Context context;
    private OnReserveClickListener listener;

    public FieldAdapter(Context context, List<Field> fields, OnReserveClickListener listener) {
        this.context = context;
        this.fields = fields;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_field, parent, false);
        return new FieldViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FieldViewHolder holder, int position) {
        Field field = fields.get(position);
        holder.bind(field);
    }

    @Override
    public int getItemCount() {
        return fields.size();
    }

    class FieldViewHolder extends RecyclerView.ViewHolder {
        ImageView fieldImage;
        TextView fieldName, fieldDescription, fieldPrice;
        MaterialButton btnReserve;

        FieldViewHolder(@NonNull View itemView) {
            super(itemView);
            fieldImage = itemView.findViewById(R.id.fieldImage);
            fieldName = itemView.findViewById(R.id.fieldName);
            fieldDescription = itemView.findViewById(R.id.fieldDescription);
            fieldPrice = itemView.findViewById(R.id.fieldPrice);
            btnReserve = itemView.findViewById(R.id.btnReserve);
        }

        void bind(Field field) {
            fieldName.setText(field.getName());
            fieldDescription.setText(field.getType());
            fieldPrice.setText(String.format(Locale.getDefault(), "DH %.2f per hour", field.getPricePerHour()));
            if (field.getImageUrl() != null && !field.getImageUrl().isEmpty()) {
                Uri imageUri = Uri.parse(field.getImageUrl());
                Glide.with(context)
                        .load(imageUri)
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(fieldImage);
            } else {
                fieldImage.setImageResource(R.drawable.placeholder_image);
            }
            btnReserve.setOnClickListener(v -> listener.onReserveClick(field));
        }
    }
}