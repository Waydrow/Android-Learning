package com.example.android.hellopets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.hellopets.adapter.PetsAdapter;
import com.example.android.hellopets.data.Pet;
import com.example.android.hellopets.util.QueryUtils;

import java.util.List;

public class PetsActivity extends AppCompatActivity {

    private PetsAdapter mAdapter = null;

    private TextView mEmptyStateTextView = null;

    private SwipeRefreshLayout swipeRefreshLayout = null;

    private List<Pet> pets = null;

    private ListView petsListView = null;

    private View loadingView = null;

    private RequestQueue mQueue = null;

    private SharedPreferences sharedPreferences = null;

    private String Pre_Breed = null;
    private String Pre_Sex = null;
    private String Pre_Character = null;
    private String Pre_Query_Method = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);

        initial();
        getData();

        petsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Integer pet_id = pets.get(position).getId();
                Intent intent = new Intent(PetsActivity.this, DetailsActivity.class);
                intent.putExtra("pet_id", pet_id);
                startActivity(intent);
            }
        });
    }

    private void initial() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        /*设置下拉刷新监听器*/
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);    //延时3秒
            }
        });
        petsListView = (ListView) findViewById(R.id.list);
        loadingView = findViewById(R.id.loading_indicator);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        petsListView.setEmptyView(mEmptyStateTextView);
        mQueue = Volley.newRequestQueue(PetsActivity.this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        getPreference();
    }

    private void getPreference(){
        Pre_Breed = sharedPreferences.getString(getString(R.string.settings_breed), "");
        Pre_Sex = sharedPreferences.getString(getString(R.string.settings_sex), "");
        Pre_Character = sharedPreferences.getString(getString(R.string.settings_character), "");
        Pre_Query_Method = sharedPreferences.getString(getString(R.string.settings_query_by_key), "0");
        //Log.v("pre1",Pre_Breed+Pre_Sex+Pre_Character+Pre_Query_Method);
    }

    private void getData() {
        //Log.v("pre2",Pre_Breed+Pre_Sex+Pre_Character+Pre_Query_Method);
        String url = "http://" + QueryUtils.ipAddress + "/Pets/Interface/index.php/Home/Index/appGetPets";
        String params = "?breed=" + Pre_Breed + "&sex=" + Pre_Sex + "&character=" + Pre_Character + "&query=" + Pre_Query_Method;
        url = url + params;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                pets = QueryUtils.extractFeatureFromJson(s);

                if (pets != null) {

                    mAdapter = new PetsAdapter(getApplicationContext(), pets);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            //注销，把偏好中的user_id置为-1
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.user_id), "-1");
            editor.putString(getString(R.string.username), "");
            editor.putString(getString(R.string.password), "");

            // TODO: 2016/12/5 clean the preference of users
            editor.putString(getString(R.string.settings_sex), "");
            editor.putString(getString(R.string.settings_breed), "");
            editor.putString(getString(R.string.settings_character), "");
            editor.putString(getString(R.string.settings_query_by_key), "0");
            editor.commit();

            Intent backToLogin = new Intent(PetsActivity.this, LoginActivity.class);
            startActivity(backToLogin);
            return true;
        }
        if (id == R.id.action_settings) {
            Intent goToSettings = new Intent(PetsActivity.this, SettingsActivity.class);
            startActivity(goToSettings);
            return true;
        }
        if (id == R.id.action_change_personal_information) {
            Intent goToChangeInformation = new Intent(PetsActivity.this, ChangeInformationActivity.class);
            startActivity(goToChangeInformation);
            return true;
        }
        if (id == R.id.action_change_password) {
            Intent goToChangePassword = new Intent(PetsActivity.this, ChangePasswordActivity.class);
            startActivity(goToChangePassword);
            return true;
        }
        if (id == R.id.action_application) {
            Intent goToApplication = new Intent(PetsActivity.this, MyApplicationActivity.class);
            startActivity(goToApplication);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
