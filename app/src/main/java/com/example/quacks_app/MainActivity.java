package com.example.quacks_app;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CRUD<User> crud = new CRUD<>(User.class);
        User phil = new User("ub23bh3");
        crud.create(phil);
        crud.readStatic(phil.getId(), (User data) -> {
            TextView textView = findViewById(R.id.text_sample);
            if (data != null) {
                textView.setText(data.getId());
            } else {
                textView.setText("Failed");
            }
        });
    }
}