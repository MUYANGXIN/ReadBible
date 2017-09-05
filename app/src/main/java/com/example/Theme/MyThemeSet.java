package com.example.Theme;

import android.app.Activity;
import android.content.Intent;

import com.example.LogMy.LogMy;
import com.example.shujuku.R;

/**
 * Created by qxx on 2017/6/4.
 */

public class MyThemeSet {

    private static int sTheme=1,sTheme2=1;


    /**
     * Set the theme of the Activity, and restart it by creating a new Activity
     * of the same type.
     */
    public static void changeToTheme(Activity activity, int theme)
    {
        sTheme = theme;
        activity.finish();

        activity.startActivity(new Intent(activity, activity.getClass()));
    }


    /** Set the theme of the activity, according to the configuration. */
    public static void setMyTheme(Activity activity)
    {

        switch (sTheme)
        {

            case 1:
                activity.setTheme(R.style.DayTheme);

                //LogMy.d("改变主题","白天");
                break;
            case 2:
                activity.setTheme(R.style.NightTheme);
                //LogMy.d("改变主题","黑夜");
                break;
            default:
                //LogMy.d("改变主题","还是白天");
                activity.setTheme(R.style.NightTheme);
                break;
        }
    }



    public int GetsTheme(){
        return sTheme;
    }
//    public int GetsTheme2(){
//        return sTheme2;
//    }
}
