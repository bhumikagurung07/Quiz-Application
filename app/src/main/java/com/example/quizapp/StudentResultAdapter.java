package com.example.quizapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class StudentResultAdapter extends BaseAdapter {

    private Context context;
    private List<StudentResultModel> resultList;

    public StudentResultAdapter(Context context, List<StudentResultModel> resultList) {
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

    static class ViewHolder {
        TextView textRank, textSpid, textName, textMark, textSubject;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.result_item, parent, false);
            holder = new ViewHolder();
            holder.textRank = convertView.findViewById(R.id.textRank);
            holder.textSpid = convertView.findViewById(R.id.textSpid); // 👈 SPID TextView
            holder.textName = convertView.findViewById(R.id.textName);
            holder.textMark = convertView.findViewById(R.id.textMark);
            holder.textSubject = convertView.findViewById(R.id.textSubject);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StudentResultModel result = resultList.get(position);
        holder.textRank.setText("Rank: " + result.getRank());
        holder.textSpid.setText("SPID: " + result.getSpid()); // 👈 Set SPID
        holder.textName.setText(result.getName());
        holder.textMark.setText("Marks: " + result.getMark());
        holder.textSubject.setText("Subject: " + result.getSubjectName());

        return convertView;
    }
}
