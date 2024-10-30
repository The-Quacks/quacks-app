package com.example.quacks_app;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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

        // Create user
        crud.create(phil);

        // Read user data
        crud.read(phil.getId(), data -> {
            TextView textView = findViewById(R.id.text_sample);
            if (data != null) {
                textView.setText(data.getId());
            } else {
                textView.setText("Failed");
            }
        });

        // Update user data
        crud.update(phil.getId(), phil, new UpdateCallback() {
            @Override
            public void onUpdateSuccess() {
                Toast.makeText(MainActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpdateFailure(Exception e) {
                Toast.makeText(MainActivity.this, "Update Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Delete user data
        crud.delete(phil.getId(), new DeleteCallback() {
            @Override
            public void onDeleteSuccess() {
                Toast.makeText(MainActivity.this, "Delete Successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteFailure(Exception e) {
                Toast.makeText(MainActivity.this, "Delete Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
