package com.example.Activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.LogMy.LogMy;
import com.example.Theme.MyThemeSet;
import com.example.ActivityAds.ActivityCollector;
import com.example.ActivityAds.MainActivity;
import com.example.Db.DbFavoritesBianLiang;
import com.example.Db.DbFavoritesCreate;
import com.example.Db.DbFavoritesService;
import com.example.Db.DbShuZiTiBianLiang;
import com.example.Db.DbShuZiTiCreate;
import com.example.Db.DbShuZiTiService;
import com.example.shujuku.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main2_ShuZiTi_dianji_ac extends MainActivity {

    //标题栏的收藏按钮
    private Button edit;
    //用来记录已经收藏的序列的编号；然后进行删除
    private int ss;
    //数据库的名称
    private String DB_NAME = "Question.db";
    //数据库的名称
    private String DB_NAME2 = "Fav.db";
    //数据库的地址
    private String DB_PATH = "/data/data/com.example.shujuku/databases/";
    //设置题量
    private int count;
    //显示当前的题目
    private int corrent;
    //当前问题ID
    private int correntID;
    //问题
    private TextView tv_que;
    //设置选项
    RadioButton[] mRadioButton = new RadioButton[4];
    //题量选择
    //private TextView text_TiLiangXuanZe;
    //不会做
    //private Button butn_XiaYiTi;
    //解析
    //private TextView tv_jiexi;
    //容器，这是什么鬼
    private RadioGroup mRadioGroup;
    //是否查看错题
    private boolean wrongView;

    private int len = 10;

    //选择题量的相关按钮
    private TextView tv_TiLiangxuanze;
    private PopupWindow popupWin;
    private View popupV;
    private TextView tv1;
    private TextView tv2;
    private TextView tv4;
    //获取位置position，确定取出那个表单的数据
    private int p;//p是卡片编号数组，from1详细记录了题目的来源,id记录来源表单中的id

    private int[] from1;
    private int[] id1;
    //转化字符串
    private String from1toS;
    private String id1toS;
    //记录原始选择，用于历史记录
    private List<Integer> selectALL;

    //这个整形list打偶数项记录了错题在题目数组list中是第几个，奇数项记录了这道题在自己表单中的ID
    //
    private List<Integer> dijige;

    //数据库服务
    private DbFavoritesCreate dbFavoritesCreate;
    private DbShuZiTiCreate dbShuZiTiCreate;
    private SQLiteDatabase dbwritet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyThemeSet.setMyTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2shuzitidianji_aclout);

        dbFavoritesCreate = new DbFavoritesCreate(this, "Fav.db", null, 2);
        dbShuZiTiCreate = new DbShuZiTiCreate(this, "Question.db", null, 3);
        // SQLiteDatabase dbwritet=dbFavoritesCreate.getWritableDatabase();
        dbwritet = dbFavoritesCreate.getWritableDatabase();

        Intent intent = getIntent();
        p = intent.getIntExtra("position", 0);

        SetT(p, 0);


        //初始化视图
        initFile();
        initFile2();
        initView();
        //初始化题目
        initPopupView();

        /**收藏按钮功能的实现*/
        edit = (Button) findViewById(R.id.title_edit1);
        //调用背景改变方法，改变按钮背景
        setBackGd();
        //监听，调用ADDFAV方法添加收藏，注意，此AddFav方法中实现了已经收藏则取消收藏的功能；
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFav(from1[corrent], correntID);
            }
        });


        //将来源和ID转化为字符串，以便存入数据库recordque中
        from1toS = Arrays.toString(from1);
        id1toS = Arrays.toString(id1);
        LogMy.d("字符串的长度", "is" + from1toS.length());

        int[] ff = new int[from1.length];
        for (int i = 0; i < from1.length; i++) {
            //LogMy.d("fffffffffffffffffffrom","f"+s.substring(i*3+1,i*3+2));
            ff[i] = Integer.parseInt(from1toS.substring(i * 3 + 1, i * 3 + 2));
        }

        LogMy.d("fffffffffffffffffffrom", "f" + Arrays.toString(ff));
    }


    //通过传入卡片编号，确定标题内容；
    public void SetT(int p, int from) {
        String t = new String();
        switch (p) {
            case 0:
                t = "数字题";
                break;
            case 1:
                t = "名称题";
                break;
            //
            case 2:
                t = "经文填空";
                break;
            case 3:
                switch (from) {
                    case 0:
                        t = "随机题(数字）";
                        break;
                    case 1:
                        t = "随机题(名称）";
                        break;
                    case 2:
                        t = "随机题(填空）";
                        break;
                }

        }
        TextView text111 = (TextView) findViewById(R.id.title_text);
        text111.setText("" + t);
    }

    private void initFile() {
        //判断数据库是否拷贝到相应的目录下
        if (new File(DB_PATH + DB_NAME).exists() == false) {
            File dir = new File(DB_PATH);
            if (!dir.exists()) {
                dir.mkdir();
            }

            //复制文件
            try {
                InputStream is = getBaseContext().getAssets().open(DB_NAME);
                OutputStream os = new FileOutputStream(DB_PATH + DB_NAME);

                //用来复制文件
                byte[] buffer = new byte[1024];
                //保存已经复制的长度
                int length;

                //开始复制
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }

                //刷新
                os.flush();
                //关闭
                os.close();
                is.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void initFile2() {
        //判断数据库是否拷贝到相应的目录下
        if (new File(DB_PATH + DB_NAME2).exists() == false) {
            File dir = new File(DB_PATH);
            if (!dir.exists()) {
                dir.mkdir();
            }

            //复制文件
            try {
                InputStream is = getBaseContext().getAssets().open(DB_NAME2);
                OutputStream os = new FileOutputStream(DB_PATH + DB_NAME2);

                //用来复制文件
                byte[] buffer = new byte[1024];
                //保存已经复制的长度
                int length;

                //开始复制
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }

                //刷新
                os.flush();
                //关闭
                os.close();
                is.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    //初始化View
    private void initView() {

        wrongView = false;

        tv_que = (TextView) findViewById(R.id.tv_que);

        mRadioButton[0] = (RadioButton) findViewById(R.id.RadioA);
        mRadioButton[1] = (RadioButton) findViewById(R.id.RadioB);
        mRadioButton[2] = (RadioButton) findViewById(R.id.RadioC);
        mRadioButton[3] = (RadioButton) findViewById(R.id.RadioD);

        //tv_jiexi = (TextView) findViewById(R.id.tv_explain);

        mRadioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);
    }

    /**
     * 题量选择功能的实现
     */
    private void initPopupView() {
        tv_TiLiangxuanze = (TextView) findViewById(R.id.text_tiliangxuanze);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        popupV = layoutInflater.inflate(R.layout.popupview, null);

        tv_TiLiangxuanze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (len == 10) {
                    len = 20;
                    tv_TiLiangxuanze.setText("1/20");
                    initDB();
                    from1toS = Arrays.toString(from1);
                    id1toS = Arrays.toString(id1);
                } else if (len == 20) {
                    len = 30;
                    tv_TiLiangxuanze.setText("1/30");
                    initDB();
                    from1toS = Arrays.toString(from1);
                    id1toS = Arrays.toString(id1);
                } else {
                    len = 10;
                    tv_TiLiangxuanze.setText("1/10");
                    initDB();
                    from1toS = Arrays.toString(from1);
                    id1toS = Arrays.toString(id1);
                }
                //popupShow(v);

            }
        });

        tv_TiLiangxuanze.setText("1/10");

        initDB();

    }

    /**
     * 取出问题
     */
    private void initDB() {
        DbShuZiTiService dbService = new DbShuZiTiService();
        final List<DbShuZiTiBianLiang> list;
        switch (p) {
            //根据卡片编号调用不同方法取出问题序列list
            case 0:
            case 1:
            case 2:
                list = dbService.getQuestion(len, p);//len是长度，p是所在表格
                break;
            case 3:
                list = dbService.getRandomQuestion(len);//因为使用了专门的Random方法，不需要再指定p
                break;
            default:
                list = dbService.getRandomQuestion(len);
                break;
        }


        //一取出来就马上记录信息
        count = list.size();
        id1 = new int[count];
        from1 = new int[count];
        for (int i = 0; i < count; i++) {

            DbShuZiTiBianLiang q = list.get(i);
            //通过From方法，把from转化成问题的来源；
            //这个时候from中的记录的就是问题序列中每个问题的表单号，只有三个值，0,1,2，其中，0为数字题，1为名称题，3为填空题
            from1[i] = From(q.DingyiSelectedAnswer);
            id1[i] = q.DingyiId;
        }


        corrent = 0;

        DbShuZiTiBianLiang q = list.get(0);

        correntID = q.DingyiId;
        tv_que.setText(q.Dingyique);

        mRadioButton[0].setText(q.DingyiA);
        mRadioButton[1].setText(q.DingyiB);
        mRadioButton[2].setText(q.DingyiC);
        mRadioButton[3].setText(q.DingyiD);

        SetT(p, from1[0]);


        //设置自动跳转下一题
        //答案选中
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < 4; i++) {
                    if (mRadioButton[i].isChecked() == true) {
                        //将用户的选择赋值给这一项
                        list.get(corrent).DingyiSelectedAnswer = i;
                        break;
                    }
                }
            }
        });
        mRadioButton[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Select(list);

            }
        });
        mRadioButton[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Select(list);

            }
        });
        mRadioButton[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Select(list);

            }
        });
        mRadioButton[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Select(list);

            }
        });

    }

    /**
     * 得到问题所属表单
     */
    public int From(int selectAnswer) {//前提：取出问题时将DingyiSelectedAnswer按照表单赋值为不同值。详见DbFAVServices.
        int from;
        switch (selectAnswer) {
            case -10:
                from = 0;
                break;
            case -11:
                from = 1;
                break;
            case -12:
                from = 2;
                break;
            default:
                from = 0;
                break;
        }
        return from;
    }

    /**
     * 设置下一题跳转，以及答题结束后续步骤
     */
    private void Select(List<DbShuZiTiBianLiang> list) {
        String rightLV;
        //判断是否为最后一题
        if (corrent < count - 1) {
            //不是最后一题则corrent加一，继续显示答题界面
            corrent++;

            DbShuZiTiBianLiang q = list.get(corrent);

            correntID = q.DingyiId;

            tv_que.setText(q.Dingyique);

            mRadioButton[0].setText(q.DingyiA);
            mRadioButton[1].setText(q.DingyiB);
            mRadioButton[2].setText(q.DingyiC);
            mRadioButton[3].setText(q.DingyiD);

            //判断是否已收藏，确定背景
            setBackGd();

            SetT(p, from1[corrent]);

            mRadioGroup.clearCheck();

            tv_TiLiangxuanze.setText((corrent + 1) + "/" + count);

            //设置选中，其实在一点击就跳转的模式下并没什么大用
            if ((q.DingyiSelectedAnswer != -1) && (q.DingyiSelectedAnswer != -10) && (q.DingyiSelectedAnswer != -11) && (q.DingyiSelectedAnswer != -12)) {
                mRadioButton[q.DingyiSelectedAnswer].setChecked(true);
            }
        } else {

            //没有题目了，开始检测正确性
            final int length = CheckAnswer2(list);//错题数目

            final List<Integer> selectList = SelectAnswer(list);//原始错误选择

            // tv_TiLiangxuanze.setText(""+count+"/"+count);
            if (length == 0) {//全部做对
                rightLV = "100%";
                new AlertDialog.Builder(Main2_ShuZiTi_dianji_ac.this).setTitle("上帝爸爸爱你！").setMessage("你好厉害，答对了所有题！")
                        .setPositiveButton("再做几道题", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intentAgain = new Intent(Main2_ShuZiTi_dianji_ac.this, Main2_ShuZiTi_dianji_ac.class);
                                intentAgain.putExtra("position", p);
                                startActivity(intentAgain);

                            }
                        }).setNegativeButton("休息一会", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCollector.finishAll();
                    }
                }).show()
                        .setCancelable(false);
            } else {
                //窗口提示
                final int pos = p;//不直接使用p是为了避免必须将p定义为final
                float d = length;//也是避免final
                float l = (1 - d / list.size()) * 100;//正确率

                DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                String p = decimalFormat.format(l);//format 返回的是字符串
                rightLV = p + "%";


                new AlertDialog.Builder(Main2_ShuZiTi_dianji_ac.this).setTitle("恭喜，答题完成！")
                        .setMessage("答对了" + (list.size() - length) + "道题," +
                                "答错了" + length + "道题" + "\n" + "正确率" + p + "%" + "\n" + "是否查看错题？").setPositiveButton("当然要看看", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intentError = new Intent(Main2_ShuZiTi_dianji_ac.this, ErrorShuZi_ac.class);
                        //将错题的数目以及卡片编号传递给错题活动
                        intentError.putExtra("length", length);
                        intentError.putExtra("pos", pos);

                        //传送来源
                        for (int j = 0; j < length; j++) {
                            intentError.putExtra("from" + j, from1[dijige.get(2 * j)]);
                        }
                        //传送错题在自己表单中的ID
                        for (int j = 0; j < length; j++) {
                            intentError.putExtra("id" + j, dijige.get(2 * j + 1));
                        }
                        //传送原始错误选择
                        for (int j = 0; j < selectList.size(); j++) {
                            intentError.putExtra("s" + j, selectList.get(j));
                        }

                        startActivity(intentError);

                    }
                }).setNegativeButton("先算了吧", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCollector.finishAll();
                    }
                }).show()
                        .setCancelable(false);
            }

            /**记录历史记录*/
            recordHistory(rightLV);

        }

    }

    /**
     * 判断错题
     */
    public int CheckAnswer2(List<DbShuZiTiBianLiang> list) {

        dijige = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            //判断对错
            if (list.get(i).DingyiRightanswer != list.get(i).DingyiSelectedAnswer) {
                //当选择的答案与正确的答案不一样时，将这道题在这次问题序列中是第几个和在自己表单的ID加进去
                dijige.add(i);
                dijige.add(list.get(i).DingyiId);

            }
        }
        //然而仅仅返回了一个长度，也就是错题个数，但这时dijige数组里已经放好了数据，可以在其他地方使用
        return dijige.size() / 2;
    }

    /**
     * 记录原始选择
     */
    public List<Integer> SelectAnswer(List<DbShuZiTiBianLiang> list) {
        List<Integer> selectList = new ArrayList<>();
        selectALL = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            //添加选择
            selectALL.add(list.get(i).DingyiSelectedAnswer);
            //判断对错
            if (list.get(i).DingyiRightanswer != list.get(i).DingyiSelectedAnswer) {
                //当选择的答案与正确的答案不一样时，将这道题回答者的选项加入，以便7在错题回顾时保留原始答题记录
                selectList.add(list.get(i).DingyiSelectedAnswer);
            }
        }
        return selectList;
    }

    /**
     * 添加收藏
     */
    public void AddFav(Integer from, Integer id) {//传入参数，来源哪张表格，以及在表格中的ID


        DbFavoritesService dbFavoritesService = new DbFavoritesService();

        List<DbFavoritesBianLiang> list = new ArrayList<>();
        list = dbFavoritesService.GetFavFromAndId();

        if (list.size() == 0) {//如果收藏表单长度为0，不用担心重复，直接添加
            ContentValues values = new ContentValues();

            //获取当前系统时间
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
            String date = sDateFormat.format(new java.util.Date());

            String T = date;

            values.put("FavId", id);
            values.put("FavTime", T);
            values.put("FavFrom", from);


            dbwritet.insert("favque", null, values);

            setBackGd();
            LogMy.d("收藏", "ghuii");

        } else {
            //否则，判断是否已存在
            Boolean isExist = isFav(from, id);

            if (!isExist) {//不存在的话，添加
                ContentValues values = new ContentValues();

                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    HH:mm:ss");//HH是24小时，hh是12小时
                String date24 = sDateFormat.format(new java.util.Date());

                String T = date24;

                values.put("FavTime", T);
                values.put("FavFrom", from);
                values.put("FavId", id);

                dbwritet.insert("favque", null, values);

                setBackGd();
                LogMy.d("收藏hgfgfgfgsg", "ghuii");

            } else {//存在，删除，即取消收藏
                dbwritet.delete("favque", "FavTime =?", new String[]{list.get(ss).DingYiFTime});
                //记得每次改变状态都要重新设置背景
                setBackGd();
            }
        }
    }

    /**
     * 是否收藏
     */
    public boolean isFav(int from, int id) {//通过来源表单和表单中ID判断；

        boolean isfav = false;
        DbFavoritesService dbFavoritesService = new DbFavoritesService();

        List<DbFavoritesBianLiang> list = new ArrayList<>();
        list = dbFavoritesService.GetFavFromAndId();

        for (int i = 0; i < list.size(); i++) {
            if ((from == list.get(i).DingYiFrom) && (id == list.get(i).DingyiFId)) {

                isfav = true;
                ss = i;
                break;
            }
        }
        return isfav;
    }

    /**
     * 按钮背景改变
     */
    private void setBackGd() {
        if (isFav(from1[corrent], correntID)) {
            edit.setBackgroundResource(R.drawable.favs);
        } else {
            edit.setBackgroundResource(R.drawable.favk);
        }
    }

    /**
     * 历史记录方法体
     */
    private void recordHistory(String rightLV) {
        int[] select = new int[selectALL.size()];
        for (int i = 0; i < selectALL.size(); i++) {
            select[i] = selectALL.get(i);
        }
        String selectALLtoS;
        selectALLtoS = Arrays.toString(select);
        //到这里，来源from1，ID1，原始选择selectALL全部已经转化为字符串from1toS，id1toS，selectALLtoS,还有一个正确率
        //准备插入数据库中吧！
        LogMy.d("fffffffffffffffffffrom", "f" + from1toS);
        LogMy.d("fffffffffffffffffffrom", "f" + id1toS);
        LogMy.d("fffffffffffffffffffrom", "f" + selectALLtoS);
        LogMy.d("fffffffffffffffffffrom", "f" + rightLV);

        ContentValues values = new ContentValues();

        //获取当前系统时间
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    HH:mm:ss");
        String date = sDateFormat.format(new java.util.Date());

        String T = date;

        values.put("RcdTime", T);
        values.put("RcdFrom", from1toS);
        values.put("RcdId", id1toS);
        values.put("RcdselectAll", selectALLtoS);
        values.put("RcdRightLV", rightLV);


        dbwritet.insert("recordque", null, values);

        //Toast.makeText(Main2_ShuZiTi_dianji_ac.this,"可执行！",Toast.LENGTH_LONG).show();

    }

    /**
     * 返回键逻辑
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            //do something...
            ActivityCollector.finishAll();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}




























