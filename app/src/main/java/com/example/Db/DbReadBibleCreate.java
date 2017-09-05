package com.example.Db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.LogMy.LogMy;

/**
 * Created by qxx on 2017/6/26.
 */

public class DbReadBibleCreate extends SQLiteOpenHelper{

    //创建收藏题表格
    public static final String CREATE_OLD="create table chuangshiji("
            +"id integer primary key autoincrement,"

            +"Juan text,"
            +"Zhang integer,"

            +"TextBible text)";

    //创建历史记录表格
    public static final String CREATE_NEW="create table mataifuyin("
            +"id integer primary key autoincrement,"

            +"Juan text,"
            +"Zhang integer,"

            +"TextBible text)";


    private Context mContext;
    public DbReadBibleCreate(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mContext=context;
    }





    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_OLD);
        db.execSQL(CREATE_NEW);
        Toast.makeText(mContext,"create succeeded",Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

        db.execSQL("drop table if exists old");
        db.execSQL("drop table if exists new");
        LogMy.d("zhendema","fhuwuifgufuifuig");
        onCreate(db);
    }

}
