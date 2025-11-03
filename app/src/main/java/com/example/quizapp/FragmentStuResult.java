package com.example.quizapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentStuResult extends Fragment {

    GridView gridView;
    List<StudentResultModel> resultList;
    StudentResultAdapter adapter;
    String dept_id;

    private static final String RESULT_URL = "http://192.168.17.97/quizapp/fetch_department_result.php?dept_id=";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stu_result, container, false);
        gridView = view.findViewById(R.id.gridViewStuResult);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        dept_id = sharedPreferences.getString("dept_id", null);

        resultList = new ArrayList<>();

        fetchResults();

        return view;
    }

    private void fetchResults() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, RESULT_URL + dept_id, null,
                response -> {
                    try {
                        resultList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);

                            String spid = obj.getString("spid"); // 👈 extract spid
                            String name = obj.getString("stu_name");
                            int mark = obj.getInt("final_mark");
                            String subName = obj.getString("sub_name");

                            // 👇 Add spid to constructor
                            resultList.add(new StudentResultModel(spid, name, mark, i + 1, subName));
                        }

                        adapter = new StudentResultAdapter(getContext(), resultList);
                        gridView.setAdapter(adapter);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing results", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(getContext(), "Error fetching results: " + error.getMessage(), Toast.LENGTH_LONG).show();
                });

        Volley.newRequestQueue(getContext()).add(request);
    }
}
