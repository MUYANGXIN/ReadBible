package com.example.Title;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.ActivityAds.ActivityCollector;
import com.example.shujuku.R;

/**
 * Created by qxx on 2017/5/19.
 */
//设置答题界面的标题栏，但这个标题好像是：数字题，怎样才能在一个类中实现改变标题的功能有待学习。
public class Title_Main extends LinearLayout{



    public Title_Main (Context context, AttributeSet attrs){
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.title_main,this);
        Button back1=(Button)findViewById(R.id.title_back);
        Button edit1=(Button)findViewById(R.id.title_edit1);




        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCollector.finishAll();
            }
        });
        edit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "收藏功能有待更新", Toast.LENGTH_SHORT).show();
            }
        });
    }









}
