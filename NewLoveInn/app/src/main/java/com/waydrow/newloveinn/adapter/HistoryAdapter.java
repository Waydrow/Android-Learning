package com.waydrow.newloveinn.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.waydrow.newloveinn.R;
import com.waydrow.newloveinn.bean.ActivitySummary;
import com.waydrow.newloveinn.bean.HistoryItem;

import java.util.List;

import static cn.jpush.android.e.c;
import static cn.jpush.android.e.n;


/**
 * Created by Waydrow on 2017/4/8.
 */

public class HistoryAdapter extends ArrayAdapter<HistoryItem> {

    public HistoryAdapter(Context context, List<HistoryItem> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        HistoryItem currentItem = getItem(position);

        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.history_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.activityNameTextView = (TextView) view.findViewById(R.id.activity_name);
            viewHolder.applyStateTextView = (TextView) view.findViewById(R.id.apply_state);
            viewHolder.applyTimeTextView = (TextView) view.findViewById(R.id.apply_time);
            viewHolder.applyRateTextView = (TextView) view.findViewById(R.id.apply_rate);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.activityNameTextView.setText(currentItem.getName());
        viewHolder.applyTimeTextView.setText(currentItem.getTime());
        String rate = currentItem.getRate();
        String rateText = "";
        if (TextUtils.isEmpty(rate)) {
            rateText = "活动未结束";
        } else {
            rateText = String.valueOf(Double.parseDouble(rate) * 2);
        }
        viewHolder.applyRateTextView.setText(rateText);
        String text = "";
        String state = currentItem.getIsjoin();
        if (state.equals("-1")) {
            text = "审核拒绝";
        } else if (state.equals("0")) {
            text = "等待审核";
        } else if (state.equals("1")) {
            text = "审核通过";
        }
        viewHolder.applyStateTextView.setText(text);

        return view;
    }

    class ViewHolder {

        TextView activityNameTextView;

        TextView applyStateTextView;

        TextView applyTimeTextView;

        TextView applyRateTextView;

    }
}
