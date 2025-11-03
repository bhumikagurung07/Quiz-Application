package com.example.quizapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentAddSubject extends Fragment {

    private EditText editTextSubId, editTextSubName;
    private Spinner spinnerDept, spinnerTech;
    private ImageView imagePreview;
    private Button btnAddSubject;
    private Bitmap selectedImageBitmap;

    private ArrayList<String> deptIds = new ArrayList<>();
    private ArrayList<String> deptNames = new ArrayList<>();
    private ArrayList<String> techIds = new ArrayList<>();
    private ArrayList<String> techNames = new ArrayList<>();

    private static final String FETCH_DEPARTMENTS_URL = "http://192.168.17.97/quizapp/fetch_department.php";
    private static final String FETCH_TEACHERS_URL = "http://192.168.17.97/quizapp/get_teachers.php";
    private static final String ADD_SUBJECT_URL = "http://192.168.17.97/quizapp/add_subject.php";
    private static final int IMAGE_PICK_CODE = 1000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_subject, container, false);

        editTextSubId = view.findViewById(R.id.editTextSubId);
        editTextSubName = view.findViewById(R.id.editTextSubName);
        spinnerDept = view.findViewById(R.id.spinnerDept);
        spinnerTech = view.findViewById(R.id.spinnerTech);
        imagePreview = view.findViewById(R.id.imagePreview);
        btnAddSubject = view.findViewById(R.id.btnAddSubject);

        loadDepartments();
        loadTeachers();

        imagePreview.setOnClickListener(v -> pickImageFromGallery());
        btnAddSubject.setOnClickListener(v -> addSubject());

        return view;
    }

    private void loadDepartments() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        StringRequest request = new StringRequest(Request.Method.GET, FETCH_DEPARTMENTS_URL,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        deptIds.clear();
                        deptNames.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String deptId = obj.getString("dept_id");
                            String deptName = obj.getString("dept_name");

                            deptIds.add(deptId);
                            deptNames.add(deptName);
                        }

                        // Debug Log
                        Log.d("DEPARTMENT_NAMES", deptNames.toString());

                        if (getActivity() != null) {
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, deptNames);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerDept.setAdapter(adapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error parsing department data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(requireContext(), "Failed to load departments", Toast.LENGTH_SHORT).show());
        queue.add(request);
    }

    private void loadTeachers() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        StringRequest request = new StringRequest(Request.Method.GET, FETCH_TEACHERS_URL,
                response -> {
                    try {
                        Log.d("API_RESPONSE_TEACHERS", response); // Log API response

                        JSONArray jsonArray = new JSONArray(response);
                        techIds.clear();
                        techNames.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String techId = obj.getString("tech_id");
                            String techName = obj.getString("fullname");

                            techIds.add(techId);
                            techNames.add(techName);
                        }

                        Log.d("TECH_NAMES", "Fetched: " + techNames.toString()); // Debugging

                        if (getActivity() != null) {
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, techNames);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerTech.setAdapter(adapter);
                        }
                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Error parsing teachers JSON", e);
                        Toast.makeText(requireContext(), "Error parsing teacher data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("API_ERROR_TEACHERS", "Failed to load teachers", error);
                    Toast.makeText(requireContext(), "Failed to load teachers", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }


    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null) {
            try {
                Uri imageUri = data.getData();
                InputStream imageStream = requireActivity().getContentResolver().openInputStream(imageUri);
                selectedImageBitmap = BitmapFactory.decodeStream(imageStream);
                imagePreview.setImageBitmap(selectedImageBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void addSubject() {
        if (selectedImageBitmap == null) {
            Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        String subId = editTextSubId.getText().toString().trim();
        String subName = editTextSubName.getText().toString().trim();
        int deptIndex = spinnerDept.getSelectedItemPosition();
        int techIndex = spinnerTech.getSelectedItemPosition();

        if (deptIndex < 0 || techIndex < 0) {
            Toast.makeText(requireContext(), "Please select department and teacher", Toast.LENGTH_SHORT).show();
            return;
        }

        String deptId = deptIds.get(deptIndex);
        String techId = techIds.get(techIndex);
        String encodedImage = encodeImageToBase64(selectedImageBitmap);

        if (subId.isEmpty() || subName.isEmpty()) {
            Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        StringRequest request = new StringRequest(Request.Method.POST, ADD_SUBJECT_URL,
                response -> {
                    if (response.trim().equals("success")) {
                        Toast.makeText(requireContext(), "Subject added successfully", Toast.LENGTH_SHORT).show();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(requireContext(), "Failed to add subject", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(requireContext(), "Error connecting to server", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("sub_id", subId);
                params.put("sub_name", subName);
                params.put("dept_id", deptId);
                params.put("tech_id", techId);
                params.put("image", encodedImage);
                return params;
            }
        };
        queue.add(request);
    }
}
