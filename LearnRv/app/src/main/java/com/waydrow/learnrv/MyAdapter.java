package com.waydrow.learnrv;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by slomo on 2016/3/9.
 */
class MyAdapter extends RecyclerView.Adapter {

    class ViewHolder extends RecyclerView.ViewHolder {

        private View root;
        private TextView tvTitle,tvContent;

        public ViewHolder(View root) {
            super(root);

            tvTitle = (TextView) root.findViewById(R.id.tvTitle);
            tvContent = (TextView) root.findViewById(R.id.tvContent);
        }

        public TextView getTvContent() {
            return tvContent;
        }

        public TextView getTvTitle() {
            return tvTitle;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        /*LayoutInflater - 布局解释器*/
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_cell, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;

        CellData cd = data[position];

        vh.getTvTitle().setText(cd.title);
        vh.getTvContent().setText(cd.conetent);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    private CellData[] data = new CellData[]{new CellData("爱特工作室", "Android开发"),
            new CellData("新闻", "这个新闻真不错"),new CellData("新闻", "这个新闻真不错"),
            new CellData("新闻", "这个新闻真不错"),new CellData("新闻", "这个新闻真不错")};

//    private String[] data = new String[]{"Hello", "IT STUDIO", "waydrow"};
}
