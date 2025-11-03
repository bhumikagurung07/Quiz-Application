package com.example.quizapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {

    private CardView manageTeacher, manageDepartment, manageSubject;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize UI elements
        manageTeacher = view.findViewById(R.id.card_manage_teacher);
        manageDepartment = view.findViewById(R.id.card_manage_department);
        manageSubject = view.findViewById(R.id.card_manage_subject);

        // Set Click Listeners to navigate to respective fragments
        manageTeacher.setOnClickListener(v -> replaceFragment(new FragmentTeacher()));
        manageDepartment.setOnClickListener(v -> replaceFragment(new FragmentDepartment()));
        manageSubject.setOnClickListener(v -> replaceFragment(new FragmentSubject()));

        return view;
    }

    // Method to replace current fragment with new fragment
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.admin_fragment_container, fragment); // Make sure fragment_container exists in your main layout
        fragmentTransaction.addToBackStack(null); // Allows back navigation
        fragmentTransaction.commit();
    }
}