package com.example.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by qxx on 2017/7/17.
 */

public abstract class Adp_ReadBibleSearch_SearchView_ComAdp<T> extends BaseAdapter {

    protected Context mContext;
    protected List<T> mData;
    protected int mLayoutId;

    public Adp_ReadBibleSearch_SearchView_ComAdp(Context context,List<T> data,int layoutId){
        mContext = context;
        mData = data;
        mLayoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Adp_ReadBibleSearch_SearchView_Holder holder = Adp_ReadBibleSearch_SearchView_Holder.getHolder(mContext,convertView,mLayoutId,parent,position);
        convert(holder,position);
        return holder.getConvertView();
    }

    /**
     * get holder convert
     */
    public abstract void convert(Adp_ReadBibleSearch_SearchView_Holder holder,int position);




}
