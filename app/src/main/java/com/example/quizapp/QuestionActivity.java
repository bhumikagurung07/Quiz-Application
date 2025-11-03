package com.example.quizapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class QuestionActivity extends AppCompatActivity {

    private TextView subjectNameTextView, marksTextView, questionTextView, timerTextView;
    private RadioGroup optionsGroup;
    private RadioButton option1, option2, option3, option4;
    private Button nextButton, submitButton;
    private CountDownTimer countDownTimer;
    private static final long TOTAL_TIME = 5 * 60 * 1000; // 5 minutes
    private long remainingTime = TOTAL_TIME; // For time tracking

    private String subId, subName, studentId;
    private boolean alreadySubmitted = false;
    private ArrayList<JSONObject> questionList = new ArrayList<>();
    private int currentQuestionIndex = 0;

    private static final String FETCH_URL = "http://192.168.17.97/quizapp/get_random_questions.php?sub_id=";
    private static final String STORE_ANSWER_URL = "http://192.168.17.97/quizapp/store_answer.php";
    private static final String STORE_RESULT_URL = "http://192.168.17.97/quizapp/store_result.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        subjectNameTextView = findViewById(R.id.subjectNameTextView);
        marksTextView = findViewById(R.id.marksTextView);
        questionTextView = findViewById(R.id.questionTextView);
        timerTextView = findViewById(R.id.timerTextView);
        optionsGroup = findViewById(R.id.optionsRadioGroup);
        option1 = findViewById(R.id.option1RadioButton);
        option2 = findViewById(R.id.option2RadioButton);
        option3 = findViewById(R.id.option3RadioButton);
        option4 = findViewById(R.id.option4RadioButton);
        nextButton = findViewById(R.id.nextButton);
        submitButton = findViewById(R.id.submitButton);
        submitButton.setVisibility(View.GONE);

        Intent intent = getIntent();
        subId = intent.getStringExtra("sub_id");
        subName = intent.getStringExtra("sub_name");

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        studentId = prefs.getString("s_id", null);

        subjectNameTextView.setText(subName);
        marksTextView.setText("Each question carries 2 marks");

        startTimer();
        fetchQuestions();

        nextButton.setOnClickListener(v -> {
            saveAnswer();
            currentQuestionIndex++;
            if (currentQuestionIndex < questionList.size()) {
                showQuestion(currentQuestionIndex);
            }
            if (currentQuestionIndex == questionList.size() - 1) {
                nextButton.setVisibility(View.GONE);
                submitButton.setVisibility(View.VISIBLE);
            }
        });

        submitButton.setOnClickListener(v -> {
            if (!alreadySubmitted) {
                alreadySubmitted = true;
                submitButton.setEnabled(false); // Prevent double clicks
                saveAnswer();
                redirectToStudentResult();
            } else {
                Toast.makeText(QuestionActivity.this, "You've already submitted your test", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(TOTAL_TIME, 1000) {
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished; // Track remaining time
                int minutes = (int) (millisUntilFinished / 60000);
                int seconds = (int) (millisUntilFinished % 60000 / 1000);
                String timeLeft = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                timerTextView.setText(timeLeft);
            }

            public void onFinish() {
                Toast.makeText(QuestionActivity.this, "Time's up!", Toast.LENGTH_LONG).show();
                redirectToStudentResult();
            }
        }.start();
    }

    private void fetchQuestions() {
        String url = FETCH_URL + subId;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            questionList.add(response.getJSONObject(i));
                        }
                        if (!questionList.isEmpty()) {
                            showQuestion(0);
                        } else {
                            Toast.makeText(this, "No questions available!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing questions!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Error fetching questions", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(this).add(request);
    }

    private void showQuestion(int index) {
        try {
            JSONObject questionObj = questionList.get(index);
            questionTextView.setText(questionObj.getString("question"));
            option1.setText(questionObj.getString("option_a"));
            option2.setText(questionObj.getString("option_b"));
            option3.setText(questionObj.getString("option_c"));
            option4.setText(questionObj.getString("option_d"));
            optionsGroup.clearCheck();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveAnswer() {
        if (optionsGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedAnswer = ((RadioButton) findViewById(optionsGroup.getCheckedRadioButtonId())).getText().toString();
        JSONObject questionObj = questionList.get(currentQuestionIndex);
        String correctAnswer;
        int obtainedMark = 0;

        try {
            correctAnswer = questionObj.getString("correct_ans");
            String qId = questionObj.getString("q_id");

            if (selectedAnswer.trim().equalsIgnoreCase(correctAnswer.trim())) {
                obtainedMark = 2;
            }

            Map<String, String> params = new HashMap<>();
            params.put("s_id", studentId);
            params.put("q_id", qId);
            params.put("selected_answer", selectedAnswer);
            params.put("obtained_mark", String.valueOf(obtainedMark));

            StringRequest postRequest = new StringRequest(Request.Method.POST, STORE_ANSWER_URL,
                    response -> {},
                    error -> error.printStackTrace()) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }
            };
            Volley.newRequestQueue(this).add(postRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void storeFinalResult(int timeConsumedInSeconds) {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String deptId = prefs.getString("dept_id", null);

        if (studentId == null || deptId == null || subId == null) {
            Toast.makeText(this, "Student ID, Department ID, or Subject ID missing", Toast.LENGTH_SHORT).show();
            return;
        }


        Map<String, String> params = new HashMap<>();
        params.put("s_id", studentId);
        params.put("dept_id", deptId);
        params.put("sub_id", subId); // ✅ Add subject ID here
        params.put("time_consumed", String.valueOf(timeConsumedInSeconds));

        StringRequest postRequest = new StringRequest(Request.Method.POST, STORE_RESULT_URL,
                response -> Log.d("StoreResult", "Response: " + response),
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Error storing result", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        Volley.newRequestQueue(this).add(postRequest);
    }


    private void redirectToStudentResult() {
        if (countDownTimer != null) countDownTimer.cancel();

        long timeSpentMillis = TOTAL_TIME - remainingTime;
        int timeSpentSeconds = (int) (timeSpentMillis / 1000);

        storeFinalResult(timeSpentSeconds);

        Intent intent = new Intent(this, StudentResult.class);
        intent.putExtra("s_id", studentId);
        intent.putExtra("time_consumed", timeSpentSeconds);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        if (countDownTimer != null) countDownTimer.cancel();
        super.onDestroy();
    }
}
