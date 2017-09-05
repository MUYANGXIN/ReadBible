package com.example.Activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Screen.DepthPageTransformer;
import com.example.Bible.BibleOld;
import com.example.LogMy.LogMy;
import com.example.ActivityAds.ActivityCollector;
import com.example.ActivityAds.MainActivity;
import com.example.Db.DbReadBibleBianLiang;
import com.example.Db.DbReadBibleCreate;
import com.example.shujuku.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ReadBible_Main_ac extends MainActivity implements View.OnClickListener {

    //目前章节
    private int currentZhang;
    //数据库长度
    private int count;

    //需要显示的卷章
    private String Juan;
    private int Zhang;

    //数据库服务
    private DbReadBibleCreate dbReadBibleCreate;
    private SQLiteDatabase dbWrite;

    private String search;//查找
    private Boolean isSearch;

    //这个list用来将所有圣经取出来存放
    private List<DbReadBibleBianLiang> list = new ArrayList<>();

    //长按显示出来的复制框popuview相关变量
    private PopupWindow popupWindow, popupWindowSearch;
    private View popupView, popupViewSearch;
    private Button cancel, copy, find, morefounction;
    public TextView txtcount;

    //预加载需要的三个listview，一个当前页，两个上下页
    private ListView listview;
    private ListView listview2;
    private ListView listview3;

    //array负责存放经文内容，array2负责存放章节
    private List<String> array = new ArrayList<>();
    private List<String> array2 = new ArrayList<>();

    //用来预加载的两类四个list,分别存放内容与章节
    private List<String> arrayNext = new ArrayList<>();
    private List<String> arrayNext2 = new ArrayList<>();
    private List<String> arrayUp = new ArrayList<>();
    private List<String> arrayUp2 = new ArrayList<>();

    //预加载需要的三个adapter
    private Adapter adapter;
    private AdapterUp adapterUp;
    private AdapterNext adapterNext;

    //是否允许复制，长按时设置为TRUE。
    private boolean copyState = false;

    //长按复制时，分别记录选中的章节和经文内容
    private List<String> selectid2 = new ArrayList<>();
    private Map<String, String> map = new LinkedHashMap<String, String>();


    //viewpager变量
    private ViewPager viewPager;
    private ArrayList<View> viewContainter;

    //标题栏设置，用于显示当前书卷及章数
    private Button btn_juan, btn_zhang;

    private int lastPage = 199;

    //记住退出时阅读位置
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    private int ListPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readbible__main_aclout);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        //从上一个活动获取书卷和第几章
        //连接数据库，添加圣经
        Intent intent = getIntent();
        Juan = intent.getStringExtra("Juan");
        Zhang = intent.getIntExtra("Zhang", 0);

        dbReadBibleCreate = new DbReadBibleCreate(this, "ReadBible.db", null, 1);
        dbWrite = dbReadBibleCreate.getWritableDatabase();

        firstAddBiblleOld();

        //初始化，取出指定卷章
        init();


        //三个view进行循环
        final View[] views = new View[3];
        viewContainter = new ArrayList<View>();
        for (int i = 0; i < 3; i++) {
            views[i] = LayoutInflater.from(this).inflate(R.layout.activity_readbible_main_viewpager, null);

            //viewpager开始添加view
            viewContainter.add(views[i]);
        }

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        listview = (ListView) views[0].findViewById(R.id.listview_item);
        listview2 = (ListView) views[1].findViewById(R.id.listview_item);
        listview3 = (ListView) views[2].findViewById(R.id.listview_item);


//        //没后缀是上个页面，后缀为2是当前页面，为3是下个页面
        btn_juan = (Button) findViewById(R.id.biaoti_juan_item);
        btn_zhang = (Button) findViewById(R.id.biaoti_zhang_item);

        adapterUp = new AdapterUp(this);
        adapter = new Adapter(this);
        adapterNext = new AdapterNext(this);


        //第二个是当前页面，第一个上个页面预加载，第三个下个页面预加载
        listview3.setAdapter(adapterNext);
        listview.setAdapter(adapterUp);
        listview2.setAdapter(adapter);


        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);


        //有关popuview的初始化
        popupView = layoutInflater.inflate(R.layout.activity_readbible_main_popuview, null);

        txtcount = (TextView) popupView.findViewById(R.id.popuview_txtcount);
        cancel = (Button) popupView.findViewById(R.id.popuview_cancel);
        copy = (Button) popupView.findViewById(R.id.popuview_copy);

        //有关popuviewSearch的初始化
        popupViewSearch = layoutInflater.inflate(R.layout.activity_readbible_main_searchpopuview, null);

        find = (Button) popupViewSearch.findViewById(R.id.popuview_srarch);
        morefounction = (Button) popupViewSearch.findViewById(R.id.popuview_more);

        cancel.setOnClickListener(this);
        copy.setOnClickListener(this);

        find.setOnClickListener(this);
        morefounction.setOnClickListener(this);


        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                //设置成400，使用户看不到边界(也就是左右各有200页，一般用户不会滑动这么多
                return 400;
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                //Warning：不要在这里调用removeView
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                //对ViewPager页号求模取出View列表中要显示的项
                position %= viewContainter.size();
                if (position < 0) {
                    position = viewContainter.size() + position + 1;
                }
                View view = viewContainter.get(position);
                //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
                ViewParent vp = view.getParent();
                if (vp != null) {
                    ViewGroup parent = (ViewGroup) vp;
                    parent.removeView(view);
                }
                container.addView(view);

                //add listeners here if necessary
                return view;
            }
        });

        //当前页设为199，使得刚好是中间的那页，即当前页
        viewPager.setCurrentItem(199);

        //动画
        viewPager.setPageTransformer(true, new DepthPageTransformer());

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
                //此方法是在状态改变的时候调用，其中arg0这个参数有三种状态（0，1，2）。
                // arg0 ==1的时辰默示正在滑动，
                // arg0==2的时辰默示滑动完毕了，
                // arg0==0的时辰默示什么都没做。
                // 当页面开始滑动的时候，三种状态的变化顺序为（1，2，0）

