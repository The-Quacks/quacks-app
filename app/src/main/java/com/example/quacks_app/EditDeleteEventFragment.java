package com.example.quacks_app;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class EditDeleteEventFragment extends DialogFragment {

    public interface EditDeleteDialogListener {
        void onEditSelected();
        void onDeleteSelected();
    }

    private EditDeleteDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (EditDeleteDialogListener) context; // Cast the activity as listener
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement EditDeleteDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Choose an Action")
                .setItems(new CharSequence[]{"Edit Event", "Delete Event"}, (dialog, which) -> {
                    if (which == 0) {
                        // Edit Event selected
                        listener.onEditSelected();
                    } else if (which == 1) {
                        // Delete Event selected
                        listener.onDeleteSelected();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        return builder.create();
    }
}
