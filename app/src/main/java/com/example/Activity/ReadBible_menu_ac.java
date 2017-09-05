package com.example.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.LogMy.LogMy;
import com.example.Screen.DepthPageTransformer;
import com.example.Theme.MyThemeSet;
import com.example.ActivityAds.MainActivity;
import com.example.Adapter.Adp_ReadBibleMenu_Adp;
import com.example.Adapter.Adp_ReadBibleMenu_BianLiang;
import com.example.Adapter.ItemClickSupport;
import com.example.shujuku.R;

import java.util.ArrayList;
import java.util.List;


public class ReadBible_menu_ac extends MainActivity {

    private RecyclerView recyclerViewJiuYue;//引入卡片布局,旧约
    private RecyclerView recyclerViewXinYue;//引入卡片布局，新约
    private RecyclerView recyclerViewZhang;

    //关于目录的一些变量

    private String ShuJuan = "创世记";
    private int nemberZhang = 50;//用来记录第二个view的显示内容

    Adp_ReadBibleMenu_BianLiang[] biblemenu;
    Adp_ReadBibleMenu_BianLiang[] biblemenuXinYue;//用来确定第一个view的显示内容

    //private DrawerLayout mDrawerLayout;
    private List<Adp_ReadBibleMenu_BianLiang> tupianList = new ArrayList<>();
    private Adp_ReadBibleMenu_Adp adapter;
    private List<Adp_ReadBibleMenu_BianLiang> tupianListXinYue = new ArrayList<>();
    private Adp_ReadBibleMenu_Adp adapterXinYue;//第一个view的适配器
    private List<Adp_ReadBibleMenu_BianLiang> tupianListZhang = new ArrayList<>();
    private Adp_ReadBibleMenu_Adp adapterZhang;//第二个view的适配器

    ViewPager pager = null;
    PagerTabStrip tabStrip = null;
    ArrayList<View> viewContainter = new ArrayList<View>();
    ArrayList<String> titleContainer = new ArrayList<String>();

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyThemeSet.setMyTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readbible_menu_ac);

        Intent intent = getIntent();
        String menuJuan;
        int menuNember;
        menuNember = intent.getIntExtra("menu", 0);
        menuJuan = intent.getStringExtra("juan");


        /**标题栏*/
        Button button = (Button) findViewById(R.id.title_back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_left, R.anim.out_right);
            }
        });
        TextView textView = (TextView) findViewById(R.id.title_text);
        textView.setText("圣经目录");

        TextView textView1 = (TextView) findViewById(R.id.title_edit1);
        textView1.setBackgroundColor(getResources().getColor(R.color.white));

        /**书卷、章两个view初始化*/
        pager = (ViewPager) this.findViewById(R.id.view_pager_biblemenu);
        tabStrip = (PagerTabStrip) this.findViewById(R.id.tabstrip_biblemenu);
        //取消tab下面的长横线
        tabStrip.setDrawFullUnderline(false);
        //设置tab的背景色
        tabStrip.setBackgroundColor(this.getResources().getColor(R.color.gray));
        //设置当前tab页签的下划线颜色
        tabStrip.setTabIndicatorColor(this.getResources().getColor(R.color.red));
        tabStrip.setTextSpacing(25);

        final View[] views = new View[2];
        views[0] = LayoutInflater.from(this).inflate(R.layout.adp_readbiblemenu_recleview, null);
        views[1] = LayoutInflater.from(this).inflate(R.layout.adp_readbiblemenu_recleview2, null);


        for (int i = 0; i < 2; i++) {

            //viewpager开始添加view
            viewContainter.add(views[i]);
            //titleContainer.add("000000");
        }
        titleContainer.add("书卷");
        titleContainer.add("章");

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

        /**第一次第一个view显示*/


        recyclerViewJiuYue = (RecyclerView) views[0].findViewById(R.id.kapian_view_biblemenu_jiuyue);//引入卡片布局
        recyclerViewXinYue = (RecyclerView) views[0].findViewById(R.id.kapian_view_biblemenu_xinyue);

        GridLayoutManager layoutManager = new GridLayoutManager(ReadBible_menu_ac.this, 4);

        recyclerViewJiuYue.setLayoutManager(layoutManager);

        GridLayoutManager layoutManager2 = new GridLayoutManager(ReadBible_menu_ac.this, 4);
        recyclerViewXinYue.setLayoutManager(layoutManager2);

        AddShuJuan();
        AddXinYue();
        tupianList.clear();
        for (int i = 0; i < biblemenu.length; i++) {
            //将图片添加到图片数组中
            tupianList.add(biblemenu[i]);
        }

        tupianListXinYue.clear();
        for (int i = 0; i < biblemenuXinYue.length; i++) {
            tupianListXinYue.add(biblemenuXinYue[i]);
        }

        adapter = new Adp_ReadBibleMenu_Adp(tupianList);
        adapterXinYue = new Adp_ReadBibleMenu_Adp(tupianListXinYue);
        recyclerViewJiuYue.setAdapter(adapter);
        recyclerViewXinYue.setAdapter(adapterXinYue);

        /**第二个view初始化*/
        recyclerViewZhang = (RecyclerView) views[1].findViewById(R.id.kapian_view_biblemenu_zhang);

        GridLayoutManager layoutManager3 = new GridLayoutManager(ReadBible_menu_ac.this, 4);

        recyclerViewZhang.setLayoutManager(layoutManager3);

        AddZhang(ShuJuan, nemberZhang);
        tupianListZhang.clear();
        for (int i = 0; i < biblemenu.length; i++) {
            //将图片添加到图片数组中

            tupianListZhang.add(biblemenu[i]);
        }


        adapterZhang = new Adp_ReadBibleMenu_Adp(tupianListZhang);
        recyclerViewZhang.setAdapter(adapterZhang);


        //旧约点击事件
        ItemClickSupport.addTo(recyclerViewJiuYue).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                ChangeGive(position);

