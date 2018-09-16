package com.waydrow.swiperefreshlayout;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeLayout;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        /*为listView 添加适配器*/
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getData()));


        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        /*设置下拉刷新监听器*/
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                    }
                }, 3000);    //延时3秒
            }
        });
    }

    /*预先定义listView显示的列表项*/
    private List<String> getData() {
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            data.add("Item list " + (i + 1));
        }
        return data;
    }
}
