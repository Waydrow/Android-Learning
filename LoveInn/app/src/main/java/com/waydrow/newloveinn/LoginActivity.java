package com.waydrow.newloveinn;

import android.content.Intent;
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

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        // remove title
        getSupportActionBar().hide();
        //login  view group
        final EditText ext_userName = (EditText) findViewById(R.id.usernum);
        final EditText ext_password = (EditText) findViewById(R.id.password);
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
                            .url("http://qcloud.waydrow.com/LoveInn/index.php/Home/App/login")
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


                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);

                }
                else{
                    Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

}
