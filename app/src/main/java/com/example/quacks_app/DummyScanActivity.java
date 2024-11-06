package com.example.quacks_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DummyScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_scan_activity_layout);

        final Button backButton = findViewById(R.id.button);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent switchActivityIntent = new Intent(getApplicationContext(), ScanQRCodeActivity.class);
                startActivity(switchActivityIntent);
                //finish();
            }
        });

    }
}
