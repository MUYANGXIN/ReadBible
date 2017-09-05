package com.example.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import android.widget.TextView;

import com.example.shujuku.R;

import java.util.List;

/**
 * Created by qxx on 2017/7/5.
 */

public class Adp_ReadBibleMain_listView_Adp extends ArrayAdapter<Adp_ReadBibleMain_listView_support>{

    private int resource;
    public Adp_ReadBibleMain_listView_Adp(Context context, int textViewResourceId, List<Adp_ReadBibleMain_listView_support> objects){
        super(context,textViewResourceId,objects);
        resource=textViewResourceId;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Adp_ReadBibleMain_listView_support photos=getItem(position);
        View view;
//        =LayoutInflater.from(getContext()).inflate(resource,null);
        ViewHolder vHolder;
        if(convertView ==null){
            view= LayoutInflater.from(getContext()).inflate(resource, parent,false);
            vHolder=new ViewHolder();
            vHolder.photoimage=(TextView)view.findViewById(R.id.listview_zhangjie);
            vHolder.photoname=(TextView)view.findViewById(R.id.listview_neirong);
            view.setTag(vHolder);

        }
        else{
            view=convertView;
            vHolder=(ViewHolder)view.getTag();
            view.setTag(vHolder);
        }
        vHolder.photoimage.setText(photos.getImaged());
        vHolder.photoname.setText(photos.getName());

        return view;
    }
    private class ViewHolder{
        TextView photoimage;
        TextView photoname;
    }

}

