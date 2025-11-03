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
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReportFragment extends Fragment {

    private GridView gridViewReport;
    private ArrayList<StudentInfoModel> studentInfoList;
    private ReportAdapter reportAdapter;
    private EditText searchStudent;


    private final String URL = "http://192.168.17.97/quizapp/fetch_student_info.php"; // Replace with your IP & path

    public ReportFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        gridViewReport = view.findViewById(R.id.gridViewReport);
        searchStudent = view.findViewById(R.id.search_student);

        studentInfoList = new ArrayList<>();
        reportAdapter = new ReportAdapter(getContext(), studentInfoList);
        gridViewReport.setAdapter(reportAdapter);

        fetchStudentInfo();
        searchStudent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterStudentList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        return view;
    }
    private void filterStudentList(String text) {
        ArrayList<StudentInfoModel> filteredList = new ArrayList<>();

        for (StudentInfoModel student : studentInfoList) {
            if (student.getSpid().toLowerCase().contains(text.toLowerCase()) ||
                    student.getDept_name().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(student);
            }
        }

        reportAdapter.updateList(filteredList);
    }



    private void fetchStudentInfo() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        StringRequest request = new StringRequest(Request.Method.GET, URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("success")) {
                            JSONArray studentsArray = jsonObject.getJSONArray("students");
                            for (int i = 0; i < studentsArray.length(); i++) {
                                JSONObject stu = studentsArray.getJSONObject(i);
                                studentInfoList.add(new StudentInfoModel(
                                        stu.getString("s_id"),
                                        stu.getString("spid"),
                                        stu.getString("password"),
                                        stu.getString("stu_name"),
                                        stu.getString("phone_no"),
                                        stu.getString("dob"),
                                        stu.getString("dept_name")
                                ));
                            }
                            reportAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("ReportFragment", "JSON Error: " + e.getMessage());
                        Toast.makeText(getContext(), "Parsing error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }
}
