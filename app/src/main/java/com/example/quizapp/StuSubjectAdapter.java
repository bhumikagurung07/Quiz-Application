package com.example.quizapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class StuSubjectAdapter extends BaseAdapter {

    private Context context;
    private List<StuSubject> subjectList;

    public StuSubjectAdapter(Context context, List<StuSubject> subjectList) {
        this.context = context;
        this.subjectList = subjectList;
    }

    @Override
    public int getCount() {
        return subjectList.size();
    }

    @Override
    public Object getItem(int position) {
        return subjectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView subjectName;
        ImageView subjectImage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_stu_subject, parent, false);
            holder = new ViewHolder();
            holder.subjectName = convertView.findViewById(R.id.subjectName);
            holder.subjectImage = convertView.findViewById(R.id.subjectImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StuSubject subject = subjectList.get(position);
        holder.subjectName.setText(subject.getSubjectName());

        // Decode base64 string to Bitmap
        if (subject.getBase64Image() != null && !subject.getBase64Image().isEmpty()) {
            byte[] decodedString = Base64.decode(subject.getBase64Image(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.subjectImage.setImageBitmap(decodedByte);
        }

        return convertView;
    }
}

