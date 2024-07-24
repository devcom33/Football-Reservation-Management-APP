package com.example.footballreservation.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.footballreservation.R;
import com.example.footballreservation.adapter.FieldAdapter;
import com.example.footballreservation.adapter.OnReserveClickListener;
import com.example.footballreservation.data.DatabaseHelper;
import com.example.footballreservation.model.Field;
import java.util.List;
import java.util.concurrent.Executors;

public class ManageFieldsFragment extends Fragment implements OnReserveClickListener {

    private EditText etFieldName, etFieldDescription, etFieldPrice, etFieldImageUrl;
    private Switch swFieldAvailability;
    private Button btnAddField;
    private RecyclerView rvFields;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_fields, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        initializeViews(view);
        btnAddField.setOnClickListener(v -> addField());
        loadFields();

        return view;
    }

    private void initializeViews(View view) {
        etFieldName = view.findViewById(R.id.etFieldName);
        etFieldDescription = view.findViewById(R.id.etFieldDescription);
        etFieldPrice = view.findViewById(R.id.etFieldPrice);
        etFieldImageUrl = view.findViewById(R.id.etFieldImageUrl);
        swFieldAvailability = view.findViewById(R.id.swFieldAvailability);
        btnAddField = view.findViewById(R.id.btnAddField);
        rvFields = view.findViewById(R.id.rvFields);
    }

    private void addField() {
        String fieldName = etFieldName.getText().toString().trim();
        String fieldDescription = etFieldDescription.getText().toString().trim();
        String fieldPriceStr = etFieldPrice.getText().toString().trim();
        String fieldImageUrl = etFieldImageUrl.getText().toString().trim();
        boolean isAvailable = swFieldAvailability.isChecked();

        Log.d("ManageFieldsFragment", "Attempting to add field: " + fieldName);

        if (fieldName.isEmpty() || fieldDescription.isEmpty() || fieldPriceStr.isEmpty() || fieldImageUrl.isEmpty()) {
            showToast("All fields must be filled");
            Log.d("ManageFieldsFragment", "Field not added: Empty fields");
            return;
        }

        double fieldPrice;
        try {
            fieldPrice = Double.parseDouble(fieldPriceStr);
        } catch (NumberFormatException e) {
            showToast("Invalid price format");
            Log.d("ManageFieldsFragment", "Field not added: Invalid price format");
            return;
        }

        Field newField = new Field(fieldName, fieldDescription, fieldPrice, isAvailable, fieldImageUrl);
        Log.d("ManageFieldsFragment", "Created new Field object: " + newField.toString());

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                long result = dbHelper.addField(newField);
                Log.d("ManageFieldsFragment", "Database insert result: " + result);
                if (result != -1) {
                    requireActivity().runOnUiThread(() -> {
                        clearInputFields();
                        showToast("Field added successfully");
                        loadFields();
                        Log.d("ManageFieldsFragment", "Field added successfully");
                    });
                } else {
                    requireActivity().runOnUiThread(() -> {
                        showToast("Failed to add field");
                        Log.d("ManageFieldsFragment", "Failed to add field to database");
                    });
                }
            } catch (Exception e) {
                Log.e("ManageFieldsFragment", "Error adding field", e);
                requireActivity().runOnUiThread(() -> showToast("Error adding field"));
            }
        });
    }

    private void loadFields() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Field> fields = dbHelper.getAllFields();
            requireActivity().runOnUiThread(() -> {
                if (fields.isEmpty()) {
                    showInfoMessage("No fields available");
                } else {
                    updateFieldList(fields);
                }
            });
        });
    }
    private void showInfoMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void updateFieldList(List<Field> fields) {
        FieldAdapter adapter = new FieldAdapter(requireContext(), fields, this);
        rvFields.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvFields.setAdapter(adapter);
    }

    private void clearInputFields() {
        etFieldName.setText("");
        etFieldDescription.setText("");
        etFieldPrice.setText("");
        etFieldImageUrl.setText("");
        swFieldAvailability.setChecked(true);
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onReserveClick(Field field) {
        Toast.makeText(requireContext(), "Reserved field: " + field.getName(), Toast.LENGTH_SHORT).show();
    }
}