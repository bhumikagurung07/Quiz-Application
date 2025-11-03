package com.example.quizapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class FragmentUpdateDelQuestion extends Fragment {

    private TextView textQId;
    private EditText edtQuestion, edtOptionA, edtOptionB, edtOptionC, edtOptionD, edtCorrectAns;
    private Button btnUpdate, btnDelete;

    private String qId;

    private static final String UPDATE_URL = "http://192.168.17.97/quizapp/update_question.php";
    private static final String DELETE_URL = "http://192.168.17.97/quizapp/delete_question.php";

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_update_del_question, container, false);

        textQId = view.findViewById(R.id.textQId);
        edtQuestion = view.findViewById(R.id.edtQuestion);
        edtOptionA = view.findViewById(R.id.edtOptionA);
        edtOptionB = view.findViewById(R.id.edtOptionB);
        edtOptionC = view.findViewById(R.id.edtOptionC);
        edtOptionD = view.findViewById(R.id.edtOptionD);
        edtCorrectAns = view.findViewById(R.id.edtCorrectAnswer);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnDelete = view.findViewById(R.id.btnDelete);

        if (getArguments() != null) {
            qId = getArguments().getString("q_id");
            textQId.setText("Q. " + qId);
            edtQuestion.setText(getArguments().getString("question"));
            edtOptionA.setText(getArguments().getString("option_a"));
            edtOptionB.setText(getArguments().getString("option_b"));
            edtOptionC.setText(getArguments().getString("option_c"));
            edtOptionD.setText(getArguments().getString("option_d"));
            edtCorrectAns.setText(getArguments().getString("correct_ans"));
        }

        btnUpdate.setOnClickListener(v -> updateQuestion());
        btnDelete.setOnClickListener(v -> deleteQuestion());

        return view;
    }

    private void updateQuestion() {
        String question = edtQuestion.getText().toString();
        String a = edtOptionA.getText().toString();
        String b = edtOptionB.getText().toString();
        String c = edtOptionC.getText().toString();
        String d = edtOptionD.getText().toString();
        String correct = edtCorrectAns.getText().toString();

        if (TextUtils.isEmpty(question) || TextUtils.isEmpty(a) || TextUtils.isEmpty(b)
                || TextUtils.isEmpty(c) || TextUtils.isEmpty(d) || TextUtils.isEmpty(correct)) {
            Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, UPDATE_URL,
                response -> Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(getContext(), "Update failed", Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("q_id", qId);
                map.put("question", question);
                map.put("option_a", a);
                map.put("option_b", b);
                map.put("option_c", c);
                map.put("option_d", d);
                map.put("correct_ans", correct);
                return map;
            }
        };

        Volley.newRequestQueue(requireContext()).add(request);
    }

    private void deleteQuestion() {
        StringRequest request = new StringRequest(Request.Method.POST, DELETE_URL,
                response -> Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(getContext(), "Delete failed", Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("q_id", qId);
                return map;
            }
        };

        Volley.newRequestQueue(requireContext()).add(request);
    }
}
