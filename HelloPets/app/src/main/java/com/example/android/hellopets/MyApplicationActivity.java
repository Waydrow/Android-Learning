package com.example.android.hellopets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.hellopets.adapter.PetsApplicationAdapter;
import com.example.android.hellopets.data.Pet;
import com.example.android.hellopets.util.QueryUtils;

import java.util.List;

public class MyApplicationActivity extends AppCompatActivity {

    private PetsApplicationAdapter mAdapter = null;

    private TextView mEmptyStateTextView = null;

    private List<Pet> pets = null;

    private SwipeRefreshLayout swipeRefreshLayout = null;

    private ListView petsListView = null;

    private View loadingView = null;

    private RequestQueue mQueue = null;

    SharedPreferences sharedPreferences = null;

    String user_id = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //共用一个布局视图
        setContentView(R.layout.activity_my_application);

        initial();
        getMyApplication();

        petsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Integer pet_id = pets.get(position).getId();
                Intent intent = new Intent(MyApplicationActivity.this, DetailsActivity.class);
                intent.putExtra("pet_id", pet_id);
                startActivity(intent);
            }
        });
    }

    private void initial() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplicationActivity.this);
        user_id = sharedPreferences.getString(getString(R.string.user_id), "-1");
        petsListView = (ListView) findViewById(R.id.list);
        loadingView = findViewById(R.id.loading_indicator);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        petsListView.setEmptyView(mEmptyStateTextView);
        mQueue = Volley.newRequestQueue(MyApplicationActivity.this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getMyApplication();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }

    private void getMyApplication() {
        String url = "http://" + QueryUtils.ipAddress + "/Pets/Interface/index.php/Home/Index/appGetWaiting?user_id=" + user_id;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                pets = QueryUtils.extractFeatureFromJson(s);

                if (pets != null) {

                    mAdapter = new PetsApplicationAdapter(getApplicationContext(), pets);

                    petsListView.setAdapter(mAdapter);

                    loadingView.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("fail", volleyError.getMessage(), volleyError);
            }
        });

        mQueue.add(stringRequest);
    }
}
