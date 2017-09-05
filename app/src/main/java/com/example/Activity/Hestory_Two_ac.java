package com.example.Activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.LogMy.LogMy;
import com.example.Theme.MyThemeSet;
import com.example.Db.DbShuZiTiBianLiang;
import com.example.Db.DbShuZiTiService;
import com.example.shujuku.R;

import java.util.ArrayList;
import java.util.List;

public class Hestory_Two_ac extends AppCompatActivity {

    ViewPager pager = null;
    PagerTabStrip tabStrip = null;
    ArrayList<View> viewContainter = new ArrayList<View>();
    ArrayList<String> titleContainer = new ArrayList<String>();

    //问题
    private TextView tv_queH;
    //设置选项
    RadioButton[] mRadioButtonH = new RadioButton[4];
    //解析
    private TextView tv_jiexiH;
    private RadioGroup mRadioGroupH;

    private List<DbShuZiTiBianLiang> listRcd;

    private Button btn_title_back;
    private TextView tv_title_text;
    private Button btn_title_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyThemeSet.setMyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hestory__two_aclout);

        //标题栏三按钮
        btn_title_back = (Button) findViewById(R.id.title_back);
        tv_title_text = (TextView) findViewById(R.id.title_text);
        btn_title_edit = (Button) findViewById(R.id.title_edit1);
        btn_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_left, R.anim.out_right);
            }
        });
        tv_title_text.setText("历史记录");

        //获取参数传值：来源，ID，选择
        Intent intent = getIntent();

        final String rlv = intent.getStringExtra("rightlv");
        final int len = intent.getIntExtra("len", 0);
        int[] from = new int[len];
        int[] id = new int[len];
        final int[] selectAll = new int[len];

        for (int i = 0; i < len; i++) {
            from[i] = intent.getIntExtra("from" + i, 0);
            id[i] = intent.getIntExtra("id" + i, 0);
            selectAll[i] = intent.getIntExtra("select" + i, 0);
        }

        //设置下划线
        pager = (ViewPager) this.findViewById(R.id.view_hsty_pager);
        tabStrip = (PagerTabStrip) this.findViewById(R.id.tabstrip_hsty);
        //取消tab下面的长横线
        tabStrip.setDrawFullUnderline(false);
        //设置tab的背景色
        tabStrip.setBackgroundColor(this.getResources().getColor(R.color.blue));
        //设置当前tab页签的下划线颜色
        tabStrip.setTabIndicatorColor(this.getResources().getColor(R.color.red));
        tabStrip.setTextSpacing(500);


        DbShuZiTiService dbShuZiTiService = new DbShuZiTiService();
        listRcd = dbShuZiTiService.GetQueByFromAndId(from, id);


        final View[] views = new View[len];
        for (int i = 0; i < len; i++) {
            views[i] = LayoutInflater.from(this).inflate(R.layout.hestory__two_view, null);

            tv_queH = (TextView) views[i].findViewById(R.id.tv_que_hesy2);
            mRadioButtonH[0] = (RadioButton) views[i].findViewById(R.id.RadioA_hsty2);
            mRadioButtonH[1] = (RadioButton) views[i].findViewById(R.id.RadioB_hsty2);
            mRadioButtonH[2] = (RadioButton) views[i].findViewById(R.id.RadioC_hsty2);
            mRadioButtonH[3] = (RadioButton) views[i].findViewById(R.id.RadioD_hsty2);

            tv_jiexiH = (TextView) views[i].findViewById(R.id.tv_explain_hsty2);
            mRadioGroupH = (RadioGroup) views[i].findViewById(R.id.mRadioGroup_hsty2);

            tv_queH.setText(listRcd.get(0).Dingyique);

            mRadioButtonH[0].setText(listRcd.get(0).DingyiA);
            mRadioButtonH[1].setText(listRcd.get(0).DingyiB);
            mRadioButtonH[2].setText(listRcd.get(0).DingyiC);
            mRadioButtonH[3].setText(listRcd.get(0).DingyiD);

            tv_jiexiH.setText(listRcd.get(0).DingyiExplain);
            tv_jiexiH.setVisibility(View.VISIBLE);


            //viewpager开始添加view
            viewContainter.add(views[i]);
            titleContainer.add("000000");
        }


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

        pager.setCurrentItem(0);

        tv_queH = (TextView) views[0].findViewById(R.id.tv_que_hesy2);
        mRadioButtonH[0] = (RadioButton) views[0].findViewById(R.id.RadioA_hsty2);
        mRadioButtonH[1] = (RadioButton) views[0].findViewById(R.id.RadioB_hsty2);
        mRadioButtonH[2] = (RadioButton) views[0].findViewById(R.id.RadioC_hsty2);
        mRadioButtonH[3] = (RadioButton) views[0].findViewById(R.id.RadioD_hsty2);

        tv_jiexiH = (TextView) views[0].findViewById(R.id.tv_explain_hsty2);
        mRadioGroupH = (RadioGroup) views[0].findViewById(R.id.mRadioGroup_hsty2);

        tv_title_text.setText(rlv + "    " + 1 + "/" + len);
        setRorF(listRcd.get(0).DingyiRightanswer, selectAll[0]);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {

                LogMy.d("ggggggggggg", "mgfghdnh");

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageSelected(final int arg0) {

                //每次滑动后的显示，arg0是滑动后当前的页面编号。

                tv_queH = (TextView) views[arg0].findViewById(R.id.tv_que_hesy2);
                mRadioButtonH[0] = (RadioButton) views[arg0].findViewById(R.id.RadioA_hsty2);
                mRadioButtonH[1] = (RadioButton) views[arg0].findViewById(R.id.RadioB_hsty2);
                mRadioButtonH[2] = (RadioButton) views[arg0].findViewById(R.id.RadioC_hsty2);
                mRadioButtonH[3] = (RadioButton) views[arg0].findViewById(R.id.RadioD_hsty2);


                tv_jiexiH = (TextView) views[arg0].findViewById(R.id.tv_explain_hsty2);
                mRadioGroupH = (RadioGroup) views[arg0].findViewById(R.id.mRadioGroup_hsty2);

                tv_queH.setText(listRcd.get(arg0).Dingyique);

                mRadioButtonH[0].setText(listRcd.get(arg0).DingyiA);
                mRadioButtonH[1].setText(listRcd.get(arg0).DingyiB);
                mRadioButtonH[2].setText(listRcd.get(arg0).DingyiC);
                mRadioButtonH[3].setText(listRcd.get(arg0).DingyiD);

                tv_jiexiH.setText(listRcd.get(arg0).DingyiExplain);
                tv_jiexiH.setVisibility(View.VISIBLE);

                tv_title_text.setText(rlv + "    " + (arg0 + 1) + "/" + len);
                setRorF(listRcd.get(arg0).DingyiRightanswer, selectAll[arg0]);
            }
        });

    }


    /**
     * 显示对与错
     */
    private void setRorF(int r, int s) {
        if (r == s) {
            btn_title_edit.setBackgroundResource(R.drawable.xiaolian);
            mRadioButtonH[r].setChecked(true);
            mRadioButtonH[r].setTextColor(getResources().getColor(R.color.green));
        } else {
            btn_title_edit.setBackgroundResource(R.drawable.kulian);
            mRadioButtonH[r].setChecked(true);
            mRadioButtonH[s].setTextColor(getResources().getColor(R.color.red));
            mRadioButtonH[r].setTextColor(getResources().getColor(R.color.green));
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
