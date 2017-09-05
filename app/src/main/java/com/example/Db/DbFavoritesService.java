package com.example.Db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qxx on 2017/5/29.
 */

public class DbFavoritesService {
    private SQLiteDatabase db;

    public DbFavoritesService(){
        db=SQLiteDatabase.openDatabase("/data/data/com.example.shujuku/databases/Fav.db",null,SQLiteDatabase.OPEN_READWRITE);
    }


    public List<DbFavoritesBianLiang>GetFavFromAndId(){
        //将已经收藏的来源和ID返回

        List<DbFavoritesBianLiang>list=new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from favque",null);

        if(cursor.getCount()>0){
            cursor.moveToFirst();
            int count=cursor.getCount();
            //count是数据库的总长度，数组a的长度是题目个数，a[i]的值是问题的ID

            //遍历数据库，将取出来的数据放进List数组中
            for(int i=0;i<count;i++){
                cursor.moveToPosition(i);
                DbFavoritesBianLiang bianliang=new DbFavoritesBianLiang();
                //获得问题的时间、次数、来源、ID
                bianliang.DingYiFTime=cursor.getString(cursor.getColumnIndex("FavTime"));
                bianliang.DingYiFrom=cursor.getInt(cursor.getColumnIndex("FavFrom"));
                bianliang.DingyiFId=cursor.getInt(cursor.getColumnIndex("FavId"));
                list.add(bianliang);
            }
        }

        return list;
    }

    public List<DbFavoritesBianLiang>GetRcd0(){
        List<DbFavoritesBianLiang>list=new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from recordque",null);

        if(cursor.getCount()>0){
            cursor.moveToFirst();
            int count=cursor.getCount();
            //count是数据库的总长度，数组a的长度是题目个数，a[i]的值是问题的ID

            //遍历数据库，将取出来的数据放进List数组中
            for(int i=0;i<count;i++){
                cursor.moveToPosition(i);
                DbFavoritesBianLiang bianliang=new DbFavoritesBianLiang();
                //获得问题的时间、次数、来源、ID
                bianliang.DingYiRcdTime=cursor.getString(cursor.getColumnIndex("RcdTime"));
                bianliang.DingYiRcdFrom=cursor.getString(cursor.getColumnIndex("RcdFrom"));
                bianliang.DingYiRcdId=cursor.getString(cursor.getColumnIndex("RcdId"));
                bianliang.DingYiRcdSelectAll=cursor.getString(cursor.getColumnIndex("RcdselectAll"));
                bianliang.DingYiRcdRightLV=cursor.getString(cursor.getColumnIndex("RcdRightLV"));

                list.add(bianliang);
            }
        }
        return list;
    }
}
