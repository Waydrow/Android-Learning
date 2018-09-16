package com.waydrow.newloveinn;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.waydrow.newloveinn.adapter.SubscribeAdapter;
import com.waydrow.newloveinn.bean.DividerItemDecoration;
import com.waydrow.newloveinn.bean.ExchangeItem;
import com.waydrow.newloveinn.bean.Subscribe;
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

public class SubscribeActivity extends AppCompatActivity {

    private static final String TAG = "SubscribeActivity";
    // 用户 id
    private String userId;

    private ProgressDialog progressDialog;

    private List<Subscribe> subscribeList = new ArrayList<>();

    private SubscribeAdapter subscribeAdapter;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // 获取用户 id
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        userId = prefs.getString(API.PREF_USER_ID, null);

        recyclerView = (RecyclerView) findViewById(R.id.sub_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        subscribeAdapter = new SubscribeAdapter(subscribeList);
        recyclerView.setAdapter(subscribeAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));

        // 加载数据
        queryFromServer();

        // item click listener
        subscribeAdapter.setOnItemClickListener(new SubscribeAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Subscribe subscribe = subscribeList.get(position);
                String isSub = subscribe.getIsSub();
                if (isSub.equals("0")) {
                    // 订阅
                    userSubscribeOrCancel(0, subscribe.getId(), position);

                } else if (isSub.equals("1")) {
                    // 取消订阅
                    userSubscribeOrCancel(1, subscribe.getId(), position);
                }
            }
        });
    }

    // 加载订阅种类列表
    private void queryFromServer() {
        showProgressDialog();
        String url = API.INTERFACE + "getCategoryList";
        RequestBody abody = new FormBody.Builder()
                .add("userid", userId)
                .build();
        HttpUtil.sendPostRequest(url, abody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(SubscribeActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                Log.d(TAG, "onResponse: " + responseText);
                subscribeList.clear();
                List<Subscribe> tmpList = Utility.handleSubscribeList(responseText);
                for (Subscribe item : tmpList) {
                    Log.d(TAG, "id: " + item.getId());
                    subscribeList.add(item);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        if (subscribeList == null) {
                            Toast.makeText(SubscribeActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                        } else {
                            subscribeAdapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(0);
                        }
                    }
                });
            }
        });
    }

    private void userSubscribeOrCancel(final int flag, String categoryId, final int position) {
        String url = API.INTERFACE;
        if (flag == 1) {
            // 取消订阅
            url += "userCancelSubscribe";
        } else {
            url += "userSubscribe";
        }
        RequestBody body = new FormBody.Builder()
                .add("userid", userId)
                .add("category_id", categoryId)
                .build();

        HttpUtil.sendPostRequest(url, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SubscribeActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseText.equals("1")) {
                            if (flag == 1) {
                                // 取消
                                subscribeList.get(position).setIsSub("0");
                            } else {
                                subscribeList.get(position).setIsSub("1");
                            }
                            subscribeAdapter.notifyDataSetChanged();

                        } else if (responseText.equals("0")) {
                            Toast.makeText(SubscribeActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

    // 显示加载框
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
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
