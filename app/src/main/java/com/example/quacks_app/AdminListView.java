package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;

/*
This is a general list screen that displays the entire databases entries depending on what the user
selected on the home screen. All the buttons from the home screen lead to this tab, but display different
things depending on what they clicked
 */

public class AdminListView extends AppCompatActivity {
    ListView genList;
    CustomAdapter genAdapter;
    Listable selected = null;

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

        ArrayList<Listable> testarr = new ArrayList<>();
        ArrayList<Listable> queryList = new ArrayList<>();

        if (viewType.equals("Events")){
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

        }
        else if (viewType.equals("Profile")){
            CRUD.readAllLive(User.class, new ReadMultipleCallback<User>() {
                @Override
                public void onReadMultipleSuccess(ArrayList<User> data) {
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
        }
        else if (viewType.equals("Facilities")) {
            CRUD.readAllLive(Facility.class, new ReadMultipleCallback<Facility>() {
                @Override
                public void onReadMultipleSuccess(ArrayList<Facility> data) {
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
        }


        genList.setOnItemClickListener((adapterView, view, i, l) ->{
            selected = testarr.get(i);
            String selecId = queryList.get(i).getDocumentId();
            Bundle SecondBundle = new Bundle();
            Intent myIntent = new Intent(AdminListView.this, AdminViewOrganizer.class);
            SecondBundle.putSerializable("value", (Serializable) selected);
            SecondBundle.putString("id", selecId);
            myIntent.putExtras(SecondBundle);
            startActivity(myIntent);

        });


        home.setOnClickListener(view -> {
            finish();
        });


    }
}
