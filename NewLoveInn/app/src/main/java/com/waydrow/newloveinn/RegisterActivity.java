package com.waydrow.newloveinn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.waydrow.newloveinn.util.API;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText ext_userName;
    private EditText ext_password;

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
        setContentView(R.layout.activity_register);
        // remove title
        getSupportActionBar().hide();

        TextView loginHere = (TextView) findViewById(R.id.login_here);
        loginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //register view group
        ext_userName = (EditText) findViewById(R.id.usernum);
        ext_password = (EditText) findViewById(R.id.password);
        Button btn_register = (Button) findViewById(R.id.signup1);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = ext_userName.getText().toString();
                String password = ext_password.getText().toString();
                //if one of exts is null
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    //do register
                    registerWithOkHttp(userName, password);
                }
            }
        });
    }

    private void registerWithOkHttp(final String userName, final String password) {
        Log.d("RegisterActicity", userName + "\n" + password);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    RequestBody requestBody = new FormBody.Builder()
                            .add("username", userName)
                            .add("password", password)
                            .build();
                    Request request = new Request.Builder()
                            .url(API.INTERFACE + "register")
                            .post(requestBody)
                            .build();

                    Response response = client.newCall(request).execute();

                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    String responseData = response.body().string();

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
                if (!response.equals("0")) {
                    // 清空缓存
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(RegisterActivity.this).edit();
                    editor.putString(API.PREF_USER_ID, null);
                    editor.putString(API.PREF_USERNAME, null);
                    editor.putString(API.PREF_PASSWORD, null);
                    editor.apply();
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
