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
 * Custom adapter for displaying a list of {@link Event} objects in a {@link android.widget.ListView}.
 */
public class EventArrayAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> events;
    private Context context;
    private Facility facility;

    /**
     * Constructor to initialize the adapter with a list of events and a facility.
     *
     * @param context  The current context of the application.
     * @param events   The list of {@link Event} objects to display.
     * @param facility The {@link Facility} associated with the events (optional).
     */
    public EventArrayAdapter(Context context,ArrayList<Event> events, Facility facility) {
        super(context, 0, events);
        this.events = events;
        this.context = context;
        this.facility = facility;
    }

    /**
     * Constructor to initialize the adapter with a list of events.
     * If the event list is empty, an empty list is assigned.
     *
     * @param context The current context of the application.
     * @param events  The list of {@link Event} objects to display.
     */
    public EventArrayAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
        this.events = (!events.isEmpty()) ? events : new ArrayList<>();
        this.context = context;
    }

    /**
     * Provides a view for an adapter view (ListView, GridView, etc.).
     *
     * @param position    The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent view group to which this view will be attached.
     * @return A View corresponding to the data at the specified position.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content, parent, false);
        }

        Event event = events.get(position);

        TextView eventName = view.findViewById(R.id.event_text);
        TextView eventDate = view.findViewById(R.id.date_text);
        TextView eventInstructor = view.findViewById(R.id.instructor_text);
        //TextView class_capacity = view.findViewById(R.id.class_capacity);

        eventName.setText(event.getEventName());
        eventDate.setText(event.getDateTime().toString());
        eventInstructor.setText(event.getDescription());

        return view;
    }
}
