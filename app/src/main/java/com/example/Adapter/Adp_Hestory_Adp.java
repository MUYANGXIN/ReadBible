package com.example.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shujuku.R;

import java.util.List;
import java.util.Map;

/**
 * Created by qxx on 2017/6/3.
 */

public class Adp_Hestory_Adp extends BaseAdapter{

    private Context context;
    private List<Map<String, Object>> list;
    private LayoutInflater inflater;

    public Adp_Hestory_Adp(Context context, List<Map<String, Object>> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        convertView = null;
        if (convertView == null) {
            inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.hestory__one_list, null);
            viewHolder = new ViewHolder();
            viewHolder.year=(TextView)convertView.findViewById(R.id.show_hsty_time);
            //viewHolder.month=(TextView)convertView.findViewById(R.id.show_time);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title_hsty);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String titleStr = list.get(position).get("title").toString();
        String time=list.get(position).get("year").toString();

        viewHolder.year.setText(time);
        viewHolder.title.setText(titleStr);

        return convertView;
    }

    static class ViewHolder {
        public TextView year;
        //public TextView month;
        public TextView title;
    }
}
