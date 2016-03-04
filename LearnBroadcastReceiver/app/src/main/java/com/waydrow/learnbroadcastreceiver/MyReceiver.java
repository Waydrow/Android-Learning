package com.waydrow.learnbroadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {

    public static final String ACTION = "com.waydrow.learnbroadcastreceiver.intent.action.MyReceiver";

    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("MyReceiver 接收到了消息");
    }
}
