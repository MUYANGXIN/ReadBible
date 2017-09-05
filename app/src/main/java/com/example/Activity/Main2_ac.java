package com.example.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.LogMy.LogMy;
import com.example.Question.QueName;
import com.example.Question.QueShuZi;
import com.example.Question.QueTianKong;
import com.example.Theme.MyThemeSet;
import com.example.ActivityAds.SysApp;
import com.example.Adapter.ItemClickSupport;
import com.example.Adapter.ZhuTuAdapter;
import com.example.Adapter.ZhuyeTupian;
import com.example.Db.DbShuZiTiBianLiang;
import com.example.Db.DbShuZiTiCreate;
import com.example.shujuku.R;

import java.util.ArrayList;
import java.util.List;

public class Main2_ac extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private int count;

    //尝试实现侧边栏点击
    private NavigationView navView;


    private List<ZhuyeTupian> tupianList = new ArrayList<>();
    private ZhuTuAdapter adapter;

    private DbShuZiTiCreate dbShuZiTiCreate;
    private SQLiteDatabase db;


    private int whereBack = 0;//whereback记录了打开另一个活动时当前main2活动中侧边栏是否打开。
    // 如果打开，则下一次重启该活动时自动打开侧边栏。否则隐藏。

    //用于取出记录的阅读位置
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    @Override
    public void onRestart() {
        super.onRestart();

        finish();
        Intent intent = new Intent(Main2_ac.this, Main2_ac.class);
//
        LogMy.d("设置whereback", "is" + whereBack);
        intent.putExtra("whereback", whereBack);
        startActivity(intent);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        MyThemeSet.setMyTheme(this);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        SysApp.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2aclout);

        ToolBarInit();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.kapian_view);//引入卡片布局
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        MyThemeSet tu = new MyThemeSet();
        int tup = tu.GetsTheme();

        if (tup == 1) {
            ZhuyeTupian[] tus = {
                    //定义一个图片组
                    new ZhuyeTupian("数字题", R.drawable.bible),
                    new ZhuyeTupian("名称题", R.drawable.bible),
                    new ZhuyeTupian("经文填空题", R.drawable.bible),
                    new ZhuyeTupian("随机题", R.drawable.bible)
            };
            for (int i = 0; i < tus.length; i++) {
                //将图片添加到图片数组中
                tupianList.add(tus[i]);
            }
            adapter = new ZhuTuAdapter(tupianList);
            recyclerView.setAdapter(adapter);

        } else {
            ZhuyeTupian[] tus = {
                    //定义一个图片组
                    new ZhuyeTupian("数字题", R.drawable.b4),
                    new ZhuyeTupian("名称题", R.drawable.b4),
                    new ZhuyeTupian("经文填空题", R.drawable.b4),
                    new ZhuyeTupian("随机题", R.drawable.b4)
            };
            for (int i = 0; i < tus.length; i++) {
                //将图片添加到图片数组中
                tupianList.add(tus[i]);
            }
            adapter = new ZhuTuAdapter(tupianList);
            recyclerView.setAdapter(adapter);
        }

        Intent intent = getIntent();
        int t = intent.getIntExtra("whereback", 0);
        if (t == 0) {

        } else {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }


        slideClick();


        //设置图片点击事件监听
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Click(position);
            }
        });

        //创建数据库
        dbShuZiTiCreate = new DbShuZiTiCreate(this, "Question.db", null, 3);


        FirstAddQueShuZi();
        FirstAddQueName();
        FirstAddQueTianKong();

    }


    //侧边栏点击事件
    private void slideClick() {
        navView = (NavigationView) findViewById(R.id.nav_view);
        // navView.setCheckedItem(R.id.nav_1);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                //侧边栏点击事件监听，点击时结束活动，返回

                switch (item.getItemId()) {
                    case R.id.nav_1:
                        Intent intent = new Intent(Main2_ac.this, Fav_One_ac.class);

                        whereBack = 1;
                        startActivity(intent);
                        break;
                    case R.id.nav_2:
                        Intent intent2 = new Intent(Main2_ac.this, Hestory_One_ac.class);
                        whereBack = 1;
                        startActivity(intent2);
                        break;
//                    case R.id.nav_3:
//                        Intent intent3=new Intent(Main2_ac.this,ReadBible_menu_ac.class);
//                        whereBack=1;
//                        startActivity(intent3);
//                        break;
                    case R.id.nav_4:
                        Intent intent4 = new Intent(Main2_ac.this, Settings_ac.class);
                        whereBack = 1;
                        startActivity(intent4);
                        break;
                    case R.id.nav_5:
                        Intent intent5 = new Intent(Main2_ac.this, FanKui_ac.class);
                        whereBack = 1;
                        startActivity(intent5);
                        break;
                    case R.id.nav_6:
                        Intent intent6 = new Intent(Main2_ac.this, FanKui_ac.class);
                        whereBack = 1;
                        startActivity(intent6);
                        break;
                    default:
                        whereBack = 1;
                        //Toast.makeText(Main2_ac.this,"guudf"+item.getItemId(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(Main2_ac.this, "请等待功能更新~", Toast.LENGTH_SHORT).show();
                        break;
                }
                //mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    //标题栏菜单按钮点击事件
    private void ToolBarInit() {

        Button titlecaidan = (Button) findViewById(R.id.titlebar_caidan);
        titlecaidan.setBackgroundResource(R.drawable.caidan);
        titlecaidan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        Button titledujing = (Button) findViewById(R.id.titlebar_dujing);
        titledujing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //取出上次记录的卷章
                pref = PreferenceManager.getDefaultSharedPreferences(Main2_ac.this);
                String juan = pref.getString("juan", "");
                int zhang = pref.getInt("zhang", 0);

                Intent intent = new Intent(Main2_ac.this, ReadBible_Main_ac.class);
                intent.putExtra("Juan", juan);
                intent.putExtra("Zhang", zhang);
                startActivity(intent);
            }
        });
    }


    private void Click(int pos) {
        if (mDrawerLayout.isDrawerOpen(navView)) {
            whereBack = 1;
        } else {
            whereBack = 0;
        }
        switch (pos) {
            //通过position的不同，打开不同的问题类型页面
            case 0:
                Intent intentShuZi = new Intent(Main2_ac.this, Main2_ShuZiTi_dianji_ac.class);
                intentShuZi.putExtra("position", pos);
                startActivity(intentShuZi);
                overridePendingTransition(R.anim.in_right, R.anim.out_left);
                break;
            case 1:
                //Toast.makeText(Main2_ac.this,"shvfouhg:"+pos,Toast.LENGTH_LONG).show();
                Intent intentName = new Intent(Main2_ac.this, Main2_ShuZiTi_dianji_ac.class);
                intentName.putExtra("position", pos);
                startActivity(intentName);
                overridePendingTransition(R.anim.in_right, R.anim.out_left);
                break;
            case 2:
                //Toast.makeText(Main2_ac.this,"shvfouhg:"+pos,Toast.LENGTH_LONG).show();
                Intent intentTianKong = new Intent(Main2_ac.this, Main2_ShuZiTi_dianji_ac.class);
                intentTianKong.putExtra("position", pos);
                startActivity(intentTianKong);
                overridePendingTransition(R.anim.in_right, R.anim.out_left);
                break;
            case 3:
                //Toast.makeText(Main2_ac.this,"shvfouhg:"+pos,Toast.LENGTH_LONG).show();
                Intent intentRandom = new Intent(Main2_ac.this, Main2_ShuZiTi_dianji_ac.class);
                intentRandom.putExtra("position", pos);
                startActivity(intentRandom);

                break;
        }

    }

    /**
     * 第一次添加数字题
     */
    public void FirstAddQueShuZi() {
        //count是准备添加数据的长度
        SQLiteDatabase dbwrite = dbShuZiTiCreate.getWritableDatabase();
        QueShuZi accQue = new QueShuZi();
        final List<DbShuZiTiBianLiang> list2 = accQue.AddQuestionShuZi();
        count = accQue.AddQueShuZiLen();

        //db连接数据库，dbLen是已经存储数字题表单的长度
        db = SQLiteDatabase.openDatabase("/data/data/com.example.shujuku/databases/Question.db", null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = db.rawQuery("select * from questionShuzi", null);
        int dbLen = cursor.getCount();

        //如果两个长度相等，则证明没有新添加问题，不用更新
        if (count == dbLen) {

        } else {//否则的话应将老数据删除，加入新数据


            dbwrite.delete("questionShuzi", "id>=?", new String[]{"0"});
            ContentValues values = new ContentValues();
            for (int i = 0; i < list2.size(); i++) {
                DbShuZiTiBianLiang q = list2.get(i);
                int id = q.DingyiId;
                String Q = q.Dingyique;
                String A = q.DingyiA;
                String B = q.DingyiB;
                String C = q.DingyiC;
                String D = q.DingyiD;
                Integer R = q.DingyiRightanswer;
                String E = q.DingyiExplain;

                values.put("QueFrom", 0);
                values.put("id", id);
                values.put("QueTitle", Q);
                values.put("QueAnswerA", A);
                values.put("QueAnswerB", B);
                values.put("QueAnswerC", C);
                values.put("QueAnswerD", D);
                values.put("QueRightAnswer", R);
                values.put("QueExplain", E);
                dbwrite.insert("questionShuzi", null, values);

            }
            //Toast.makeText(Main2_ac.this,"add 数字题 successed",Toast.LENGTH_SHORT).show();

        }

    }

    /**
     * 添加名称题
     */
    public void FirstAddQueName() {
        //count是准备添加数据的长度
        SQLiteDatabase dbwrite = dbShuZiTiCreate.getWritableDatabase();
        QueName accQue = new QueName();
        final List<DbShuZiTiBianLiang> list2 = accQue.AddQuestionName();
        count = accQue.AddQueNameLen();

        //db连接数据库，dbLen是已经存储名称题表单的长度
        db = SQLiteDatabase.openDatabase("/data/data/com.example.shujuku/databases/Question.db", null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = db.rawQuery("select * from questionName", null);
        int dbLen = cursor.getCount();

        //如果两个长度相等，则证明没有新添加问题，不用更新
        if (count == dbLen) {

        } else {//否则的话应将老数据删除，加入新数据


            dbwrite.delete("questionName", "id>=?", new String[]{"0"});
            ContentValues values = new ContentValues();
            for (int i = 0; i < list2.size(); i++) {
                DbShuZiTiBianLiang q = list2.get(i);
                int id = q.DingyiId;
                String Q = q.Dingyique;
                String A = q.DingyiA;
                String B = q.DingyiB;
                String C = q.DingyiC;
                String D = q.DingyiD;
                Integer R = q.DingyiRightanswer;
                String E = q.DingyiExplain;

                values.put("QueFrom", 1);
                values.put("id", id);
                values.put("QueTitle", Q);
                values.put("QueAnswerA", A);
                values.put("QueAnswerB", B);
                values.put("QueAnswerC", C);
                values.put("QueAnswerD", D);
                values.put("QueRightAnswer", R);
                values.put("QueExplain", E);
                dbwrite.insert("questionName", null, values);

            }
            //Toast.makeText(Main2_ac.this,"add Name successed",Toast.LENGTH_SHORT).show();

        }

    }

    /**
     * 添加填空题
     */
    public void FirstAddQueTianKong() {
        //count是准备添加数据的长度
        SQLiteDatabase dbwrite = dbShuZiTiCreate.getWritableDatabase();
        QueTianKong accQue = new QueTianKong();
        final List<DbShuZiTiBianLiang> list2 = accQue.AddQuestionTianKong();
        count = accQue.AddQueTianKongLen();

        //db连接数据库，dbLen是已经存储名称题表单的长度
        db = SQLiteDatabase.openDatabase("/data/data/com.example.shujuku/databases/Question.db", null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = db.rawQuery("select * from questionTianKong", null);
        int dbLen = cursor.getCount();

        //如果两个长度相等，则证明没有新添加问题，不用更新
        if (count == dbLen) {

        } else {//否则的话应将老数据删除，加入新数据


            dbwrite.delete("questionTianKong", "id>=?", new String[]{"0"});
            ContentValues values = new ContentValues();
            for (int i = 0; i < list2.size(); i++) {
                DbShuZiTiBianLiang q = list2.get(i);
                int id = q.DingyiId;
                String Q = q.Dingyique;
                String A = q.DingyiA;
                String B = q.DingyiB;
                String C = q.DingyiC;
                String D = q.DingyiD;
                Integer R = q.DingyiRightanswer;
                String E = q.DingyiExplain;

                values.put("QueFrom", 2);
                values.put("id", id);
                values.put("QueTitle", Q);
                values.put("QueAnswerA", A);
                values.put("QueAnswerB", B);
                values.put("QueAnswerC", C);
                values.put("QueAnswerD", D);
                values.put("QueRightAnswer", R);
                values.put("QueExplain", E);
                dbwrite.insert("questionTianKong", null, values);

            }
            //Toast.makeText(Main2_ac.this,"add TianKong successed",Toast.LENGTH_SHORT).show();

        }

    }


//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        //处理系统返回键的逻辑
//        if (keyCode == KeyEvent.KEYCODE_BACK
//                && event.getRepeatCount() == 0) {
//            //do something...
//            //ActivityCollector.finishAll();
//            super.onDestroy();
//            System.exit(0);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }


//
//    @Override
//    protected void onDestroy() {
//
//            super.onDestroy();
//            System.exit(0);
//        // 或者下面这种方式
//        //android.os.Process.killProcess(android.os.Process.myPid());
//    }


    /**
     * 返回键处理
     */
    private long firstTime = 0;

    @Override//关闭侧滑页面
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isDrawerOpen(navView)) {
                mDrawerLayout.closeDrawers();

                whereBack = 0;
                LogMy.d("4设置whereback", "is" + whereBack);
            } else {

                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 1000) {                                         //如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;//更新firstTime
                    return true;
                } else {
                    SysApp.getInstance().exit();
                }                                             //两次按键小于2秒时，退出应用

            }
        }
        return true;
    }


}
