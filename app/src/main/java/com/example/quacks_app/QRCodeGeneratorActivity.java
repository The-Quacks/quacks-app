package com.example.quacks_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class QRCodeGeneratorActivity extends AppCompatActivity {
    private Event event;
    private User user;
    private Facility facility;
    private EventList eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.qr_code_generator);


        event = (Event) getIntent().getSerializableExtra("Event");
        user = (User) getIntent().getSerializableExtra("User");
        facility = (Facility) getIntent().getSerializableExtra("Facility");
        Bitmap qrcode = QRCodeUtil.encode(event.getDocumentId(), 1000, 1000);


        ImageView imageView = findViewById(R.id.code);
        imageView.setImageBitmap(qrcode);

        Button button = findViewById(R.id.next_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QRCodeGeneratorActivity.this, UploadPosterActivity.class);
                intent.putExtra("User", user);
                intent.putExtra("Facility", facility);
                intent.putExtra("Event", event);
                startActivity(intent);
                finish();
            }
        });
    }
}
