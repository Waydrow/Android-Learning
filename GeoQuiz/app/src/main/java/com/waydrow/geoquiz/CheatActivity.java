package com.waydrow.geoquiz;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    public static final String EXTRA_ANSWER_IS_TRUE =
            "com.waydrow.geoquiz.answer_is_true";
    public static final String EXTRA_ANSWER_SHOWN =
            "com.waydrow.geoquiz.answer_shown";

    private static final String FLAG_IS_CHEATED = "save_cheat_state";
    private static final String FLAG_IS_CHEATED_TEXT = "answer_shown_text";

    private boolean cheatState = false; //是否cheat
    private boolean mAnswerIsTrue;
    private String answerShownText;

    private TextView mAnswerTextView;
    private Button mShowAnswer;

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
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

        /*若保存cheat状态成功，接收之*/
        if(savedInstanceState!=null) {
            cheatState = savedInstanceState.getBoolean(FLAG_IS_CHEATED, false);
        }
        setAnswerShownResult(cheatState);

        /*从QuizActivity接收正确答案*/
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answerTextView);
        if(savedInstanceState!=null) {
            answerShownText = savedInstanceState.getString(FLAG_IS_CHEATED_TEXT, "");
            mAnswerTextView.setText(answerShownText);
        }

        mShowAnswer = (Button) findViewById(R.id.show_answer_btn);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAnswerIsTrue) {
                    answerShownText = "True";
                    mAnswerTextView.setText(answerShownText);
                } else {
                    answerShownText = "False";
                    mAnswerTextView.setText(answerShownText);
                }
                cheatState = true;
                setAnswerShownResult(cheatState);
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(FLAG_IS_CHEATED, cheatState);
        savedInstanceState.putString(FLAG_IS_CHEATED_TEXT, answerShownText);
    }
}
