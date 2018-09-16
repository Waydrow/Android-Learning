package com.waydrow.newloveinn.push;

import android.app.Application;

import com.waydrow.newloveinn.MainActivity;
import com.waydrow.newloveinn.R;

import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Waydrow on 2016/11/29.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
