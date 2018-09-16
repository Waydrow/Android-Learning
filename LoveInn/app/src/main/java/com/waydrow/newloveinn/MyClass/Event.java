package com.waydrow.newloveinn.MyClass;

/**
 * Created by Waydrow on 2016/11/27.
 */

// 公益活动类
public class Event {

    // ID
    private int mId;

    // 活动标题
    private String mTitle;

    // 活动时间
    private String mTime;

    // 活动地点
    private String mLocation;

    // 活动简介
    private String mDesc;

    // 组织机构
    private String mMonitor;

    // 联系方式
    private String mContact;

    // 活动详情
    private String mInfo;

    // 活动规模, 需求人数
    private int mNeedPeople;

    // 已报名人数
    private int mJoiner;

    // 活动图片
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    // 无图片标志 -1
    private static final int NO_IMAGE_PROVIDED = -1;


    // 简要信息Constructor
    public Event(int mId, String mTitle, String mTime, String mDesc, int mImageResourceId) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mTime = mTime;
        this.mDesc = mDesc;
        this.mImageResourceId = mImageResourceId;
    }

    // 无图片的详情 Constructor
    public Event(int mId, String mTitle, String mTime, String mLocation, String mDesc,
                 String mMonitor, String mContact, String mInfo, int mNeedPeople, int mJoiner) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mTime = mTime;
        this.mLocation = mLocation;
        this.mDesc = mDesc;
        this.mMonitor = mMonitor;
        this.mContact = mContact;
        this.mInfo = mInfo;
        this.mNeedPeople = mNeedPeople;
        this.mJoiner = mJoiner;
    }

    // 有图片的详情 Constructor
    public Event(int mId, String mTitle, String mTime, String mLocation, String mDesc,
                 String mMonitor, String mContact, String mInfo, int mNeedPeople, int mJoiner, int mImageResourceId) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mTime = mTime;
        this.mLocation = mLocation;
        this.mDesc = mDesc;
        this.mMonitor = mMonitor;
        this.mContact = mContact;
        this.mInfo = mInfo;
        this.mNeedPeople = mNeedPeople;
        this.mJoiner = mJoiner;
        this.mImageResourceId = mImageResourceId;
    }

    // Getter methods
    public int getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmTime() {
        return mTime;
    }

    public String getmLocation() {
        return mLocation;
    }

    public String getmDesc() {
        return mDesc;
    }

    public String getmMonitor() {
        return mMonitor;
    }

    public String getmContact() {
        return mContact;
    }

    public String getmInfo() {
        return mInfo;
    }

    public int getmNeedPeople() {
        return mNeedPeople;
    }

    public int getmJoiner() {
        return mJoiner;
    }

    public int getmImageResourceId() {
        return mImageResourceId;
    }

    // Setter methods
    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public void setmMonitor(String mMonitor) {
        this.mMonitor = mMonitor;
    }

    public void setmContact(String mContact) {
        this.mContact = mContact;
    }

    public void setmInfo(String mInfo) {
        this.mInfo = mInfo;
    }

    public void setmNeedPeople(int mNeedPeople) {
        this.mNeedPeople = mNeedPeople;
    }

    public void setmJoiner(int mJoiner) {
        this.mJoiner = mJoiner;
    }

    // 判断是否使用了图片
    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }
}
