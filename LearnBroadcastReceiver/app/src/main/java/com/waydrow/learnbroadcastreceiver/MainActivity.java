package com.waydrow.learnbroadcastreceiver;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnSendMsg).setOnClickListener(this);
        findViewById(R.id.btnReg).setOnClickListener(this);
        findViewById(R.id.btnUnreg).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSendMsg:
//                Intent i = new Intent(this,MyReceiver.class);
                Intent i = new Intent(MyReceiver.ACTION);
                i.putExtra("data","waydrow");
//                sendBroadcast(i);
                sendOrderedBroadcast(i,null);   //当传入优先级较高的BroadcastReceiver后即终止发送广播，应使用此方法
                break;
            case R.id.btnReg:
                if(receiver==null){
                    receiver = new MyReceiver();
                    registerReceiver(receiver,new IntentFilter(MyReceiver.ACTION));
                }
                break;
            case R.id.btnUnreg:
                if(receiver!=null){
                    unregisterReceiver(receiver);
                    receiver = null;
                }
                break;
        }
    }

    private MyReceiver receiver = null;
}
