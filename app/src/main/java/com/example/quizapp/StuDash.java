package com.example.quizapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StuDash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_dash);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Handle BottomNavigationView item selection
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.navigation_shome) {
                selectedFragment = new FragmentStuHome();
            } else if (item.getItemId() == R.id.navigation_sresult) {
                selectedFragment = new FragmentStuResult();
            }
            else if (item.getItemId() == R.id.navigation_sprofile) {
                selectedFragment = new FragmentStuProfile();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.student_fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });

        // Load HomeFragment by default
        if (savedInstanceState == null) {
            bottomNavigationView.post(() -> bottomNavigationView.setSelectedItemId(R.id.navigation_shome));
        }
    }
}
