package com.waydrow.habittracker.data;

import java.io.Serializable;

/**
 * Created by Waydrow on 2016/11/18.
 */

/* Habit Class*/
public class Habit implements Serializable {

    private int _id;

    // title of habit
    private String mTitle;

    // location of habit
    private String mLocation;

    // times of habit in a week
    private int mTimes;

    // Constructor
    public Habit(int id, String mTitle, String mLocation, int mTimes) {
        this._id = id;
        this.mTitle = mTitle;
        this.mLocation = mLocation;
        this.mTimes = mTimes;
    }

    // getter methods
    public String getTitle() {
        return mTitle;
    }

    public String getLocation() {
        return mLocation;
    }

    public int getTimes() {
        return mTimes;
    }

    public int getId() {
        return _id;
    }
}
