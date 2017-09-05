package com.example.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.LogMy.LogMy;
import com.example.Theme.MyThemeSet;
import com.example.Db.DbFavoritesCreate;
import com.example.shujuku.R;

import java.util.ArrayList;


public class Fav_Two_ac extends Fav_One_ac {


    //问题
    private TextView tv_queF;
    //设置选项
    RadioButton[] mRadioButtonF = new RadioButton[4];
    //解析
    private TextView tv_jiexiF;

    private RadioGroup mRadioGroupF;
    //记录收藏问题时候的时间，用来删除收藏
    String tm;

    private Button btn_edit1;


    ViewPager pager = null;
    PagerTabStrip tabStrip = null;
    ArrayList<View> viewContainter = new ArrayList<View>();
    ArrayList<String> titleContainer = new ArrayList<String>();

    private DbFavoritesCreate dbFavoritesCreate;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyThemeSet.setMyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fav_two_aclout);

        dbFavoritesCreate = new DbFavoritesCreate(this, "Fav.db", null, 2);
        final SQLiteDatabase dbwritet = dbFavoritesCreate.getWritableDatabase();

        Intent intent = getIntent();
        final int p = intent.getIntExtra("pos", 0);

        final TextView title = (TextView) findViewById(R.id.title_text);
        title.setText("收藏夹" + "" + (p + 1) + "/" + listFavGet.size());
        title.setTextSize(20);
        Button btn_back = (Button) findViewById(R.id.title_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_left, R.anim.out_right);
            }
        });
        btn_edit1 = (Button) findViewById(R.id.title_edit1);
        //btn_edit1.setText("已收藏");
        btn_edit1.setBackgroundResource(R.drawable.favs);
        btn_edit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Fav_Two_ac.this).setTitle("确认要移除这条收藏吗？")
                        .setPositiveButton("移除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dbwritet.delete("favque", "FavTime=?", new String[]{tm});
                                Toast.makeText(Fav_Two_ac.this, "下次打开收藏夹就不会出现这道题啦！", Toast.LENGTH_SHORT).show();
                            }

                        }).setNegativeButton("不移除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });

        pager = (ViewPager) this.findViewById(R.id.view_pager);
        tabStrip = (PagerTabStrip) this.findViewById(R.id.tabstrip);
        //取消tab下面的长横线
        tabStrip.setDrawFullUnderline(false);
        //设置tab的背景色
        tabStrip.setBackgroundColor(this.getResources().getColor(R.color.blue));
        //设置当前tab页签的下划线颜色
        tabStrip.setTabIndicatorColor(this.getResources().getColor(R.color.red));
        tabStrip.setTextSpacing(500);


        final View[] views = new View[listFavGet.size()];
        for (int i = 0; i < listFavGet.size(); i++) {
            views[i] = LayoutInflater.from(this).inflate(R.layout.fav_two_view, null);

            tv_queF = (TextView) views[i].findViewById(R.id.tv_queF);
            mRadioButtonF[0] = (RadioButton) views[i].findViewById(R.id.RadioAF);
            mRadioButtonF[1] = (RadioButton) views[i].findViewById(R.id.RadioBF);
            mRadioButtonF[2] = (RadioButton) views[i].findViewById(R.id.RadioCF);
            mRadioButtonF[3] = (RadioButton) views[i].findViewById(R.id.RadioDF);

            tv_jiexiF = (TextView) views[i].findViewById(R.id.tv_explainF);
            mRadioGroupF = (RadioGroup) views[i].findViewById(R.id.mRadioGroupF);

            tv_queF.setText(listFavGet.get(p).Dingyique);

            mRadioButtonF[0].setText(listFavGet.get(p).DingyiA);
            mRadioButtonF[1].setText(listFavGet.get(p).DingyiB);
            mRadioButtonF[2].setText(listFavGet.get(p).DingyiC);
            mRadioButtonF[3].setText(listFavGet.get(p).DingyiD);

            tv_jiexiF.setText(listFavGet.get(p).DingyiExplain);


            //viewpager开始添加view
            viewContainter.add(views[i]);
            titleContainer.add("000000");
        }

//        //页签项
//        titleContainer.add("网易新闻");
//        titleContainer.add("网易体育");
//        titleContainer.add("网易财经");
//        titleContainer.add("网易女人");

        pager.setAdapter(new PagerAdapter() {

            //viewpager中的组件数量
            @Override
            public int getCount() {
                return viewContainter.size();
            }

            //滑动切换的时候销毁当前的组件
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {

//                mRadioGroupF.clearCheck();
//                tv_jiexiF.setVisibility(View.GONE);
                ((ViewPager) container).removeView(viewContainter.get(position));
            }

            //每次滑动的时候生成的组件
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager) container).addView(viewContainter.get(position));
                return viewContainter.get(position);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public CharSequence getPageTitle(int position) {

                //return null;

                return titleContainer.get(position);
            }
        });

        pager.setCurrentItem(p);
        LogMy.d("z这道题的来源和id", "gg" + listFavF.get(p).DingYiFrom + listFavF.get(p).DingyiFId);
        tm = listFavF.get(p).DingYiFTime;

        //下面这段代码仅仅为了实现第一个页面的点击选项时可以自动显示出来解释，感觉有点不值得，但viewpager的点击事件还不会，只能这样了
        tv_jiexiF = (TextView) views[p].findViewById(R.id.tv_explainF);
        mRadioButtonF[0] = (RadioButton) views[p].findViewById(R.id.RadioAF);
        mRadioButtonF[1] = (RadioButton) views[p].findViewById(R.id.RadioBF);
        mRadioButtonF[2] = (RadioButton) views[p].findViewById(R.id.RadioCF);
        mRadioButtonF[3] = (RadioButton) views[p].findViewById(R.id.RadioDF);


        Button btn_a = (Button) views[p].findViewById(R.id.RadioAF);
        btn_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickResult(0, p);
            }
        });
        Button btn_b = (Button) views[p].findViewById(R.id.RadioBF);
        btn_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickResult(1, p);
            }
        });
        Button btn_c = (Button) views[p].findViewById(R.id.RadioCF);
        btn_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickResult(2, p);
            }
        });
        Button btn_d = (Button) views[p].findViewById(R.id.RadioDF);
        btn_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickResult(3, p);
            }
        });


        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {
                if (arg0 == 0) {
                    LogMy.d("arg0", "=000000");
                } else if (arg0 == 1) {
                    LogMy.d("arg0", "=111111");
                } else {
                    LogMy.d("arg0", "=222222");
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {


            }

            @Override
            public void onPageSelected(final int arg0) {

                //每次滑动后的显示，arg0是滑动后当前的页面编号。

                tv_queF = (TextView) views[arg0].findViewById(R.id.tv_queF);
                mRadioButtonF[0] = (RadioButton) views[arg0].findViewById(R.id.RadioAF);
                mRadioButtonF[1] = (RadioButton) views[arg0].findViewById(R.id.RadioBF);
                mRadioButtonF[2] = (RadioButton) views[arg0].findViewById(R.id.RadioCF);
                mRadioButtonF[3] = (RadioButton) views[arg0].findViewById(R.id.RadioDF);


                tv_jiexiF = (TextView) views[arg0].findViewById(R.id.tv_explainF);
                mRadioGroupF = (RadioGroup) views[arg0].findViewById(R.id.mRadioGroupF);

                tv_queF.setText(listFavGet.get(arg0).Dingyique);

                mRadioButtonF[0].setText(listFavGet.get(arg0).DingyiA);
                mRadioButtonF[1].setText(listFavGet.get(arg0).DingyiB);
                mRadioButtonF[2].setText(listFavGet.get(arg0).DingyiC);
                mRadioButtonF[3].setText(listFavGet.get(arg0).DingyiD);

                tv_jiexiF.setText(listFavGet.get(arg0).DingyiExplain);

                LogMy.d("z这道题的来源和id", "gg" + listFavF.get(arg0).DingYiFrom + listFavF.get(arg0).DingyiFId);
                tm = listFavF.get(arg0).DingYiFTime;

                title.setText("收藏夹" + "  " + (arg0 + 1) + "/" + listFavGet.size());

                //点击时自动显示答案
                mRadioButtonF[0].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClickResult(0, arg0);
                    }
                });
                mRadioButtonF[1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClickResult(1, arg0);
                    }
                });
                mRadioButtonF[2].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClickResult(2, arg0);
                    }
                });
                mRadioButtonF[3].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ClickResult(3, arg0);
                    }
                });

            }
        });


    }

    //实现点击时自动改变选项背景颜色，并显示解释.
    private void ClickResult(int sel, final int po) {

        tv_jiexiF.setVisibility(View.VISIBLE);
        //答案选中

        if (sel == listFavGet.get(po).DingyiRightanswer) {
            mRadioButtonF[sel].setTextColor(getResources().getColor(R.color.green));

        } else {
            mRadioButtonF[sel].setTextColor(getResources().getColor(R.color.red));
        }


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
