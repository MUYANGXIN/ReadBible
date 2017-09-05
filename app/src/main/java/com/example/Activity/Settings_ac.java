package com.example.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.Theme.MyThemeSet;
import com.example.shujuku.R;

public class Settings_ac extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyThemeSet.setMyTheme(this);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_aclout);

        MyThemeSet themeSet = new MyThemeSet();
        final int the = themeSet.GetsTheme();

        Button btn_title_back = (Button) findViewById(R.id.title_back);
        btn_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_left, R.anim.out_right);

            }
        });
        TextView tv_title_text = (TextView) findViewById(R.id.title_text);
        tv_title_text.setText("简单的设置");
        Button btn_title_edit = (Button) findViewById(R.id.title_edit1);

        btn_title_edit.setBackgroundResource(R.color.gray);

        Button btn_theme = (Button) findViewById(R.id.btn_mode);
        btn_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (the == 1) {
                    MyThemeSet.changeToTheme(Settings_ac.this, 2);

                } else {
                    MyThemeSet.changeToTheme(Settings_ac.this, 1);

                }

            }
        });

    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //处理系统返回键的逻辑
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            //do something...
            finish();
            overridePendingTransition(R.anim.in_left, R.anim.out_right);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