//                recyclerViewZhang=(RecyclerView)views[1].findViewById(R.id.kapian_view_biblemenu_zhang);
//
//                GridLayoutManager layoutManager=new GridLayoutManager(ReadBible_menu_ac.this,4);
//
//                recyclerViewZhang.setLayoutManager(layoutManager);

                AddZhang(ShuJuan, nemberZhang);
                tupianListZhang.clear();
                for (int i = 0; i < biblemenu.length; i++) {
                    //将图片添加到图片数组中

                    tupianListZhang.add(biblemenu[i]);
                }
                adapterZhang = new Adp_ReadBibleMenu_Adp(tupianListZhang);
                recyclerViewZhang.setAdapter(adapterZhang);


                pager.setCurrentItem(1);
            }
        });
        //新约点击事件
        ItemClickSupport.addTo(recyclerViewXinYue).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                ChangeGiveXinYue(position);

                recyclerViewZhang = (RecyclerView) views[1].findViewById(R.id.kapian_view_biblemenu_zhang);

                GridLayoutManager layoutManager = new GridLayoutManager(ReadBible_menu_ac.this, 4);

                recyclerViewZhang.setLayoutManager(layoutManager);

                AddZhang(ShuJuan, nemberZhang);
                tupianListZhang.clear();
                for (int i = 0; i < biblemenu.length; i++) {
                    //将图片添加到图片数组中

                    tupianListZhang.add(biblemenu[i]);
                }

                adapterZhang = new Adp_ReadBibleMenu_Adp(tupianListZhang);
                recyclerViewZhang.setAdapter(adapterZhang);

                pager.setCurrentItem(1);
            }
        });

        ItemClickSupport.addTo(recyclerViewZhang).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                Intent intent = new Intent(ReadBible_menu_ac.this, ReadBible_Main_ac.class);
                intent.putExtra("Juan", ShuJuan);
                intent.putExtra("Zhang", position + 1);
                startActivity(intent);
            }
        });


        if (menuNember == 0) {
            pager.setCurrentItem(menuNember);
        } else {

            ShuJuan = menuJuan;
            ChangeNemBerZhang(menuJuan);


            InitView2();
            pager.setCurrentItem(menuNember);

        }

        pager.setPageTransformer(true, new DepthPageTransformer());
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {

                LogMy.d("ggggggggggg", "mgfghdnh");

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                LogMy.d("ggggggggggg", "mgfghdnh");
//
//                if(arg0==0){
//                    GridLayoutManager layoutManager=new GridLayoutManager(ReadBible_menu_ac.this,4);
//                    recyclerView.setLayoutManager(layoutManager);
//                    Adp_ReadBibleMenu_BianLiang []biblemenu={
//                            new Adp_ReadBibleMenu_BianLiang("创","创世记",R.drawable.bible),
//                            new Adp_ReadBibleMenu_BianLiang("创","创世记",R.drawable.bible),
//                            new Adp_ReadBibleMenu_BianLiang("创","创世记",R.drawable.bible),
//                            new Adp_ReadBibleMenu_BianLiang("创","创世记",R.drawable.bible),
//                            new Adp_ReadBibleMenu_BianLiang("创","创世记",R.drawable.bible),
//                            new Adp_ReadBibleMenu_BianLiang("创","创世记",R.drawable.bible),
//                            new Adp_ReadBibleMenu_BianLiang("创","创世记",R.drawable.bible),
//                            new Adp_ReadBibleMenu_BianLiang("创世记","创世记",R.drawable.bible),
//                            new Adp_ReadBibleMenu_BianLiang("创世记","创世记",R.drawable.bible),
//                            new Adp_ReadBibleMenu_BianLiang("创世记","创世记",R.drawable.bible),
//                            new Adp_ReadBibleMenu_BianLiang("创世记","创世记",R.drawable.bible)
//                    };
//                    tupianList.clear();
//                    for(int i=0;i<biblemenu.length;i++){
//                        //将图片添加到图片数组中
//
//                        tupianList.add(biblemenu[i]);
//                    }
//
//                    adapter=new Adp_ReadBibleMenu_Adp(tupianList);
//                    recyclerView.setAdapter(adapter);
//
//
//                }else{
//                    RecyclerView recyclerView=(RecyclerView)findViewById(R.id.kapian_view_biblemenu);//引入卡片布局
//                    GridLayoutManager layoutManager=new GridLayoutManager(ReadBible_menu_ac.this,4);
//                    recyclerView.setLayoutManager(layoutManager);
//                    Adp_ReadBibleMenu_BianLiang []biblemenu2={
//                            new Adp_ReadBibleMenu_BianLiang("创世记","创世记",R.drawable.bible),
//                            new Adp_ReadBibleMenu_BianLiang("创世记","创世记",R.drawable.bible),
//                            new Adp_ReadBibleMenu_BianLiang("创世记","创世记",R.drawable.bible),
//                            new Adp_ReadBibleMenu_BianLiang("创世记","创世记",R.drawable.bible),
//                            new Adp_ReadBibleMenu_BianLiang("创世记","创世记",R.drawable.bible),
//                            new Adp_ReadBibleMenu_BianLiang("创世记","创世记",R.drawable.bible),
//                            new Adp_ReadBibleMenu_BianLiang("创世记","创世记",R.drawable.bible),
//                            new Adp_ReadBibleMenu_BianLiang("创世记","创世记",R.drawable.bible),
//                            new Adp_ReadBibleMenu_BianLiang("创世记","创世记",R.drawable.bible),
//                            new Adp_ReadBibleMenu_BianLiang("创世记","创世记",R.drawable.bible),
//                            new Adp_ReadBibleMenu_BianLiang("创世记","创世记",R.drawable.bible)
//                    };
//                    tupianList.clear();
//                    for(int i=0;i<biblemenu2.length;i++){
//                        //将图片添加到图片数组中
//
//                        tupianList.add(biblemenu2[i]);
//                    }
//                    LogMy.d("fu","hgi"+arg0);
//
//                    adapter=new Adp_ReadBibleMenu_Adp(tupianList);
//                    recyclerView.setAdapter(adapter);
//                }

            }

            @Override
            public void onPageSelected(final int arg0) {

                //每次滑动后的显示，arg0是滑动后当前的页面编号。


                //mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout_biblemenu);


                if (arg0 == 0) {

//                    recyclerViewJiuYue=(RecyclerView)views[arg0].findViewById(R.id.kapian_view_biblemenu_jiuyue);//引入卡片布局
//                    recyclerViewXinYue=(RecyclerView)views[arg0].findViewById(R.id.kapian_view_biblemenu_xinyue);
//
//
//                    GridLayoutManager layoutManager=new GridLayoutManager(ReadBible_menu_ac.this,4);
//                    GridLayoutManager layoutManager2=new GridLayoutManager(ReadBible_menu_ac.this,4);
//
//                    recyclerViewJiuYue.setLayoutManager(layoutManager);
//                    recyclerViewXinYue.setLayoutManager(layoutManager2);

//                    AddShuJuan();
//                    AddXinYue();
//                    tupianList.clear();
//                    for(int i=0;i<biblemenu.length;i++){
//                        //将图片添加到图片数组中
//                        tupianList.add(biblemenu[i]);
//                    }
//
//                    tupianListXinYue.clear();
//                    for(int i=0;i<biblemenuXinYue.length;i++){
//                        tupianListXinYue.add(biblemenuXinYue[i]);
//                    }

//                    adapter=new Adp_ReadBibleMenu_Adp(tupianList);
//                    adapterXinYue=new Adp_ReadBibleMenu_Adp(tupianListXinYue);
//                    recyclerViewJiuYue.setAdapter(adapter);
//                    recyclerViewXinYue.setAdapter(adapterXinYue);

                    //vv=ReturnView();
                    // recyclerView.findContainingItemView(getCurrentFocus()).setBackgroundColor(getResources().getColor(R.color.black));
                    ItemClickSupport.addTo(recyclerViewJiuYue).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                        @Override
                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                            ChangeGive(position);
                            InitView2();

                            pager.setCurrentItem(arg0 + 1);
                        }
                    });
                    ItemClickSupport.addTo(recyclerViewXinYue).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                        @Override
                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                            ChangeGiveXinYue(position);
                            InitView2();

                            pager.setCurrentItem(arg0 + 1);
                        }
                    });
