package com.example.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.LogMy.LogMy;
import com.example.ActivityAds.SysApp;
import com.example.Db.AccountHelper;

import com.example.Db.DbShuZiTiCreate;
import com.example.shujuku.R;

public class Main_ac extends AppCompatActivity {
    private EditText editaccount, editpassword;
    private Button buttonlogin, buttonzhuce;
    private AccountHelper dbHelper;
    private DbShuZiTiCreate dbShuZiTiCreate;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;
    public boolean panduan = new Boolean(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_aclout);
        SysApp.getInstance().addActivity(this);

        editaccount = (EditText) findViewById(R.id.edit_account);
        editpassword = (EditText) findViewById(R.id.edit_password);
        buttonlogin = (Button) findViewById(R.id.button_login);
        buttonzhuce = (Button) findViewById(R.id.button_zhuce);
        //如果选择了记住密码，就将账号密码设置到文本框中
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        rememberPass = (CheckBox) findViewById(R.id.check_box);
        boolean isRemember = pref.getBoolean("remember_pass", true);
        if (isRemember) {
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            editaccount.setText(account);
            editpassword.setText(password);
            rememberPass.setChecked(true);
        }
        //dbShuZiTiCreate=new DbShuZiTiCreate(this,"Question.db",null,1);
        //DatabaseContext dbContext = new DatabaseContext(this);
        //dbHelper = new AccountHelper(dbContext,"Account.db",null,1);
        dbHelper = new AccountHelper(this, "Account.db", null, 1);
        //点击注册按钮时跳转到注册界面并创建数据库
        buttonzhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dbHelper.getWritableDatabase();
                Intent intent1 = new Intent(Main_ac.this, ZhuCe_ac.class);
                startActivity(intent1);
            }

        });

        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account2 = editaccount.getText().toString();
                String password2 = editpassword.getText().toString();
                //获得数据库中已存储信息，与输入信息比较
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.query("Account", null, null, null, null, null, null);

                LogMy.d("daozheli", "cursor.moveToFirst()");
                if (cursor.moveToFirst()) {


                    do {
                        //遍历账户，取出数据
                        String account1 = cursor.getString(cursor.getColumnIndex("account"));
                        String password1 = cursor.getString(cursor.getColumnIndex("password"));
                        if (account1.equals(account2) && password1.equals(password2)) {
                            editor = pref.edit();//当输入正确时再记住密码
                            if (rememberPass.isChecked()) {//检查复选框是否被选中
                                editor.putBoolean("remember_pass", true);
                                editor.putString("account", account2);
                                editor.putString("password", password2);
                            } else {
                                editor.clear();
                            }
                            editor.commit();
                            //账号密码匹配时panduan为TRUE，终止循环；
                            panduan = true;
                            break;
                        } else {
                            panduan = false;

                        }
                    } while (cursor.moveToNext());
                }


                if (panduan) {
                    //dbShuZiTiCreate.getWritableDatabase();
                    Intent intent1 = new Intent(Main_ac.this, Main2_ac.class);
//                   intent1.putExtra("theme",0);
                    intent1.putExtra("whereback", 0);
                    startActivity(intent1);
                } else {
                    Toast.makeText(Main_ac.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                }

                cursor.close();
            }

        });
    }
}
