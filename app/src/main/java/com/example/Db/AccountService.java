package com.example.Db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qxx on 2017/5/19.
 */

public class AccountService {
    private SQLiteDatabase db;

    //构造方法
    public AccountService(){
        //连接数据库
        db=SQLiteDatabase.openDatabase("/data/data/com.example.shujuku/databases/Account.db", null, SQLiteDatabase.OPEN_READWRITE);
    }
    //获取数据库数据
    public List<AccountDingyiBianliang>getAccount(){
        List<AccountDingyiBianliang> list=new ArrayList<>();

        Cursor cursor=db.rawQuery("select * from account",null);
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            int count=cursor.getCount();
            //遍历数据库的account表，以后其他类中想获得数据时调用此方法即可，不用再遍历数据库
            for(int i=0;i<count;i++){
                cursor.moveToPosition(i);
                AccountDingyiBianliang account_fordb=new AccountDingyiBianliang();
                account_fordb.account_dingyi=cursor.getString(cursor.getColumnIndex("account"));
                account_fordb.password_dingyi=cursor.getString(cursor.getColumnIndex("password"));
                list.add(account_fordb);
            }
        }
        return list;
    }
}
