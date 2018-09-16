package com.waydrow.newloveinn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.waydrow.newloveinn.bean.ActivityAll;
import com.waydrow.newloveinn.util.API;
import com.waydrow.newloveinn.util.HttpUtil;
import com.waydrow.newloveinn.util.Utility;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.TooManyListenersException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class AinfoActivity extends AppCompatActivity {

    public static final String ACTIVITY_ID = "activity_id";

    private TextView activityInfoTextView; // 详情

    private TextView beginTimeTextView;
    private TextView endTimeTextView;
    private TextView locationTextView;
    private TextView contactTextView;
    private TextView capacityTextView;

    private ImageView activityImageView; // 活动图片

    private CollapsingToolbarLayout collapsingToolbar;

    private ProgressDialog progressDialog;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ainfo);
        // init
        beginTimeTextView = (TextView) findViewById(R.id.begintime_textview);
        endTimeTextView = (TextView) findViewById(R.id.endtime_textview);
        locationTextView = (TextView) findViewById(R.id.location_textview);
        contactTextView = (TextView) findViewById(R.id.contact_textview);
        capacityTextView = (TextView) findViewById(R.id.capacity_textview);

        activityImageView = (ImageView) findViewById(R.id.activity_image_view);
        activityInfoTextView = (TextView) findViewById(R.id.info_textview);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        // 设置toolbar
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // 返回按钮
        }

        // 获取打开的活动id
        Intent intent = getIntent();
        final String activityId = intent.getStringExtra("activity_id");
        // 获取活动详情
        getActivityInfo(activityId);

        // 报名按钮
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AinfoActivity.this);
                int isPass = prefs.getInt(API.PREF_USER_ISPASS, 0);
                if (isPass != 1) {
                    Toast.makeText(AinfoActivity.this, "您还未通过审核! 请先完善个人资料", Toast.LENGTH_SHORT).show();
                } else {
                    // 用户id
                    String userId = prefs.getString(API.PREF_USER_ID, null);
                    // 报名所需参数
                    RequestBody body = new FormBody.Builder()
                            .add("user_id", userId)
                            .add("activity_id", activityId)
                            .build();
                    String url = API.INTERFACE + "apply"; // 报名接口
                    // 发起报名请求
                    HttpUtil.sendPostRequest(url, body, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Toast.makeText(AinfoActivity.this, "报名失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String responseText = response.body().string();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (responseText.equals("0")) {
                                        Toast.makeText(AinfoActivity.this, "报名失败, 请稍后重试", Toast.LENGTH_SHORT).show();
                                    } else if (responseText.equals("-1")) {
                                        Toast.makeText(AinfoActivity.this, "您已经报过名了哦!", Toast.LENGTH_SHORT).show();
                                    } else if (responseText.equals("1")) {
                                        Snackbar.make(v, "报名成功, 请到我的活动中查看!", Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    private void getActivityInfo(String id) {
        showProgressDialog();
        String url = API.INTERFACE + "getActivityInfoById";
        RequestBody body = new FormBody.Builder()
                .add("id", id)
                .build();
        HttpUtil.sendPostRequest(url, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                closeProgressDialog();
                Toast.makeText(AinfoActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                final ActivityAll activityAll = Utility.handleActivityInfo(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(AinfoActivity.this).load(API.IMG_HEADER + activityAll.getPhoto())
                                .thumbnail(0.1f)
                                .into(activityImageView);
                        collapsingToolbar.setTitle(activityAll.getName());
                        beginTimeTextView.setText(activityAll.getBegintime());
                        endTimeTextView.setText(activityAll.getEndtime());
                        locationTextView.setText(activityAll.getLocation());
                        contactTextView.setText(activityAll.getContact());
                        capacityTextView.setText(activityAll.getCapacity());
                        activityInfoTextView.setText(activityAll.getInfo());
                        closeProgressDialog();
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
