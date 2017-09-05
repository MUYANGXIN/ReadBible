package com.example.Db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.Question.QueShuZi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by qxx on 2017/5/19.
 */

public class DbShuZiTiService {
    private SQLiteDatabase db;

    //public int len=5;
    private QueShuZi ran;
    private String pos;

    //连接数据库
    public DbShuZiTiService(){
        db=SQLiteDatabase.openDatabase("/data/data/com.example.shujuku/databases/Question.db",null,SQLiteDatabase.OPEN_READWRITE);
    }

    //获得数据库数据
    //这个方法的作用是获得长度为len的随机问题序列
    public List<DbShuZiTiBianLiang> getQuestion(int len,int p){
        List<DbShuZiTiBianLiang>list=new ArrayList<>();

        final int f;
        switch (p){
            case 0:
                pos="questionShuzi";
                f=-10;
                break;
            case 1:
                pos="questionName";
                f=-11;
                break;
            case 2:
                pos="questionTianKong";
                f=-12;
                break;
            default:
                f=-1;
                break;

        }

        Cursor cursor=db.rawQuery("select * from "+pos,null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            int count=cursor.getCount();
            //count是数据库的总长度，数组a的长度是题目个数，a[i]的值是问题的ID


            ran=new QueShuZi();
            int []a=ran.GetRandomNumber(count,len);


            //遍历数据库，将取出来的数据放进List数组中
            for(int i=0;i<a.length;i++){
                cursor.moveToPosition(a[i]);
                DbShuZiTiBianLiang bianliang=new DbShuZiTiBianLiang();
                //获得问题的序号、标题、选项、正确答案及解析
                //bianliang.DingyiId=cursor.getInt(cursor.getColumnIndex("QueId"));
                bianliang.DingyiId=a[i];
                bianliang.Dingyique=cursor.getString(cursor.getColumnIndex("QueTitle"));
                bianliang.DingyiA=cursor.getString(cursor.getColumnIndex("QueAnswerA"));
                bianliang.DingyiB=cursor.getString(cursor.getColumnIndex("QueAnswerB"));
                bianliang.DingyiC=cursor.getString(cursor.getColumnIndex("QueAnswerC"));
                bianliang.DingyiD=cursor.getString(cursor.getColumnIndex("QueAnswerD"));
                //QueRightAnswer的值应该在0-3之间，分别对应ABCD
                bianliang.DingyiRightanswer=cursor.getInt(cursor.getColumnIndex("QueRightAnswer"));
                bianliang.DingyiExplain=cursor.getString(cursor.getColumnIndex("QueExplain"));
                bianliang.DingyiSelectedAnswer=f;
                list.add(bianliang);

            }
        }
        return list;
    }

    //取出来数组a[]指定的问题


