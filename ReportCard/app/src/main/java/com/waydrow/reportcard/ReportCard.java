package com.waydrow.reportcard;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Waydrow on 2016/11/14.
 */

/*ReportCard成绩单类*/

public class ReportCard {

    /*学生姓名*/
    private String mName;

    /*学生年龄*/
    private int mAge;

    /*学年*/
    private String mTime;

    /*成绩*/
    private Map<String, Float> mGrades;

    /*构造函数*/
    public ReportCard(String mName, int mAge, String mTime, Map<String, Float> mGrades) {
        this.mName = mName;
        this.mAge = mAge;
        this.mTime = mTime;
        this.mGrades = mGrades;
    }

    /*通过课程名得到某门课的成绩*/
    public float getOneGrade(String key) {
        return mGrades.get(key);
    }

    /*通过课程名设置某门课的成绩*/
    public void setOneGrade(String key, float grade) {
        mGrades.put(key, grade);
    }

    /*getter setter方法*/
    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmAge() {
        return mAge;
    }

    public void setmAge(int mAge) {
        this.mAge = mAge;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public Map<String, Float> getmGrades() {
        return mGrades;
    }

    public void setmGrades(Map<String, Float> mGrades) {
        this.mGrades = mGrades;
    }

    @Override
    public String toString() {
        String str = "Name: '" + mName + "\n" +
                "Age: " + mAge + "\n" +
                "Time: '" + mTime + "\n" +
                "Grades: {" + "\n";

        /*遍历成绩map*/
        Iterator<Map.Entry<String, Float>> entries = mGrades.entrySet().iterator();
        while(entries.hasNext()) {
            Map.Entry<String, Float> entry = entries.next();

            str += entry.getKey() + ": " + entry.getValue() + "\n";
        }
        str += "}";
        return str;
    }
}
