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

public class ImageAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private ArrayList<String> dataList;
    private int resource;

    public ImageAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> dataList) {
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
