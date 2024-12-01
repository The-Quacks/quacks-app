package com.example.quacks_app;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AdminImageListView extends AppCompatActivity {
    ListView listView;
    ImageAdapter adapter;
    String selected = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.list_image);

        ArrayList<String> listData = new ArrayList<>();
        Button delButton = findViewById(R.id.image_delete_button);
        ImageButton homeButton = findViewById(R.id.home_images);
        listView = findViewById(R.id.image_list_view);
        CRUD.readLive("yYc9ikIhSODAcZ4180Hi", ImageList.class, new ReadCallback<ImageList>() {
            @Override
            public void onReadSuccess(ImageList data) {
                listData.clear();
                listData.addAll(data.getImageIds());
                adapter = new ImageAdapter(AdminImageListView.this, R.layout.list_image_item, listData);
                listView.setAdapter(adapter);
            }

            @Override
            public void onReadFailure(Exception e) {
                Toast.makeText(AdminImageListView.this, "Lol, Lmao even", Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            selected = listData.get(i);
        });

        delButton.setOnClickListener(view -> {
            if (selected != null){
                CRUD.removeImage(selected, new DeleteCallback() {
                    @Override
                    public void onDeleteSuccess() {

                        Toast.makeText(AdminImageListView.this, "Successfully Deleted Image", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDeleteFailure(Exception e) {

                    }
                });
            }
        });

        homeButton.setOnClickListener(view -> {
            finish();
        });


    }
}
