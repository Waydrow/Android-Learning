package com.waydrow.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.waydrow.news.R;
import com.waydrow.news.model.News;
import com.waydrow.news.utils.HttpUtils;

import java.util.List;

/**
 * Created by slomo on 2016/3/7.
 */
public class NewsAdapter extends BaseAdapter {

    private Context context;
    private List<News> newsList;

    public NewsAdapter(Context context, List<News> newsList){
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.news_item, null);
        }
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvDesc = (TextView) convertView.findViewById(R.id.tvDesc);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        ImageView ivPic = (ImageView) convertView.findViewById(R.id.ivPic);

        News news = newsList.get(position);
        tvTitle.setText(news.getTitle());
        tvTime.setText(news.getTime());
        tvDesc.setText(news.getDesc());

        String pic_url = news.getPic_url();
        HttpUtils.setPicBitmap(ivPic, pic_url);

        return convertView;
    }
}
