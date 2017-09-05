package com.example.Db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by qxx on 2017/5/10.
 */

public class AccountHelper extends SQLiteOpenHelper {
    //其实应该命名为create，这个类的作用是创建数据库
    public static final String CREATE_ACCOUNT="create table account("
            +"id integer primary key autoincrement,"
            +"account text,"
            +"password text)";
    private Context mContext;
    public AccountHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_ACCOUNT);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}
