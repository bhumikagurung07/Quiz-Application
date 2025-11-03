package com.example.quizapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class StudentAdapter extends BaseAdapter {

    private Context context;
    private List<StudentModel> studentList;

    public StudentAdapter(Context context, List<StudentModel> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    @Override
    public int getCount() {
        return studentList.size();
    }

    @Override
    public Object getItem(int position) {
        return studentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StudentModel student = studentList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
        }

        TextView txtRid = convertView.findViewById(R.id.txtRid);
        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtSpid = convertView.findViewById(R.id.txtSpid);

        txtRid.setText(student.getR_id());
        txtName.setText(student.getName());
        txtSpid.setText(student.getSpid());

        return convertView;
    }
}
