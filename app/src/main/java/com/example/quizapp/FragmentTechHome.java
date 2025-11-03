package com.example.quizapp;

import android.os.Bundle;
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

public class FragmentTechHome extends Fragment {

    private GridView subjectGridView;
    private SubjectAdapter subjectAdapter;
    private List<Subject> subjectList;

    private static final String URL = "http://192.168.17.97/quizapp/fetch_subject.php"; // Update with your correct URL

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tech_home, container, false);

        subjectGridView = view.findViewById(R.id.SubjectGridView);
        subjectList = new ArrayList<>();
        subjectAdapter = new SubjectAdapter(requireContext(), subjectList);
        subjectGridView.setAdapter(subjectAdapter);

        subjectGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view1, int position, long id) {
                Subject selectedSubject = subjectList.get(position);
                openAddQuestionFragment(selectedSubject.getSubId(), selectedSubject.getSubName());
            }
        });

        loadSubjects();

        return view;
    }

    private void loadSubjects() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                response -> {
                    subjectList.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject subject = response.getJSONObject(i);
                            String subId = subject.getString("sub_id");
                            String subName = subject.getString("sub_name");
                            String deptId = subject.getString("dept_id");
                            String techId = subject.getString("tech_id");
                            String imageUrl = subject.getString("image"); // Full URL should be sent from PHP

                            subjectList.add(new Subject(subId, subName, deptId, techId, imageUrl));
                        }
                        subjectAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error Parsing Subject Data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(requireContext(), "Failed to Load Subjects", Toast.LENGTH_SHORT).show());

        queue.add(request);
    }

    private void openAddQuestionFragment(String subId, String subName) {
        FragmentQuestion fragment = new FragmentQuestion(); // ✅ Correct replacement
        Bundle bundle = new Bundle();
        bundle.putString("sub_id", subId);
        bundle.putString("sub_name", subName);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_tcontainer, fragment); // ✅ Make sure this ID is correct
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
