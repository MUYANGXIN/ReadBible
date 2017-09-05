package com.example.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shujuku.R;

import java.util.List;

/**
 * Created by qxx on 2017/5/12.
 */

public class ZhuTuAdapter extends RecyclerView.Adapter<ZhuTuAdapter.ViewHolder>{
    private Context mContext;
    private List<ZhuyeTupian>mTupianList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView tupianImage;
        TextView tupianName;
        public ViewHolder(View view){
            super(view);
            cardView=(CardView) view;
            tupianImage=(ImageView)view.findViewById(R.id.zhuyetupian_xuanxiang);
            tupianName=(TextView)view.findViewById(R.id.zhuyetupian_text);
        }
    }
    //主页图片适配器，作用未知
    public ZhuTuAdapter(List<ZhuyeTupian>tupianList){
        mTupianList=tupianList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if(mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.zhuyetupian,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        ZhuyeTupian tu=mTupianList.get(position);
        holder.tupianName.setText(tu.getName());
        Glide.with(mContext).load(tu.getImageId()).into(holder.tupianImage);
    }
    @Override
    public int getItemCount(){
        return mTupianList.size();
    }

}





























