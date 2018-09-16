package com.waydrow.learncomponents;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public class ChooseADate extends AppCompatActivity {

    private Button btnChooseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_adate);

        btnChooseDate = (Button) findViewById(R.id.chooseDate);
        btnChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ChooseADate.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        /*月份从0计数*/
                        String theDate = String.format("%d-%d-%d",year,monthOfYear+1,dayOfMonth);

                        System.out.println(theDate);
                        btnChooseDate.setText(theDate);
                    }
                },2016,2,30).show();
            }
        });
    }
}
