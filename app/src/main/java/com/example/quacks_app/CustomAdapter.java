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

/*
Custom ArrayAdapter so we can assign headings and subheadings, not just a single peice of text
 */

public class CustomAdapter extends ArrayAdapter<Event> {
    private Context mContext;
    private ArrayList<Event> dataList;
    private int resource;

    public CustomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Event> dataList) {
        super(context, resource, dataList);
        this.mContext = context;
        this.resource = resource;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(resource, parent, false);
        }

        TextView field1 = view.findViewById(R.id.admin_field1_view);
        TextView field2 = view.findViewById(R.id.admin_field2_view);

        String data1 = dataList.get(position).getDisplay();
        String data2 = dataList.get(position).getSubDisplay();

        field1.setText(data1);
        field2.setText(data2);

        return view;
    }
}
