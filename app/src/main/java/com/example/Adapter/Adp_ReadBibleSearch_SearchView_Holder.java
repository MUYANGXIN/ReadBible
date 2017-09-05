package com.example.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by qxx on 2017/7/17.
 */

public class Adp_ReadBibleSearch_SearchView_Holder {

    private SparseArray<View> mViews;
    private Context mContext;
    private View mConvertView;
    private int mPosition;
    /**
     * init holder
     */
    public Adp_ReadBibleSearch_SearchView_Holder(Context context, int layoutId, ViewGroup parent, int position) {
        mConvertView = LayoutInflater.from(context).inflate(layoutId,parent,false);
        mViews = new SparseArray<>();
        mPosition = position;
        mConvertView.setTag(this);
    }

    /**
     *  获取viewHolder
     */
    public static Adp_ReadBibleSearch_SearchView_Holder getHolder(Context context, View convertView,
                                       int layoutId, ViewGroup parent, int position) {
        if(convertView == null){
            return new Adp_ReadBibleSearch_SearchView_Holder(context,layoutId,parent,position);
        }else{
            Adp_ReadBibleSearch_SearchView_Holder holder = (Adp_ReadBibleSearch_SearchView_Holder)convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    public View getConvertView(){
        return mConvertView;
    }

    /**
     * get view
     */
    public <T extends View> T getView(int viewId){
        View view = mViews.get(viewId);
        if(view == null){
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T)view;
    }

    /**
     * set text
     */
    public Adp_ReadBibleSearch_SearchView_Holder setText(int viewId, String text){
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     *  set image res
     */
    public Adp_ReadBibleSearch_SearchView_Holder setImageResource(int viewId,int resId){
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
        return this;
    }

    /**
     *  set image bitmap
     */
    public Adp_ReadBibleSearch_SearchView_Holder setImageBitmap(int viewId,Bitmap bitmap){
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bitmap);
        return this;
    }

}
