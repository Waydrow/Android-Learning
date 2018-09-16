package com.waydrow.booklisting;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        ListView bookListView = (ListView) findViewById(R.id.book_list);
        /*得到传递来的数据*/
        Intent intent = getIntent();
        if("action".equals(intent.getAction())) {
            ArrayList<Book> books = (ArrayList<Book>) intent.getSerializableExtra("bookList");
            BookAdapter bookAdapter = new BookAdapter(this, books);
            bookListView.setAdapter(bookAdapter);
        }
    }
}
