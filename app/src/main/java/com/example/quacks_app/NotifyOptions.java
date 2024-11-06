package com.example.quacks_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class NotifyOptions extends AppCompatActivity {
    private Button pool_applicants;
    private Button select_applicants;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify_options);

        pool_applicants = findViewById(R.id.random_button);
        select_applicants = findViewById(R.id.pick_button);
        back =findViewById(R.id.back_button);

        pool_applicants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        select_applicants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
