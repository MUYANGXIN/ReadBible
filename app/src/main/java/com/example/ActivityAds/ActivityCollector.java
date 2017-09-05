package com.example.ActivityAds;

import android.app.Activity;

import com.example.shujuku.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qxx on 2017/5/10.
 */

public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        //调用此方法销毁一切活动
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
                activity.overridePendingTransition(R.anim.in_left, R.anim.out_right);
            }
        }
    }
}