//        if (arg0 == 0) {
//
//        } else if (arg0 == 1) {
//
//        } else {
//
//        }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                //当页面在滑动的时候会调用此方法，在滑动被停止之前，此方法回一直得到调用。
                // 其中三个参数的含义分别为：
                // arg0 :当前页面，及你点击滑动的页面
                // arg1:当前页面偏移的百分比
                // arg2:当前页面偏移的像素位置

            }

            @Override
            public void onPageSelected(final int arg0) {
                //此方法是页面跳转完后得到调用，arg0是你当前选中的页面的Position（位置编号）。

                //基本思路是这样：滑动完成后，对前后页进行预加载，也就是改变arrayUp和arrayNext的内容，
                // 然后前后两个listview重新setAdapter，完成预加载
                //注意问题：滑动时需要将popuview、map、selected等于复制相关的东西全部清零。


                selectid2.clear();
                map.clear();
                if (copyState) {
                    copyState = false;
                    popupWindow.dismiss();//清空
                }

                //通过arg0和lastPage的比较，确定向左还是向右滑动
                if (arg0 > lastPage) {
                    if (currentZhang < list.size() - 1) {
                        currentZhang++;
                    } else {
                        currentZhang = 0;
                    }

                    lastPage = arg0;
                } else {
                    if (currentZhang > 0) {
                        currentZhang--;
                    } else {
                        currentZhang = list.size() - 1;
                    }

                    lastPage = arg0;
                }


                changeBeforeLoad(arg0);


            }
        });

    }

    /**
     * 改变预装载，也就是改变array，arrayUp，arrayNext的内容
     */
    private void changeBeforeLoad(int arg0) {

        if (arg0 % 3 == 2) {//listview3在前台


            //预加载下一章，此时arrayUp装载下一章
            if (currentZhang < list.size() - 1) {
                SpannableString jingwenUp = new SpannableString(list.get((currentZhang + 1)).DingYiTextBible);

                String jingwenUp0 = jingwenUp.toString();
                String[] fengeUp = jingwenUp0.split("\\s{1,}");

                arrayUp.clear();
                arrayUp2.clear();
                for (int i = 0; i < fengeUp.length / 2; i++) {

                    arrayUp.add(fengeUp[2 * i + 1]);
                    arrayUp2.add(fengeUp[2 * i]);
                }


            } else {
                SpannableString jingwenUp = new SpannableString(list.get(0).DingYiTextBible);

                String jingwenUp0 = jingwenUp.toString();
                String[] fengeUp = jingwenUp0.split("\\s{1,}");

                arrayUp.clear();
                arrayUp2.clear();
                for (int i = 0; i < fengeUp.length / 2; i++) {

                    arrayUp.add(fengeUp[2 * i + 1]);
                    arrayUp2.add(fengeUp[2 * i]);
                }
            }

            //加载当前章，此时arrayNext装载当前章
            SpannableString jingwen0 = new SpannableString(list.get(currentZhang).DingYiTextBible);

            String jingwen00 = jingwen0.toString();
            String[] fenge = jingwen00.split("\\s{1,}");

            arrayNext.clear();
            arrayNext2.clear();
            for (int i = 0; i < fenge.length / 2; i++) {

                arrayNext.add(fenge[2 * i + 1]);
                arrayNext2.add(fenge[2 * i]);
            }
            //预加载上一章，此时array装载上一章
            if (currentZhang > 0) {
                SpannableString jingwenNext = new SpannableString(list.get((currentZhang - 1)).DingYiTextBible);

                String jingwenNext0 = jingwenNext.toString();
                String[] fengeNext = jingwenNext0.split("\\s{1,}");

                array.clear();
                array2.clear();
                for (int i = 0; i < fengeNext.length / 2; i++) {

                    array.add(fengeNext[2 * i + 1]);
                    array2.add(fengeNext[2 * i]);
                }
            } else {
                SpannableString jingwenNext = new SpannableString(list.get(list.size() - 1).DingYiTextBible);

                String jingwenNext0 = jingwenNext.toString();
                String[] fengeNext = jingwenNext0.split("\\s{1,}");

                array.clear();
                array2.clear();
                for (int i = 0; i < fengeNext.length / 2; i++) {

                    array.add(fengeNext[2 * i + 1]);
                    array2.add(fengeNext[2 * i]);
                }
            }

//            for (int i=0;i<list.size();i++){
//                LogMy.d("list","is"+list.get(currentZhang).DingYiZhang);
//            }


            listview.setAdapter(adapterUp);
            listview2.setAdapter(adapter);

        }

        if (arg0 % 3 == 0) {
            //此时listview在前台，
            // 也就是说，arrayNext装载上一章，arrayUp装载当前章，array装载下一章

            //预加载下一章，此时array装载下一章
            if (currentZhang < list.size() - 1) {
                SpannableString jingwenUp = new SpannableString(list.get((currentZhang + 1)).DingYiTextBible);

                String jingwenUp0 = jingwenUp.toString();
                String[] fengeUp = jingwenUp0.split("\\s{1,}");

                array.clear();
                array2.clear();
                for (int i = 0; i < fengeUp.length / 2; i++) {

                    array.add(fengeUp[2 * i + 1]);
                    array2.add(fengeUp[2 * i]);
                }
            } else {
                SpannableString jingwenUp = new SpannableString(list.get(0).DingYiTextBible);

                String jingwenUp0 = jingwenUp.toString();
                String[] fengeUp = jingwenUp0.split("\\s{1,}");

                array.clear();
                array2.clear();
                for (int i = 0; i < fengeUp.length / 2; i++) {

                    array.add(fengeUp[2 * i + 1]);
                    array2.add(fengeUp[2 * i]);
                }
            }

            //加载当前章，此时arrayUp装载当前章
            SpannableString jingwen0 = new SpannableString(list.get(currentZhang).DingYiTextBible);

            String jingwen00 = jingwen0.toString();
            String[] fenge = jingwen00.split("\\s{1,}");

            arrayUp.clear();
            arrayUp2.clear();
            for (int i = 0; i < fenge.length / 2; i++) {

                arrayUp.add(fenge[2 * i + 1]);
                arrayUp2.add(fenge[2 * i]);
            }
            //预加载上一章，此时arrayNext装载上一章
            if (currentZhang > 0) {
                SpannableString jingwenNext = new SpannableString(list.get((currentZhang - 1)).DingYiTextBible);

                String jingwenNext0 = jingwenNext.toString();
                String[] fengeNext = jingwenNext0.split("\\s{1,}");

                arrayNext.clear();
                arrayNext2.clear();
                for (int i = 0; i < fengeNext.length / 2; i++) {

                    arrayNext.add(fengeNext[2 * i + 1]);
                    arrayNext2.add(fengeNext[2 * i]);
                }
            } else {
                SpannableString jingwenNext = new SpannableString(list.get(list.size() - 1).DingYiTextBible);

                String jingwenNext0 = jingwenNext.toString();
                String[] fengeNext = jingwenNext0.split("\\s{1,}");

                arrayNext.clear();
                arrayNext2.clear();
                for (int i = 0; i < fengeNext.length / 2; i++) {

                    arrayNext.add(fengeNext[2 * i + 1]);
                    arrayNext2.add(fengeNext[2 * i]);
                }
            }

            listview3.setAdapter(adapterNext);
            listview2.setAdapter(adapter);

        }

        if (arg0 % 3 == 1) {//此时listview2在前台

            //加载当前章，array装载当前章
            SpannableString jingwen0 = new SpannableString(list.get(currentZhang).DingYiTextBible);

            String jingwen00 = jingwen0.toString();
            String[] fenge = jingwen00.split("\\s{1,}");

            array.clear();
            array2.clear();
            for (int i = 0; i < fenge.length / 2; i++) {

                array.add(fenge[2 * i + 1]);
                array2.add(fenge[2 * i]);
            }


            //预加载上一章,arrayUp装载上一章
            if (currentZhang > 0) {


                SpannableString jingwenUp = new SpannableString(list.get((currentZhang - 1)).DingYiTextBible);

                String jingwenUp0 = jingwenUp.toString();
                String[] fengeUp = jingwenUp0.split("\\s{1,}");

                arrayUp.clear();
                arrayUp2.clear();
                for (int i = 0; i < fengeUp.length / 2; i++) {

                    arrayUp.add(fengeUp[2 * i + 1]);
                    arrayUp2.add(fengeUp[2 * i]);
                }
            } else {


                SpannableString jingwenUp = new SpannableString(list.get((list.size() - 1)).DingYiTextBible);

                String jingwenUp0 = jingwenUp.toString();
                String[] fengeUp = jingwenUp0.split("\\s{1,}");

                arrayUp.clear();
                arrayUp2.clear();
                for (int i = 0; i < fengeUp.length / 2; i++) {

                    arrayUp.add(fengeUp[2 * i + 1]);
                    arrayUp2.add(fengeUp[2 * i]);
                }
            }
            //预加载下一章,arrayNext装载下一章
            if (currentZhang < list.size() - 1) {
                SpannableString jingwenNext = new SpannableString(list.get((currentZhang + 1)).DingYiTextBible);

                String jingwenNext0 = jingwenNext.toString();
                String[] fengeNext = jingwenNext0.split("\\s{1,}");

                arrayNext.clear();
                arrayNext2.clear();
                for (int i = 0; i < fengeNext.length / 2; i++) {

                    arrayNext.add(fengeNext[2 * i + 1]);
                    arrayNext2.add(fengeNext[2 * i]);
                }
            } else {
                SpannableString jingwenNext = new SpannableString(list.get(0).DingYiTextBible);

                String jingwenNext0 = jingwenNext.toString();
                String[] fengeNext = jingwenNext0.split("\\s{1,}");

                arrayNext.clear();
                arrayNext2.clear();
                for (int i = 0; i < fengeNext.length / 2; i++) {

                    arrayNext.add(fengeNext[2 * i + 1]);
                    arrayNext2.add(fengeNext[2 * i]);
                }
            }

            listview.setAdapter(adapterUp);
            listview3.setAdapter(adapterNext);
        }


    }


    /**
     * 取出所有经文
     */
    void init() {
        Cursor cursor = dbWrite.rawQuery("select * from chuangshiji", null);

        LogMy.d("cursor.getCount()", " " + cursor.getCount());
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            count = cursor.getCount();
            //count是数据库的总长度，数组a的长度是题目个数，a[i]的值是问题的ID

            //遍历数据库，将取出来的数据放进List数组中
            for (int i = 0; i < count; i++) {
                cursor.moveToPosition(i);
                DbReadBibleBianLiang bianliang = new DbReadBibleBianLiang();
                //获得卷、章、内容
                bianliang.DingYiShuJuan = cursor.getString(cursor.getColumnIndex("Juan"));
                bianliang.DingYiZhang = cursor.getInt(cursor.getColumnIndex("Zhang"));
                bianliang.DingYiTextBible = cursor.getString(cursor.getColumnIndex("TextBible"));

                LogMy.d("id", "" + cursor.getString(cursor.getColumnIndex("Juan")) + cursor.getInt(cursor.getColumnIndex("Zhang")));
                list.add(bianliang);
                //此时list装载了所有的圣经
            }
        }


        /**定位到书卷、章*/

        for (int i = 0; i < count; i++) {


            if ((list.get(i).DingYiShuJuan).equals(Juan)) {//判断书卷

                if (Zhang == list.get(i).DingYiZhang) {//判断章

                    currentZhang = i;

                    //这个currentZhang其实是该要取出来的那一章在整本圣经中的第几章数，
                    // 也就是list中的编号
                    break;
                }
            }
        }


        //加载当前章
        SpannableString jingwen0 = new SpannableString(list.get(currentZhang).DingYiTextBible);

        String jingwen00 = jingwen0.toString();
        String[] fenge = jingwen00.split("\\s{1,}");

        array.clear();
        array2.clear();
        for (int i = 0; i < fenge.length / 2; i++) {

            //array装载经文，array2装载章节
            array.add(fenge[2 * i + 1]);
            array2.add(fenge[2 * i]);
        }


        //预加载上一章
        if (currentZhang > 0) {
            SpannableString jingwenUp = new SpannableString(list.get((currentZhang - 1)).DingYiTextBible);

            String jingwenUp0 = jingwenUp.toString();
            String[] fengeUp = jingwenUp0.split("\\s{1,}");

            arrayUp.clear();
            arrayUp2.clear();
            for (int i = 0; i < fengeUp.length / 2; i++) {

                arrayUp.add(fengeUp[2 * i + 1]);
                arrayUp2.add(fengeUp[2 * i]);
            }
        } else {
            SpannableString jingwenUp = new SpannableString(list.get((list.size() - 1)).DingYiTextBible);

            String jingwenUp0 = jingwenUp.toString();
            String[] fengeUp = jingwenUp0.split("\\s{1,}");

            arrayUp.clear();
            arrayUp2.clear();
            for (int i = 0; i < fengeUp.length / 2; i++) {

                arrayUp.add(fengeUp[2 * i + 1]);
                arrayUp2.add(fengeUp[2 * i]);
            }
        }
        //预加载下一章
        if (currentZhang < list.size() - 1) {
            SpannableString jingwenNext = new SpannableString(list.get((currentZhang + 1)).DingYiTextBible);

            String jingwenNext0 = jingwenNext.toString();
            String[] fengeNext = jingwenNext0.split("\\s{1,}");

            arrayNext.clear();
            arrayNext2.clear();
            for (int i = 0; i < fengeNext.length / 2; i++) {

                arrayNext.add(fengeNext[2 * i + 1]);
                arrayNext2.add(fengeNext[2 * i]);
            }
        } else {
            SpannableString jingwenNext = new SpannableString(list.get(0).DingYiTextBible);

            String jingwenNext0 = jingwenNext.toString();
            String[] fengeNext = jingwenNext0.split("\\s{1,}");

            arrayNext.clear();
            arrayNext2.clear();
            for (int i = 0; i < fengeNext.length / 2; i++) {

                arrayNext.add(fengeNext[2 * i + 1]);
                arrayNext2.add(fengeNext[2 * i]);
            }
        }

    }

    /***/
    //这个方法的作用尚不明确，我也不知道什么时候复制到这里一个方法。
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("操作");
    }

    /**
     * 自定义三个Adapter,一个当前页面，两个预加载
     */
    class Adapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater = null;
        private HashMap<Integer, View> mView;
        private View view;


        public Adapter(Context context) {
            this.context = context;


            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = new HashMap<Integer, View>();


            for (int i = 0; i < array.size(); i++) {
                //将所有的view实例化

                view = inflater.inflate(R.layout.activity_readbible_main_listview, null);
                mView.put(i, view);

            }

        }

        public int getCount() {
            // TODO Auto-generated method stub
            return array.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return array.get(position);
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            view = mView.get(position);


            btn_juan.setText("" + list.get(currentZhang).DingYiShuJuan);
            btn_zhang.setText("" + list.get(currentZhang).DingYiZhang);

            btn_juan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(ReadBible_Main_ac.this, ReadBible_menu_ac.class);
                    intent.putExtra("juan", list.get(currentZhang).DingYiShuJuan);
                    intent.putExtra("menu", 0);
                    startActivity(intent);
                }
            });
            btn_zhang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(ReadBible_Main_ac.this, ReadBible_menu_ac.class);
                    intent.putExtra("juan", list.get(currentZhang).DingYiShuJuan);
                    intent.putExtra("menu", 1);
                    startActivity(intent);
                }
            });


            view = inflater.inflate(R.layout.activity_readbible_main_listview, null);

            final TextView txt = (TextView) view.findViewById(R.id.listview_neirong);
            TextView txt2 = (TextView) view.findViewById(R.id.listview_zhangjie);
            final CheckBox ceb = (CheckBox) view.findViewById(R.id.listview_check);


            //显示好经文内容
            //txt.setText(array.get(position));
            txt.setText(keyWordSearch(array.get(position)));
            txt2.setText(array2.get(position));


            isCheckBoxCanSee(ceb);//确定复选框状态
            ceb.setChecked(isCheckBoxSelected(position));//检查是否已选中

            if (isCheckBoxSelected(position)) {
                txt.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线

            }


            //长按监听
            view.setOnLongClickListener(new Onlongclick());

            //点击监听
            view.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (copyState) {
                        //若复制允许，则已选中的点击时取消，未选中的点击时选中
                        if (ceb.isChecked()) {

                            //txt.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
                            //txt.getPaint().setFlags(0);

                            ceb.setChecked(false);

                            selectid2.remove(array2.get(position));
                            map.remove("" + position);

                        } else {
                            ceb.setChecked(true);


                            selectid2.add(array2.get(position));
                            map.put("" + position, "" + array.get(position));
                        }

                        txtcount.setText("共选择了" + map.size() + "项");


                    } else {

                        popupShowSearch(v);
                        // Toast.makeText(context, "" + array.get(position), Toast.LENGTH_LONG).show();
                    }
                }
            });

            mView.put(position, view);

            return view;
        }

        class Onlongclick implements View.OnLongClickListener {

            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub

                if (copyState) {
                    //复制状态长按无反应
                } else {
                    copyState = true;

                    //首先清空容器
                    selectid2.clear();
                    map.clear();

                    //刷新当前页，使复选框显示出来
                    listview2.setAdapter(adapter);

                    //显示复制选项popuview
                    popupShow(v);
                    txtcount.setText("共选择了0项");

                }

                return true;
            }
        }
    }

    //AdapterUp、AdapterNext与Adapter不同之处在于显示经文内容时，分别选用了arrayUp，arrayNext，array。其他代码相同，不再添加注释
    class AdapterUp extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater = null;
        private HashMap<Integer, View> mView;
        private View view;


        public AdapterUp(Context context) {
            this.context = context;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = new HashMap<Integer, View>();

            for (int i = 0; i < arrayUp.size(); i++) {
                //将所有的view实例化
                view = inflater.inflate(R.layout.activity_readbible_main_listview, null);

                mView.put(i, view);

            }

        }

        public int getCount() {
            // TODO Auto-generated method stub
            return arrayUp.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return arrayUp.get(position);
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        public View getView(final int position, View convertView, final ViewGroup parent) {
            // TODO Auto-generated method stub

            view = mView.get(position);


            view = inflater.inflate(R.layout.activity_readbible_main_listview, null);
            TextView txt = (TextView) view.findViewById(R.id.listview_neirong);
            TextView txt2 = (TextView) view.findViewById(R.id.listview_zhangjie);
            final CheckBox ceb = (CheckBox) view.findViewById(R.id.listview_check);

            //显示经文内容
            txt.setText(arrayUp.get(position));
            txt2.setText(arrayUp2.get(position));

            isCheckBoxCanSee(ceb);//确定复选框状态
            ceb.setChecked(isCheckBoxSelected(position));//检查是否已选中

            view.setOnLongClickListener(new Onlongclick());

            view.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (copyState) {
                        //若复制允许，则已选中的点击时取消，未选中的点击时选中
                        if (ceb.isChecked()) {

                            ceb.setChecked(false);

                            map.remove("" + position);
                            selectid2.remove(arrayUp2.get(position));

                        } else {
                            ceb.setChecked(true);

                            map.put("" + position, "" + arrayUp.get(position));
                            selectid2.add(arrayUp2.get(position));
                        }

                        txtcount.setText("共选择了" + map.size() + "项");

                    } else {
                        Toast.makeText(context, "点击了" + position + arrayUp.get(position), Toast.LENGTH_LONG).show();
                    }
                }
            });

            return view;
        }

        class Onlongclick implements View.OnLongClickListener {

            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub

                if (copyState) {

                } else {
                    copyState = true;

                    map.clear();
                    selectid2.clear();

                    listview.setAdapter(adapterUp);

                    popupShow(v);

                    txtcount.setText("共选择了0项");

                }

                return true;
            }
        }
    }

    class AdapterNext extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater = null;
        private HashMap<Integer, View> mView;
        private View view;


        public AdapterNext(Context context) {
            this.context = context;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = new HashMap<Integer, View>();

            for (int i = 0; i < arrayNext.size(); i++) {
                //将所有的view实例化
                view = inflater.inflate(R.layout.activity_readbible_main_listview, null);

                mView.put(i, view);

            }
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return arrayNext.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return arrayNext.get(position);
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            view = mView.get(position);


            view = inflater.inflate(R.layout.activity_readbible_main_listview, null);
            TextView txt = (TextView) view.findViewById(R.id.listview_neirong);
            TextView txt2 = (TextView) view.findViewById(R.id.listview_zhangjie);
            final CheckBox ceb = (CheckBox) view.findViewById(R.id.listview_check);

            //显示好经文内容
            txt.setText(arrayNext.get(position));
            txt2.setText(arrayNext2.get(position));

            isCheckBoxCanSee(ceb);//确定复选框状态
            ceb.setChecked(isCheckBoxSelected(position));//检查是否已选中

            view.setOnLongClickListener(new Onlongclick());

            view.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (copyState) {
                        //若复制允许，则已选中的点击时取消，未选中的点击时选中
                        if (ceb.isChecked()) {

                            ceb.setChecked(false);
                            map.remove("" + position);
                            selectid2.remove(arrayNext2.get(position));

                        } else {

                            ceb.setChecked(true);
                            map.put("" + position, "" + arrayNext.get(position));
                            selectid2.add(arrayNext2.get(position));
                        }
                        txtcount.setText("共选择了" + map.size() + "项");


                    } else {
                        Toast.makeText(context, "" + arrayNext.get(position), Toast.LENGTH_LONG).show();
                    }
                }
            });

            mView.put(position, view);

            return view;
        }

        class Onlongclick implements View.OnLongClickListener {

            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub

                if (copyState) {

                } else {
                    copyState = true;

                    map.clear();
                    selectid2.clear();

                    listview3.setAdapter(adapterNext);

                    popupShow(v);

                    txtcount.setText("共选择了0项");

                }

                return true;
            }
        }
    }


