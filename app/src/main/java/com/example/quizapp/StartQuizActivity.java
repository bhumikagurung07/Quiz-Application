package com.example.quizapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StartQuizActivity extends AppCompatActivity {

    private TextView subjectTitle, subjectInfo;
    private Button startQuizBtn;

    private String subjectId, subjectName;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz);

        subjectTitle = findViewById(R.id.subjectTitle);
        subjectInfo = findViewById(R.id.subjectInfo);
        startQuizBtn = findViewById(R.id.startQuizBtn);

        // Get subject ID and Name from intent
        Intent intent = getIntent();
        subjectId = intent.getStringExtra("sub_id");
        subjectName = intent.getStringExtra("sub_name");

        // Display subject info
        subjectTitle.setText(subjectName);
        subjectInfo.setText("Ready for the " + subjectName + " quiz?");

        // Navigate to QuestionActivity on button click
        startQuizBtn.setOnClickListener(v -> {
            Intent quizIntent = new Intent(StartQuizActivity.this, QuestionActivity.class);
            quizIntent.putExtra("sub_id", subjectId);
            quizIntent.putExtra("sub_name", subjectName);
            startActivity(quizIntent);
        });
    }
}
