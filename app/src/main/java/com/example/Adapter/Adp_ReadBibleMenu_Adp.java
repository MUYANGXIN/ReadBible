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
 * Created by qxx on 2017/6/24.
 */

public class Adp_ReadBibleMenu_Adp extends RecyclerView.Adapter<Adp_ReadBibleMenu_Adp.ViewHolder>{
    private Context mContext;
    private List<Adp_ReadBibleMenu_BianLiang> mTupianList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView tupianImage;
        TextView tupianName,tupianName2;
        public ViewHolder(View view){
            super(view);
            cardView=(CardView) view;
            tupianImage=(ImageView)view.findViewById(R.id.adp_readbibllemenu_image1);
            tupianName=(TextView)view.findViewById(R.id.adp_readbibllemenu_text1);
            tupianName2=(TextView)view.findViewById(R.id.adp_readbibllemenu_text2);

        }
    }

    //主页图片适配器，作用未知
    public Adp_ReadBibleMenu_Adp(List<Adp_ReadBibleMenu_BianLiang>tupianList){
        mTupianList=tupianList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(mContext).inflate(R.layout.adp_readbiblemenu_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Adp_ReadBibleMenu_BianLiang tu=mTupianList.get(position);
        holder.tupianName.setText(tu.getName());
        holder.tupianName2.setText(tu.getName2());
        Glide.with(mContext).load(tu.getImageId()).into(holder.tupianImage);
    }

    @Override
    public int getItemCount(){
        return mTupianList.size();
    }

}