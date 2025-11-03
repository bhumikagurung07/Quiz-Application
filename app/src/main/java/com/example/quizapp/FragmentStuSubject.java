package com.example.quizapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentStuSubject extends Fragment {

    private static final String ARG_DEPT_ID = "dept_id";
    private String deptId;
    private GridView subjectGridView;
    private StuSubjectAdapter subjectAdapter;
    private ArrayList<StuSubject> subjectList;

    private static final String FETCH_SUBJECTS_URL = "http://192.168.17.97/quizapp/fetch_stu_subject.php?dept_id=";

    public static FragmentStuSubject newInstance(String deptId) {
        FragmentStuSubject fragment = new FragmentStuSubject();
        Bundle args = new Bundle();
        args.putString(ARG_DEPT_ID, deptId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stu_subject, container, false);

        subjectGridView = view.findViewById(R.id.subjectGridView);
        subjectList = new ArrayList<>();
        subjectAdapter = new StuSubjectAdapter(requireContext(), subjectList);
        subjectGridView.setAdapter(subjectAdapter);
        subjectGridView.setOnItemClickListener((parent, view1, position, id) -> {
            StuSubject selectedSubject = subjectList.get(position);

            //sub_id in shared Preference
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("sub_id", selectedSubject.getSubjectId());
            editor.apply();


            Intent intent = new Intent(requireContext(), StartQuizActivity.class);
            intent.putExtra("sub_id", selectedSubject.getSubjectId());
            intent.putExtra("sub_name", selectedSubject.getSubjectName());
            startActivity(intent);
        });


        if (getArguments() != null) {
            deptId = getArguments().getString(ARG_DEPT_ID);
            fetchSubjects(deptId);
        }

        return view;
    }

    private void fetchSubjects(String deptId) {
        String url = FETCH_SUBJECTS_URL + deptId;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    subjectList.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            String subjectId = obj.getString("sub_id");
                            String subjectName = obj.getString("sub_name");
                            String base64Image = obj.getString("image");

                            subjectList.add(new StuSubject(subjectId, subjectName, base64Image));
                        }
                        subjectAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Parsing Error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(requireContext(), "Failed to load subjects", Toast.LENGTH_SHORT).show();
                });

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }


}
