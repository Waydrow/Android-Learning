package com.example.android.miwok;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.resource;

/**
 * Created by Waydrow on 2016/10/14.
 */

public class WordAdapter extends ArrayAdapter<Word> {

    /*Resource Id of background color*/
    private int mColorResourceId;


    public WordAdapter(Context context, ArrayList<Word> words, int colorResourceId) {
        super(context, 0, words);
        mColorResourceId = colorResourceId;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false
            );
        }

        Word currentWord = getItem(position);

        TextView defaultText = (TextView) listItemView.findViewById(R.id.default_text_view);
        TextView miwokText = (TextView) listItemView.findViewById(R.id.miwok_text_view);
        defaultText.setText(currentWord.getmDefaultTranslation());
        miwokText.setText(currentWord.getmMiwokTranslation());

        ImageView image = (ImageView) listItemView.findViewById(R.id.image_view);
        if(currentWord.hasImage()) {
            image.setImageResource(currentWord.getmImageResourceId());
            image.setVisibility(View.VISIBLE);
        } else {
            image.setVisibility(View.GONE);
        }


        /*设置主题颜色*/
        View textContainer = listItemView.findViewById(R.id.text_container);
        int color = ContextCompat.getColor(getContext(), mColorResourceId);
        textContainer.setBackgroundColor(color);



        return listItemView;


    }
}
