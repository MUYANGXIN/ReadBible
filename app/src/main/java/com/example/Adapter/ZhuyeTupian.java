package com.example.Adapter;

/**
 * Created by qxx on 2017/5/12.
 */

public class ZhuyeTupian {
    private String name;
    private int imageId;
    //主页图片的两个属性：名称与地址编号
    public ZhuyeTupian(String name,int imageId){
        this.name=name;
        this.imageId=imageId;
    }
    public String getName(){
        return name;
    }
    public int getImageId(){
        return imageId;
    }

}
