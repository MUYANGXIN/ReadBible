package com.example.ActivityAds;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by qxx on 2017/5/31.
 */

/**
 * 结束程序
 */
public class SysApp extends Application {

    private List<Activity> mList = new LinkedList<Activity>();
    private static SysApp instance;

    private SysApp() {
    }

    public synchronized static SysApp getInstance() {
        if (null == instance) {
            instance = new SysApp();
        }
        return instance;
    }

    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

}
