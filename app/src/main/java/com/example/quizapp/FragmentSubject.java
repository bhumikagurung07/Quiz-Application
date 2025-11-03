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

public class FragmentSubject extends Fragment {

    private GridView subjectGridView;
    private SubjectAdapter subjectAdapter;
    private List<Subject> subjectList;
    private FloatingActionButton fabAddSubject;
    private static final String URL = "http://192.168.17.97/quizapp/fetch_subject.php"; // Update with your PHP script URL

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subject, container, false);

        subjectGridView = view.findViewById(R.id.SubjectGridView);
        fabAddSubject = view.findViewById(R.id.add_subject_button);
        subjectList = new ArrayList<>();
        subjectAdapter = new SubjectAdapter(requireContext(), subjectList);
        subjectGridView.setAdapter(subjectAdapter);

        loadSubjects();

        // Floating Action Button to open FragmentAddSubject
        fabAddSubject.setOnClickListener(v -> openAddSubjectFragment());

        return view;
    }

    private void loadSubjects() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        subjectList.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject subject = response.getJSONObject(i);
                                String subId = subject.getString("sub_id");
                                String subName = subject.getString("sub_name");
                                String deptId = subject.getString("dept_id");
                                String techId = subject.getString("tech_id");
                                String imageUrl = subject.getString("image");  // Ensure correct URL from PHP

                                // Check if the image URL is valid
                                if (!imageUrl.isEmpty() && imageUrl.startsWith("http")) {
                                    subjectList.add(new Subject(subId, subName, deptId, techId, imageUrl));
                                } else {
                                    Toast.makeText(requireContext(), "Invalid Image URL", Toast.LENGTH_SHORT).show();
                                }
                            }
                            subjectAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Error Parsing Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireContext(), "Failed to Load Subjects", Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(request);
    }

    private void openAddSubjectFragment() {
        FragmentAddSubject fragmentAddSubject = new FragmentAddSubject();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.admin_fragment_container, fragmentAddSubject);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