//
                } else {
                    // InitView2();
                    ItemClickSupport.addTo(recyclerViewZhang).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                        @Override
                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                            Intent intent = new Intent(ReadBible_menu_ac.this, ReadBible_Main_ac.class);
                            intent.putExtra("Juan", ShuJuan);
                            intent.putExtra("Zhang", position + 1);
                            startActivity(intent);
                        }
                    });
                }


            }
        });

    }

    private void InitView2() {

        AddZhang(ShuJuan, nemberZhang);
        tupianListZhang.clear();
        for (int i = 0; i < biblemenu.length; i++) {
            //将图片添加到图片数组中

            tupianListZhang.add(biblemenu[i]);
        }
        adapterZhang = new Adp_ReadBibleMenu_Adp(tupianListZhang);
        recyclerViewZhang.setAdapter(adapterZhang);

    }

    private void AddShuJuan() {
        biblemenu = new Adp_ReadBibleMenu_BianLiang[]{
                new Adp_ReadBibleMenu_BianLiang("创", "创世记", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("出", "出埃及记", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("利", "利未记", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("民", "民数记", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("申", "申命记", R.drawable.bible),

                new Adp_ReadBibleMenu_BianLiang("书", "约书亚记", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("士", "士师记", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("得", "路得记", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("撒上", "撒母耳记上", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("撒下", "撒母耳记下", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("王上", "列王纪上", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("王下", "列王纪下", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("代上", "历代志上", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("代下", "历代志下", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("拉", "以斯拉记", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("尼", "尼希米记", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("斯", "以斯帖记", R.drawable.bible),

                new Adp_ReadBibleMenu_BianLiang("伯", "约伯记", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("诗", "诗篇", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("箴", "箴言", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("传", "传道书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("歌", "雅歌", R.drawable.bible),

                new Adp_ReadBibleMenu_BianLiang("赛", "以赛亚书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("耶", "耶利米书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("哀", "耶利米哀歌", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("结", "以西结书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("但", "但以理书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("何", "何西阿书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("珥", "约珥书", R.drawable.bible),

                new Adp_ReadBibleMenu_BianLiang("摩", "阿摩司书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("俄", "俄巴底亚书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("拿", "约拿书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("弥", "弥迦书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("鸿", "那鸿书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("哈", "哈巴谷书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("番", "西番雅书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("该", "哈该书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("亚", "撒迦利亚", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("玛", "玛拉基书", R.drawable.bible),
        };
    }

    private void AddXinYue() {
        biblemenuXinYue = new Adp_ReadBibleMenu_BianLiang[]{
                new Adp_ReadBibleMenu_BianLiang("太", "马太福音", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("可", "马可福音", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("路", "路加福音", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("约", "约翰福音", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("徒", "使徒行传", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("罗", "罗马书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("林前", "哥林多前书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("林后", "哥林多后书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("加", "加拉太书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("弗", "以弗所书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("腓", "腓利比书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("西", "歌罗西书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("帖前", "帖撒罗尼迦前书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("帖后", "帖撒罗尼迦后书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("提前", "提摩太前书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("提后", "提摩太后书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("多", "提多书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("门", "腓利门书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("来", "希伯来书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("雅", "雅各书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("彼前", "彼得前书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("彼后", "彼得后书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("约一", "约翰一书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("约二", "约翰二书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("约三", "约翰三书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("犹", "犹大书", R.drawable.bible),
                new Adp_ReadBibleMenu_BianLiang("启", "启示录", R.drawable.bible),
//                new Adp_ReadBibleMenu_BianLiang("","",R.drawable.bible),
        };
    }

    private void AddZhang(String shuJuan, int nemberShuJuan) {
        biblemenu = new Adp_ReadBibleMenu_BianLiang[nemberShuJuan];

        String nember;
        for (int i = 0; i < nemberShuJuan; i++) {
            nember = Integer.toString(i + 1);
            biblemenu[i] = new Adp_ReadBibleMenu_BianLiang(shuJuan, nember, R.drawable.bible);
        }

    }

    private void ChangeGive(int current) {
        switch (current) {
            case 0:
                ShuJuan = "创世记";
                nemberZhang = 50;
                break;
            case 1:
                ShuJuan = "出埃及记";
                nemberZhang = 40;
                break;
            case 2:
                ShuJuan = "利未记";
                nemberZhang = 27;
                break;
            case 3:
                ShuJuan = "民数记";
                nemberZhang = 36;
                break;
            case 4:
                ShuJuan = "申命记";
                nemberZhang = 34;
                break;
            case 5:
                ShuJuan = "约书亚记";
                nemberZhang = 24;
                break;
            case 6:
                ShuJuan = "士师记";
                nemberZhang = 21;
                break;
            case 7:
                ShuJuan = "路得记";
                nemberZhang = 4;
                break;
            case 8:
                ShuJuan = "撒上";
                nemberZhang = 31;
                break;
            case 9:
                ShuJuan = "撒下";
                nemberZhang = 24;
                break;
            case 10:
                ShuJuan = "列王纪上";
                nemberZhang = 22;
                break;
            case 11:
                ShuJuan = "列王纪下";
                nemberZhang = 25;
                break;
            case 12:
                ShuJuan = "历代志上";
                nemberZhang = 29;
                break;
            case 13:
                ShuJuan = "历代志下";
                nemberZhang = 36;
                break;
            case 14:
                ShuJuan = "以斯拉记";
                nemberZhang = 10;
                break;
            case 15:
                ShuJuan = "尼希米记";
                nemberZhang = 13;
                break;

            case 16:
                ShuJuan = "以斯帖记";
                nemberZhang = 10;
                break;
            case 17:
                ShuJuan = "约伯记";
                nemberZhang = 42;
                break;
            case 18:
                ShuJuan = "诗篇";
                nemberZhang = 150;
                break;
            case 19:
                ShuJuan = "箴言";
                nemberZhang = 31;
                break;
            case 20:
                ShuJuan = "传道书";
                nemberZhang = 12;
                break;
            case 21:
                ShuJuan = "雅歌";
                nemberZhang = 8;
                break;
            case 22:
                ShuJuan = "以赛亚书";
                nemberZhang = 66;
                break;
            case 23:
                ShuJuan = "耶利米书";
                nemberZhang = 52;
                break;
            case 24:
                ShuJuan = "耶利米哀歌";
                nemberZhang = 5;
                break;
            case 25:
                ShuJuan = "以西结书";
                nemberZhang = 48;
                break;
            case 26:
                ShuJuan = "但以理书";
                nemberZhang = 12;
                break;
            case 27:
                ShuJuan = "何西阿书";
                nemberZhang = 14;
                break;
            case 28:
                ShuJuan = "约珥书";
                nemberZhang = 3;
                break;
            case 29:
                ShuJuan = "阿摩司书";
                nemberZhang = 9;
                break;
            case 30:
                ShuJuan = "俄巴底亚书";
                nemberZhang = 1;
                break;
            case 31:
                ShuJuan = "约拿书";
                nemberZhang = 4;
                break;
            case 32:
                ShuJuan = "弥迦书";
                nemberZhang = 7;
                break;
            case 33:
                ShuJuan = "那鸿书";
                nemberZhang = 3;
                break;
            case 34:
                ShuJuan = "哈巴谷书";
                nemberZhang = 3;
                break;
            case 35:
                ShuJuan = "西番雅书";
                nemberZhang = 3;
                break;
            case 36:
                ShuJuan = "哈该书";
                nemberZhang = 2;
                break;
            case 37:
                ShuJuan = "撒迦利亚书";
                nemberZhang = 14;
                break;
            case 38:
                ShuJuan = "玛拉基书";
                nemberZhang = 4;
                break;

            default:
                break;

        }
    }

    private void ChangeGiveXinYue(int current) {
        switch (current + 1) {
            case 1:
                ShuJuan = "马太福音";
                nemberZhang = 28;
                break;
            case 2:
                ShuJuan = "马可福音";
                nemberZhang = 16;
                break;
            case 3:
                ShuJuan = "路加福音";
                nemberZhang = 24;
                break;
            case 4:
                ShuJuan = "约翰福音";
                nemberZhang = 21;
                break;
            case 5:
                ShuJuan = "使徒行传";
                nemberZhang = 28;
                break;
            case 6:
                ShuJuan = "罗马书";
                nemberZhang = 16;
                break;
            case 7:
                ShuJuan = "林前";
                nemberZhang = 16;
                break;
            case 8:
                ShuJuan = "林后";
                nemberZhang = 13;
                break;
            case 9:
                ShuJuan = "加拉太书";
                nemberZhang = 6;
                break;
            case 10:
                ShuJuan = "以弗所书";
                nemberZhang = 6;
                break;
            case 11:
                ShuJuan = "腓利比书";
                nemberZhang = 4;
                break;
            case 12:
                ShuJuan = "歌罗西书";
                nemberZhang = 4;
                break;
            case 13:
                ShuJuan = "帖前";
                nemberZhang = 5;
                break;
            case 14:
                ShuJuan = "帖后";
                nemberZhang = 3;
                break;
            case 15:
                ShuJuan = "提前";
                nemberZhang = 6;
                break;
            case 16:
                ShuJuan = "提后";
                nemberZhang = 4;
                break;
            case 17:
                ShuJuan = "提多书";
                nemberZhang = 3;
                break;
            case 18:
                ShuJuan = "腓利门书";
                nemberZhang = 1;
                break;
            case 19:
                ShuJuan = "希伯来书";
                nemberZhang = 13;
                break;
            case 20:
                ShuJuan = "雅各书";
                nemberZhang = 5;
                break;
            case 21:
                ShuJuan = "彼得前书";
                nemberZhang = 5;
                break;
            case 22:
                ShuJuan = "彼得后书";
                nemberZhang = 3;
                break;
            case 23:
                ShuJuan = "约翰一书";
                nemberZhang = 5;
                break;
            case 24:
                ShuJuan = "约翰二书";
                nemberZhang = 1;
                break;
            case 25:
                ShuJuan = "约翰三书";
                nemberZhang = 1;
                break;
            case 26:
                ShuJuan = "犹大书";
                nemberZhang = 1;
                break;
            case 27:
                ShuJuan = "启示录";
                nemberZhang = 22;
                break;
            default:
                break;
        }
    }

    private void ChangeNemBerZhang(String menuJuan) {
        switch (menuJuan) {
            case "创世记":
                nemberZhang = 50;
                break;
            case "出埃及记":
                nemberZhang = 40;
                break;
            case "利未记":
                nemberZhang = 27;
                break;
            case "民数记":
                nemberZhang = 36;
                break;
            case "申命记":
                nemberZhang = 34;
                break;
            case "约书亚记":
                nemberZhang = 24;
                break;
            case "士师记":
                nemberZhang = 21;
                break;
            case "路得记":
                nemberZhang = 4;
                break;
            case "撒上":
                nemberZhang = 31;
                break;
            case "撒下":
                nemberZhang = 24;
                break;
            case "列王记上":
                nemberZhang = 22;
                break;
            case "列王记下":
                nemberZhang = 25;
                break;
            case "历代志上":
                nemberZhang = 29;
                break;
            case "历代志下":
                nemberZhang = 36;
                break;
            case "以斯拉记":
                nemberZhang = 10;
                break;
            case "尼希米记":
                nemberZhang = 13;
                break;
            case "以斯帖记":
                nemberZhang = 10;
                break;
            case "约伯记":
                nemberZhang = 42;
                break;
            case "诗篇":
                nemberZhang = 150;
                break;
            case "箴言":
                nemberZhang = 31;
                break;
            case "传道书":
                nemberZhang = 12;
                break;
            case "雅歌":
                nemberZhang = 8;
                break;
            case "以赛亚书":
                nemberZhang = 66;
                break;
            case "耶利米书":
                nemberZhang = 52;
                break;
            case "哀":
                nemberZhang = 5;
                break;
            case "以西结书":
                nemberZhang = 48;
                break;
            case "但以理书":
                nemberZhang = 12;
                break;
            case "何西阿书":
                nemberZhang = 14;
                break;
            case "约珥书":
                nemberZhang = 3;
                break;
            case "阿摩司书":
                nemberZhang = 9;
                break;
            case "俄":
                nemberZhang = 1;
                break;
            case "约拿书":
                nemberZhang = 4;
                break;
            case "弥迦书":
                nemberZhang = 7;
                break;
            case "那鸿书":
                nemberZhang = 3;
                break;
            case "哈巴谷书":
                nemberZhang = 3;
                break;
            case "西番雅书":
                nemberZhang = 3;
                break;
            case "哈该书":
                nemberZhang = 2;
                break;
            case "亚":
                nemberZhang = 14;
                break;
            case "玛拉基书":
                nemberZhang = 4;
                break;
            case "马太福音":
                nemberZhang = 28;
                break;
            case "马可福音":
                nemberZhang = 16;
                break;
            case "路加福音":
                nemberZhang = 24;
                break;
            case "约翰福音":
                nemberZhang = 21;
                break;
            case "使徒行传":
                nemberZhang = 28;
                break;
            case "罗马书":
                nemberZhang = 16;
                break;
            case "林前":
                nemberZhang = 16;
                break;
            case "林后":
                nemberZhang = 13;
                break;
            case "加拉太书":
                nemberZhang = 6;
                break;
            case "以弗所书":
                nemberZhang = 6;
                break;
            case "腓利比书":
                nemberZhang = 4;
                break;
            case "歌罗西书":
                nemberZhang = 4;
                break;
            case "帖前":
                nemberZhang = 5;
                break;
            case "帖后":
                nemberZhang = 3;
                break;
            case "提前":
                nemberZhang = 6;
                break;
            case "提后":
                nemberZhang = 4;
                break;
            case "提多书":
                nemberZhang = 3;
                break;
            case "腓利门书":
                nemberZhang = 1;
                break;
            case "希伯来书":
                nemberZhang = 13;
                break;
            case "雅各书":
                nemberZhang = 5;
                break;
            case "彼得前书":
                nemberZhang = 5;
                break;
            case "彼得后书":
                nemberZhang = 3;
                break;
            case "约翰一书":
                nemberZhang = 5;
                break;
            case "约翰二书":
                nemberZhang = 1;
                break;
            case "约翰三书":
                nemberZhang = 1;
                break;
            case "犹大书":
                nemberZhang = 1;
                break;
            case "启示录":
                nemberZhang = 22;
                break;

            default:
                nemberZhang = 66;
                break;
        }
    }


//旧约全书：
//
//    创世纪、、、、、、、、、、
//    、、、、、、、、、、、、、、、、、、、、、、、、、、、、
//
//    新约全书：
//
//    马太福音、马可福音、路加福音、约翰福音、使徒行传、罗马书、歌林多前书、歌林多后书、加拉太书、
//    以弗所书、腓利比书、歌罗西书、帖撒罗尼迦前书、帖撒罗尼迦后书、提摩太前书、提摩太后书、提多书、
//    腓利门书、希伯来书、雅各书、彼得前书、彼得后书、约翰一书、约翰二书、约翰三书、犹大书、启示录

}
