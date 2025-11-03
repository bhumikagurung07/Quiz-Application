package com.example.quizapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class FragmentTeacher extends Fragment {

    private GridView teacherListView;
    private EditText searchTeacher;
    private FloatingActionButton addTeacherButton;
    private List<Teacher> teacherList;
    private TeacherAdapter
            adapter;

    // Use 10.0.2.2 for Emulator, keep 192.168.1.6 for real device
    private static final String URL = "http://192.168.17.97/quizapp/get_teachers.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher, container, false);

        teacherListView = view.findViewById(R.id.teacher_grid_view);
        searchTeacher = view.findViewById(R.id.search_teacher);
        addTeacherButton = view.findViewById(R.id.add_teacher_button);

        teacherList = new ArrayList<>();
        adapter = new TeacherAdapter(requireContext(), teacherList);
        teacherListView.setAdapter(adapter);

        loadTeachers();

        addTeacherButton.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
           transaction.replace(R.id.admin_fragment_container, new FragmentAddTeacher());
            transaction.addToBackStack(null);
           transaction.commit();
        });

        searchTeacher.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTeachers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void loadTeachers() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> {
                    teacherList.clear();
                    Log.d("TeacherFragment", "Data received: " + response.toString());

                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject teacher = response.getJSONObject(i);
                            String techId = teacher.getString("tech_id");
                            String fullname = teacher.getString("fullname");
                            String username = teacher.getString("username");
                            String password = teacher.getString("password");

                            teacherList.add(new Teacher(techId, fullname, username, password));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("TeacherFragment", "Volley Error: " + error.toString());
                    Toast.makeText(requireContext(), "Failed to fetch data. Check server logs.", Toast.LENGTH_LONG).show();
                });

        queue.add(request);
    }

    private void filterTeachers(String text) {
        List<Teacher> filteredList = new ArrayList<>();
        for (Teacher teacher : teacherList) {
            if (teacher.getFullName().toLowerCase().contains(text.toLowerCase()) ||
                    teacher.getUsername().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(teacher);
            }
        }
        adapter = new TeacherAdapter(requireContext(), filteredList);
        teacherListView.setAdapter(adapter);
    }
}
