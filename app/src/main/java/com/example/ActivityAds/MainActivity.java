package com.example.ActivityAds;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by qxx on 2017/5/10.
 */

/**
 * 结束活动。需要一次性结束的活动继承此类
 */
public class MainActivity extends Activity {
    //创建活动管理器，将需要统一管理的类继承为这个类的子类，就可以统一管理
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
