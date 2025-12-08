package com.example.finalproject_hailea;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    Context context;

    public ImageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return Profile.profilePictures.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView cell = new ImageView(this.context);
        cell.setImageResource(Profile.profilePictures[position]);
        cell.setLayoutParams(new ViewGroup.LayoutParams(400,400));
        return cell;
    }

}
