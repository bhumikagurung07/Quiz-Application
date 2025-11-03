package com.example.quizapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class FragmentAddQuestion extends Fragment {

    private EditText editQuestion, editOptionA, editOptionB, editOptionC, editOptionD, editCorrectAnswer;
    private Button buttonSubmit;
    private String subId;

    public FragmentAddQuestion() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_question, container, false);

        // Initialize views
        editQuestion = view.findViewById(R.id.editQuestion);
        editOptionA = view.findViewById(R.id.editOptionA);
        editOptionB = view.findViewById(R.id.editOptionB);
        editOptionC = view.findViewById(R.id.editOptionC);
        editOptionD = view.findViewById(R.id.editOptionD);
        editCorrectAnswer = view.findViewById(R.id.editCorrectAnswer);
        buttonSubmit = view.findViewById(R.id.buttonSubmit);

        // Get subject ID from bundle
        if (getArguments() != null) {
            subId = getArguments().getString("sub_id");
        }

        // Handle submit click
        buttonSubmit.setOnClickListener(v -> {
            String question = editQuestion.getText().toString();
            String a = editOptionA.getText().toString();
            String b = editOptionB.getText().toString();
            String c = editOptionC.getText().toString();
            String d = editOptionD.getText().toString();
            String correct = editCorrectAnswer.getText().toString();

            if (question.isEmpty() || a.isEmpty() || b.isEmpty() || c.isEmpty() || d.isEmpty() || correct.isEmpty()) {
                Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Call method to insert question
            insertQuestion(subId, question, a, b, c, d, correct);
        });

        return view;
    }

    private void insertQuestion(String subId, String question, String a, String b, String c, String d, String correct) {
        String url = "http://192.168.17.97/quizapp/insert_question.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(getContext(), "Question Added Successfully", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                },
                error -> Toast.makeText(getContext(), "Failed to add question", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("sub_id", subId);
                map.put("question", question);
                map.put("option_a", a);
                map.put("option_b", b);
                map.put("option_c", c);
                map.put("option_d", d);
                map.put("correct_ans", correct);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }
}
