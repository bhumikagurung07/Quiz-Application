package com.example.quizapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentStuHome extends Fragment {

    private GridView departmentGridView;
    private DepartmentAdapter departmentAdapter;
    private List<Department> departmentList;
    private static final String URL = "http://192.168.17.97/quizapp/fetch_department.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stu_home, container, false);

        departmentGridView = view.findViewById(R.id.DepartmentGridView);
        departmentList = new ArrayList<>();
        departmentAdapter = new DepartmentAdapter(requireContext(), departmentList);
        departmentGridView.setAdapter(departmentAdapter);

        loadDepartments();

        departmentGridView.setOnItemClickListener((parent, view1, position, id) -> {
            Department selectedDept = departmentList.get(position);
            String deptId = selectedDept.getDeptId();

            if (deptId != null && !deptId.isEmpty()) {
                FragmentStuSubject fragmentStuSubject = new FragmentStuSubject();
                Bundle bundle = new Bundle();
                bundle.putString("dept_id", deptId);
                fragmentStuSubject.setArguments(bundle);

                try {
                    FragmentTransaction transaction = requireActivity()
                            .getSupportFragmentManager()
                            .beginTransaction();
                    transaction.replace(R.id.student_fragment_container, fragmentStuSubject); // Make sure this ID exists!
                    transaction.addToBackStack(null);
                    transaction.commit();
                } catch (Exception e) {
                    Log.e("FragmentTransaction", "Error: ", e);
                    Toast.makeText(getContext(), "Error loading subject fragment.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Invalid Department ID", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void loadDepartments() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> {
                    departmentList.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject department = response.getJSONObject(i);
                            String deptId = department.getString("dept_id");
                            String deptName = department.getString("dept_name");
                            String imageUrl = department.getString("image");

                            departmentList.add(new Department(deptId, deptName, imageUrl));
                        }
                        departmentAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Parsing error", e);
                        Toast.makeText(requireContext(), "Error Parsing Data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("VOLLEY_ERROR", "Fetch Error", error);
                    Toast.makeText(requireContext(), "Failed to Load Departments", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }
}
