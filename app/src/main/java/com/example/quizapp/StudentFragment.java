package com.example.quizapp;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.quizapp.StudentAdapter;
import com.example.quizapp.StudentModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentFragment extends Fragment {

    private GridView gridViewStudents;
    private FloatingActionButton fabAddStudent;
    private List<StudentModel> studentList;
    private StudentAdapter studentAdapter;

    private final String URL = "http://192.168.17.97/quizapp/fetch_registered_students.php"; // Replace with your URL

    public StudentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student, container, false);

        gridViewStudents = view.findViewById(R.id.gridViewStudents);
        fabAddStudent = view.findViewById(R.id.fabAddStudent);
        studentList = new ArrayList<>();
        studentAdapter = new StudentAdapter(getContext(), studentList);
        gridViewStudents.setAdapter(studentAdapter);


        loadStudentData();

        fabAddStudent.setOnClickListener(v -> {
            FragmentAddStudent addStudentFragment = new FragmentAddStudent();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.admin_fragment_container, addStudentFragment) // 🔁 Use your container ID here
                    .addToBackStack(null)
                    .commit();
        });


        return view;
    }

    private void loadStudentData() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        StringRequest request = new StringRequest(Request.Method.GET, URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.has("success") && jsonObject.getBoolean("success")) {
                            JSONArray studentArray = jsonObject.getJSONArray("students");
                            for (int i = 0; i < studentArray.length(); i++) {
                                JSONObject stu = studentArray.getJSONObject(i);
                                studentList.add(new StudentModel(
                                        stu.getString("r_id"),
                                        stu.getString("name"),
                                        stu.getString("spid")
                                ));
                            }
                            studentAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "No student data found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("StudentError", "JSON Parsing Error: " + e.getMessage());
                        Toast.makeText(getContext(), "Error parsing data", Toast.LENGTH_SHORT).show();
                    }

                },
                error -> Toast.makeText(getContext(), "Volley Error: " + error.getMessage(), Toast.LENGTH_SHORT).show());

        queue.add(request);
    }
}
