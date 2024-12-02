package com.example.quacks_app;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

/**
 * A DialogFragment for providing options to edit or delete an event.
 */
public class EditDeleteEventFragment extends DialogFragment {

    /**
     * Interface for handling edit and delete actions from the dialog.
     * Activities that use this fragment must implement this interface.
     */
    public interface EditDeleteDialogListener {
        /**
         * Callback for when the "Edit Event" option is selected.
         */
        void onEditSelected();
        /**
         * Callback for when the "Delete Event" option is selected.
         */
        void onDeleteSelected();
    }

    private EditDeleteDialogListener listener;

    /**
     * Attaches the fragment to the hosting activity and verifies that the activity
     * implements the {@link EditDeleteDialogListener} interface.
     *
     * @param context The context of the hosting activity.
     * @throws ClassCastException If the hosting activity does not implement {@link EditDeleteDialogListener}.
     */
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

    /**
     * Creates the dialog for editing or deleting an event.
     * The dialog presents three options: Edit Event, Delete Event, or Cancel.
     *
     * @param savedInstanceState The previously saved instance state, if any.
     * @return The constructed {@link Dialog} instance.
     */
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
