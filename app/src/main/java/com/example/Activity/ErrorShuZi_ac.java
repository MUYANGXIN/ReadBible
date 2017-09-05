package com.example.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.LogMy.LogMy;
import com.example.ActivityAds.ActivityCollector;
import com.example.Db.DbShuZiTiBianLiang;
import com.example.Db.DbShuZiTiService;
import com.example.shujuku.R;


import java.util.List;


public class ErrorShuZi_ac extends Main2_ShuZiTi_dianji_ac {
    //错题解释框
    private boolean wrongView;
    //问题
    private TextView tv_que;
    //显示进度
    private TextView tv_muqianti;
    //设置选项
    RadioButton[] mRadioButton = new RadioButton[4];
    //解析
    private TextView tv_jiexi;

    private RadioGroup mRadioGroup;
    //下一题
    private Button button_nextQ;
    private int corrent;
    private int count;
    private int correntID;

    //收藏按钮
    private Button edit2;

    //from数组，存放问题来源表单
    private int[] f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.errorshuzi_aclout);


        edit2 = (Button) findViewById(R.id.title_edit2);
        edit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用父类添加收藏方法
                AddFav(f[corrent], correntID);
                setBackGd2();
            }
        });

        //通过循环得到上一个活动的参数传值,l是错题数目，p是卡片编号
        Intent intent = getIntent();
        int l = intent.getIntExtra("length", 0);
        final int p = intent.getIntExtra("pos", 0);
        LogMy.d("aaaaa", "::" + l);

        //错题from
        f = new int[l];
        for (int i = 0; i < l; i++) {
            f[i] = intent.getIntExtra("from" + i, 0);
        }
        //错题ID
        int[] a = new int[l];
        for (int i = 0; i < l; i++) {
            a[i] = intent.getIntExtra("id" + i, 0);
        }
        //原始选择
        final int[] s = new int[l];
        for (int i = 0; i < l; i++) {
            s[i] = intent.getIntExtra("s" + i, 0);
        }

        DbShuZiTiService dbService = new DbShuZiTiService();
        final List<DbShuZiTiBianLiang> ListR = dbService.GetQueByFromAndId(f, a);

        initView1();


        count = ListR.size();


        wrongView = true;
        //设置显示第一道错题
        DbShuZiTiBianLiang q = ListR.get(corrent);
        correntID = q.DingyiId;

        tv_que.setText(q.Dingyique);

        mRadioButton[0].setText(q.DingyiA);
        mRadioButton[1].setText(q.DingyiB);
        mRadioButton[2].setText(q.DingyiC);
        mRadioButton[3].setText(q.DingyiD);

        tv_jiexi.setText(q.DingyiExplain);

        setBackGd2();
        tv_muqianti.setText("1/" + l);
        //显示结果
        tv_jiexi.setVisibility(View.VISIBLE);
        mRadioButton[s[0]].setChecked(true);

        Button button_up = (Button) findViewById(R.id.btn_up);
        button_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (corrent > 0) {

                    corrent = corrent - 1;

                    DbShuZiTiBianLiang q = ListR.get(corrent);
                    correntID = q.DingyiId;
                    tv_que.setText(q.Dingyique);


                    mRadioButton[0].setText(q.DingyiA);
                    mRadioButton[1].setText(q.DingyiB);
                    mRadioButton[2].setText(q.DingyiC);
                    mRadioButton[3].setText(q.DingyiD);

                    tv_jiexi.setText(q.DingyiExplain);

                    setBackGd2();
                    tv_muqianti.setText((corrent + 1) + "/" + count);

                    mRadioGroup.clearCheck();
                    mRadioButton[s[corrent]].setChecked(true);
                }
            }
        });

        button_nextQ = (Button) findViewById(R.id.btn_next);
        button_nextQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (corrent < count - 1) {

                    corrent = corrent + 1;

                    DbShuZiTiBianLiang q = ListR.get(corrent);
                    correntID = q.DingyiId;
                    tv_que.setText(q.Dingyique);


                    mRadioButton[0].setText(q.DingyiA);
                    mRadioButton[1].setText(q.DingyiB);
                    mRadioButton[2].setText(q.DingyiC);
                    mRadioButton[3].setText(q.DingyiD);

                    tv_jiexi.setText(q.DingyiExplain);

                    setBackGd2();
                    tv_muqianti.setText((corrent + 1) + "/" + count);

                    mRadioGroup.clearCheck();
                    mRadioButton[s[corrent]].setChecked(true);


                } else {

                    new AlertDialog.Builder(ErrorShuZi_ac.this).setTitle("你好棒！").setMessage("已经完成错题回顾，要不要再做几道试试呢？")
                            .setPositiveButton("再试试", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intentAgain = new Intent(ErrorShuZi_ac.this, Main2_ShuZiTi_dianji_ac.class);
                                    intentAgain.putExtra("position", p);
                                    startActivity(intentAgain);
                                }
                            }).setNegativeButton("不了，休息一会", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCollector.finishAll();

                        }
                    }).show()
                            .setCancelable(true);
                }
            }
        });


    }

    /**
     * 初始化视图
     */
    private void initView1() {
        //初始化view

        wrongView = true;
        tv_muqianti = (TextView) findViewById(R.id.tv_muqian);

        tv_que = (TextView) findViewById(R.id.tv_que2);

        mRadioButton[0] = (RadioButton) findViewById(R.id.RadioA2);
        mRadioButton[1] = (RadioButton) findViewById(R.id.RadioB2);
        mRadioButton[2] = (RadioButton) findViewById(R.id.RadioC2);
        mRadioButton[3] = (RadioButton) findViewById(R.id.RadioD2);


        tv_jiexi = (TextView) findViewById(R.id.tv_explain2);

        mRadioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);
    }

    /**
     * 改变按钮背景
     */
    private void setBackGd2() {
        if (isFav(f[corrent], correntID)) {

            edit2.setBackgroundResource(R.drawable.favs);

        } else {
            edit2.setBackgroundResource(R.drawable.favk);
        }
    }

    /**
     * 返回键逻辑
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //处理系统返回键的逻辑
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            //do something...
            ActivityCollector.finishAll();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
