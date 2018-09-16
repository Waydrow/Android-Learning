package com.waydrow.newloveinn.bean;

/**
 * Created by Waydrow on 2017/5/16.
 */

public class Subscribe {

    private String id;

    private String name;

    private String isSub; // 是否被该用户订阅

    public Subscribe(String id, String name, String isSub) {
        this.id = id;
        this.name = name;
        this.isSub = isSub;
    }

    public String getIsSub() {
        return isSub;
    }

    public void setIsSub(String isSub) {
        this.isSub = isSub;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
