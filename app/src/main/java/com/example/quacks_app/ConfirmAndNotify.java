package com.example.quacks_app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The {@code ConfirmAndNotify} class represents a confirmation page in the Quacks app.
 * This page is displayed to the user to confirm their intent to proceed with notifying
 * applicants. It ensures that the user has reviewed their choice before notifications
 * are sent out.
 */
public class ConfirmAndNotify extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_and_notify);

    }
}
