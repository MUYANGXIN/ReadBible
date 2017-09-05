package com.example.Adapter;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by qxx on 2017/7/5.
 */

public class Adp_ReadBibleMain_listView_support extends AppCompatActivity {

    private String name;
    private String  imaged;
    public Adp_ReadBibleMain_listView_support(String zhangjie,String neirong){
        this.name=zhangjie;
        this.imaged=neirong;
    }
    public String getName(){
        return name;
    }
    public String  getImaged(){
        return imaged;
    }
}
