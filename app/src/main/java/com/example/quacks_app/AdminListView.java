package com.example.quacks_app;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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

public class AdminListView extends AppCompatActivity {
    ListView genList;
    CustomAdapter genAdapter;
    ArrayList<Listable> dataList;
    Listable selected = null;

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

        ArrayList<User> testarr = new ArrayList<>();
        dataList = new ArrayList<>();

        Database testdb = new Database();
        FirebaseFirestore db = testdb.getDb();
        db.collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                testarr.add(document.toObject(User.class));
                                System.out.println(testarr.get(0).getDeviceId());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        if (viewType.equals("Profile")){
            // Query database, create list of total profiles, call getters and create lists
            data = new String[]{"Aiden Peacock", "Kaiden Leslie", "Diyaa Yadav", "Joise Paananen", "Nicholas Weiss", "Marko Srnic"};
            subdata = new String[]{"Administrator", "Organizer", "Entrant", "Administrator", "Organizer", "Entrant"};

            classData = new Listable[]{new Listable(data[0], subdata[1]), new Listable(data[1], subdata[1]), new Listable(data[2], subdata[2]), new Listable(data[3], subdata[3]), new Listable(data[4], subdata[4]), new Listable(data[5], subdata[5])};
        }
        else if (viewType.equals("Events")) {
            data = new String[]{"Spin Class", "Halloween Costume Sale", "Recreational Basketball League", "Dance Recital"};
            subdata = new String[]{"Entrants Joined: 10", "Entrants Joined: 34", "Entrants Joined: 40", "Entrants Joined: 32"};
            classData = new Listable[]{new Listable(data[0], subdata[0]), new Listable(data[1], subdata[1]), new Listable(data[2], subdata[2])};
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

        genList.setOnItemClickListener((adapterView, view, i, l) ->{
            selected = dataList.get(i);
            Toast.makeText(this, selected.getDisplay(), Toast.LENGTH_LONG).show();
        });

    }
}
