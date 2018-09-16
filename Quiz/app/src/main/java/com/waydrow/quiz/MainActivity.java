package com.waydrow.quiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import static com.waydrow.quiz.R.id.advice;
import static com.waydrow.quiz.R.id.crash;
import static com.waydrow.quiz.R.id.search_mag_icon;
import static com.waydrow.quiz.R.id.sexGroup;
import static com.waydrow.quiz.R.id.shoppingGroup;

public class MainActivity extends AppCompatActivity {

    /*性别*/
    RadioGroup sexGroup;
    /*购物选择*/
    RadioGroup shoppingGroup;

    /*选择的性别*/
    String sexChoosed = null;
    /*选择的购物方式*/
    String shoppingChoosed = null;

    private Toast mToast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*性别选择绑定视图*/
        sexGroup = (RadioGroup) findViewById(R.id.sexGroup);
        /*购物选择绑定视图*/
        shoppingGroup = (RadioGroup) findViewById(R.id.shoppingGroup);
        /*提交按钮绑定视图*/
        Button submit = (Button) findViewById(R.id.submit);

        /*选择性别状态绑定*/
        sexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
               RadioButton sex = (RadioButton) findViewById(checkedId);
                sexChoosed = sex.getText().toString();
            }
        });

         /*购物选择状态绑定*/
        shoppingGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton shopping = (RadioButton) findViewById(checkedId);
                shoppingChoosed = shopping.getText().toString();
            }
        });

        /*提交按钮的点击事件*/
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    /*提交函数*/
    public void submit() {
        /*姓名*/
        EditText name = (EditText) findViewById(R.id.name);
        /*支付方式*/
        CheckBox crash = (CheckBox) findViewById(R.id.crash);
        CheckBox alipay = (CheckBox) findViewById(R.id.alipay);
        /*建议*/
        EditText advice = (EditText) findViewById(R.id.advice);

        String nameText = name.getText().toString();
        boolean isCrash = crash.isChecked();
        boolean isAlipay = alipay.isChecked();
        String adviceText = advice.getText().toString();
        /*若建议未填写, 则设置无*/
        if(adviceText.equals("")) {
            adviceText = "无";
        }

        /*提交检查*/
        if(nameText.equals("")) {
            //Toast.makeText(getApplicationContext(), R.string.pleaseInputName, Toast.LENGTH_SHORT).show();
            showToast(R.string.pleaseInputName);
        }
        else if(sexChoosed == null) {
//            Toast.makeText(getApplicationContext(), R.string.pleaseChooseSex, Toast.LENGTH_SHORT).show();
            showToast(R.string.pleaseChooseSex);
        } else if(shoppingChoosed == null) {
//            Toast.makeText(getApplicationContext(), R.string.pleaseChooseShopping, Toast.LENGTH_SHORT).show();
            showToast(R.string.pleaseChooseShopping);
        } else if(!isCrash && !isAlipay) {
//            Toast.makeText(getApplicationContext(), R.string.pleaseChoosePay, Toast.LENGTH_SHORT).show();
            showToast(R.string.pleaseChoosePay);
        } else {
            String summary = createSummary(nameText, sexChoosed, shoppingChoosed, isCrash, isAlipay, adviceText);
//            Toast.makeText(getApplicationContext(), summary, Toast.LENGTH_LONG).show();
            showToast(summary);
        }

    }

    /*汇总函数*/
    private String createSummary(String nameText, String sexChoosed, String shoppingChoosed, boolean isCrash, boolean isAlipay, String adviceText) {

        String summary = "";

        summary += "姓名: " + nameText + "\n";
        summary += "性别: " + sexChoosed + "\n";
        summary += "购物习惯: " + shoppingChoosed + "\n";
        summary += "现金支付: " + isCrash + "\n";
        summary += "支付宝支付: " + isAlipay + "\n";
        summary += "建议: " + adviceText + "\n";

        return summary;

    }

    private void showToast(int resourceId) {
        if(mToast == null) {
            mToast = Toast.makeText(getApplicationContext(), resourceId, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(resourceId);
        }

        mToast.show();
    }

    private void showToast(String s) {
        if(mToast == null) {
            mToast = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(s);
        }

        mToast.show();
    }

}
