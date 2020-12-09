package com.yugentechlabs.hpquizadmin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yugentechlabs.hpquizadmin.Model.Level;
import com.yugentechlabs.hpquizadmin.R;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ImageView imageView;
    TextView level;
    ArrayList<Level> levels;

    public MainAdapter(Context c, List<Level> arr) {
        levels=new ArrayList<Level>();
        for(int i=0;i<arr.size();i++){
            levels.add(arr.get(i));
        }
        context=c;
    }

    @Override
    public int getCount() {
        return levels.size();
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
        if(inflater==null)
            inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null)
            convertView=inflater.inflate(R.layout.grid_item,null);
        imageView=convertView.findViewById(R.id.imageView);
        level=convertView.findViewById(R.id.level);
        level.setText(String.valueOf(levels.get(position).getLevelnum()));
        imageView.setImageResource(R.drawable.levels);
        return convertView;
    }


}
