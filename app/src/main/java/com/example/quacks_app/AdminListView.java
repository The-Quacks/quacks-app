package com.example.quacks_app;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.io.Serializable;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

/*
This is a general list screen that displays the entire databases entries depending on what the user
selected on the home screen. All the buttons from the home screen lead to this tab, but display different
things depending on what they clicked
 */

public class AdminListView extends AppCompatActivity {
    ListView genList;
    CustomAdapter genAdapter;
    Event selected = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_list_layout);
        genList = findViewById(R.id.gen_list);

        Bundle bundle = getIntent().getExtras();
        String viewType = bundle.getString("viewType");
        TextView type = findViewById(R.id.listViewType);
        type.setText(viewType);

        ImageButton home = findViewById(R.id.homeIcon);

        ArrayList<Event> testarr = new ArrayList<>();
        ArrayList<Event> queryList = new ArrayList<>();

        CRUD.readAllLive(Event.class, new ReadMultipleCallback<Event>() {
            @Override
            public void onReadMultipleSuccess(ArrayList<Event> data) {
                queryList.clear();
                testarr.clear();
                queryList.addAll(data);
                testarr.addAll(data);
                genAdapter = new CustomAdapter(AdminListView.this, R.layout.admin_profile_content, testarr);
                genList.setAdapter(genAdapter);

            }

            @Override
            public void onReadMultipleFailure(Exception e) {

            }
        });

        genList.setOnItemClickListener((adapterView, view, i, l) ->{
            selected = testarr.get(i);
            String selecId = queryList.get(i).getId();
            Bundle SecondBundle = new Bundle();
            Intent myIntent = new Intent(AdminListView.this, AdminViewOrganizer.class);
            SecondBundle.putSerializable("value", selected);
            SecondBundle.putString("id", selecId);
            myIntent.putExtras(SecondBundle);
            startActivity(myIntent);

        });

        home.setOnClickListener(view -> {
            finish();
        });


    }
}
