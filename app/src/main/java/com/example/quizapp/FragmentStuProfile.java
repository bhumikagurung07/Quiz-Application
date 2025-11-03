package com.example.quizapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

public class FragmentStuProfile extends Fragment {

    ImageView imageProfile;
    TextView textSpid,txtDeptName;
    EditText editStudentName, editPassword, editPhone, editDob, editDept;
    Button btnSave;
    String s_id;

    private static final String PROFILE_URL = "http://192.168.17.97/quizapp/fetch_student.php?s_id=";
    private static final String UPDATE_URL = "http://192.168.17.97/quizapp/update_student.php";

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stu_profile, container, false);

        imageProfile = view.findViewById(R.id.imageProfile);
        textSpid = view.findViewById(R.id.textSpid);
        editStudentName = view.findViewById(R.id.editStudentName);
        editPassword = view.findViewById(R.id.editPassword);
        editPhone = view.findViewById(R.id.editPhone);
        editDob = view.findViewById(R.id.editDob);
        txtDeptName=view.findViewById(R.id.txtDept);
        btnSave = view.findViewById(R.id.btnSaveChanges);

        SharedPreferences prefs = getContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        s_id = prefs.getString("s_id", null);

        if (s_id != null) {
            fetchProfile();
        } else {
            Toast.makeText(getContext(), "Student ID not found in SharedPreferences", Toast.LENGTH_SHORT).show();
        }

        btnSave.setOnClickListener(v -> updateProfile());

        return view;
    }

    private void fetchProfile() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, PROFILE_URL + s_id, null,
                response -> {
                    try {
                        String spid = response.getString("spid");
                        String stuName = response.getString("stu_name");
                        String password = response.getString("password");
                        String phone = response.getString("phone_no");
                        String dob = response.getString("dob");
                        String dept = response.getString("dept_name");
                        String imageUrl = response.getString("image");

                        textSpid.setText("SPID: " + spid);
                        editStudentName.setText(stuName);
                        editPassword.setText(password);
                        editPhone.setText(phone);
                        editDob.setText(dob);
                        txtDeptName.setText(dept);

                        Glide.with(getContext())
                                .load(imageUrl)
                                .placeholder(R.drawable.stud)
                                .error(R.drawable.stud)
                                .into(imageProfile);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing profile", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(getContext(), "Failed to fetch profile", Toast.LENGTH_LONG).show();
                });

        Volley.newRequestQueue(getContext()).add(request);
    }

    private void updateProfile() {
        String name = editStudentName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String dob = editDob.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || dob.isEmpty()) {
            Toast.makeText(getContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject data = new JSONObject();
        try {
            data.put("s_id", s_id);
            data.put("stu_name", name);
            data.put("phone_no", phone);
            data.put("dob", dob);
            // Removed: data.put("dept_name", dept);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, UPDATE_URL, data,
                response -> {
                    if (response.optBoolean("success")) {
                        Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Update failed: " + response.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(getContext(), "Update failed. Server error.", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(getContext()).add(request);
    }

}
