package com.waydrow.newcampusbox;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Waydrow on 2016/4/4.
 */
@SuppressLint("ValidFragment")
public class ClassFragment extends Fragment {

    /**
     * 标题栏文字
     */
    protected TextView textTitle;
    /**
     * 第一个无内容的格子
     */
    protected TextView empty;
    /**
     * 星期一的格子
     */
    protected TextView monColum;
    /**
     * 星期二的格子
     */
    protected TextView tueColum;
    /**
     * 星期三的格子
     */
    protected TextView wedColum;
    /**
     * 星期四的格子
     */
    protected TextView thrusColum;
    /**
     * 星期五的格子
     */
    protected TextView friColum;
    /**
     * 星期六的格子
     */
    protected TextView satColum;
    /**
     * 星期日的格子
     */
    protected TextView sunColum;
    /**
     * 课程表body部分布局
     */
    protected RelativeLayout course_table_layout;
    /** 选择周数弹出窗口 */
//    protected PopupWindow weekListWindow;
    /** 显示周数的listview*/
//    protected ListView weekListView;
    /** 选择周数弹出窗口的layout */
//    protected View popupWindowLayout;
    /**
     * 课程信息
     **/
    protected JSONArray classJsonArray;
    protected Map<Integer, List<CourseInfo>> courseInfoMap = new HashMap<Integer, List<CourseInfo>>();
    /**
     * 保存显示课程信息的TextView
     **/
    protected List<TextView> courseTextViewList = new ArrayList<TextView>();
    /**
     * 保存每个textview对应的课程信息 map,key为哪一天（如星期一则key为1）
     **/
    protected Map<Integer, List<CourseInfo>> textviewCourseInfoMap = new HashMap<Integer, List<CourseInfo>>();
    /**
     * 课程格子平均宽度
     **/
    protected int aveWidth;
    /**
     * 屏幕宽度
     **/
    protected int screenWidth;
    /**
     * 格子高度
     **/
    protected int gridHeight = 80;
    /**
     * 最大课程节数
     **/
    protected int maxCourseNum = 12;

//    protected Button goBackButton;

    protected ProgressDialog pDialog;

