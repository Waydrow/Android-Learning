package com.waydrow.newcampusbox;


import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RadioGroup.OnCheckedChangeListener {

    private RadioGroup rg_tab_bar;
    private RadioButton rb_channel;

    //Fragment Object
    private MyFragment fg1, fg2, fg3, fg4;
    private FragmentManager fManager;

    private ClassFragment classTable;

    private String num = "";
    private String [] score ;

    private JSONArray classJsonArray;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Campus Box");
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);


        /*侧边栏*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*下方导航栏*/
        fManager = getSupportFragmentManager();
        rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rg_tab_bar.setOnCheckedChangeListener(this);
        //获取第一个单选按钮，并设置其为选中状态
        rb_channel = (RadioButton) findViewById(R.id.rb_info);
        rb_channel.setChecked(true);

        num = getIntent().getStringExtra("num");    //获取学号

        getScore(); //获取成绩
        getClassInfo();



    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);

        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.action_search).getActionView();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_add) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId, menu);
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            String msg = "";
            switch (item.getItemId()){
//                case R.id.action_camera:
//                    msg += "Click Camera";
//                    break;
//                case R.id.action_send:
//                    msg += "Click Send";
//                    break;
                case R.id.action_location:
                    msg += "Click Location";
                    break;
            }
            if(!msg.equals("")) {
                Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
            }

            return true;
        }
    };

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (checkedId) {
            case R.id.rb_info:
                if (fg1 == null) {
                    fg1 = new MyFragment(new String[]{"hello", "world"});
                    fTransaction.add(R.id.ly_content, fg1);
                } else {
                    fTransaction.show(fg1);
                }
                break;
            case R.id.rb_score:
                if (fg2 == null) {
                    if(score == null) {
                        Toast.makeText(MainActivity.this, "很抱歉，此时无法取得成绩！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "成绩获取成功！", Toast.LENGTH_SHORT).show();
                    }
                    fg2 = new MyFragment(score);
                    fTransaction.add(R.id.ly_content, fg2);
                } else {
                    fTransaction.show(fg2);
                }
                break;
            case R.id.rb_course:
                if (classTable == null) {
                    classTable = new ClassFragment(classJsonArray);
                    fTransaction.add(R.id.ly_content, classTable);
                } else {
                    fTransaction.show(classTable);
                }
                break;
            case R.id.rb_mine:
                if (fg4 == null) {
                    fg4 = new MyFragment(new String[]{"This is me", "hello world"});
                    fTransaction.add(R.id.ly_content, fg4);
                } else {
                    fTransaction.show(fg4);
                }
                break;
        }
        fTransaction.commit();
    }

    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (fg1 != null) fragmentTransaction.hide(fg1);
        if (fg2 != null) fragmentTransaction.hide(fg2);
        if (classTable != null) fragmentTransaction.hide(classTable);
        if (fg4 != null) fragmentTransaction.hide(fg4);
    }

    private void setScore(JSONArray jsonArray) {

        ArrayList data=new ArrayList();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String subject = jsonObject.getString("subject");
                String point = jsonObject.getString("score");
                data.add(subject+":   "+point);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        score=new String[data.size()];
        for(int i=0;i<data.size();i++)
        {
            score[i]=data.get(i).toString();
        }
//        FragmentTransaction fragmentTransaction = fManager.beginTransaction();
//        fg1 = new MyFragment(score);
//        fragmentTransaction.add(R.id.ly_content, fg1);
//        fragmentTransaction.commit();
    }

    /*获取成绩*/
    private void getScore() {
        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://it.ouc.edu.cn/ImageCup/index.php/Home/index/score", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                System.out.println("成绩：" + s);
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://it.ouc.edu.cn/ImageCup/json.html", new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
//                        Toast.makeText(MainActivity.this, "成绩获取成功！", Toast.LENGTH_LONG).show();
                        setScore(jsonArray);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(MainActivity.this, "很抱歉，此时无法取得成绩！", Toast.LENGTH_LONG).show();
                    }
                });
                requestQueue.add(jsonArrayRequest);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                Toast.makeText(MainActivity.this,"无法取得成绩",Toast.LENGTH_LONG).show();
                System.out.println("无法取得成绩");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("userCode", num);
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    /*获取课表*/
    private void getClassInfo() {
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest requestClass = new StringRequest(Request.Method.POST, "http://it.ouc.edu.cn/ImageCup/index.php/Home/index/classinfo", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://it.ouc.edu.cn/ImageCup/class.html", new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        System.out.println("课程表: " + jsonArray);
//                        Toast.makeText(MainActivity.this, "课程表获取成功!", Toast.LENGTH_LONG).show();
                        classJsonArray = jsonArray;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("很抱歉，此时无法取得课表！");
//                        Toast.makeText(MainActivity.this, "很抱歉，此时无法取得课表！", Toast.LENGTH_LONG).show();
                    }
                });
                requestQueue.add(jsonArrayRequest);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("无法取得课程表");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("xh", num);
                return hashMap;
            }
        };
        requestQueue.add(requestClass);
    }
}