    //获得随机题，大致思路是获得题目长度，然后分为三份（其实只有10、20、40三种选择，可以定好每个选择下美中踢得数目），然后
    // 分别连接各自数据库，取出三分之一的题目，放进list数组中，然后返回。
    public List<DbShuZiTiBianLiang> getRandomQuestion(int len){
        List<DbShuZiTiBianLiang>list=new ArrayList<>();
        List<DbShuZiTiBianLiang>list2=new ArrayList<>();

        int[] len0;
        int[] len1;
        int[] len2;
        switch (len){

            case 10:
                len0=new int[3];
                len1=new int[3];
                len2=new int[4];
                break;
            case 20:
                len0=new int[7];
                len1=new int[7];
                len2=new int[6];
                break;
            case 30:
                len0=new int[10];
                len1=new int[10];
                len2=new int[10];
                break;
            default:
                len0=new int[3];
                len1=new int[3];
                len2=new int[4];
                break;
        }

        //分割好长度，再分别从三张表中取出随机问题
        ran=new QueShuZi();
        Cursor cursor0=db.rawQuery("select * from questionShuzi",null);
        if(cursor0.getCount()>0){
            cursor0.moveToFirst();
            int count=cursor0.getCount();
            //count是数据库的总长度，数组a的长度是题目个数，a[i]的值是问题的ID


            int []a=ran.GetRandomNumber(count,len0.length);


            //遍历数据库，将取出来的数据放进List数组中
            for(int i=0;i<a.length;i++){
                cursor0.moveToPosition(a[i]);
                DbShuZiTiBianLiang bianliang=new DbShuZiTiBianLiang();
                //获得问题的序号、标题、选项、正确答案及解析
                //bianliang.DingyiId=cursor.getInt(cursor.getColumnIndex("QueId"));
                bianliang.DingyiId=a[i];
                bianliang.Dingyique=cursor0.getString(cursor0.getColumnIndex("QueTitle"));
                bianliang.DingyiA=cursor0.getString(cursor0.getColumnIndex("QueAnswerA"));
                bianliang.DingyiB=cursor0.getString(cursor0.getColumnIndex("QueAnswerB"));
                bianliang.DingyiC=cursor0.getString(cursor0.getColumnIndex("QueAnswerC"));
                bianliang.DingyiD=cursor0.getString(cursor0.getColumnIndex("QueAnswerD"));
                //QueRightAnswer的值应该在0-3之间，分别对应ABCD
                bianliang.DingyiRightanswer=cursor0.getInt(cursor0.getColumnIndex("QueRightAnswer"));
                bianliang.DingyiExplain=cursor0.getString(cursor0.getColumnIndex("QueExplain"));
                bianliang.DingyiSelectedAnswer=-10;
                list.add(bianliang);

            }
        }

        //取出名称题
        Cursor cursor1=db.rawQuery("select * from questionName",null);
        if(cursor1.getCount()>0){
            cursor1.moveToFirst();
            int count=cursor1.getCount();
            //count是数据库的总长度，数组a的长度是题目个数，a[i]的值是问题的ID

            //ran=new QueShuZi();
            int []a=ran.GetRandomNumber(count,len1.length);

            //遍历数据库，将取出来的数据放进List数组中
            for(int i=0;i<a.length;i++){
                cursor1.moveToPosition(a[i]);
                DbShuZiTiBianLiang bianliang=new DbShuZiTiBianLiang();
                //获得问题的序号、标题、选项、正确答案及解析
                //bianliang.DingyiId=cursor.getInt(cursor.getColumnIndex("QueId"));
                bianliang.DingyiId=a[i];
                bianliang.Dingyique=cursor1.getString(cursor1.getColumnIndex("QueTitle"));
                bianliang.DingyiA=cursor1.getString(cursor1.getColumnIndex("QueAnswerA"));
                bianliang.DingyiB=cursor1.getString(cursor1.getColumnIndex("QueAnswerB"));
                bianliang.DingyiC=cursor1.getString(cursor1.getColumnIndex("QueAnswerC"));
                bianliang.DingyiD=cursor1.getString(cursor1.getColumnIndex("QueAnswerD"));
                //QueRightAnswer的值应该在0-3之间，分别对应ABCD
                bianliang.DingyiRightanswer=cursor1.getInt(cursor1.getColumnIndex("QueRightAnswer"));
                bianliang.DingyiExplain=cursor1.getString(cursor1.getColumnIndex("QueExplain"));
                bianliang.DingyiSelectedAnswer=-11;
                list.add(bianliang);

            }
        }

        //取出填空题
        Cursor cursor2=db.rawQuery("select * from questionTianKong",null);
        if(cursor2.getCount()>0){
            cursor2.moveToFirst();
            int count=cursor2.getCount();
            //count是数据库的总长度，数组a的长度是题目个数，a[i]的值是问题的ID

            ran=new QueShuZi();
            int []a=ran.GetRandomNumber(count,len2.length);

            //遍历数据库，将取出来的数据放进List数组中
            for(int i=0;i<a.length;i++){
                cursor2.moveToPosition(a[i]);
                DbShuZiTiBianLiang bianliang=new DbShuZiTiBianLiang();
                //获得问题的序号、标题、选项、正确答案及解析
                //bianliang.DingyiId=cursor.getInt(cursor.getColumnIndex("QueId"));
                bianliang.DingyiId=a[i];
                bianliang.Dingyique=cursor2.getString(cursor2.getColumnIndex("QueTitle"));
                bianliang.DingyiA=cursor2.getString(cursor2.getColumnIndex("QueAnswerA"));
                bianliang.DingyiB=cursor2.getString(cursor2.getColumnIndex("QueAnswerB"));
                bianliang.DingyiC=cursor2.getString(cursor2.getColumnIndex("QueAnswerC"));
                bianliang.DingyiD=cursor2.getString(cursor2.getColumnIndex("QueAnswerD"));
                //QueRightAnswer的值应该在0-3之间，分别对应ABCD
                bianliang.DingyiRightanswer=cursor2.getInt(cursor2.getColumnIndex("QueRightAnswer"));
                bianliang.DingyiExplain=cursor2.getString(cursor2.getColumnIndex("QueExplain"));
                bianliang.DingyiSelectedAnswer=-12;
                list.add(bianliang);

            }
        }
        //在这里返回的list实际上还是有一定的顺序，前几个是数字，中间是name，后面是填空，理想情况应该是完全混乱

        //接下来产生一个随机不重复数组，就可以将list的顺序彻底打乱
        Random q=new Random();
        int []no=new int[len];
        int []ret=new int[len];

        for(int i=0;i<len;i++) {
            no[i] = i;
        }

        int end=len-1;
        for(int i=0;i<len;i++){
            int a=q.nextInt(end+1);
            ret[i]=no[a];
            no[a]=no[end];
            end--;

        }

        for(int i=0;i<ret.length;i++){
            list2.add(list.get(ret[i]));
            //LogMy.d("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",""+ret[i]);
        }

        return list2;
    }


