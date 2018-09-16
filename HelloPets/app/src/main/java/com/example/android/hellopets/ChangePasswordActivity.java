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
import android.widget.TextView;
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

public class ChangePasswordActivity extends AppCompatActivity {

    TextView oldpasswordTextView = null;
    TextView newpassword1TextView = null;
    TextView newpassword2TextView = null;
    private Button changepasswordButton = null;

    private SharedPreferences sharedPreferences = null;
    private RequestQueue mQueue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initial();
    }

    private void initial() {
        oldpasswordTextView = (TextView) findViewById(R.id.old_password);
        newpassword1TextView = (TextView) findViewById(R.id.new_password1);
        newpassword2TextView = (TextView) findViewById(R.id.new_password2);

        changepasswordButton = (Button) findViewById(R.id.change_password);
        changepasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIfUpdateDialog();
            }
        });
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ChangePasswordActivity.this);
        mQueue = Volley.newRequestQueue(ChangePasswordActivity.this);
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
                changePassword();
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

    private void changePassword() {
        final String username = sharedPreferences.getString(getString(R.string.username), "");
        final String oldPassword = oldpasswordTextView.getText().toString();
        final String newPassword1 = newpassword1TextView.getText().toString();
        String newPassword2 = newpassword2TextView.getText().toString();

        if (oldPassword.equals("") || newPassword1.equals("") || newPassword2.equals("")) {
            Toast.makeText(ChangePasswordActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword1.equals(newPassword2)) {
            Toast.makeText(ChangePasswordActivity.this, "两次新密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://" + QueryUtils.ipAddress + "/Pets/Interface/index.php/Home/Index/appChangePassword";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.equals("1")) {
                    Toast.makeText(ChangePasswordActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChangePasswordActivity.this, PetsActivity.class);
                    startActivity(intent);
                } else if (s.equals("-1")) {
                    Toast.makeText(ChangePasswordActivity.this, "原密码错误", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "修改失败，请稍后重试", Toast.LENGTH_SHORT).show();
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
                map.put("username", username);
                map.put("old_password", oldPassword);
                map.put("new_password", newPassword1);
                return map;
            }
        };

        mQueue.add(stringRequest);
    }
}
