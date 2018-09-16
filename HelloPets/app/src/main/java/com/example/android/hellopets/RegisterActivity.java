package com.example.android.hellopets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.hellopets.util.QueryUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText = null;
    private EditText password1EditText = null;
    private EditText password2EditText = null;
    private EditText realnameEditText = null;
    private RadioGroup sexRadioGroup = null;
    private EditText idcardEditText = null;
    private EditText phoneEditText = null;
    private EditText addressEditText = null;
    private Button registerButton = null;
    private SharedPreferences sharedPreferences=null;

    private RequestQueue mQueue=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initial();
    }

    private void initial(){
        usernameEditText = (EditText) findViewById(R.id.username);
        password1EditText=(EditText)findViewById(R.id.password1);
        password2EditText=(EditText)findViewById(R.id.password2);
        realnameEditText=(EditText)findViewById(R.id.realname);
        sexRadioGroup= (RadioGroup) findViewById(R.id.usersex);
        idcardEditText=(EditText)findViewById(R.id.idcard);
        phoneEditText=(EditText)findViewById(R.id.phone);
        addressEditText=(EditText)findViewById(R.id.address);
        registerButton=(Button)findViewById(R.id.register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regist();
            }
        });
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(RegisterActivity.this);
        mQueue= Volley.newRequestQueue(RegisterActivity.this);
    }

    private void regist(){
        final String username=usernameEditText.getText().toString();
        final String password1=password1EditText.getText().toString();
        final String password2=password2EditText.getText().toString();
        final String realname=realnameEditText.getText().toString();
        //获取选中的值
        final String usersex=((RadioButton)findViewById(sexRadioGroup.getCheckedRadioButtonId())).getText().toString();
        final String idcard=idcardEditText.getText().toString();
        final String phone=phoneEditText.getText().toString();
        final String address=addressEditText.getText().toString();

        if(username.equals("")){
            Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password1.equals("")||password2.equals("")){
            Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!password1.equals(password2)){
            Toast.makeText(RegisterActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        if(realname.equals("")){
            Toast.makeText(RegisterActivity.this, "真实姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if(idcard.equals("")){
            Toast.makeText(RegisterActivity.this, "身份证不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String regexIdCard = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
        if(!Pattern.matches(regexIdCard,idcard)){
            Toast.makeText(RegisterActivity.this, "身份证格式有误", Toast.LENGTH_SHORT).show();
            return;
        }

        if(phone.equals("")){
            Toast.makeText(RegisterActivity.this, "电话号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String regexPhone = "[0-9]{11}";
        if(!Pattern.matches(regexPhone,phone)){
            Toast.makeText(RegisterActivity.this, "手机号格式有误", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://"+ QueryUtils.ipAddress+"/Pets/Interface/index.php/Home/Index/appRegister";
        final StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if(!s.equals("0")){
                    //Log.v("userid",s);
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getString(R.string.user_id), s);
                    editor.putString(getString(R.string.username), username);
                    editor.putString(getString(R.string.password), password1);
                    editor.commit();

                    Intent intent = new Intent(RegisterActivity.this, PetsActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(RegisterActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.getMessage(), volleyError);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map=new HashMap<String,String>();
                map.put("username",username);
                map.put("password",password1);
                map.put("realname",realname);
                map.put("sex",usersex);
                map.put("idcard",idcard);
                map.put("phone",phone);
                map.put("address",address);
                return map;
            }
        };

        mQueue.add(stringRequest);
    }
}
