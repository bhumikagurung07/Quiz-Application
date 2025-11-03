package com.example.quizapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ResultModel> resultList;
    public ResultAdapter(Context context, ArrayList<ResultModel> resultList) {
        this.context = context;
        this.resultList = resultList;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Object getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void updateList(ArrayList<ResultModel> filteredList) {
        this.resultList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ResultModel model = resultList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_allresult, parent, false);
        }

        TextView txtResultId = convertView.findViewById(R.id.textResultId);
        TextView txtStudentName = convertView.findViewById(R.id.textStudentName);
        TextView txtDeptName = convertView.findViewById(R.id.textDeptName);
        TextView txtSubName = convertView.findViewById(R.id.textSubName);
        TextView txtFinalMark = convertView.findViewById(R.id.textFinalMark);
        TextView txtTime = convertView.findViewById(R.id.textTime);

        txtResultId.setText(model.getResultId());
        txtStudentName.setText(model.getSpid());
        txtDeptName.setText(model.getDeptName());
        txtSubName.setText(model.getSubName());
        txtFinalMark.setText(model.getFinalMark());
        txtTime.setText(model.getTimeConsumed());
        return convertView;
    }
}