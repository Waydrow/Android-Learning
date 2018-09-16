package com.waydrow.newloveinn.MyClass;

/**
 * Created by Waydrow on 2016/11/27.
 */

public class Organization {

    // 机构名称
    private String mName;

    // 机构简介
    private String mDesc;

    // 机构地点
    private String mLocation;

    // 机构图片
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    // 无图片标志
    private static final int NO_IMAGE_PROVIDED = -1;

    // 无图片 Constructor
    public Organization(String mName, String mDesc, String mLocation) {
        this.mName = mName;
        this.mDesc = mDesc;
        this.mLocation = mLocation;
    }

    // 有图片 Constructor
    public Organization(String mName, String mDesc, String mLocation, int mImageResourceId) {
        this.mName = mName;
        this.mDesc = mDesc;
        this.mLocation = mLocation;
        this.mImageResourceId = mImageResourceId;
    }

    // Getter methods
    public String getmName() {
        return mName;
    }

    public String getmDesc() {
        return mDesc;
    }

    public String getmLocation() {
        return mLocation;
    }

    public int getmImageResourceId() {
        return mImageResourceId;
    }

    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }

}
