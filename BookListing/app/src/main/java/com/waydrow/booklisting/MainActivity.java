package com.waydrow.booklisting;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;   //进度条
    private EditText mBookSearchEditText;   //搜索文本框
    private Button mSearchBtn;  //搜索按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBookSearchEditText = (EditText) findViewById(R.id.book_key);
        mSearchBtn = (Button) findViewById(R.id.search_btn);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        /*搜索按钮点击*/
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookKey = mBookSearchEditText.getText().toString();
                if (bookKey.equals("")) {   //判断是否为空
                    Toast.makeText(MainActivity.this, "您还没有输入关键字哦", Toast.LENGTH_SHORT).show();
                } else {
                    /*拼接url*/
                    String url = "https://www.googleapis.com/books/v1/volumes?q=" + bookKey + "&maxResults=15";
                    /*发起http请求*/
                    BookAsyncTask bookAsyncTask = new BookAsyncTask(url);
                    bookAsyncTask.execute();
                }
            }
        });
    }


    /*自定义AsyncTask类*/
    private class BookAsyncTask extends AsyncTask<URL, Void, ArrayList<Book>> {

        private static final String LOG_TAG = "ERROR";
        private String mUrl;

        /*Constructor*/
        public BookAsyncTask(String url) {
            this.mUrl = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*显示出加载进度条*/
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Book> doInBackground(URL... urls) {
            /* Create a URL object */
            URL url = createUrl(mUrl);

            String jsonResponse = "";
            try {
                /*返回json data*/
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
            }

            /*解析json data至arraylist*/
            ArrayList<Book> books = extractBookJson(jsonResponse);
            return books;
        }


        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            /*隐藏进度条*/
            mProgressBar.setVisibility(View.GONE);
            if(books == null) {
                Toast.makeText(MainActivity.this, "没有查询到符合条件的书, 请重试", Toast.LENGTH_SHORT).show();
                return;
            }
            /*更新UI线程*/
            Intent intent = new Intent(MainActivity.this, BookListActivity.class);
            intent.setAction("action");
            intent.putExtra("bookList", books);
            startActivity(intent);
        }

        /*
        * Return new URL object from a string URL
        * */
        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Error with creating URL", e);
                e.printStackTrace();
                return null;
            }

            return url;
        }

        /*HTTP Request*/
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";

            if(url == null) {
                return jsonResponse;
            }

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.connect();

                if(urlConnection.getResponseCode() == 200) { //请求成功
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else {
                    Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Can't get json response", e);
            } finally {
                /*断开连接*/
                if(urlConnection != null) {
                    urlConnection.disconnect();
                }
                if(inputStream != null) {
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        /*输入流*/
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if(inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while(line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        /*解析Json*/
        private ArrayList<Book> extractBookJson(String bookJSON) {

            /*判断是否为空*/
            if(TextUtils.isEmpty(bookJSON)) {
                return null;
            }

            ArrayList<Book> books = new ArrayList<>();

            try {
                JSONObject baseJsonResponse = new JSONObject(bookJSON);
                JSONArray itemsArray = baseJsonResponse.getJSONArray("items");

                for(int i=0; i<itemsArray.length(); i++) {
                    JSONObject bookObject = itemsArray.getJSONObject(i);
                    JSONObject bookInfo = bookObject.getJSONObject("volumeInfo");

                    String title = bookInfo.getString("title"); //书名
                    JSONArray authorsArray = bookInfo.getJSONArray("authors"); //作者
                    String authors = "";
                    for(int j=0; j<authorsArray.length(); j++) {
                        authors += authorsArray.getString(j);
                        if(j<authorsArray.length()-1) {
                            authors += ",  ";
                        }
                    }
                    Book book = new Book(title, authors);
                    books.add(book);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return books;
        }
    }
}
