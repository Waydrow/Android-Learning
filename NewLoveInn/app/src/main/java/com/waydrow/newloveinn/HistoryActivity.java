package com.waydrow.newloveinn;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.waydrow.newloveinn.adapter.HistoryAdapter;
import com.waydrow.newloveinn.bean.HistoryItem;
import com.waydrow.newloveinn.util.API;
import com.waydrow.newloveinn.util.HttpUtil;
import com.waydrow.newloveinn.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HistoryActivity extends AppCompatActivity {

    private static final String TAG = "HistoryActivity";

    private ProgressDialog progressDialog;

    private ListView listView;

    private HistoryAdapter historyAdapter;

    private SwipeRefreshLayout swipeRefresh;

    private List<HistoryItem> historyItemsList = new ArrayList<>();

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // 获取用户 id
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        userId = prefs.getString(API.PREF_USER_ID, null);

        // listview and adapter
        listView = (ListView) findViewById(R.id.history_list_view);
        historyAdapter = new HistoryAdapter(this, historyItemsList);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        listView.setAdapter(historyAdapter);

        queryFromServer();

        // 下拉刷新
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryFromServer();
            }
        });

        // 设置 ListView 列表项的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HistoryItem currentItem = historyItemsList.get(position);
                String isJoin = currentItem.getIsjoin();
                String text = "";
                if (isJoin.equals("-1")) {
                    text = "很遗憾, 您未通过审核";
                } else if (isJoin.equals("0")) {
                    text = "请等待审核";
                } else if (isJoin.equals("1")) {
                    text = "恭喜, 您已通过审核";
                }
                Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 从服务器获取历史活动信息
    private void queryFromServer() {
        showProgressDialog();
        String url = API.INTERFACE + "historyInfo";
        RequestBody body = new FormBody.Builder()
                .add("user_id", userId)
                .build();
        HttpUtil.sendPostRequest(url, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(HistoryActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                Log.d(TAG, "onResponse: " + responseText);
                historyItemsList.clear();
                List<HistoryItem> tempList = Utility.handleHistoryList(responseText);
                for (HistoryItem item : tempList) {
                    historyItemsList.add(item);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        if (historyItemsList == null) {
                            Toast.makeText(HistoryActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                        } else {
                            historyAdapter.notifyDataSetChanged();
                            listView.setSelection(0);
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    // 显示加载框
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(HistoryActivity.this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    // 关闭加载框
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
