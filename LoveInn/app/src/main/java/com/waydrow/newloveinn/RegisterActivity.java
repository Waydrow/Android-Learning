package com.waydrow.newloveinn;

import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        // remove title
        getSupportActionBar().hide();

        TextView loginHere = (TextView) findViewById(R.id.login_here);
        loginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        //register view group
        final EditText ext_userName= (EditText) findViewById(R.id.usernum);
        final EditText ext_password= (EditText) findViewById(R.id.password);
        Button btn_register= (Button) findViewById(R.id.signup1);

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

    private  void registerWithOkHttp(final String userName, final String password){
        Log.d("RegisterActicity",userName+"\n"+password);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client=new OkHttpClient();

                    RequestBody requestBody=new FormBody.Builder()
                            .add("username",userName)
                            .add("password",password)
                            .build();
                    Request request=new Request.Builder()
                            .url("http://qcloud.waydrow.com/LoveInn/index.php/Home/App/register")
                            .post(requestBody)
                            .build();

                    Response response=client.newCall(request).execute();

                    if(!response.isSuccessful())  throw new IOException("Unexpected code " + response);

                    String responseData = response.body().string();

                    showResponse(responseData);
                }catch (Exception e){
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


                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);

                }
                else{
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
