package com.waydrow.newloveinn.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.waydrow.newloveinn.AinfoActivity;
import com.waydrow.newloveinn.adapter.ActivityAdapter;
import com.waydrow.newloveinn.bean.ActivitySummary;
import com.waydrow.newloveinn.bean.Agency;
import com.waydrow.newloveinn.R;
import com.waydrow.newloveinn.adapter.AgencyAdapter;
import com.waydrow.newloveinn.util.API;
import com.waydrow.newloveinn.util.HttpUtil;
import com.waydrow.newloveinn.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Waydrow on 2016/11/27.
 */

public class AgencyFragment extends Fragment {

    private static final String TAG = "AgencyFragment";

    private ListView listView;

    private ProgressDialog progressDialog;

    private AgencyAdapter agencyAdapter;

    private SwipeRefreshLayout swipeRefresh;

    private List<Agency> agencyList = new ArrayList<>();

    public AgencyFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.agency_list, container, false);

        agencyAdapter = new AgencyAdapter(getActivity(), agencyList);
        swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_agency_list);
        listView = (ListView) rootView.findViewById(R.id.agency_list);
        listView.setAdapter(agencyAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 设置列表的item点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        // 从服务器获取活动列表
        queryFromServer();

        // 下拉刷新
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryFromServer();
            }
        });
    }

    // 从服务器获取活动列表
    private void queryFromServer() {
        showProgressDialog();
        String url = API.INTERFACE + "getAgencyList";
        HttpUtil.sendGetRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                closeProgressDialog(); // 关闭加载框
                Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
                swipeRefresh.setRefreshing(false); // 取消下拉刷新
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resonseText = response.body().string();
                Log.d(TAG, "onResponse: " + resonseText);
                agencyList.clear();
                List<Agency> tempList = Utility.handleAgencyList(resonseText);
                for (Agency agency : tempList) {
                    agencyList.add(agency);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        // Log.d(TAG, "run: " + activitySummaryList.get(0).getName());
                        if (agencyList == null) {
                            Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
                        } else {
                            agencyAdapter.notifyDataSetChanged();
                            listView.setSelection(0);
                        }
                        swipeRefresh.setRefreshing(false); // 取消下拉刷新
                    }
                });
            }
        });
    }

    // 显示加载框
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    // 关闭加载框
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
