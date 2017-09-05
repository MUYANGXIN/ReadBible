package com.example.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.Theme.MyThemeSet;
import com.example.shujuku.R;

public class FanKui_ac extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        MyThemeSet.setMyTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fankui_aclout);
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
