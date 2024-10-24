package com.example.quacks_app;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class AdminListView extends AppCompatActivity {
    ListView genList;
    ArrayAdapter<String> genAdapter;
    ArrayList<String> dataList;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_list_layout);
        genList = findViewById(R.id.gen_list);

        String[] data ={"Edmonton", "Vancouver", "Moscow", "Sydney", "Berlin", "Vienna", "Tokyo", "Beijing", "Osaka", "New Delhi"};
        dataList = new ArrayList<>();
        dataList.addAll(Arrays.asList(data));

        genAdapter = new ArrayAdapter<>(this, R.layout.admin_profile_content, dataList);

        genList.setAdapter(genAdapter);

    }
}
