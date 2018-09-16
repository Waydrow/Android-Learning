package com.waydrow.newloveinn.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.waydrow.newloveinn.R;
import com.waydrow.newloveinn.bean.Subscribe;

import java.util.List;

/**
 * Created by Waydrow on 2017/5/16.
 */

public class SubscribeAdapter extends RecyclerView.Adapter<SubscribeAdapter.ViewHolder> {

    private List<Subscribe> subscribeList;

    private onItemClickListener listener;

    public interface onItemClickListener {
        void onItemClick(View view, int position);
    };

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subName;
        Button subBtn;

        public ViewHolder (View view) {
            super(view);
            subName = (TextView) view.findViewById(R.id.subscribe_name);
            subBtn = (Button) view.findViewById(R.id.subscribe_btn);
        }
    }

    public SubscribeAdapter(List<Subscribe> list) {
        this.subscribeList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subscribe_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Subscribe subscribe = subscribeList.get(position);
        holder.subName.setText(subscribe.getName());
        if (subscribe.getIsSub().equals("0")) {
            // 未订阅
            holder.subBtn.setText("订阅");
            holder.subBtn.setBackgroundColor(Color.parseColor("#FF4081"));
        } else if (subscribe.getIsSub().equals("1")) {
            holder.subBtn.setText("取消订阅");
            holder.subBtn.setBackgroundColor(Color.parseColor("#8c8c8c"));
        }

        holder.subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // callback
                listener.onItemClick(holder.subBtn, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subscribeList.size();
    }
}
