package com.example.quizapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentQuestion extends Fragment {

    private GridView gridView;
    private QuestionAdapter adapter;
    private ArrayList<Question> questionList;
    private String subId;

    private static final String BASE_URL = "http://192.168.17.97/quizapp/fetch_question.php?sub_id=";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        gridView = view.findViewById(R.id.gridViewQuestions);
        FloatingActionButton fabAdd = view.findViewById(R.id.fabAddQuestion);

        fabAdd.setOnClickListener(v -> {
            FragmentAddQuestion addQuestionFragment = new FragmentAddQuestion();
            Bundle bundle = new Bundle();
            bundle.putString("sub_id", subId);  // Pass subject ID to AddQuestion Fragment
            addQuestionFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_tcontainer, addQuestionFragment) // Make sure this ID matches your container in activity
                    .addToBackStack(null)
                    .commit();
        });
        questionList = new ArrayList<>();
        adapter = new QuestionAdapter(requireContext(), questionList);
        gridView.setAdapter(adapter);

        if (getArguments() != null) {
            subId = getArguments().getString("sub_id");
            loadQuestions(subId);
        } else {
            Toast.makeText(getContext(), "No Subject ID passed", Toast.LENGTH_SHORT).show();
        }
        return view;

    }
    private void loadQuestions(String subId) {
        String url = BASE_URL + subId;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    questionList.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);

                            String qNo = obj.getString("q_id");
                            String question = obj.getString("question");
                            String optionA = obj.getString("option_a");
                            String optionB = obj.getString("option_b");
                            String optionC = obj.getString("option_c");
                            String optionD = obj.getString("option_d");
                            String correct = obj.getString("correct_ans");

                            questionList.add(new Question(qNo, question, optionA, optionB, optionC, optionD, correct));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Failed to load questions", Toast.LENGTH_SHORT).show());

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }
}
