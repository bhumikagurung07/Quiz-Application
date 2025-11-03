package com.example.quizapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class QuestionAdapter extends BaseAdapter {

    private Context context;
    private List<Question> questionList;

    public QuestionAdapter(Context context, List<Question> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    @Override
    public int getCount() {
        return questionList.size();
    }

    @Override
    public Object getItem(int position) {
        return questionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Question question = questionList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_question, parent, false);
        }

        TextView textQNo = convertView.findViewById(R.id.textQNo);
        TextView textQuestion = convertView.findViewById(R.id.textQuestion);
        TextView textOptionA = convertView.findViewById(R.id.textOptionA);
        TextView textOptionB = convertView.findViewById(R.id.textOptionB);
        TextView textOptionC = convertView.findViewById(R.id.textOptionC);
        TextView textOptionD = convertView.findViewById(R.id.textOptionD);
        TextView textCorrectAnswer = convertView.findViewById(R.id.textCorrectAnswer);

        textQNo.setText("Q" + question.getQId());
        textQuestion.setText(question.getQuestion());
        textOptionA.setText("A. " + question.getOptionA());
        textOptionB.setText("B. " + question.getOptionB());
        textOptionC.setText("C. " + question.getOptionC());
        textOptionD.setText("D. " + question.getOptionD());
        textCorrectAnswer.setText("Correct Answer: " + question.getCorrectAnswer());


        // Set click listener for the card to open FragmentUpdateDelQuestion
        convertView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("q_id", question.getQId());
            bundle.putString("question", question.getQuestion());
            bundle.putString("option_a", question.getOptionA());
            bundle.putString("option_b", question.getOptionB());
            bundle.putString("option_c", question.getOptionC());
            bundle.putString("option_d", question.getOptionD());
            bundle.putString("correct_ans", question.getCorrectAnswer());

            FragmentUpdateDelQuestion fragment = new FragmentUpdateDelQuestion();
            fragment.setArguments(bundle); // ✅ Pass the bundle here

            ((AppCompatActivity) context).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.tech_fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });


        return convertView;
    }
}
