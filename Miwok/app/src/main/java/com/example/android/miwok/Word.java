package com.example.android.miwok;

/**
 * Created by Waydrow on 2016/10/14.
 */

public class Word {

    /*Defalut translation for the word*/
    private String mDefaultTranslation;

    /* Miwok translation for the word*/
    private String mMiwokTranslation;

    /*Miwok image*/
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    /*无图片标志为1*/
    private static final int NO_IMAGE_PROVIDED = -1;

    /*音频发音文件*/
    private int mAudioResourceId;

    /*无图片引入的Constructor*/
    public Word(String mDefaultTranslation, String mMiwokTranslation, int mAudioResourceId) {
        this.mDefaultTranslation = mDefaultTranslation;
        this.mMiwokTranslation = mMiwokTranslation;
        this.mAudioResourceId = mAudioResourceId;
    }

    /*有图片引入的Construcor*/
    public Word(String mDefaultTranslation, String mMiwokTranslation, int mImageResourceId, int mAudioResourceId) {
        this.mDefaultTranslation = mDefaultTranslation;
        this.mMiwokTranslation = mMiwokTranslation;
        this.mImageResourceId = mImageResourceId;
        this.mAudioResourceId = mAudioResourceId;
    }

    public String getmDefaultTranslation() {
        return mDefaultTranslation;
    }

    public String getmMiwokTranslation() {
        return mMiwokTranslation;
    }

    public int getmImageResourceId() {
        return mImageResourceId;
    }

    public int getmAudioResourceId() {
        return mAudioResourceId;
    }

    @Override
    public String toString() {
        return "Word{" +
                "mDefaultTranslation='" + mDefaultTranslation + '\'' +
                ", mMiwokTranslation='" + mMiwokTranslation + '\'' +
                ", mImageResourceId=" + mImageResourceId +
                ", mAudioResourceId=" + mAudioResourceId +
                '}';
    }

    /*是否使用了图片*/
    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }
}
