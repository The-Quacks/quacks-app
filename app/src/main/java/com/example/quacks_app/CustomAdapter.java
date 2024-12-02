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
 * A custom {@link ArrayAdapter} for displaying items with both headings and subheadings.
 * This adapter is designed to work with {@link Listable} objects, which provide display
 * and sub-display text fields for customization.
 */
public class CustomAdapter extends ArrayAdapter<Listable> {
    private Context mContext;
    private ArrayList<Listable> dataList;
    private int resource;

    /**
     * Constructs a new {@code CustomAdapter}.
     *
     * @param context  The context in which the adapter is being used.
     * @param resource The resource ID for the layout file that defines the list item views.
     * @param dataList The list of {@link Listable} objects to display.
     */
    public CustomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Listable> dataList) {
        super(context, resource, dataList);
        this.mContext = context;
        this.resource = resource;
        this.dataList = dataList;
    }

    /**
     * Provides a view for an adapter view (such as a {@link android.widget.ListView}).
     *
     * @param position    The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible. If it is {@code null}, a new view will be created.
     * @param parent      The parent view that this view will be attached to.
     * @return A {@link View} corresponding to the data at the specified position.
     */
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
