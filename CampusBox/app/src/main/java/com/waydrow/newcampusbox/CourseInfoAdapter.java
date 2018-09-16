package com.waydrow.newcampusbox;

import java.util.List;

//import nd.leiyi.crims.R;
//import nd.leiyi.crims.gallery3D.CourseInfoGallery;
//import nd.leiyi.crims.model.CourseInfo;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Waydrow on 2016/4/4.
 */
public class CourseInfoAdapter extends BaseAdapter {

    private Context context;
    private TextView[] courseTextViewList;
    private int screenWidth;
    private int currentWeek;
    public CourseInfoAdapter(Context context, List<CourseInfo> courseList, int width, int currentWeek) {
        super();
        this.screenWidth = width;
        this.context = context;
        this.currentWeek = currentWeek;
        createGalleryWithCourseList(courseList);
    }

    private void createGalleryWithCourseList(List<CourseInfo> courseList){
        //五种颜色的背景
        int[] background = {R.drawable.course_info_blue, R.drawable.course_info_green,
                R.drawable.course_info_red, R.drawable.course_info_red,
                R.drawable.course_info_yellow};
        this.courseTextViewList = new TextView[courseList.size()];
        for(int i = 0; i < courseList.size(); i ++) {
            final CourseInfo course = courseList.get(i);
            TextView textView = new TextView(context);
            textView.setText(course.getCourseName() + "@" + course.getClassRoom());
            textView.setLayoutParams(new CourseInfoGallery.LayoutParams((screenWidth / 6) *3, (screenWidth / 6) *3));
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setPadding(10, 0, 0, 0);
            /*if(course.getBeginWeek() <= currentWeek && course.getEndWeek() >= currentWeek &&
                    (course.getCourseType() == CourseInfo.ALL ||
                            (course.getCourseType() == CourseInfo.EVEN && currentWeek % 2 == 0) ||
                            (course.getCourseType() == CourseInfo.ODD && currentWeek % 2 != 0)))
            {
                //选择一个颜色背景
                int colorIndex = ((course.getBeginIndex() - 1) * 8 + course.getDay()) % (background.length - 1);
                textView.setBackgroundResource(background[colorIndex]);
            } else
            {
                textView.setBackgroundResource(R.drawable.course_info_light_grey);
            }*/
            //选择一个颜色背景
            int colorIndex = ((course.getBeginIndex() - 1) * 8 + course.getDay()) % (background.length - 1);
            textView.setBackgroundResource(background[colorIndex]);

            textView.getBackground().setAlpha(222);
//          textView.setOnClickListener(new OnClickListener() {
//              @Override
//              public void onClick(View arg0) {
//                  // TODO Auto-generated method stub
//                  Intent intent = new Intent();
//                  Bundle mBundle = new Bundle();
//                  mBundle.putSerializable("courseInfo", course);
//                  intent.putExtras(mBundle);
//                  intent.setClass(context, DetailCourseInfoActivity.class);
//                  context.startActivity(intent);
//              }
//          });
            this.courseTextViewList[i] = textView;
        }
    }
    @Override
    public int getCount() {

        return courseTextViewList.length;
    }

    @Override
    public Object getItem(int index) {

        return courseTextViewList[index];
    }

    @Override
    public long getItemId(int arg0) {

        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return courseTextViewList[position];
    }

    public float getScale(boolean focused, int offset) {
        return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
    }



}