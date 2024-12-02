package com.example.quacks_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * The {@code ApplicantArrayAdapter} class is a custom adapter for displaying {@link Cartable} objects
 * in a {@link android.widget.ListView}. It binds the data from {@code Cartable} objects to the views
 * in a custom layout.
 */
public class ApplicantArrayAdapter extends ArrayAdapter<Cartable> {
    private ArrayList<Cartable> users;
    private Context context;

    /**
     * Constructs an {@code ApplicantArrayAdapter}.
     *
     * @param context the context in which the adapter is used
     * @param users   the list of {@link Cartable} objects to display
     */
    public ApplicantArrayAdapter(Context context, ArrayList<Cartable> users){
        super(context,0, users);
        this.users = users;
        this.context = context;
    }

    /**
     * Provides a view for an adapter view (e.g., {@link android.widget.ListView}).
     *
     * @param position    the position of the item within the adapter's data set
     * @param convertView the old view to reuse, if possible
     * @param parent      the parent that this view will eventually be attached to
     * @return a view corresponding to the data at the specified position
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content, parent, false);
        }

        TextView eventName = view.findViewById(R.id.event_text);
        TextView date= view.findViewById(R.id.date_text);
        TextView instructor = view.findViewById(R.id.instructor_text);

        Cartable user = users.get(position);
            eventName.setText(user.field);
            date.setText(user.subfield);


        return view;
    }
}
