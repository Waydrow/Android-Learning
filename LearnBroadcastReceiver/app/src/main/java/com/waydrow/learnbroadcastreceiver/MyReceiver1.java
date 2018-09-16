package com.waydrow.learnbroadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver1 extends BroadcastReceiver {
    public MyReceiver1() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("MyReceiver1 接收到了消息");
        abortBroadcast();   //终止发送广播
    }
}
