package com.example.Db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by qxx on 2017/6/26.
 */

public class DbReadBibleService {
    private SQLiteDatabase db;

    public DbReadBibleService(){
        db=SQLiteDatabase.openDatabase("/data/data/com.example.shujuku/databases/ReadBible.db",null,SQLiteDatabase.OPEN_READWRITE);
    }



}
