package com.example.quacks_app;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class AdminListView extends AppCompatActivity {
    ListView genList;
    CustomAdapter genAdapter;
    ArrayList<Listable> dataList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_list_layout);
        genList = findViewById(R.id.gen_list);
        String[] data;
        String[] subdata;
        Listable[] classData;

        Bundle bundle = getIntent().getExtras();
        String viewType = bundle.getString("viewType");
        TextView type = findViewById(R.id.listViewType);
        type.setText(viewType);

        dataList = new ArrayList<>();
        if (viewType.equals("Profile")){
            // Query database, create list of total profiles, call getters and create lists
            data = new String[]{"Aiden Peacock", "Kaiden Leslie", "Diyaa Yadav", "Joise Paananen", "Nicholas Weiss", "Marko Srnic"};
            subdata = new String[]{"Administrator", "Organizer", "Entrant", "Administrator", "Organizer", "Entrant"};
            classData = new Listable[]{new Listable(data[0], subdata[0]), new Listable(data[1], subdata[1]), new Listable(data[2], subdata[2]), new Listable(data[3], subdata[3]), new Listable(data[4], subdata[4]), new Listable(data[5], subdata[5])};
        }
        else if (viewType.equals("Events")) {
            data = new String[]{"Spin Class", "Halloween Costume Sale", "Recreational Basketball League", "Dance Recital"};
            subdata = new String[]{"Entrants Joined: 10", "Entrants Joined: 34", "Entrants Joined: 40", "Entrants Joined: 32"};
            classData = new Listable[]{new Listable(data[0], subdata[0]), new Listable(data[1], subdata[1]), new Listable(data[2], subdata[2])};
        }
        else if (viewType.equals("Images")) {
            data = new String[]{"Image1", "Image2"};
            subdata = new String[]{"Spin Class", "Halloween Costume Sale"};
            classData = new Listable[]{new Listable(data[0], subdata[0]), new Listable(data[1], subdata[1])};
        }
        else if (viewType.equals("Facilities")) {
            data = new String[]{"Kinsmen Recreation Center", "Terwilleger Recreation Center"};
            subdata = new String[]{"Kaiden Leslie", "Josie Paananen"};
            classData = new Listable[]{new Listable(data[0], subdata[0]), new Listable(data[1], subdata[1])};
        }
        else{
            data = new String[]{"Kinsmen Recreation Center", "Terwilleger Recreation Center"};
            subdata = new String[]{"Kaiden Leslie", "Josie Paananen"};
            classData = new Listable[]{new Listable(data[0], subdata[0]), new Listable(data[1], subdata[1])};
        }

        dataList.addAll(Arrays.asList(classData));

        genAdapter = new CustomAdapter(this, R.layout.admin_profile_content, dataList);
        genList.setAdapter(genAdapter);

    }
}
