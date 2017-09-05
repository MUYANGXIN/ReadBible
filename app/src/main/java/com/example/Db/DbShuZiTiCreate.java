package com.example.Db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.LogMy.LogMy;

/**
 * Created by qxx on 2017/5/19.
 */

public class DbShuZiTiCreate extends SQLiteOpenHelper {
    //创建数字题表格
    public static final String CREATE_QUESTION="create table questionShuzi("
            +"id integer primary key autoincrement,"
            +"QueFrom integer"
            +"QueId integer,"
            +"QueTitle text,"
            +"QueAnswerA text,"
            +"QueAnswerB text,"
            +"QueAnswerC text,"
            +"QueAnswerD text,"
            +"QueRightAnswer text,"
            +"QueExplain text)";
    //创建人名题表格
    public static final String CREATE_QUESTIONNAME="create table questionName("
            +"id integer primary key autoincrement,"
            +"QueFrom integer"
            +"QueId integer,"
            +"QueTitle text,"
            +"QueAnswerA text,"
            +"QueAnswerB text,"
            +"QueAnswerC text,"
            +"QueAnswerD text,"
            +"QueRightAnswer text,"
            +"QueExplain text)";
    //创建填空题表格
    public static final String CREATE_QUESTIONTIANKONG="create table questionTianKong("
            +"id integer primary key autoincrement,"
            +"QueFrom integer"
            +"QueId integer,"
            +"QueTitle text,"
            +"QueAnswerA text,"
            +"QueAnswerB text,"
            +"QueAnswerC text,"
            +"QueAnswerD text,"
            +"QueRightAnswer text,"
            +"QueExplain text)";
    //创建随机题表格，但随机题是不是可以随机从已经存在的三个表格里抽取呢？需思考，但先建立了再说
    public static final String CREATE_QUESTIONRANDOM="create table questionRandom("
            +"id integer primary key autoincrement,"
            +"QueFrom integer"
            +"QueId integer,"
            +"QueTitle text,"
            +"QueAnswerA text,"
            +"QueAnswerB text,"
            +"QueAnswerC text,"
            +"QueAnswerD text,"
            +"QueRightAnswer text,"
            +"QueExplain text)";

    private Context mContext;
    public DbShuZiTiCreate(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
        mContext=context;
    }





    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_QUESTION);
        db.execSQL(CREATE_QUESTIONNAME);
        db.execSQL(CREATE_QUESTIONTIANKONG);
        db.execSQL(CREATE_QUESTIONRANDOM);
        Toast.makeText(mContext,"create succeeded",Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

        db.execSQL("drop table if exists questionShuzi");
        db.execSQL("drop table if exists questionName");
        db.execSQL("drop table if exists questionTianKong");
        db.execSQL("drop table if exists questionRandom");
        LogMy.d("zhendema","fhuwuifgufuifuig");
        onCreate(db);
    }
}
