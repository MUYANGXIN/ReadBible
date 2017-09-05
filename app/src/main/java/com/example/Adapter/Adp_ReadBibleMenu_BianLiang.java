package com.example.Adapter;

/**
 * Created by qxx on 2017/6/24.
 */

public class Adp_ReadBibleMenu_BianLiang {
    private String name,name2;
    private int imageId;
    //主页图片的两个属性：名称与地址编号
    public Adp_ReadBibleMenu_BianLiang(String name,String name2,int imageId){

        this.name=name;
        this.name2=name2;
        this.imageId=imageId;
    }
    public String getName(){
        return name;
    }
    public String getName2(){
        return name2;
    }
    public int getImageId(){
        return imageId;
    }
}
