package com.example.quacks_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * The {@code ImageAdapter} class is a custom {@code ArrayAdapter} designed to display images
 * in a ListView or GridView using image IDs to retrieve images from a database.
 */
public class ImageAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private ArrayList<String> dataList;
    private int resource;

    /**
     * Constructs an {@code ImageAdapter} with the specified context, layout resource, and data list.
     *
     * @param context  The current context.
     * @param resource The resource ID for the layout file.
     * @param dataList An {@code ArrayList} of image IDs to display.
     */
    public ImageAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> dataList) {
        super(context, resource, dataList);
        this.mContext = context;
        this.resource = resource;
        this.dataList = dataList;
    }

    /**
     * Returns a view for each item in the data set.
     *
     * @param position    The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent      The parent view that this view will eventually be attached to.
     * @return A {@code View} corresponding to the data at the specified position.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(resource, parent, false);
        }

        ImageView dispImg = view.findViewById(R.id.image_view);

        String imgId = dataList.get(position);

        CRUD.downloadImage(imgId, new ReadCallback<Bitmap>() {
            @Override
            public void onReadSuccess(Bitmap data) {
                dispImg.post(() -> dispImg.setImageBitmap(data));
            }

            @Override
            public void onReadFailure(Exception e) {

            }
        });

        return view;
    }
}
