package com.example.Db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.LogMy.LogMy;

/**
 * Created by qxx on 2017/5/29.
 */

public class DbFavoritesCreate extends SQLiteOpenHelper{

    //创建收藏题表格
    public static final String CREATE_FAVQUE="create table favque("
            +"id integer primary key autoincrement,"
            +"FavId integer,"
            +"FavTime text,"
            +"FavFrom integer)";

    //创建历史记录表格
    public static final String CREATE_FAVRECORD="create table recordque("
            +"id integer primary key autoincrement,"

            +"RcdTime text,"

            +"RcdFrom text,"
            +"RcdId text,"

            +"RcdselectAll text,"
            +"RcdRightLV text)";
   

    private Context mContext;
    public DbFavoritesCreate(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mContext=context;
    }





    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_FAVQUE);
        db.execSQL(CREATE_FAVRECORD);
       // Toast.makeText(mContext,"create succeeded",Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

        db.execSQL("drop table if exists favque");
        db.execSQL("drop table if exists recordque");
        LogMy.d("zhendema","fhuwuifgufuifuig");
        onCreate(db);
    }
}
