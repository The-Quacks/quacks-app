package com.example.quacks_app;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A dialog fragment for editing user profile information.
 * It allows the user to update their username, email, and phone number.
 * The updated information is saved to Firebase Firestore using the device ID.
 */
public class EditDialogueFragment extends DialogFragment {
    private EditText usernameEditText, emailEditText, phoneEditText;
    private Button saveButton, cancelButton;
    private FirebaseFirestore db;
    private String deviceID;
    private OnProfileUpdatedListener listener;

    /**
     * Sets the listener for profile updates.
     * This listener will be notified when the user profile is successfully updated.
     *
     * @param listener The listener to notify when the profile is updated.
     */
    public void setOnProfileUpdatedListener(OnProfileUpdatedListener listener) {
        this.listener = listener;
    }

    /**
     * Called to create the view hierarchy associated with the fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate views.
     * @param container          The parent view that this fragment's UI will be attached to.
     * @param savedInstanceState A Bundle containing data from the previous instance.
     * @return The root View of the inflated layout.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogue_fragment_edit, container, false);

        usernameEditText = view.findViewById(R.id.usernameEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        saveButton = view.findViewById(R.id.saveButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        db = FirebaseFirestore.getInstance();
        deviceID = Settings.Secure.getString(requireActivity().getContentResolver(), Settings.Secure.ANDROID_ID);

        loadUserProfile();

        saveButton.setOnClickListener(v -> saveUserProfile());
        cancelButton.setOnClickListener(v -> dismiss());

        return view;
    }

    /**
     * Loads the user's profile from Firestore based on the device ID.
     * Populates the EditText fields with existing data if available.
     */
    private void loadUserProfile() {
        db.collection("User").document(deviceID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        UserProfile userProfile = documentSnapshot.toObject(UserProfile.class);
                        if (userProfile != null) {
                            usernameEditText.setText(userProfile.getUserName());
                            emailEditText.setText(userProfile.getEmail());
                            phoneEditText.setText(userProfile.getPhoneNumber());
                        }
                    }
                });
    }

    /**
     * Saves the updated user profile to Firestore using the device ID.
     * Notifies the listener of the update and dismisses the dialog on success.
     */
    private void saveUserProfile() {
        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phoneNumber = phoneEditText.getText().toString();

        // Update the UserProfile object
        UserProfile updatedProfile = new UserProfile(username, email, phoneNumber);

        db.collection("User").document(deviceID)
                .set(updatedProfile)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();

                    // Notify the listener with the updated profile
                    if (listener != null) {
                        listener.onProfileUpdated(updatedProfile); // Pass the updated profile
                    }
                    dismiss();
                })
                .addOnFailureListener(e -> {
                    Log.e("EditProfileDialog", "Failed to update profile", e);
                    Toast.makeText(getContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Listener interface for notifying when the user profile is updated.
     */
    public interface OnProfileUpdatedListener {
        /**
         * Called when the user profile has been updated successfully.
         *
         * @param updatedProfile The updated UserProfile object.
         */
        void onProfileUpdated(UserProfile updatedProfile);
    }


}
