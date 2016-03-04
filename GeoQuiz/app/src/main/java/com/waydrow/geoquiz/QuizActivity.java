package com.waydrow.geoquiz;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String CHEATER_STATE = "cheater";

    private Button mTrueButton; //True按钮
    private Button mFalseButton; //False按钮
    private Button mNextButton; //下一步按钮
    private Button mPrevButton; //上一题按钮
    private TextView mQuestionTextView; //问题区域
    private Button mCheatButton; //Cheat
    private boolean mIsCheater;

    /*当前问题索引*/
    private int mCurrentIndex = 0;

    /*问题数组*/
    private TrueFalse[] mQuestionBank = new TrueFalse[] {
            new TrueFalse(R.string.question_oceans, true),
            new TrueFalse(R.string.question_mideast, false),
            new TrueFalse(R.string.question_africa, false),
            new TrueFalse(R.string.question_americas, true),
            new TrueFalse(R.string.question_asia, true)
    };

    /*更新当前问题*/
    private void updateQuestion(){
//        Log.d(TAG, "Updating question text for question #" + mCurrentIndex, new Exception());
        int question = mQuestionBank[mCurrentIndex].getmQuestion();
        mQuestionTextView.setText(question);
    }

    /*返回上一个问题*/
    private void prevQuestion(){
        /*如果已是第一个问题，无法后退*/
        if(mCurrentIndex == 0) {
            Toast.makeText(this, R.string.firstQuestion, Toast.LENGTH_SHORT).show();
        }
        if(mCurrentIndex > 0) {
            mCurrentIndex--;
            int question = mQuestionBank[mCurrentIndex].getmQuestion();
            mQuestionTextView.setText(question);
        }
    }

    /*检查问题答案是否正确*/
    private void checkAnswer(boolean userPressedBtn){
        boolean trueAnswer = mQuestionBank[mCurrentIndex].ismTrueQuestion();

        int messageResId = 0;

        if(mIsCheater) {
            messageResId = R.string.judgment_toast;
        } else {
            if(userPressedBtn == trueAnswer){
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
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

        /*作弊返回后，防止用户通过旋转屏幕来清除作弊痕迹*/
        if(savedInstanceState!=null) {
            mIsCheater = savedInstanceState.getBoolean(CHEATER_STATE, false);
        }
        /*获取问题区域*/
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        /*True按钮*/
        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*弹出回答结果*/
                checkAnswer(true);
            }
        });

        /*False按钮*/
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*弹出回答结果*/
                checkAnswer(false);
            }
        });

        /*下一步按钮*/
        mNextButton = (Button) findViewById(R.id.next_btn);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentIndex==mQuestionBank.length-1) {
                    Toast.makeText(QuizActivity.this, R.string.lastQuestion, Toast.LENGTH_SHORT).show();
                } else {
                    mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                    mIsCheater = false;
                    /*更新显示问题*/
                    updateQuestion();
                }
            }
        });

        /*上一步按钮*/
        mPrevButton = (Button) findViewById(R.id.pre_btn);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevQuestion();
            }
        });

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        /*Cheat ! */
        mCheatButton = (Button) findViewById(R.id.cheat_btn);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuizActivity.this, CheatActivity.class);
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].ismTrueQuestion();
                i.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, answerIsTrue);
                startActivityForResult(i, 0);
            }
        });

        updateQuestion();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(CHEATER_STATE, mIsCheater);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null) {
            return;
        }
        mIsCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
    }
}
