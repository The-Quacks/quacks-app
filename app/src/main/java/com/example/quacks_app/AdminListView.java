package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The {@code AdminListView} class represents a general-purpose list view screen in the Quacks app.
 * It displays database entries dynamically based on the type selected by the user on the home screen.
 * Supported types include Events, Profiles, and Facilities.
 */
public class AdminListView extends AppCompatActivity {
    ListView genList;
    CustomAdapter genAdapter;
    Listable selected = null;

    /**
     * Initializes the activity. Dynamically populates the list view based on the user-selected type
     * and sets up navigation and interaction logic.
     * @param savedInstanceState {@code null} if the activity is being created for the first time.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_list_layout);
        // Initialize ListView
        genList = findViewById(R.id.gen_list);

        // Retrieve view type from intent extras
        Bundle bundle = getIntent().getExtras();
        String viewType = bundle.getString("viewType");

        // Set the title of the screen based on the view type
        TextView type = findViewById(R.id.listViewType);
        type.setText(viewType);

        // Initialize the home button
        ImageButton home = findViewById(R.id.homeIcon);

        // Temporary lists for displaying and querying data
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
