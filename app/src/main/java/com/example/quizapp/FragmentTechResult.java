package com.example.quizapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;
import java.util.List;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.quizapp.R;
import com.example.quizapp.ResultAdapter;
import com.example.quizapp.ResultModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FragmentTechResult extends Fragment {

    private GridView gridResult;
    private EditText searchResult;
    private ResultAdapter resultAdapter;
    private ArrayList<ResultModel> resultList = new ArrayList<>();

    private final String URL = "http://192.168.17.97/quizapp/fetch_all_student_result.php";

    public FragmentTechResult() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tech_result, container, false);

        gridResult = view.findViewById(R.id.gridResult);
        searchResult=view.findViewById(R.id.search_result);

        // searchViewResult = view.findViewById(R.id.searchViewResult);

        fetchResults();
        searchResult.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterResults(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });



       /* searchViewResult.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterResults(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterResults(newText);
                return true;
            }
        });*/

        return view;
    }

    private void filterResults(String text) {
        ArrayList<ResultModel> filteredList = new ArrayList<>();

        for (ResultModel result : resultList) {
            if (result.getDeptName().toLowerCase().contains(text.toLowerCase()) ||
                    result.getSpid().toLowerCase().contains(text.toLowerCase()) ||
                    result.getSubName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(result);
            }
        }
        resultAdapter.updateList(filteredList);
    }


    private void fetchResults() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                response -> {
                    try {
                        if (response.getString("status").equals("success")) {
                            JSONArray dataArray = response.getJSONArray("data");
                            resultList.clear();

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject obj = dataArray.getJSONObject(i);

                                ResultModel model = new ResultModel(
                                        obj.getString("result_id"),
                                        obj.getString("spid"),
                                        obj.getString("dept_name"),
                                        obj.getString("sub_name"),
                                        obj.getString("final_mark"),
                                        obj.getString("time_consumed")
                                );
                                resultList.add(model);
                            }

                            resultAdapter = new ResultAdapter(requireContext(), resultList);
                            gridResult.setAdapter(resultAdapter);
                        } else {
                            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Parse error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Volley error", Toast.LENGTH_SHORT).show());

        queue.add(request);
    }
    

   /*private void filterResults(String query) {
        ArrayList<ResultModel> filteredList = new ArrayList<>();

        for (ResultModel model : resultList) {
            if (model.getSpid().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)) ||
                    model.getDeptName().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)) ||
                    model.getSubName().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                filteredList.add(model);
            }
        }

        resultAdapter.updateList(filteredList);
    }*/
}
