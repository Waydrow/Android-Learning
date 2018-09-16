package com.waydrow.newloveinn.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.waydrow.newloveinn.R;
import com.waydrow.newloveinn.bean.ExchangeItem;
import com.waydrow.newloveinn.bean.HistoryItem;

import java.util.List;


/**
 * Created by Waydrow on 2017/4/8.
 */

public class ExchangeAdapter extends ArrayAdapter<ExchangeItem> implements View.OnClickListener {

    private Callback callback;

    @Override
    public void onClick(View v) {
        callback.click(v);
    }

    // 按钮点击的回调接口
    public interface Callback {
        public void click(View v);
    }

    public ExchangeAdapter(Context context, List<ExchangeItem> objects, Callback callback) {
        super(context, 0, objects);
        this.callback = callback;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ExchangeItem currentItem = getItem(position);

        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.exchange_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.exnameTextView = (TextView) view.findViewById(R.id.exchange_name);
            viewHolder.exmoneyTextView = (TextView) view.findViewById(R.id.exchange_money);
            viewHolder.exchangeBtn = (Button) view.findViewById(R.id.exchange_btn);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.exnameTextView.setText(currentItem.getExname());
        viewHolder.exmoneyTextView.setText(currentItem.getExmoney());

        // 设置按钮的点击事件
        viewHolder.exchangeBtn.setOnClickListener(this);
        viewHolder.exchangeBtn.setTag(position);

        return view;
    }

    class ViewHolder {

        TextView exnameTextView;

        TextView exmoneyTextView;

        Button exchangeBtn;
    }
}