//       记录listview的位置
//    private AbsListView.OnScrollListener ScrollLis = new AbsListView.OnScrollListener() {
//
//        @Override
//        public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
//        }
//
//        @Override
//        public void onScrollStateChanged(AbsListView view, int scrollState) {
//            if(scrollState== AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
//                ListPosition=listview.getFirstVisiblePosition();  //ListPos记录当前可见的List顶端的一行的位置
//            }
//        }
//    };


    /**
     * 确定复选框是否可见
     */
    private void isCheckBoxCanSee(CheckBox checkBox) {
        if (copyState) {//根据是否允许复制来判断，允许复制则可见，否则不可见

            checkBox.setVisibility(View.VISIBLE);

        } else {

            checkBox.setVisibility(View.GONE);

        }
    }

    /**
     * 确定复选框是否选中
     */
    private boolean isCheckBoxSelected(int position) {
        //根据map里已经存储的项目判断，已经存储的则选中
        Boolean isCheck = false;
        for (Map.Entry<String, String> m : map.entrySet()) {
            if (position == Integer.parseInt(m.getKey())) {
                isCheck = true;
                break;
            }
        }
        return isCheck;
    }

    /**
     * 长按listview时开始复制，弹出popuview
     */
    private void popupShow(View view) {

        // 显示popup window
        popupWindow = new PopupWindow(popupView, 500, 100);

        //显示时外部可点击，且点击不取消popuview
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(false);

        // 透明背景,可是不透明，待优化
        Drawable transparent = new ColorDrawable(Color.WHITE);
        popupWindow.setBackgroundDrawable(transparent);

        // 获取位置,底部
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 20);


    }

    /**
     * 点击listview时显出菜单栏，弹出popuviewSearch
     */
    private void popupShowSearch(View view) {

        // 显示popup window
        popupWindowSearch = new PopupWindow(popupViewSearch, 500, 100);

        //显示时外部点击取消popuview
        popupWindowSearch.setOutsideTouchable(true);
        popupWindowSearch.setFocusable(true);

        // 透明背景,可是不透明，待优化
        Drawable transparent = new ColorDrawable(Color.WHITE);
        popupWindowSearch.setBackgroundDrawable(transparent);

        // 获取位置,底部
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindowSearch.showAtLocation(view, Gravity.TOP, 0, 20);


    }

    /**
     * popuview的点击事件
     */
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.popuview_cancel:

                copyState = false;//禁止复制,popuview 消失

                map.clear();
                selectid2.clear();//清空

                listview.setAdapter(adapterUp);
                listview2.setAdapter(adapter);
                listview3.setAdapter(adapterNext);

                popupWindow.dismiss();

                break;
            case R.id.popuview_copy:

                copyState = false;//禁止复制,popuview消失

