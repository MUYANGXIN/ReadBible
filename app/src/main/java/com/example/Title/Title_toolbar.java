package com.example.Title;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.shujuku.R;

/**
 * Created by qxx on 2017/6/14.
 */

public class Title_toolbar extends LinearLayout {

    public Title_toolbar (Context context, AttributeSet attrs){
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.titlebar,this);


    }
}
