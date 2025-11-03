package com.example.quizapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ReportAdapter extends BaseAdapter {

    private Context context;
    private List<StudentInfoModel> studentList;

    public ReportAdapter(Context context, List<StudentInfoModel> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    @Override
    public int getCount() {
        return studentList.size();
    }

    @Override
    public Object getItem(int i) {
        return studentList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_student_info, viewGroup, false);
        }

        TextView txtSid = convertView.findViewById(R.id.txtstusid);
        TextView txtSpid = convertView.findViewById(R.id.txtstuspid);
        TextView txtPassword = convertView.findViewById(R.id.txtstupassword);
        TextView txtName = convertView.findViewById(R.id.txtstuname);
        TextView txtPhone = convertView.findViewById(R.id.txtstuphone);
        TextView txtDob = convertView.findViewById(R.id.txtstudob);
        TextView txtDept = convertView.findViewById(R.id.txtstudept);

        StudentInfoModel s = studentList.get(i);
        txtSid.setText(s.getS_id());
        txtSpid.setText(s.getSpid());
        txtPassword.setText(s.getPassword());
        txtName.setText(s.getName());
        txtPhone.setText(s.getPhone_no());
        txtDob.setText(s.getDob());
        txtDept.setText(s.getDept_name());

        return convertView;
    }
    public void updateList(List<StudentInfoModel> filteredList) {
        this.studentList = filteredList;
        notifyDataSetChanged();
    }

}
