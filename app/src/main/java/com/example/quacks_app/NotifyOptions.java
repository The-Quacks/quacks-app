package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class NotifyOptions extends AppCompatActivity {
    private Button pool_applicants;
    private Button select_applicants;
    private Button back;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify_options);

        pool_applicants = findViewById(R.id.random_button);
        select_applicants = findViewById(R.id.pick_button);
        back =findViewById(R.id.back_button);

        Event event = (Event) getIntent().getSerializableExtra("Event");

        pool_applicants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotifyOptions.this, SelectAtRandom.class);
                intent.putExtra("Event", event);
                startActivity(intent);


            }
        });

        select_applicants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotifyOptions.this, PickApplicant.class);
                intent.putExtra("Event", event);
                startActivity(intent);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
