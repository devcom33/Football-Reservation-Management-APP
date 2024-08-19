package com.example.footballreservation.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.footballreservation.adapter.FieldAdminActionListener;
import com.google.android.material.switchmaterial.SwitchMaterial;

import android.widget.ImageView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.footballreservation.R;
import com.example.footballreservation.adapter.FieldAdapterAdmin;
import com.example.footballreservation.adapter.OnReserveClickListener;
import com.example.footballreservation.data.DatabaseHelper;
import com.example.footballreservation.model.Field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ManageFieldsFragment extends Fragment implements FieldAdminActionListener {
    private FieldAdapterAdmin adapter;
    private List<Field> fields;

    private EditText etFieldName, etFieldDescription, etFieldPrice, etFieldImageUrl;
    private SwitchMaterial swFieldAvailability;
    private Button btnAddField;
    private RecyclerView rvFields;
    private DatabaseHelper dbHelper;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView ivFieldImage;
    private Uri imageUri;
    Button btnSelectImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_fields, container, false);

        dbHelper = new DatabaseHelper(requireContext());
        fields = new ArrayList<>();
        initializeViews(view);
        btnSelectImage.setOnClickListener(v -> openImagePicker());
        btnAddField.setOnClickListener(v -> addField());
        loadFields();

        return view;
    }


    private void initializeViews(View view) {
        etFieldName = view.findViewById(R.id.etFieldName);
        etFieldDescription = view.findViewById(R.id.etFieldDescription);
        etFieldPrice = view.findViewById(R.id.etFieldPrice);
        swFieldAvailability = view.findViewById(R.id.swFieldAvailability);
        btnAddField = view.findViewById(R.id.btnAddField);
        rvFields = view.findViewById(R.id.rvFields);
        ivFieldImage = view.findViewById(R.id.ivFieldImage);
        btnSelectImage = view.findViewById(R.id.btnSelectImage);
    }

    private void addField() {
        String fieldName = etFieldName.getText().toString().trim();
        String fieldDescription = etFieldDescription.getText().toString().trim();
        String fieldPriceStr = etFieldPrice.getText().toString().trim();
        boolean isAvailable = swFieldAvailability.isChecked();

        Log.d("ManageFieldsFragment", "Attempting to add field: " + fieldName);

        if (fieldName.isEmpty() || fieldDescription.isEmpty() || fieldPriceStr.isEmpty() || imageUri == null) {
            showToast("All fields must be filled and an image must be selected");
            Log.d("ManageFieldsFragment", "Field not added: Empty fields or no image");
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

        // Here, instead of using the URL, we'll use the imageUri
        Field newField = new Field(fieldName, fieldDescription, fieldPrice, isAvailable, imageUri.toString());
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
    @Override
    public void onDeleteClick(Field field) {
        // Implement delete logic here
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Field")
                .setMessage("Are you sure you want to delete this field?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    deleteField(field);
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteField(Field field) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                boolean deleted = dbHelper.deleteField(field.getId());
                requireActivity().runOnUiThread(() -> {
                    if (deleted) {
                        int index = fields.indexOf(field);
                        if (index != -1) {
                            fields.remove(index);
                            adapter.notifyItemRemoved(index);
                        }
                        Toast.makeText(requireContext(), "Field deleted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Failed to delete field", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.e("ManageFieldsFragment", "Error deleting field", e);
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Error occurred while deleting field", Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
    private void loadFields() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Field> loadedFields = dbHelper.getAllFields();
            requireActivity().runOnUiThread(() -> {
                fields.clear();
                fields.addAll(loadedFields);
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
        if (adapter == null) {
            adapter = new FieldAdapterAdmin(requireContext(), fields, this);
            rvFields.setLayoutManager(new LinearLayoutManager(requireContext()));
            rvFields.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void clearInputFields() {
        etFieldName.setText("");
        etFieldDescription.setText("");
        etFieldPrice.setText("");
        swFieldAvailability.setChecked(true);
        ivFieldImage.setImageResource(android.R.color.darker_gray);
        imageUri = null;
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }


    //here for open the image picker
    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Field Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                ivFieldImage.setImageBitmap(MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri));
            } catch (IOException e) {
                e.printStackTrace();
                showToast("Failed to load image");
            }
        }
    }
}