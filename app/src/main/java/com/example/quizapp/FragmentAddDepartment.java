package com.example.quizapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FragmentAddDepartment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText editDeptId, editDeptName;
    private ImageView imageView;
    private Bitmap bitmap;
    private Uri filePath;
    private String uploadUrl = "http://192.168.17.97/quizapp/add_department.php"; // PHP script URL

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_department, container, false);

        editDeptId = view.findViewById(R.id.edit_dept_id);
        editDeptName = view.findViewById(R.id.edit_dept_name);
        imageView = view.findViewById(R.id.image_department);
        Button btnSubmit = view.findViewById(R.id.btn_add_department);

        imageView.setOnClickListener(v -> openGallery());

        btnSubmit.setOnClickListener(v -> {
            if (validateInputs()) {
                uploadDepartment();
            }
        });

        return view;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validateInputs() {
        if (editDeptId.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Enter Department ID", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (editDeptName.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "Enter Department Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (bitmap == null) {
            Toast.makeText(getContext(), "Select an Image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void uploadDepartment() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadUrl,
                response -> {
                    if (response.trim().equals("Success")) {
                        Toast.makeText(getContext(), "Department Added Successfully!", Toast.LENGTH_LONG).show();
                        getParentFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(getContext(), "Upload Failed: " + response, Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Upload Failed: " + error.getMessage(), Toast.LENGTH_LONG).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("dept_id", editDeptId.getText().toString().trim());
                params.put("dept_name", editDeptName.getText().toString().trim());
                params.put("image", imageToString(bitmap));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}
