package com.example.quizapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentDepartment extends Fragment {

    private GridView departmentGridView;
    private DepartmentAdapter departmentAdapter;
    private List<Department> departmentList;
    private FloatingActionButton fabAddDepartment;
    private static final String URL = "http://192.168.17.97/quizapp/fetch_department.php"; // Update with your PHP script URL

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_department, container, false);

        departmentGridView = view.findViewById(R.id.DepartmentGridView);
        fabAddDepartment = view.findViewById(R.id.add_teacher_button);
        departmentList = new ArrayList<>();
        departmentAdapter = new DepartmentAdapter(requireContext(), departmentList);
        departmentGridView.setAdapter(departmentAdapter);

        loadDepartments();

        // Floating Action Button to open FragmentAddDepartment
        fabAddDepartment.setOnClickListener(v -> openAddDepartmentFragment());

        return view;
    }

    private void loadDepartments() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        departmentList.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject department = response.getJSONObject(i);
                                String deptId = department.getString("dept_id");
                                String deptName = department.getString("dept_name");
                                String imageUrl = department.getString("image");  // Ensure correct URL from PHP

                                // Check if the image URL is valid
                                if (!imageUrl.isEmpty() && imageUrl.startsWith("http")) {
                                    departmentList.add(new Department(deptId, deptName, imageUrl));
                                } else {
                                    Toast.makeText(requireContext(), "Invalid Image URL", Toast.LENGTH_SHORT).show();
                                }
                            }
                            departmentAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Error Parsing Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireContext(), "Failed to Load Departments", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(request);
    }




    private void openAddDepartmentFragment() {
        FragmentAddDepartment fragmentAddDepartment = new FragmentAddDepartment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.admin_fragment_container, fragmentAddDepartment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
