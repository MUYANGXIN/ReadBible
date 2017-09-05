package com.example.Title;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ActivityAds.ActivityCollector;
import com.example.shujuku.R;

/**
 * Created by qxx on 2017/5/22.
 */
//设置错题回顾活动的标题栏
public class Title_ErrorQue extends LinearLayout {
    public Title_ErrorQue (Context context, AttributeSet attrs){
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.title_errorque,this);
        Button back1=(Button)findViewById(R.id.title_back2);
        Button edit1=(Button)findViewById(R.id.title_edit2);
        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCollector.finishAll();
            }
        });
        edit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "you clicked Edit button", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

