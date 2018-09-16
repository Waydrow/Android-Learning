package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;
import java.text.NumberFormat;

import static android.R.attr.name;
import static android.R.id.message;
import static com.example.android.justjava.R.id.creamOrderBox;


/*
* This app displays an order form to order coffee*/
public class MainActivity extends AppCompatActivity {

    int quantity = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*
    * This method is called when the order button is clicked
    * */
    public void submitOrder(View view) {
        //displayPrice(quantity * 5);
        EditText nameEdit = (EditText) findViewById(R.id.nameEdit);
        CheckBox creamOrderBox = (CheckBox) findViewById(R.id.creamOrderBox);
        CheckBox chocolateOrderBox = (CheckBox) findViewById(R.id.chocolateOrderBox);

        boolean isCreamOrdered = creamOrderBox.isChecked();
        boolean isChocolateOrdered = chocolateOrderBox.isChecked();

        String name = nameEdit.getText().toString();

        String priceMessage = createOrderSummary(name, quantity, isCreamOrdered, isChocolateOrdered);
        //displayMessage(priceMessage);


        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, "zhengzuowu@126.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Just Java for " + name);
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if(intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public String createOrderSummary(String name, int quantity, boolean hasWhippedCream, boolean hasChocolate) {
        int perPrice = 5;
        if(hasWhippedCream){
            perPrice++;
        }
        if(hasChocolate) {
            perPrice += 2;
        }
        String message = getString(R.string.order_summary_name, name);
        message += "\nWhippedCream: " + hasWhippedCream + "\n";
        message += "Chocolate: " + hasChocolate + "\n";
        message += "Toatal Price: $" + quantity * perPrice + "\n";
        message += "Thank you !\n";

        return message;
    }


    /*
    * This method displays the given quantity value on the screen
    * */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }


    public void increment(View view) {
        quantity++;
        display(quantity);
    }

    public void decrement(View view) {
        if(quantity==1) {
            Toast.makeText(this,"最少点一杯哦 !", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity--;
        display(quantity);
    }
}
