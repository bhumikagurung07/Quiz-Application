package com.example.quizapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private String tableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.login_Username);
        etPassword = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.login_button);

        // Get user type from intent
        tableName = getIntent().getStringExtra("tableName");

        btnLogin.setOnClickListener(view -> loginUser());
    }

    private void loginUser() {
        final String username = etUsername.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                URL url = new URL("http://192.168.17.97/quizapp/login.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String postData = "username=" + URLEncoder.encode(username, "UTF-8") +
                        "&password=" + URLEncoder.encode(password, "UTF-8") +
                        "&table=" + URLEncoder.encode(tableName, "UTF-8");

                try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
                    writer.write(postData);
                    writer.flush();
                }

                StringBuilder responseBuilder = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }
                }

                final String response = responseBuilder.toString();
                Log.d("LOGIN_RESPONSE", response);

                runOnUiThread(() -> handleLoginResponse(response));

                conn.disconnect();

            } catch (final Exception e) {
                Log.e("LOGIN_ERROR", "Error: " + e.getMessage(), e);
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    private void handleLoginResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            boolean success = jsonResponse.getBoolean("success");
            String message = jsonResponse.getString("message");

            if (success) {
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();

                String role = jsonResponse.getString("role");
                Intent intent = null;

                if (role.equals("admin_mst")) {
                    intent = new Intent(LoginActivity.this, AdminDash.class);
                } else if (role.equals("teacher_info")) {
                    intent = new Intent(LoginActivity.this, TechDash.class);
                    String techId = jsonResponse.getString("tech_id");
                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("tech_id", techId);
                    editor.apply();
                    intent.putExtra("tech_id", techId);
                } else {
                    Toast.makeText(LoginActivity.this, "Unknown user role", Toast.LENGTH_LONG).show();
                    return;
                }

                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, "Error parsing response", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}