//
                //取出键值的方法
//                //第一种 用for循环的方式
//                for (Map.Entry<String,String> m :map.entrySet())  {
//                    LogMy.d("用for循环的方式"+m.getKey(),"\t"+m.getValue());
//                }
//
//                //利用迭代 （Iterator）
//                Set set=map.entrySet();
//                Iterator iterator=set.iterator();
//                while(iterator.hasNext()){
//                    Map.Entry<String, Object> enter=(Map.Entry<String, Object>) iterator.next();
//                    LogMy.d("利用迭代"+enter.getKey(),"\t"+enter.getValue());
//                }


                //selectid转化为字符串。注意，selectid是list，若用户选择复制时没有按经文顺序，
                //则粘贴出来经文顺序是乱的。所以要在转化字符串之前对selectid进行顺序调整。


                if (map.size() > 0) {
                    /**排序，加入粘贴板*/
                    StringBuffer copyEnd = new StringBuffer();
                    if (map != null && map.size() > 0) {

                        Map<String, String> resultMap = sortMapByKey(map);//对map进行排序

                        for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                            //将排序后的resultMap加入复制板
                            copyEnd.append(entry.getValue());
                        }

                    }

                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("simple text", copyEnd);
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(this, "复制成功", Toast.LENGTH_SHORT).show();

                    selectid2.clear();
                    map.clear();

                    listview.setAdapter(adapterUp);
                    listview2.setAdapter(adapter);
                    listview3.setAdapter(adapterNext);
                    popupWindow.dismiss();

                } else {
                    Toast.makeText(this, "还没有选择复制的经文", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.popuview_srarch:
                Intent intent = new Intent(ReadBible_Main_ac.this, ReadBible_Search_ac.class);
                startActivity(intent);
                LogMy.d("sousuo", "finish");
                break;


            default:
                break;
        }

    }

    /**
     * map排序
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        //按键值排序

        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, String> sortMap = new TreeMap<String, String>(new Comparator<String>() {
            public int compare(String str1, String str2) {

                int i = Integer.parseInt(str1);
                int j = Integer.parseInt(str2);

                return i - j;
            }
        });

        sortMap.putAll(map);

        return sortMap;
    }

    /**
     * 关键词定位
     * 返回字符串
     */
    private SpannableString keyWordSearch(String waitSearch) {
        //关键词定位
        SpannableString alreadySearch = new SpannableString(waitSearch);

        search = "耶和华";
        Pattern p = Pattern.compile(search);


        Matcher m = p.matcher(alreadySearch);
        int start;
        int end;
//        int k=1;

        while (m.find()) {
//            if(k==1){
//                k++;
//            }

            start = m.start();
            end = m.end();
            alreadySearch.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return alreadySearch;
    }

    /**
     * 第一次添加圣经
     */
    public void firstAddBiblleOld() {

        Cursor cursor = dbWrite.rawQuery("select * from chuangshiji", null);

        int dbLen = cursor.getCount();

        ContentValues values = new ContentValues();

        BibleOld bibleOld = new BibleOld();
        List<DbReadBibleBianLiang> list1 = bibleOld.AddOld();

        //如果两个长度相等，则证明没有新添加问题，不用更新
        if (dbLen == list1.size()) {

        } else {//否则的话应将老数据删除，加入新数据

            dbWrite.delete("chuangshiji", "id>=?", new String[]{"0"});

            for (int i = 0; i < list1.size(); i++) {

                DbReadBibleBianLiang bianLiang = list1.get(i);

                values.put("Juan", bianLiang.DingYiShuJuan);
                values.put("Zhang", bianLiang.DingYiZhang);
                values.put("TextBible", bianLiang.DingYiTextBible);

                dbWrite.insert("chuangshiji", null, values);

            }
        }
    }

    /**
     * 返回键处理
     */
    @Override//关闭侧滑页面
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (copyState) {

                map.clear();
                selectid2.clear();

                popupWindow.dismiss();
                copyState = false;

                listview.setAdapter(adapterUp);
                listview2.setAdapter(adapter);
                listview3.setAdapter(adapterNext);

            } else {

                //存储当前卷章
                editor = pref.edit();
                editor.putString("juan", list.get(currentZhang).DingYiShuJuan);
                editor.putInt("zhang", list.get(currentZhang).DingYiZhang);
                editor.commit();
                ActivityCollector.finishAll();
            }
        }
        return true;
    }


