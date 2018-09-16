package com.waydrow.newcampusbox;

import java.io.Serializable;

/**
 * Created by Waydrow on 2016/4/4.
 */
public class CourseInfo implements Serializable {

    private static final long serialVersionUID = 2074656067805712769L;
    /** id */
    private int id;

    /** 课程名称  */
    private String courseName;

    /** 上课教室 */
    private String classRoom;

    /** 老师  */
//    private String teacher;

    /** 上课时间（哪一天）（周一--周日） */
    private int day;

    /** 上课时间（哪一节）开始（1--12） */
    private int beginIndex;

    /** 上课时间（哪一节）结束（1--12） */
//    private int overIndex;

    /** 上课时间（哪一节）节数（1--12） */
    private int endIndex;

    /** 上课时间（哪一周） 开始 */
//    private int beginWeek;

    /** 上课时间（哪一周） 结束 */
//    private int endWeek;

    /** 课程类型（单周还是双周） **/
//    private int courseType;

    public static final int ALL = 1;
    public static final int ODD = 2;
    public static final int EVEN = 3;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getBeginIndex() {
        return beginIndex;
    }

    public void setBeginIndex(int beginIndex) {
        this.beginIndex = beginIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }
}
