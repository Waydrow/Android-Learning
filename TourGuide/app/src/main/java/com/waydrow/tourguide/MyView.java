package com.waydrow.tourguide;

/**
 * Created by Waydrow on 2016/11/19.
 */

public class MyView {

    // 景点名字
    private String mName;

    // 营业时间
    private String mTime;

    // 景点地址
    private String mAddress;

    // 景点图片
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    // 无图片标志为1
    private static final int NO_IMAGE_PROVIDED = -1;

    // 无图片Constructor
    public MyView(String mName, String mTime, String mAddress) {
        this.mName = mName;
        this.mTime = mTime;
        this.mAddress = mAddress;
    }

    // 有图片Constructor
    public MyView(String mName, String mTime, String mAddress, int mImageResourceId) {
        this.mName = mName;
        this.mTime = mTime;
        this.mAddress = mAddress;
        this.mImageResourceId = mImageResourceId;
    }

    public String getmName() {
        return mName;
    }

    public String getmTime() {
        return mTime;
    }

    public String getmAddress() {
        return mAddress;
    }

    public int getmImageResourceId() {
        return mImageResourceId;
    }

    // 是否使用了图片
    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }
}
