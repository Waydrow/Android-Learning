package com.waydrow.newloveinn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.waydrow.newloveinn.adapter.CategoryAdapter;
import com.waydrow.newloveinn.bean.Volunteer;
import com.waydrow.newloveinn.util.API;
import com.waydrow.newloveinn.util.HttpUtil;
import com.waydrow.newloveinn.util.Utility;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private CircleImageView userImageView;

    private TextView usernameTextView;

    private TextView realnameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getVolunteerInfo();

        // 侧滑栏
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                //super.onDrawerOpened(drawerView);
                // 填充用户图片与用户名
                getVolunteerInfo();
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // 获取navigation中的头部元素
        View headerView = navigationView.getHeaderView(0);
        userImageView = (CircleImageView) headerView.findViewById(R.id.user_imageview);
        usernameTextView = (TextView) headerView.findViewById(R.id.username);
        realnameTextView = (TextView) headerView.findViewById(R.id.realname);

        // view pager
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        CategoryAdapter categoryAdapter = new CategoryAdapter(getSupportFragmentManager(), this);
        // set adapter for view pager
        viewPager.setAdapter(categoryAdapter);

        // set up tab layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_profile) {
            Intent intent = new Intent(this, UserActivity.class);
            startActivity(intent);
        } else if (id == R.id.my_activity) {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.love_bank) {
            Intent intent = new Intent(this, LoveMoneyActivity.class);
            startActivity(intent);
        } else if (id == R.id.my_subscribe) {
            Intent intent = new Intent(this, SubscribeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_quit) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            editor.putString(API.PREF_USER_ID, null);
            editor.apply();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // 获取个人信息
    private void getVolunteerInfo() {
        String user_id;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (pref.getString(API.PREF_USER_ID, null) != null) {
            user_id = pref.getString(API.PREF_USER_ID, null);
            String url = API.INTERFACE + "getVolunteerInfo";
            RequestBody body = new FormBody.Builder()
                    .add("id", user_id)
                    .build();
            HttpUtil.sendPostRequest(url, body, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(MainActivity.this, "获取个人信息失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    Log.d(TAG, "onResponse: " + responseText);
                    final Volunteer volunteer = Utility.handleVolunInfo(responseText);
                    // 存储是否通过审核
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                    editor.putInt(API.PREF_USER_ISPASS, volunteer.getIspass());
                    editor.apply();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (volunteer.getAvatar() != null) {
                                Glide.with(MainActivity.this).load(API.IMG_HEADER + volunteer.getAvatar()).crossFade().into(userImageView);
                            } else {
                                userImageView.setImageResource(R.drawable.avatar);
                            }
                            usernameTextView.setText(volunteer.getUsername());
                            realnameTextView.setText(volunteer.getRealname());
                        }
                    });
                }
            });
        }
    }
}
