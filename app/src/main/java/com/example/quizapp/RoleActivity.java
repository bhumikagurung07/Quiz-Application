package com.example.quizapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class RoleActivity extends AppCompatActivity {

    CardView cardAdmin, cardTeacher, cardStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);

        // Toolbar Setup
        // Initialize Toolbar
       Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("IT Quiz App");
        }

        // Initialize CardViews
        cardAdmin = findViewById(R.id.card_admin);
        cardTeacher = findViewById(R.id.card_teacher);
        cardStudent = findViewById(R.id.card_student);

        // Click Listeners
        cardAdmin.setOnClickListener(v -> openLogin("admin_mst", "admin"));
        cardTeacher.setOnClickListener(v -> openLogin("teacher_info", "teacher"));
        cardStudent.setOnClickListener(v -> openStudentRegister("student_info","student"));
    }

    private void openLogin(String tableName, String role) {
        Intent intent = new Intent(RoleActivity.this, LoginActivity.class);
        intent.putExtra("tableName", tableName);
        intent.putExtra("role", role);
        startActivity(intent);
    }

    private void openStudentRegister(String tableName, String role) {
        Intent intent = new Intent(RoleActivity.this, StudentRegisterActivity.class);
        intent.putExtra("tableName", tableName);
        intent.putExtra("role", role);
        startActivity(intent);
    }
}
