package com.example.quizapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class StudentResult extends AppCompatActivity {

    TextView finalMarkTextView, timeConsumedTextView;
    String s_id, dept_id;
    private static final String RESULT_URL = "http://192.168.17.97/quizapp/fetch_result.php?s_id=";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_result);

        finalMarkTextView = findViewById(R.id.finalMarkTextView);
        timeConsumedTextView = findViewById(R.id.timeConsumedTextView);
        Button backToHomeBtn = findViewById(R.id.backToHomeBtn);
        backToHomeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(StudentResult.this, StuDash.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // ✅ Get values from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        s_id = prefs.getString("s_id", null);
        //dept_id = prefs.getString("dept_id", null); // Optional use

        if (s_id == null) {
            Toast.makeText(this, "Student ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        fetchResult();
    }

    private void fetchResult() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, RESULT_URL + s_id, null,
                response -> {
                    try {
                        if (response.length() > 0) {
                            JSONObject resultObj = response.getJSONObject(0);
                            int finalMark = resultObj.getInt("final_mark");
                            int timeConsumed = resultObj.getInt("time_consumed");

                            finalMarkTextView.setText("Your Score: " + finalMark);
                            timeConsumedTextView.setText("Time Consumed: " + formatTime(timeConsumed));
                        } else {
                            Toast.makeText(this, "Result not found!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing result", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Volley Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                });

        Volley.newRequestQueue(this).add(request);
    }
    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