    /**通过from和ID获得题目*/
    public List<DbShuZiTiBianLiang>GetQueByFromAndId(int []from,int[] id){

        List<DbShuZiTiBianLiang>list=new ArrayList<>();

        for(int j=0;j<from.length;j++){
            switch (from[j]){
                case 0:
                    pos="questionShuzi";
                    break;
                case 1:
                    pos="questionName";
                    break;
                case 2:
                    pos="questionTianKong";
                    break;

            }

            Cursor cursor=db.rawQuery("select * from "+pos,null);
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                //int count=cursor.getCount();
                //count是数据库的总长度，数组a的长度是题目个数，a[i]的值是问题的ID

                //遍历数据库，将取出来的数据放进List数组中
                //for(int i=0;i<len;i++){

                cursor.moveToPosition(id[j]);
                DbShuZiTiBianLiang bianliang=new DbShuZiTiBianLiang();
                //获得问题的序号、标题、选项、正确答案及解析
                //bianliang.DingyiId=cursor.getInt(cursor.getColumnIndex("QueId"));
                bianliang.DingyiId=id[j];
                bianliang.Dingyique=cursor.getString(cursor.getColumnIndex("QueTitle"));
                bianliang.DingyiA=cursor.getString(cursor.getColumnIndex("QueAnswerA"));
                bianliang.DingyiB=cursor.getString(cursor.getColumnIndex("QueAnswerB"));
                bianliang.DingyiC=cursor.getString(cursor.getColumnIndex("QueAnswerC"));
                bianliang.DingyiD=cursor.getString(cursor.getColumnIndex("QueAnswerD"));
                //QueRightAnswer的值应该在0-3之间，分别对应ABCD
                bianliang.DingyiRightanswer=cursor.getInt(cursor.getColumnIndex("QueRightAnswer"));
                bianliang.DingyiExplain=cursor.getString(cursor.getColumnIndex("QueExplain"));
                bianliang.DingyiSelectedAnswer=-1;
                list.add(bianliang);
                //}
            }
        }


        return list;
    }

//    public List<DbShuZiTiBianLiang> getErrorQue(int[]f,int []a){
//        List<DbShuZiTiBianLiang>list=new ArrayList<>();
//
//        db=SQLiteDatabase.openDatabase("/data/data/com.example.shujuku/databases/Question.db",null,SQLiteDatabase.OPEN_READWRITE);
//
//        for(int j=0;j<f.length;j++){
//            switch (f[j]){
//                case 0:
//                    pos="questionShuzi";
//                    break;
//                case 1:
//                    pos="questionName";
//                    break;
//                case 2:
//                    pos="questionTianKong";
//                    break;
//
//            }
//
//            Cursor cursor=db.rawQuery("select * from "+pos,null);
//            if(cursor.getCount()>0){
//                cursor.moveToFirst();
//                //int count=cursor.getCount();
//                //count是数据库的总长度，数组a的长度是题目个数，a[i]的值是问题的ID
//
//
//
//                //遍历数据库，将取出来的数据放进List数组中
//                //for(int i=0;i<a.length;i++){
//                    cursor.moveToPosition(a[j]);
//                    DbShuZiTiBianLiang bianliang=new DbShuZiTiBianLiang();
//                    //获得问题的序号、标题、选项、正确答案及解析
//                    //bianliang.DingyiId=cursor.getInt(cursor.getColumnIndex("QueId"));
//                    bianliang.DingyiId=a[j];
//                    bianliang.Dingyique=cursor.getString(cursor.getColumnIndex("QueTitle"));
//                    bianliang.DingyiA=cursor.getString(cursor.getColumnIndex("QueAnswerA"));
//                    bianliang.DingyiB=cursor.getString(cursor.getColumnIndex("QueAnswerB"));
//                    bianliang.DingyiC=cursor.getString(cursor.getColumnIndex("QueAnswerC"));
//                    bianliang.DingyiD=cursor.getString(cursor.getColumnIndex("QueAnswerD"));
//                    //QueRightAnswer的值应该在0-3之间，分别对应ABCD
//                    bianliang.DingyiRightanswer=cursor.getInt(cursor.getColumnIndex("QueRightAnswer"));
//                    bianliang.DingyiExplain=cursor.getString(cursor.getColumnIndex("QueExplain"));
//                    bianliang.DingyiSelectedAnswer=-1;
//                    list.add(bianliang);
//
//                //}
//            }
//        }
//
//        return list;
//    }


}
