package com.example.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.Theme.MyThemeSet;
import com.example.Adapter.Adp_Hestory_Adp;
import com.example.Db.DbFavoritesBianLiang;
import com.example.Db.DbFavoritesCreate;
import com.example.Db.DbFavoritesService;
import com.example.shujuku.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hestory_One_ac extends AppCompatActivity {

    private Adp_Hestory_Adp adp_hestory_adp;
    private ListView listView;
    public List<DbFavoritesBianLiang> listRcd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyThemeSet.setMyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hestory__one_aclout);

        //标题三按钮
        Button btn_title_back = (Button) findViewById(R.id.title_back);
        TextView tv_title_text = (TextView) findViewById(R.id.title_text);
        Button btn_title_edit = (Button) findViewById(R.id.title_edit1);
        btn_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_left, R.anim.out_right);
            }
        });
        tv_title_text.setText("答题历史");
        btn_title_edit.setBackgroundResource(R.drawable.shanchu);
        final DbFavoritesCreate dbFavoritesCreate = new DbFavoritesCreate(this, "Fav.db", null, 2);

        btn_title_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Hestory_One_ac.this).setTitle("确认要清空答题记录吗？").setMessage("删除后将不可恢复")
                        .setPositiveButton("清空", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final SQLiteDatabase dbwritet = dbFavoritesCreate.getWritableDatabase();
                                dbwritet.delete("recordque", "id>=?", new String[]{"0"});
                                finish();
                            }

                        }).setNegativeButton("不清空", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

            }
        });

        listView = (ListView) this.findViewById(R.id.list_view);
        listView.setDividerHeight(0);

        /**从数据库获得历史记录*/
        final List<Map<String, Object>> list = GetRcd1();
        adp_hestory_adp = new Adp_Hestory_Adp(this, list);
        listView.setAdapter(adp_hestory_adp);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(Hestory_One_ac.this, Hestory_Two_ac.class);

                String fromS = listRcd.get(position).DingYiRcdFrom;
                String idS = listRcd.get(position).DingYiRcdId;
                String[] idfen = idS.split(",");
                String selectAllS = listRcd.get(position).DingYiRcdSelectAll;
                String rightLvS = listRcd.get(position).DingYiRcdRightLV;


                int[] from = new int[fromS.length() / 3];
                int[] idd = new int[fromS.length() / 3];
                int[] selectAll = new int[fromS.length() / 3];

                for (int i = 0; i < (fromS.length() / 3); i++) {
                    //LogMy.d("fffffffffffffffffffrom","f"+s.substring(i*3+1,i*3+2));
                    from[i] = Integer.parseInt(fromS.substring(i * 3 + 1, i * 3 + 2));
                }
                for (int i = 0; i < ((fromS.length() / 3)); i++) {
                    //LogMy.d("fffffffffffffffffffrom","f"+s.substring(i*3+1,i*3+2));
                    if (i == (fromS.length() / 3) - 1) {
                        String fen = idfen[i];
                        idd[i] = Integer.parseInt(idfen[i].substring(1, fen.length() - 1));
                    } else {
                        idd[i] = Integer.parseInt(idfen[i].substring(1));
                    }

                }
                for (int i = 0; i < (fromS.length() / 3); i++) {
                    //LogMy.d("fffffffffffffffffffrom","f"+s.substring(i*3+1,i*3+2));
                    selectAll[i] = Integer.parseInt(selectAllS.substring(i * 3 + 1, i * 3 + 2));
                }

//
//                for(int i=0;i<(fromS.length()/3);i++){
//                    LogMy.d("分割的字符串","is"+idfen[i]);
//                }


                for (int i = 0; i < (fromS.length() / 3); i++) {
                    intent.putExtra("from" + i, from[i]);
                    intent.putExtra("id" + i, idd[i]);
                    intent.putExtra("select" + i, selectAll[i]);
                }

                intent.putExtra("rightlv", rightLvS);
                intent.putExtra("len", from.length);


                startActivity(intent);

            }
        });


    }

    /**
     * 获得历史记录list
     */
    private List<Map<String, Object>> GetRcd1() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        DbFavoritesService dbFavoritesService = new DbFavoritesService();

        listRcd = dbFavoritesService.GetRcd0();

        Map<String, Object> map = new HashMap<String, Object>();

        for (int i = 0; i < listRcd.size(); i++) {

            map = new HashMap<String, Object>();
            map.put("title", "正确率： " + listRcd.get(i).DingYiRcdRightLV);
            map.put("year", listRcd.get(i).DingYiRcdTime);
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

