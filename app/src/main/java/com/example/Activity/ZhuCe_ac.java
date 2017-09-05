package com.example.Activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.LogMy.LogMy;
import com.example.Db.AccountDingyiBianliang;
import com.example.Db.AccountHelper;
import com.example.Db.AccountService;
import com.example.shujuku.R;

import java.util.List;

public class ZhuCe_ac extends AppCompatActivity {
    private EditText acc, pass;
    private AccountHelper dbHelper;
    private int count;
    private boolean w = new Boolean(false);
    private boolean w2 = new Boolean(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhuce_aclout);
        acc = (EditText) findViewById(R.id.edit_account1);
        pass = (EditText) findViewById(R.id.edit_password1);
        //
        dbHelper = new AccountHelper(this, "Account.db", null, 1);
        Button button1 = (Button) findViewById(R.id.button_zhuce);
        FirstAdd();


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点击注册时开始进行数据核对

                Check();
                LogMy.d("daozheli::", "okokok");


            }
        });
    }

    public void Check() {
        AccountService accService = new AccountService();
        String account2 = acc.getText().toString();
        String password2 = pass.getText().toString();
        final List<AccountDingyiBianliang> list = accService.getAccount();
        count = list.size();
        //遍历数据库，检查账户名称是否重复

        LogMy.d("daozheli::", "gdshredgbgfn" + count);
        for (int i = 0; i < count; i++) {
            LogMy.d("daozheli::", "dd" + count);
            AccountDingyiBianliang quchu = list.get(i);
            String account1 = quchu.account_dingyi;
            //String  password1=quchu.password_dingyi;
            if (account1.equals(account2)) {
                w = false;
                Toast.makeText(ZhuCe_ac.this, "账户名称已存在", Toast.LENGTH_SHORT).show();
                break;
            } else {
                w = true;
            }

        }
        if (w) {
            String regex = "^\\d{6}";
            boolean e = password2.matches(regex);
            if (!e) {
                Toast.makeText(ZhuCe_ac.this, "密码必须是六位数字", Toast.LENGTH_SHORT).show();
                w2 = false;

            } else {
                w2 = true;
            }
            //检查密码是否是六位数字
        }


        if (w2) {//数据满足要求时向数据库添加信息
            SQLiteDatabase dbwrite = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            //添加数据
            values.put("account", account2);
            values.put("password", password2);
            dbwrite.insert("Account", null, values);
            //db.delete("Account","password>?",new String[]{"123456"});
            Toast.makeText(ZhuCe_ac.this, "注册成功", Toast.LENGTH_SHORT).show();
            //销毁当前活动：
            finish();
        }
    }

    public void FirstAdd() {
        //讲道理不该有这个方法，但为了在第一次验证账户名是否重复时数据库表长度不为0，无奈之举
        AccountService accService = new AccountService();
        final List<AccountDingyiBianliang> list = accService.getAccount();
        count = list.size();
        if (count == 0) {
            SQLiteDatabase dbwrite = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            //添加数据
            values.put("account", "qxx");
            values.put("password", "123456");
            dbwrite.insert("Account", null, values);
            //db.delete("Account","password>?",new String[]{"123456"});
        }

    }

}
