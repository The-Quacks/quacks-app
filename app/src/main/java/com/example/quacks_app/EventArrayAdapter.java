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

public class EventArrayAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> events;
    private Context context;
    private Facility facility;

    public EventArrayAdapter(Context context,ArrayList<Event> events, Facility facility) {
        super(context, 0, events);
        this.events = events;
        this.context = context;
        this.facility = facility;
    }

    public EventArrayAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
        this.events = (!events.isEmpty()) ? events : new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content, parent, false);
        }

        Event event = events.get(position);

        TextView eventName = view.findViewById(R.id.event_text);
        TextView date = view.findViewById(R.id.date_text);
        TextView instructor = view.findViewById(R.id.instructor_text);
        //TextView class_capacity = view.findViewById(R.id.class_capacity);

        eventName.setText(event.getDateTime().toString());
        date.setText(event.getDescription());
        if (facility != null) {
            instructor.setText(facility.getName());
        } else {
            instructor.setVisibility(View.GONE);
        }

        return view;
    }
}
