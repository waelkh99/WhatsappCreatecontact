package com.example.whatsappcreatecontact;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {
    Context context;
    ArrayList<Bitmap> imageID;
    LayoutInflater inflater;

    public GridAdapter() {
    }

    public GridAdapter(Context context, ArrayList<Bitmap> imageID) {
        this.context = context;
        this.imageID = imageID;

    }

    @Override
    public int getCount() {
        return imageID.size() ;
    }

    @Override
    public Object getItem(int i) {
        return imageID.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater==null){
            inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (view==null){
            view=inflater.inflate(R.layout.grid_item,null);
        }

        ImageView imageView=view.findViewById(R.id.gridImageView);
        imageView.setImageBitmap(imageID.get(i));

        return view;
    }
}
