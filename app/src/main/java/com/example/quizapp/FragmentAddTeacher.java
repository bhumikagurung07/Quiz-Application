package com.example.quizapp;
//FragmentAddTeacher


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class FragmentAddTeacher extends Fragment {

    private EditText teacherName, username, password;
    private Button addTeacherButton;
    private static final String URL = "http://192.168.17.97/quizapp/add_teacher.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_teacher, container, false);

        teacherName = view.findViewById(R.id.Teacher_name);
        username = view.findViewById(R.id.Username);
        password = view.findViewById(R.id.Password);
        addTeacherButton = view.findViewById(R.id.add_teacher);

        addTeacherButton.setOnClickListener(v -> addTeacher());

        return view;
    }

    private void addTeacher() {
        String name = teacherName.getText().toString().trim();
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (name.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(requireContext());

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> {
                    Toast.makeText(requireContext(), "Teacher added successfully!", Toast.LENGTH_SHORT).show();
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.admin_fragment_container, new FragmentTeacher());
                    transaction.commit();
                },
                error -> Toast.makeText(requireContext(), "Failed to add teacher", Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("fullname", name);
                params.put("username", user);
                params.put("password", pass);
                return params;
            }
        };

        queue.add(request);
    }
}
