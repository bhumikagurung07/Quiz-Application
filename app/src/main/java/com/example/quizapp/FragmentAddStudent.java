package com.example.quizapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class FragmentAddStudent extends Fragment {

    private EditText edtRid, edtName, edtSpid;
    private Button btnSubmit;

    private final String INSERT_URL = "http://192.168.17.97/quizapp/insert_registered_student.php"; // change as needed

    public FragmentAddStudent() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_student, container, false);

        edtRid = view.findViewById(R.id.edtRid);
        edtName = view.findViewById(R.id.edtName);
        edtSpid = view.findViewById(R.id.edtSpid);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> {
            String rid = edtRid.getText().toString().trim();
            String name = edtName.getText().toString().trim();
            String spid = edtSpid.getText().toString().trim();

            if (TextUtils.isEmpty(rid) || TextUtils.isEmpty(name) || TextUtils.isEmpty(spid)) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                insertStudent(rid, name, spid);
            }
        });

        return view;
    }

    private void insertStudent(String rid, String name, String spid) {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, INSERT_URL,
                response -> {
                    if (response.contains("success")) {
                        Toast.makeText(getContext(), "Student added successfully", Toast.LENGTH_SHORT).show();
                        edtRid.setText("");
                        edtName.setText("");
                        edtSpid.setText("");
                    } else {
                        Toast.makeText(getContext(), "Failed to add student", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("r_id", rid);
                params.put("name", name);
                params.put("spid", spid);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
