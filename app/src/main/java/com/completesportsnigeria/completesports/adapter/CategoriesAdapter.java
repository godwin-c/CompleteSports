package com.completesportsnigeria.completesports.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.completesportsnigeria.completesports.R;
import com.completesportsnigeria.completesports.classes.Categories;

import java.util.ArrayList;

public class CategoriesAdapter extends BaseAdapter {

    Context c;
    ArrayList<Categories> categories;
    LayoutInflater inflater;

    public CategoriesAdapter(Context c, ArrayList<Categories> categories) {
        this.c = c;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater==null)
        {
            inflater= (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.model,parent,false);
        }

        TextView nameTxt= (TextView) convertView.findViewById(R.id.nameTxt);
//        ImageView img= (ImageView) convertView.findViewById(R.id.movieImage);

        final String name = categories.get(position).getTitle();
//        int image=tvShows.get(position).getImage();

        nameTxt.setText(name);
//        img.setImageResource(image);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c,name,Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
