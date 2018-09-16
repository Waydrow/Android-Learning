package com.waydrow.newloveinn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.waydrow.newloveinn.util.API;
import com.waydrow.newloveinn.util.PushUtil;

import java.io.IOException;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText ext_userName;

    private EditText ext_password;

    private static final int MSG_SET_ALIAS = 1001;

    private static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // fullscreen
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        // remove title
        getSupportActionBar().hide();

        // init
        initLogin();

        //login  view group
        ext_userName = (EditText) findViewById(R.id.usernum);
        ext_password = (EditText) findViewById(R.id.password);
        Button btn_login = (Button) findViewById(R.id.login_btn);
        //set
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = ext_userName.getText().toString();
                String password = ext_password.getText().toString();
                //if one of exts is null
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    //do login
                    loginWithOkHttp(userName, password);
                }
            }
        });

        //regist
        TextView createAccount = (TextView) findViewById(R.id.create_account);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initLogin() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString("user_id", null) != null) {
            // 存在缓存
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //login
    private void loginWithOkHttp(final String userName, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //constuctor httpclient
                    OkHttpClient client = new OkHttpClient();
                    //post data body
                    RequestBody requestBody = new FormBody.Builder()
                            .add("username", userName)
                            .add("password", password)
                            .build();

                    Request request = new Request.Builder()
                            .url(API.INTERFACE + "login")
                            .post(requestBody)
                            .build();
                    //execute request
                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    String responseData = response.body().string();
                    Log.d("loginActivity", responseData);
                    showResponse(responseData);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    // update UI
    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!response.equals("0")){
                    String userId = response.toString();
                    SharedPreferences.Editor editor = PreferenceManager.
                            getDefaultSharedPreferences(LoginActivity.this).edit();
                    editor.putString(API.PREF_USERNAME, ext_userName.getText().toString());
                    editor.putString(API.PREF_PASSWORD, ext_password.getText().toString());
                    editor.putString(API.PREF_USER_ID, userId);
                    editor.apply();

                    // 为当前用户设置别名
                    setAlias(userId);

                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 设置别名
    private void setAlias(String alias){
        //调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    JPushInterface.setAlias(getApplicationContext(), (String) msg.obj, mAliasCallback);
                    //JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;

                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    if (PushUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        Log.i(TAG, "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }

            //PushUtil.showToast(logs, getApplicationContext());
        }

    };

}
