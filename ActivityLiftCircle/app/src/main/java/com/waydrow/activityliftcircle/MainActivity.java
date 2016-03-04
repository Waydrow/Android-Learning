package com.waydrow.activityliftcircle;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText mEditText;
    //定义一个String类型的变量来存储我们在EditText中输入的值
    private String mString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mEditText = (EditText) findViewById(R.id.editText);

        System.out.println("on create");
    }

    @Override
    protected void onStart() {
        super.onStart();

        System.out.println("on start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("on resume");
    }

    //当按下Home键后，将EditText中输入的值存入mString变量
    @Override
    protected void onPause() {
        super.onPause();
        mString = mEditText.getText().toString();
        System.out.println("on Pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("on stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("on destroy");
    }

    //当按Home键时，然后再次启动该应用，我们恢复其原先状态
    @Override
    protected void onRestart() {
        super.onRestart();
        mEditText.setText(mString);
        System.out.println("on restrat");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
