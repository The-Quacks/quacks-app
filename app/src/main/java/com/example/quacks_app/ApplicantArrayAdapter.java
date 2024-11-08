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

public class ApplicantArrayAdapter extends ArrayAdapter<User> {
    private ArrayList<User> users;
    private Context context;

    public ApplicantArrayAdapter(Context context, ArrayList<User> users){
        super(context,0, users);
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content, parent, false);
        }

        User user = users.get(position);
        UserProfile profile = user.getUserProfile();


        TextView eventName = view.findViewById(R.id.event_text);
        TextView date= view.findViewById(R.id.date_text);
        TextView instructor = view.findViewById(R.id.instructor_text);
        //TextView class_capacity = view.findViewById(R.id.class_capacity);

        eventName.setText(profile.getUserName());
        date.setText(profile.getPhoneNumber());
        instructor.setText(profile.getEmail());

        return view;
    }
}
