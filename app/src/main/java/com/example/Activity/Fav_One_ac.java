package com.example.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.Theme.MyThemeSet;
import com.example.Adapter.AdpFav_Adp;

import com.example.Db.DbFavoritesBianLiang;
import com.example.Db.DbFavoritesCreate;
import com.example.Db.DbFavoritesService;
import com.example.Db.DbShuZiTiBianLiang;
import com.example.Db.DbShuZiTiService;
import com.example.shujuku.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fav_One_ac extends AppCompatActivity {

    private ListView listView;
    public List<DbFavoritesBianLiang> listFavF = new ArrayList<>();
    public List<DbShuZiTiBianLiang> listFavGet = new ArrayList<>();
    private AdpFav_Adp favAdapter;
    private DbFavoritesCreate dbFavoritesCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyThemeSet.setMyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fav_one_aclout);


        TextView title = (TextView) findViewById(R.id.title_text);

        Button btn_title_back = (Button) findViewById(R.id.title_back);
        btn_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_left, R.anim.out_right);
            }
        });

        Button btn_shoucang = (Button) findViewById(R.id.title_edit1);
        btn_shoucang.setBackgroundResource(R.drawable.favs);


        listView = (ListView) this.findViewById(R.id.list_view);
        listView.setDividerHeight(0);

        dbFavoritesCreate = new DbFavoritesCreate(this, "Fav.db", null, 2);
        final List<Map<String, Object>> list = getData();

        title.setText("我的收藏" + "(" + list.size() + ")");
        title.setTextSize(20);

        if (list.size() == 0) {

        } else {
            btn_shoucang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(Fav_One_ac.this,"确认要取消收藏吗？",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(Fav_One_ac.this, Fav_Two_ac.class);
                    intent.putExtra("pos", 0);
                    startActivity(intent);
                }
            });
            favAdapter = new AdpFav_Adp(this, list);
            listView.setAdapter(favAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    Intent intent = new Intent(Fav_One_ac.this, Fav_Two_ac.class);
                    intent.putExtra("pos", position);
                    startActivity(intent);

                }
            });
        }

    }

    /**
     * 按照来源的ID取出问题
     */
    public List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();

        SQLiteDatabase dbwritet = dbFavoritesCreate.getWritableDatabase();

        DbFavoritesService dbFavoritesService = new DbFavoritesService();
        listFavF = dbFavoritesService.GetFavFromAndId();//此listFav是已经收藏的来源和ID
        DbShuZiTiService dbShuZiTiService = new DbShuZiTiService();

        int[] f = new int[listFavF.size()];
        int[] id = new int[listFavF.size()];
        for (int i = 0; i < listFavF.size(); i++) {
            f[i] = listFavF.get(i).DingYiFrom;
            id[i] = listFavF.get(i).DingyiFId;
        }

        //通过f（来源）和ID（地址），从GetFav中得到具体的问题。
        //listFavGet是已经取出来的问题
        listFavGet = dbShuZiTiService.GetQueByFromAndId(f, id);

        String time;
        String que;
        for (int i = 0; i < listFavF.size(); i++) {
            time = listFavF.get(i).DingYiFTime.toString();
            que = listFavGet.get(i).Dingyique;

            map = new HashMap<String, Object>();
            map.put("title", que);
            map.put("year", time);
            list.add(map);
        }

        return list;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //处理系统返回键的逻辑
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            //do something...
            finish();
            overridePendingTransition(R.anim.in_left, R.anim.out_right);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
