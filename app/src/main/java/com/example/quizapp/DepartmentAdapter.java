package com.example.quizapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.List;

public class DepartmentAdapter extends BaseAdapter {

    private Context context;
    private List<Department> departmentList;

    public DepartmentAdapter(Context context, List<Department> departmentList) {
        this.context = context;
        this.departmentList = departmentList;
    }

    @Override
    public int getCount() {
        return departmentList.size();
    }

    @Override
    public Object getItem(int position) {
        return departmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_department, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.imageDepartmentItem);
            holder.textView = convertView.findViewById(R.id.textDepartmentName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Department department = departmentList.get(position);
        holder.textView.setText(department.getDeptName());

        // Load image with Picasso, adding placeholders and error handling
        Picasso.get()
                .load(department.getImageUrl())
                .placeholder(R.drawable.log)  // Placeholder while loading
                .error(R.drawable.teach)       // Error image if load fails
                .into(holder.imageView);

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
