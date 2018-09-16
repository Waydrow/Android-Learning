package com.waydrow.booklisting;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Waydrow on 2016/11/15.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, List<Book> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        Book currentBook = getItem(position);

        TextView bookTitle = (TextView) listItemView.findViewById(R.id.title);
        TextView bookAuthors = (TextView) listItemView.findViewById(R.id.author);
        /*填充书名*/
        bookTitle.setText(currentBook.getTitle());
        /*填充作者*/
        bookAuthors.setText(currentBook.getAuthors());

        return listItemView;
    }


}
