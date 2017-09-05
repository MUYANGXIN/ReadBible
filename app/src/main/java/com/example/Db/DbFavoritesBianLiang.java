package com.example.Db;

/**
 * Created by qxx on 2017/5/29.
 */

public class DbFavoritesBianLiang {

    //日期,用来记录收藏的日期
    public String DingYiFTime;

    //from，用来记录这道题来自哪张题库表，然后结合ID查找到对应题目
    public int DingYiFrom;

    //ID
    public int DingyiFId;
    //以上四项的使用顺序为：时间、次数、from、ID

    //历史记录所用变量
    public String DingYiRcdTime;
    public String DingYiRcdFrom;
    public String DingYiRcdId;
    public String DingYiRcdSelectAll;
    public String DingYiRcdRightLV;

}
