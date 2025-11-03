package com.example.quizapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;
import java.util.List;

public class TeacherAdapter extends ArrayAdapter<Teacher> {

    private Context context;
    private List<Teacher> teacherList;

    public TeacherAdapter(Context context, List<Teacher> teacherList) {
        super(context, R.layout.list_item_teacher, teacherList);
        this.context = context;
        this.teacherList = teacherList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.list_item_teacher, parent, false);
        }

        Teacher teacher = teacherList.get(position);

        TextView txtFullName = listItemView.findViewById(R.id.txt_teacher_name);
        TextView txtUsername = listItemView.findViewById(R.id.txt_teacher_username);
        TextView txtPassword = listItemView.findViewById(R.id.txt_teacher_password);

        txtFullName.setText(teacher.getFullName());
        txtUsername.setText(teacher.getUsername());
        txtPassword.setText(teacher.getPassword());

        return listItemView;
    }
}
