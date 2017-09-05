package com.example.Adapter;

import android.content.Context;

import com.example.shujuku.R;

import java.util.List;

/**
 * Created by qxx on 2017/7/17.
 */

public class Adp_ReadBibleSearch_SearchView_Adp extends Adp_ReadBibleSearch_SearchView_ComAdp<Adp_ReadBibleSearch_SearchView_BianLiang>{

    public Adp_ReadBibleSearch_SearchView_Adp(Context context, List<Adp_ReadBibleSearch_SearchView_BianLiang> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(Adp_ReadBibleSearch_SearchView_Holder holder, int position) {
        holder.setImageResource(R.id.item_search_iv_icon,mData.get(position).getIconId())
                .setText(R.id.item_search_tv_title,mData.get(position).getTitle())
                .setText(R.id.item_search_tv_content,mData.get(position).getContent())
                .setText(R.id.item_search_tv_comments,mData.get(position).getComments());
    }
}
