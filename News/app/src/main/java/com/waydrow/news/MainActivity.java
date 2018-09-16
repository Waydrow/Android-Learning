package com.waydrow.news;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.waydrow.news.adapter.NewsAdapter;
import com.waydrow.news.utils.HttpUtils;
import com.waydrow.news.model.News;

//import java.util.logging.Handler;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView lvNews;
    private NewsAdapter adapter;
    private List<News> newsList;

    public static final String GET_NEWS_URL = "http://192.168.1.107/NewsDemo/getNewsJSON.php";

    public Handler getNewsHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String jsonData = (String) msg.obj;
//            System.out.println(jsonData);
            try {
                JSONArray jsonArray = new JSONArray(jsonData);
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    String title = object.getString("title");
                    String desc = object.getString("desc");
                    String time = object.getString("time");
                    String content_url = object.getString("content_url");
                    String pic_url = object.getString("pic_url");
                    System.out.println("title = " + title);
                    System.out.println("pic_url = " + pic_url);
                    newsList.add(new News(title, desc, time, content_url, pic_url));
                }
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvNews = (ListView) findViewById(R.id.lvNews);
        newsList = new ArrayList<News>();
        adapter = new NewsAdapter(this, newsList);

        lvNews.setAdapter(adapter);
        lvNews.setOnItemClickListener(this);

        HttpUtils.getNewsJSON(GET_NEWS_URL, getNewsHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        News news = newsList.get(position);
        Intent intent = new Intent(this,BrowseNewsActivity.class);
        intent.putExtra("content_url", news.getContent_url());
        startActivity(intent);
    }
}
