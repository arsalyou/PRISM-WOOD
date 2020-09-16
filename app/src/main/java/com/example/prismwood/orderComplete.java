package com.example.prismwood;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class orderComplete extends AppCompatActivity {

    TextView orderid;
    TextView orderdate;
    TextView packeddate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complete);

        Intent details = getIntent();
        String OrderID = details.getStringExtra("orderID");

        orderid = findViewById(R.id.order_id);
        orderid.setText(OrderID);

        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
        orderdate = findViewById(R.id.ordered_date);
        packeddate = findViewById(R.id.packed_date);

        orderdate.setText(currentDateTimeString);
        packeddate.setText(currentDateTimeString);




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home = new Intent(this, MainActivity.class);
        this.startActivity(home);
    }
}