package com.example.prismwood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.util.List;

public class customerDetails extends AppCompatActivity {
    Button saveDetails;
    EditText cusname;
    EditText cusaddr;
    EditText telephone;
    EditText city;
    EditText email;
    EditText password;
    Context context;
    String id; // customerID
    String Name, Addr, City, Phone, Email;
    public  static String amt;
    int fetch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        cusname = findViewById(R.id.cus_name);
        cusaddr = findViewById(R.id.cus_address);
        telephone = findViewById(R.id.cus_phone);
        city = findViewById(R.id.cus_city);
        email = findViewById(R.id.cus_email);

        context = this;
         Name= cusname.getText().toString();
         Addr = cusaddr.getText().toString();
         City = city.getText().toString();
         Phone = telephone.getText().toString();
         Email = email.getText().toString();
        saveDetails = findViewById(R.id.save_customer);
        Intent fromUpdateActivity = getIntent();
         fetch = fromUpdateActivity.getIntExtra("fetchdetails",-1);
        List<String> customerDetails;
        if (fetch == 1){
            customerDetails = new PrefManager(context).getAddr();
            cusname.setText(customerDetails.get(1));
            cusaddr.setText(customerDetails.get(2));
            telephone.setText(customerDetails.get(3));
            city.setText(customerDetails.get(4));
        }

    }

    public void onPostClicked(View view) throws JSONException {
        if (fetch != 1) {
            if (!TextUtils.isEmpty(cusname.getText().toString()) && !TextUtils.isEmpty(city.getText().toString()) && !TextUtils.isEmpty(cusaddr.getText().toString()) && !TextUtils.isEmpty(telephone.getText().toString())) {
                if (telephone.getText().length() == 11) {
                    postCustomer local = new postCustomer(cusname.getText().toString(), telephone.getText().toString(), cusaddr.getText().toString(), city.getText().toString(), email.getText().toString(), context);
                    local.getCustomerId();
                    Intent details = getIntent();
                    amt = details.getStringExtra("total_amt");

                    //new PrefManager(context).saveAddr(id,Name , Addr, City, Phone, Email );

                } else {
                    Toast.makeText(context, "Mobile Number must be 11 Digits", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Fill missing details", Toast.LENGTH_SHORT).show();
            }
        }else{
            //In Future, also update via Update query
            if (!TextUtils.isEmpty(cusname.getText().toString()) && !TextUtils.isEmpty(city.getText().toString()) && !TextUtils.isEmpty(cusaddr.getText().toString()) && !TextUtils.isEmpty(telephone.getText().toString())) {
                if (telephone.getText().length() == 11) {
                    new PrefManager(context).saveAddr(addressDetails.customerid, cusname.getText().toString(), cusaddr.getText().toString(), city.getText().toString(), telephone.getText().toString() , email.getText().toString());
                    Intent cont = new Intent(context, addressDetails.class);
                    cont.putExtra("total_amt", amt);
                    context.startActivity(cont);
                } else {
                    Toast.makeText(context, "Mobile Number must be 11 Digits", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Fill missing details", Toast.LENGTH_SHORT).show();
            }
        }

    }



}