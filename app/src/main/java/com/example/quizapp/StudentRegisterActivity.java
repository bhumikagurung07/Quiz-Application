package com.example.quizapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class StudentRegisterActivity extends AppCompatActivity {

    private EditText etSpid, etPassword, etName, etPhone, etDob;
    private Spinner spinnerDepartment;
    private ImageView ivStudentImage;
    private Button btnRegister;
    private String selectedDeptId;
    private Bitmap selectedImageBitmap;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String REGISTER_URL = "http://192.168.17.97/quizapp/register_student.php";
    private static final String FETCH_DEPARTMENTS_URL = "http://192.168.17.97/quizapp/fetch_department.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        etSpid = findViewById(R.id.et_spid);
        etPassword = findViewById(R.id.et_password);
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etDob = findViewById(R.id.et_dob);
        spinnerDepartment = findViewById(R.id.spinner_department);
        ivStudentImage = findViewById(R.id.iv_student_image);
        btnRegister = findViewById(R.id.btn_register);

        loadDepartments();

        ivStudentImage.setOnClickListener(v -> selectImage());

        btnRegister.setOnClickListener(v -> registerStudent());
        etDob.setOnClickListener(v -> showDatePickerDialog());

        TextView tvLogin = findViewById(R.id.tv_login);
        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(StudentRegisterActivity.this, StudentLoginActivity.class);
            startActivity(intent);
        });
    }
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = selectedYear + "-" +
                            String.format("%02d", selectedMonth + 1) + "-" +
                            String.format("%02d", selectedDay);
                    etDob.setText(formattedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }


    private void loadDepartments() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FETCH_DEPARTMENTS_URL,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        ArrayList<String> deptNames = new ArrayList<>();
                        ArrayList<String> deptIds = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            deptIds.add(obj.getString("dept_id"));
                            deptNames.add(obj.getString("dept_name"));
                        }

                        if (deptIds.isEmpty()) {
                            Toast.makeText(this, "No departments found", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, deptNames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDepartment.setAdapter(adapter);

                        // Default selection
                        selectedDeptId = deptIds.get(0);

                        spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                selectedDeptId = deptIds.get(position);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) { }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing departments", Toast.LENGTH_SHORT).show();
                    }
                }, error -> Toast.makeText(this, "Failed to load departments", Toast.LENGTH_SHORT).show());

        queue.add(stringRequest);
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ivStudentImage.setImageBitmap(selectedImageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Image selection failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void registerStudent() {
        String spid = etSpid.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String dob = etDob.getText().toString().trim();

        if (spid.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty() || dob.isEmpty() || selectedDeptId == null) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidPhoneNumber(phone)) {
            Toast.makeText(this, "Enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageBitmap == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        String imageString = encodeImage(selectedImageBitmap);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");

                        if (success) {
                            // Retrieve data from API response
                            String sId = jsonObject.getString("s_id");
                            String deptId = jsonObject.getString("dept_id");
                            String imagePath = jsonObject.getString("image");

                            //Store in SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("s_id", sId);
                            editor.putString("dept_id", deptId);
                            editor.putString("image", imagePath);
                            editor.apply();


                            Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                            // Redirect to Student Login Activity
                            Intent intent = new Intent(StudentRegisterActivity.this, StudentLoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Handle "SPID not found" case
                            String message = jsonObject.getString("message");
                            if (message.equals("SPID not found")) {
                                Toast.makeText(this, "No such SPID found. Please contact admin.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(this,"Your registration is already completed! please login",Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Registration Failed. Please try again.", Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("spid", spid);
                params.put("password", password);
                params.put("stu_name", name);
                params.put("phone_no", phone);
                params.put("dob", dob);
                params.put("dept_id", selectedDeptId);
                params.put("image", imageString);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

    // Helper method for phone number validation
    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{10}");
    }
    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }
}