    public ClassFragment(JSONArray jsonArray) {
        this.classJsonArray = jsonArray;    //通过构造函数传入课表信息
        int classLength = classJsonArray.length();
        int dayTemp = 1;
        List<CourseInfo> courseInfoList = new ArrayList<>();
        /*遍历json数据, 并将数据整理存入map中*/
        for(int i = 0; i<classLength; i++) {
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                int dayCurrent = jsonObject.getInt("id");

                CourseInfo courseInfos = new CourseInfo();
                courseInfos.setDay(dayCurrent);
                courseInfos.setId(jsonObject.getInt("idOnly"));
                courseInfos.setCourseName(jsonObject.getString("subject"));
                courseInfos.setClassRoom(jsonObject.getString("location"));
                courseInfos.setBeginIndex(jsonObject.getInt("beginIndex"));
                courseInfos.setEndIndex(jsonObject.getInt("overIndex"));

                /*将每天上的课放入一个list中*/
                if(dayTemp == dayCurrent) {
                    courseInfoList.add(courseInfos);
                    System.out.println("courseInfoList: "+ courseInfoList.toString());
                } else {
                    /*将每天上的课put到一个map中*/
                    courseInfoMap.put(dayTemp, courseInfoList);
                    //System.out.println("courseInfoMaP: " + courseInfoMap.toString());
                    dayTemp = dayCurrent;
                    courseInfoList = null;  //将courseInfoList指向null
                    courseInfoList = new ArrayList<>(); //重新分配一个ArrayList
                    courseInfoList.add(courseInfos);
                }

                /*最后将最后一天的课放入map*/
                if(i==classLength-1) {
                    courseInfoMap.put(dayTemp, courseInfoList);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.classinfo, container, false);


        //获得列头的控件  
        empty = (TextView) view.findViewById(R.id.test_empty);
        monColum = (TextView) view.findViewById(R.id.test_monday_course);
        tueColum = (TextView) view.findViewById(R.id.test_tuesday_course);
        wedColum = (TextView) view.findViewById(R.id.test_wednesday_course);
        thrusColum = (TextView) view.findViewById(R.id.test_thursday_course);
        friColum = (TextView) view.findViewById(R.id.test_friday_course);
        satColum = (TextView) view.findViewById(R.id.test_saturday_course);
        sunColum = (TextView) view.findViewById(R.id.test_sunday_course);

        // 列表布局文件
        course_table_layout = (RelativeLayout) view.findViewById(R.id.test_course_rl);
        DisplayMetrics dm = getResources().getDisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //屏幕宽度
        int width = dm.widthPixels;

        //平均宽度
        int aveWidth = width / 8;
        //给列头设置宽度
        empty.setWidth(aveWidth * 3 / 4);
        monColum.setWidth(aveWidth * 33 / 32 + 1);
        tueColum.setWidth(aveWidth * 33 / 32 + 1);
        wedColum.setWidth(aveWidth * 33 / 32 + 1);
        thrusColum.setWidth(aveWidth * 33 / 32 + 1);
        friColum.setWidth(aveWidth * 33 / 32 + 1);
        satColum.setWidth(aveWidth * 33 / 32 + 1);
        sunColum.setWidth(aveWidth * 33 / 32 + 1);
        this.screenWidth = width;
        this.aveWidth = aveWidth;
        //初始化body部分
        init();

        return view;
    }

    private void init() {

        DisplayMetrics dm = getResources().getDisplayMetrics();
        //屏幕高度
        int height = dm.heightPixels;
        gridHeight = height / maxCourseNum;
        //设置课表界面
        //动态生成12 * maxCourseNum个textview
        for (int i = 1; i <= maxCourseNum; i++) {

            for (int j = 1; j <= 8; j++) {

                TextView tx = new TextView(getActivity());
                tx.setId((i - 1) * 8 + j);
                //除了最后一列，都使用course_text_view_bg背景（最后一列没有右边框）
                if (j < 8)
                    tx.setBackgroundDrawable(getActivity().
                            getResources().getDrawable(R.drawable.course_text_view_bg));
                else
                    tx.setBackgroundDrawable(getActivity().
                            getResources().getDrawable(R.drawable.course_table_last_colum));
                //相对布局参数
                tx.getBackground().setAlpha(200);
                RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
                        aveWidth * 33 / 32 + 1,
                        gridHeight);
                //文字对齐方式
                tx.setGravity(Gravity.CENTER);
                //字体样式
                tx.setTextAppearance(getContext(), R.style.courseTableText);
                //如果是第一列，需要设置课的序号（1 到 12）
                if (j == 1) {
                    tx.setText(String.valueOf(i));
                    rp.width = aveWidth * 3 / 4;
                    //设置他们的相对位置
                    if (i == 1)
                        rp.addRule(RelativeLayout.BELOW, empty.getId());
                    else
                        rp.addRule(RelativeLayout.BELOW, (i - 1) * 8);
                } else {
                    rp.addRule(RelativeLayout.RIGHT_OF, (i - 1) * 8 + j - 1);
                    rp.addRule(RelativeLayout.ALIGN_TOP, (i - 1) * 8 + j - 1);
                    tx.setText("");
                }

                tx.setLayoutParams(rp);
                course_table_layout.addView(tx);
            }
        }

        pDialog = new ProgressDialog(getContext());
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setMessage("正在加载信息。。。");
        pDialog.setIndeterminate(true);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(false);
        pDialog.show();

        if(classJsonArray==null) {
            Toast.makeText(getContext(), "课程表获取失败!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(getContext(), "课程表获取成功!", Toast.LENGTH_SHORT).show();
        }
        //种颜色的背景
        int[] background = {R.drawable.course_info_blue, R.drawable.course_info_green,
                R.drawable.course_info_red, R.drawable.course_info_red,
                R.drawable.course_info_yellow, R.drawable.course_info_purple};
        final Activity mActivity = getActivity();
        pDialog.dismiss();
        for(Map.Entry<Integer, List<CourseInfo>> entry: courseInfoMap.entrySet())
        {

            //查找出最顶层的课程信息（顶层课程信息即显示在最上层的课程，最顶层的课程信息满足两个条件 1、当前周数在该课程的周数范围内 2、该课程的节数跨度最大
            CourseInfo upperCourse = null;
            //list里保存的是一周内某 一天的课程
            final List<CourseInfo> list = new ArrayList<CourseInfo>(entry.getValue());
            //
            //按开始的时间（哪一节）进行排序
            Collections.sort(list, new Comparator<CourseInfo>() {
                @Override
                public int compare(CourseInfo arg0, CourseInfo arg1) {

                    if (arg0.getBeginIndex() < arg1.getBeginIndex())
                        return -1;
                    else
                        return 1;
                }

            });
            int lastListSize;

            do {

                lastListSize = list.size();
                Iterator<CourseInfo> iter = list.iterator();
//                upperCourse = iter.next();
                //先查找出第一个在周数范围内的课
                while(iter.hasNext())
                {
                    CourseInfo c = iter.next();
                    if(c.getEndIndex() <= maxCourseNum)
                    {
                        //判断是单周还是双周的课
                        /*if(c.getCourseType() == CourseInfo.ALL ||
                                (c.getCourseType() == CourseInfo.EVEN && currentWeek % 2 == 0) ||
                                (c.getCourseType() == CourseInfo.ODD && currentWeek % 2 != 0) )*/
                        if(true) {
                            //从list中移除该项，并设置这节课为顶层课
                            iter.remove();
                            upperCourse = c;
                            break;
                        }
                    }
                }
                System.out.println("测试: "+upperCourse.getCourseName());
                if(upperCourse != null)
                {
                    List<CourseInfo> courseInfoList = new ArrayList<CourseInfo>();
                    courseInfoList.add(upperCourse);
                    int index = 0;
                    iter = list.iterator();
                    //查找这一天有哪些课与刚刚查找出来的顶层课相交
                    while(iter.hasNext())
                    {
                        CourseInfo c = iter.next();
                        //先判断该课程与upperCourse是否相交，如果相交加入courseInfoList中
                        if((c.getBeginIndex() <= upperCourse.getBeginIndex()
                                &&upperCourse.getBeginIndex() < c.getEndIndex())
                                ||(upperCourse.getBeginIndex() <= c.getBeginIndex()
                                && c.getBeginIndex() < upperCourse.getEndIndex()))
                        {
                            courseInfoList.add(c);
                            iter.remove();
                            //在判断哪个跨度大，跨度大的为顶层课程信息
                            /*if((c.getEndIndex() - c.getBeginIndex()) > (upperCourse.getEndIndex() - upperCourse.getBeginIndex())
                                    && ((c.getBeginWeek() <= currentWeek && c.getEndWeek() >= currentWeek) || currentWeek == -1))*/
                            if((c.getEndIndex() - c.getBeginIndex()) > (upperCourse.getEndIndex() - upperCourse.getBeginIndex()))
                            {
                                upperCourse = c;
                                index ++;
                            }

                        }

                    }
                    //记录顶层课程在courseInfoList中的索引位置
                    final int upperCourseIndex = index;
                    // 动态生成课程信息TextView
                    TextView courseInfo = new TextView(mActivity);
                    courseInfo.setId(1000 + upperCourse.getDay() * 100 + upperCourse.getBeginIndex() * 10 + upperCourse.getId());
                    int id = courseInfo.getId();
                    textviewCourseInfoMap.put(id, courseInfoList);
                    //如果上课教室为空，则不显示
                    if(!upperCourse.getClassRoom().equals("")) {
                        courseInfo.setText(upperCourse.getCourseName() + "\n@" + upperCourse.getClassRoom());
                    } else {
                        courseInfo.setText(upperCourse.getCourseName());
                    }
                    //该textview的高度根据其节数的跨度来设置
                    RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                            aveWidth * 31 / 32,
                            (gridHeight - 5) * 2 + (upperCourse.getEndIndex() - upperCourse.getBeginIndex() - 1) * gridHeight);
                    //textview的位置由课程开始节数和上课的时间（day of week）确定
                    rlp.topMargin = 5 + (upperCourse.getBeginIndex() - 1) * gridHeight;
                    rlp.leftMargin = 2;
                    // 前面生成格子时的ID就是根据Day来设置的
                    rlp.addRule(RelativeLayout.RIGHT_OF, upperCourse.getDay());
                    //字体居中中
                    courseInfo.setGravity(Gravity.CENTER);
                    //选择一个颜色背景
                    int colorIndex = ((upperCourse.getBeginIndex() - 1) * 8 + upperCourse.getDay()) % (background.length - 1);
                    courseInfo.setBackgroundResource(background[colorIndex]);
                    courseInfo.setTextSize(12);
                    courseInfo.setLayoutParams(rlp);
                    courseInfo.setTextColor(Color.WHITE);
                    //设置不透明度
                    courseInfo.getBackground().setAlpha(200);
                    // 设置监听事件
                    courseInfo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            Log.i("text_view", String.valueOf(arg0.getId()));
                            Map<Integer, List<CourseInfo>> map = textviewCourseInfoMap;
                            final List<CourseInfo> tempList = map.get(arg0.getId());
                            if (tempList.size() > 1) {
                                //如果有多个课程，则设置点击弹出gallery 3d 对话框
                                LayoutInflater layoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View galleryView = layoutInflater.inflate(R.layout.course_info_gallery_layout, null);
                                final Dialog coursePopupDialog = new AlertDialog.Builder(mActivity).create();
                                coursePopupDialog.setCanceledOnTouchOutside(true);
                                coursePopupDialog.setCancelable(true);
                                coursePopupDialog.show();
                                WindowManager.LayoutParams params = coursePopupDialog.getWindow().getAttributes();
                                params.width = ViewGroup.LayoutParams.FILL_PARENT;
                                coursePopupDialog.getWindow().setAttributes(params);
                                CourseInfoAdapter adapter = new CourseInfoAdapter(mActivity, tempList, screenWidth, 1);
                                CourseInfoGallery gallery = (CourseInfoGallery) galleryView.findViewById(R.id.course_info_gallery);
                                gallery.setSpacing(10);
                                gallery.setAdapter(adapter);
                                gallery.setSelection(upperCourseIndex);
                                /*gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(
                                            AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                                        CourseInfo courseInfo = tempList.get(arg2);
                                        Intent intent = new Intent();
                                        Bundle mBundle = new Bundle();
                                        mBundle.putSerializable("courseInfo", courseInfo);
                                        intent.putExtras(mBundle);
                                        intent.setClass(mActivity, DetailCourseInfoActivity.class);
                                        mActivity.startActivity(intent);
                                        coursePopupDialog.dismiss();
                                    }
                                });*/
                                coursePopupDialog.setContentView(galleryView);
                            }
                            /*else
                            {
                                Intent intent = new Intent();
                                Bundle mBundle = new Bundle();
                                mBundle.putSerializable("courseInfo", tempList.get(0));
                                intent.putExtras(mBundle);
                                intent.setClass(mActivity, DetailCourseInfoActivity.class);
                                mActivity.startActivity(intent);
                                return;
                            }*/
                        }

                    });
                    course_table_layout.addView(courseInfo);
                    courseTextViewList.add(courseInfo);
                    upperCourse = null;
                }
            } while(list.size() < lastListSize && list.size() != 0);
        }

    }
}