//    private TextView textView;
//    private ScrollView view;
//    private int i;
//    private int currentZhang;//目前章节
//    private int count;//数据库长度
//
//    private String Juan;
//    private int Zhang;
//
//    //private DbReadBibleService dbReadBibleService;
//    private DbReadBibleCreate dbReadBibleCreate;
//    private SQLiteDatabase dbWrite;
//
//    private String search;//查找
//
//    private List<DbReadBibleBianLiang>list=new ArrayList<>();
//
//    private List<Adp_ReadBibleMain_listView_support>list_view=new ArrayList<>();
//
//    private Toolbar mMainToolbar = null;
//
//
//    private float mStartY = 0, mLastY = 0, mLastDeltaY;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_readbible__main_aclout);
//
//        dbReadBibleCreate=new DbReadBibleCreate(this,"ReadBible.db",null,1);
//        dbWrite=dbReadBibleCreate.getWritableDatabase();
//
//        FirstAddBiblleOld();
//
//        textView=(TextView)findViewById(R.id.tv_readbible_readmain);
//
//        search="耶和华";
//
//        Cursor cursor=dbWrite.rawQuery("select * from chuangshiji",null);
//
//        LogMy.d("cursor.getCount()"," "+cursor.getCount());
//        if(cursor.getCount()>0){
//            cursor.moveToFirst();
//            count=cursor.getCount();
//            //count是数据库的总长度，数组a的长度是题目个数，a[i]的值是问题的ID
//
//            //遍历数据库，将取出来的数据放进List数组中
//            for(int i=0;i<count;i++){
//                cursor.moveToPosition(i);
//                DbReadBibleBianLiang bianliang=new DbReadBibleBianLiang();
//                //获得卷、章、内容
//                bianliang.DingYiShuJuan=cursor.getString(cursor.getColumnIndex("Juan"));
//                bianliang.DingYiZhang=cursor.getInt(cursor.getColumnIndex("Zhang"));
//                bianliang.DingYiTextBible=cursor.getString(cursor.getColumnIndex("TextBible"));
//
//                LogMy.d("id",""+cursor.getString(cursor.getColumnIndex("Juan"))+cursor.getInt(cursor.getColumnIndex("Zhang")));
//                list.add(bianliang);
//
//            }
//        }
//
//
//        mMainToolbar=(Toolbar)findViewById(R.id.toolbar);
//       // textView.setText(list.get(1).DingYiTextBible);
//        view=(ScrollView)findViewById(R.id.scolview_readmain);
//        view.setLongClickable(true);
//        view.setOnTouchListener(new MyGestureListener(this));
//
//        /**定位到书卷、章*/
//        String j;
//        for(int i=0;i<count;i++){
//             j=list.get(i).DingYiShuJuan;
//
//            if(j.equals(Juan)){
//
//                if(Zhang==list.get(i).DingYiZhang){
//
//                    currentZhang=i;
//                    break;
//                }
//            }
//        }
//
//
//        SpannableString s = new SpannableString(list.get(currentZhang).DingYiTextBible);
//
//
//
//        //搜索功能
//        Pattern p = Pattern.compile(search);
//        Matcher m = p.matcher(s);
//        int start;
//        int end;
//        int k=1;
//
//        i=0;
//        while (m.find()) {
//            if(k==1){
//                i=m.start();
//                k++;
//            }
//
//             start = m.start();
//             end = m.end();
//            s.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//
//        String ss=s.toString();
//        String []fenge=ss.split("\\s{1,}");
//        LogMy.d("经文长度",""+fenge.length);
//        for(int i=0;i<fenge.length;i++){
//            LogMy.d("分割后",""+fenge[i]);
//
//        }
//
//        for (int i=0;i<fenge.length/2;i++){
//            Adp_ReadBibleMain_listView_support one=new Adp_ReadBibleMain_listView_support(fenge[2*i+1],fenge[2*i]);
//            list_view.add(one);
//
//        }
//
//        /**listview的设置*/
//        Adp_ReadBibleMain_listView_Adp adapter = new Adp_ReadBibleMain_listView_Adp(ReadBible_Main_ac.this, R.layout.activity_readbible_main_listview, list_view);
//        final ListView listView = (ListView) findViewById(R.id.list_item);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> parent,View view,int position,long id){
//                Adp_ReadBibleMain_listView_support photos=list_view.get(position);
//                LogMy.d("点击事件",""+photos);
//            }
//        });
//        listView.setOnTouchListener(new View.OnTouchListener(){
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final float y = event.getY();
//                float translationY = mMainToolbar.getTranslationY();
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        mStartY = y;
//                        mLastY = mStartY;
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        float mDeltaY = y - mLastY;
//
//                        float newTansY = translationY + mDeltaY;
//                        if (newTansY <= 0 && newTansY >= -mMainToolbar.getHeight()) {
//                            mMainToolbar.setTranslationY(newTansY);
//                        }
//                        mLastY = y;
//                        mLastDeltaY = mDeltaY;
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        ObjectAnimator animator = null;
//                        //Log.d(TAG, "mLastDeltaY=" + mLastDeltaY);
//                        if (mLastDeltaY < -3 && listView.getFirstVisiblePosition() > 1) {
//                            //Log.v(TAG, "listView.first=" + mMainListView.getFirstVisiblePosition());
//                            animator = ObjectAnimator.ofFloat(mMainToolbar, "translationY", mMainToolbar.getTranslationY(), -mMainToolbar.getHeight());
//                        } else {
//                            animator = ObjectAnimator.ofFloat(mMainToolbar, "translationY", mMainToolbar.getTranslationY(), 0);
//                        }
//                        animator.setDuration(100);
//                        animator.start();
//                        animator.setInterpolator(AnimationUtils.loadInterpolator(ReadBible_Main_ac.this, android.R.interpolator.linear));
//                        break;
//                }
//                return false;
//            }
//        });
//    //});
//
//
//        textView.setText(s);
//
//        view.post(new Runnable() {
//            @Override
//            public void run() {
//                view.scrollTo(0,i);
//                LogMy.d("看看能不能滚动","哈哈");
//            }
//        });
//
//
//    }
//
//    /**实现左右滑动监听，以及关键词定位*/
//    private class MyGestureListener extends ScreenLeftAndRightSlide {
//        public MyGestureListener(Context context) {
//            super(context);
//        }
//
//        @Override
//        public boolean left() {
//            view.post(new Runnable() {
//                @Override
//                public void run() {
//                    view.scrollTo(0,0);
//
//                }
//            });
//
//            LogMy.d("看看能不能滚动","哈哈"+currentZhang+count);
//            if (currentZhang<count-1){
//                currentZhang++;
//            }
//
//            SpannableString s = new SpannableString(list.get(currentZhang).DingYiTextBible);
//
//            Pattern p = Pattern.compile(search);
//
//
//            Matcher m = p.matcher(s);
//            int start;
//            int end;
//            int k=1;
//
//            i=0;
//            while (m.find()) {
//                if(k==1){
//                    i=m.start();
//                    k++;
//                }
//
//                start = m.start();
//                end = m.end();
//                s.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//            textView.setText(s);
//
//            view.post(new Runnable() {
//                @Override
//                public void run() {
//                    view.scrollTo(0,i);
//                }
//            });
//
//            LogMy.d("weishenme", "向左滑");
//            return super.left();
//        }
//
//        @Override
//        public boolean right() {
//            view.post(new Runnable() {
//                @Override
//                public void run() {
//                    view.scrollTo(0,0);
//                }
//            });
//
//            if (currentZhang>0){
//                currentZhang--;
//            }
//
//            SpannableString s = new SpannableString(list.get(currentZhang).DingYiTextBible);
//
//            Pattern p = Pattern.compile(search);
//
//
//            Matcher m = p.matcher(s);
//            int start;
//            int end;
//            int k=1;
//
//            i=0;
//            while (m.find()) {
//                if(k==1){
//                    i=m.start();
//                    k++;
//                }
//
//                start = m.start();
//                end = m.end();
//                s.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//            textView.setText(s);
//
//            view.post(new Runnable() {
//                @Override
//                public void run() {
//                    view.scrollTo(0,i);
//
//                }
//            });
//
//            LogMy.d("test", "向右滑");
//            return super.right();
//        }
//    }
//
//
//}
}
