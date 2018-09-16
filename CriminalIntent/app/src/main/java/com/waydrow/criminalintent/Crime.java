package com.waydrow.criminalintent;

import java.util.UUID;

/**
 * Created by slomo on 2016/2/15.
 */
public class Crime {

    private UUID mId;
    private String mTitle;

    public Crime(){
        mId = UUID.randomUUID();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String Title) {
        this.mTitle = Title;
    }

    public UUID getId() {
        return mId;
    }

}
