package com.example.android.hellopets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.hellopets.data.Pet;
import com.example.android.hellopets.util.QueryUtils;

import java.util.HashMap;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    Pet pet = null;
    RequestQueue mQueue = null;
    SharedPreferences sharedPreferences = null;

    private ImageView petPicImageView = null;
    private TextView petnameTextView = null;
    private TextView petbreedTextView = null;
    private TextView petageTextView = null;
    private TextView petsexTextView = null;
    private TextView petentertimeTextView = null;
    private TextView isbackTextView = null;
    private TextView backreasonTextView = null;
    private TextView characterTextView = null;
    private TextView healthyTextView = null;
    private Button applyButton = null;

    String user_id = "-1";
    int pet_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initial();

        //接受点击列表项传递过来的pet_id
        Intent i = getIntent();
        pet_id = i.getExtras().getInt("pet_id");
        //进行详细信息的获取
        getSinglePet(pet_id);

    }

    private void initial() {
        mQueue = Volley.newRequestQueue(DetailsActivity.this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DetailsActivity.this);
        user_id = sharedPreferences.getString(getString(R.string.user_id), "-1");

        petPicImageView = (ImageView) findViewById(R.id.pet_img);
        petnameTextView = (TextView) findViewById(R.id.pet_name);
        petbreedTextView = (TextView) findViewById(R.id.pet_breed);
        petageTextView = (TextView) findViewById(R.id.pet_age);
        petsexTextView = (TextView) findViewById(R.id.pet_sex);
        petentertimeTextView = (TextView) findViewById(R.id.pet_enter_time);
        isbackTextView = (TextView) findViewById(R.id.pet_is_back);
        backreasonTextView = (TextView) findViewById(R.id.pet_back_reason);
        characterTextView = (TextView) findViewById(R.id.pet_character);
        healthyTextView = (TextView) findViewById(R.id.pet_healthy);
        applyButton = (Button) findViewById(R.id.apply);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyPet(Integer.parseInt(user_id), pet_id);
            }
        });
    }

    private void getSinglePet(Integer pet_id) {
        String url = "http://" + QueryUtils.ipAddress + "/Pets/Interface/index.php/Home/Index/appGetPetById?pet_id=" + pet_id;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.v("success", s);
                pet = QueryUtils.extractFeatureFromJson(s).get(0);
                if (pet.getImg() != null && !pet.getImg().equals("")) {
                    getImg(pet.getImg());
                } else {
                    petPicImageView.setImageResource(R.drawable.doge);
                }
                petnameTextView.setText("宠物名称:  " + pet.getPetname());
                petbreedTextView.setText("宠物品种:  " + pet.getBreed());
                petsexTextView.setText("宠物性别:  " + pet.getSex());
                petageTextView.setText("宠物年龄:  " + pet.getAge());
                petentertimeTextView.setText("进入收养所的时间:  " + pet.getEntertime());
                isbackTextView.setText("宠物被退回次数:  " + pet.getIsback());
                backreasonTextView.setText("被退回原因:  " + ifNull(pet.getBackreason()));
                characterTextView.setText("宠物性格:  " + ifNull(pet.getCharacter()));
                healthyTextView.setText("宠物健康状况:  " + ifNull(pet.getHealthy()));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("fail", volleyError.getMessage(), volleyError);
            }
        });
        mQueue.add(stringRequest);
    }

    private void getImg(String path) {
        String url = "http://" + QueryUtils.ipAddress + path;
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                petPicImageView.setImageBitmap(bitmap);
            }
        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mQueue.add(imageRequest);
    }

    private void applyPet(final Integer user_id, final Integer pet_id) {
        //Log.v("tag", "userid:" + user_id + "petid" + pet_id);
        String url = "http://" + QueryUtils.ipAddress + "/Pets/Interface/index.php/Home/Index/appDealApplication";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.v("TAG", s);
                if (s.equals("1")) {
                    Toast.makeText(DetailsActivity.this, "申请成功,请等待审核~您可以在右上角菜单我的申请中查看", Toast.LENGTH_SHORT).show();
                } else if (s.equals("-1")) {
                    Toast.makeText(DetailsActivity.this, "请勿重复申请", Toast.LENGTH_SHORT).show();
                } else if (s.equals("-2")) {
                    Toast.makeText(DetailsActivity.this, "不能领养超过3只宠物", Toast.LENGTH_SHORT).show();
                } else if (s.equals("-3")) {
                    Toast.makeText(DetailsActivity.this, "您有5个待处理的申请，不能再申请了噢~", Toast.LENGTH_SHORT).show();
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
                Map<String, String> map = new HashMap<>();
                map.put("user_id", user_id + "");
                map.put("pet_id", pet_id + "");
                return map;
            }
        };
        mQueue.add(stringRequest);
    }

    // TODO: 2016/12/6 add the pet into sqlite
    private void collectionPet() {

    }

    private String ifNull(String str) {
        if (str == null || str.equals("") || str.equals("null")) {
            return "暂无数据";
        }
        return str;
    }
}
