package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TechDash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech_dash);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Handle BottomNavigationView item selection
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.navigation_thome) {
                selectedFragment = new FragmentTechHome();
            } else if (item.getItemId() == R.id.navigation_tresult) {
                selectedFragment = new FragmentTechResult();
            } else if (item.getItemId() == R.id.navigation_tlogout) {
                Intent intent = new Intent(getApplicationContext(), RoleActivity.class);
                startActivity(intent);
                return true;
            }


            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.tech_fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });

        // Load HomeFragment by default
        if (savedInstanceState == null) {
            bottomNavigationView.post(() -> bottomNavigationView.setSelectedItemId(R.id.navigation_thome));
        }
    }
}
