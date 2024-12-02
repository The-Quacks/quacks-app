package com.example.quacks_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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

        CRUD.storeImage(qrcode, new ReadCallback<String>() {
            @Override
            public void onReadSuccess(String data) {
                event.setQrCodePath(data);
                CRUD.update(event, new UpdateCallback() {
                    @Override
                    public void onUpdateSuccess() {
                        Toast.makeText(QRCodeGeneratorActivity.this, "Successfully stored QR code", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onUpdateFailure(Exception e) {
                        Toast.makeText(QRCodeGeneratorActivity.this, "Failed to update event", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onReadFailure(Exception e) {
                Toast.makeText(QRCodeGeneratorActivity.this, "Failed to store QR code", Toast.LENGTH_SHORT).show();

            }
        });


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
