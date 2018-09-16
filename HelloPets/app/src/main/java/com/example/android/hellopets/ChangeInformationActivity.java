package com.example.android.hellopets;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.android.hellopets.data.User;
import com.example.android.hellopets.util.QueryUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.example.android.hellopets.R.id.idcard;

public class ChangeInformationActivity extends AppCompatActivity {

    private EditText realnameEditText = null;
    private RadioGroup sexRadioGroup = null;
    private RadioButton maleRadioButton = null;
    private RadioButton femaleRadioButton = null;
    private EditText idcardEditText = null;
    private EditText phoneEditText = null;
    private EditText addressEditText = null;
    private Button changeButton = null;
    private SharedPreferences sharedPreferences = null;

    private RequestQueue mQueue = null;
    private String user_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_information);
        initial();
        getInfo();
    }

    private void initial() {
        realnameEditText = (EditText) findViewById(R.id.realname);
        sexRadioGroup = (RadioGroup) findViewById(R.id.usersex);
        maleRadioButton = (RadioButton) findViewById(R.id.male);
        femaleRadioButton = (RadioButton) findViewById(R.id.female);
        idcardEditText = (EditText) findViewById(idcard);
        phoneEditText = (EditText) findViewById(R.id.phone);
        addressEditText = (EditText) findViewById(R.id.address);
        changeButton = (Button) findViewById(R.id.change);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIfUpdateDialog();
            }
        });
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ChangeInformationActivity.this);
        mQueue = Volley.newRequestQueue(ChangeInformationActivity.this);
        user_id = sharedPreferences.getString(getString(R.string.user_id), "-1");
    }

    //make sure if update
    private void showIfUpdateDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.if_update);
        builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                change();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getInfo() {
        String url = "http://" + QueryUtils.ipAddress + "/Pets/Interface/index.php/Home/Index/appUserInfo?user_id=" + user_id;
        final StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                User user = QueryUtils.extractFeatureFromUserJson(s);
                realnameEditText.setText(user.getRealname());
                idcardEditText.setText(user.getIdcard());
                phoneEditText.setText(user.getPhone());
                addressEditText.setText(user.getAddress());
                if (user.getSex().equals("m")) {
                    maleRadioButton.setChecked(true);
                } else {
                    femaleRadioButton.setChecked(true);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.getMessage(), volleyError);
            }
        });
        mQueue.add(stringRequest);
    }

    private void change() {
        final String realname = realnameEditText.getText().toString();
        //获取选中的值
        final String usersex = ((RadioButton) findViewById(sexRadioGroup.getCheckedRadioButtonId())).getText().toString();
        final String idcard = idcardEditText.getText().toString();
        final String phone = phoneEditText.getText().toString();
        final String address = addressEditText.getText().toString();
//        Log.e("sex213", usersex);
        final String sexSend;
        if(usersex.equals("男")) {
            sexSend = "m";
        } else {
            sexSend = "f";
        }

        if (realname.equals("")) {
            Toast.makeText(ChangeInformationActivity.this, "真实姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (idcard.equals("")) {
            Toast.makeText(ChangeInformationActivity.this, "身份证不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String regexIdCard = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
        if (!Pattern.matches(regexIdCard, idcard)) {
            Toast.makeText(ChangeInformationActivity.this, "身份证格式有误", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phone.equals("")) {
            Toast.makeText(ChangeInformationActivity.this, "电话号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String regexPhone = "[0-9]{11}";
        if (!Pattern.matches(regexPhone, phone)) {
            Toast.makeText(ChangeInformationActivity.this, "手机号格式有误", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://" + QueryUtils.ipAddress + "/Pets/Interface/index.php/Home/Index/appChangeUserInfo";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.equals("1")) {
                    Toast.makeText(ChangeInformationActivity.this, "信息修改成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChangeInformationActivity.this, PetsActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ChangeInformationActivity.this, "信息修改失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.getMessage(), volleyError);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                String username = sharedPreferences.getString(getString(R.string.username), "");
                map.put("username", username);
                map.put("realname", realname);
                map.put("sex", sexSend);
                map.put("idcard", idcard);
                map.put("phone", phone);
                map.put("address", address);
                return map;
            }
        };
        mQueue.add(stringRequest);
    }
}
