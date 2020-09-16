package com.example.prismwood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class addressDetails extends AppCompatActivity {
    TextView name;
    TextView addr;
    TextView contact;

    TextView product_total;
    TextView delivery_charges;
    TextView Overall_total;

    Button updatedetails;
    Button place_order;
    Context context;
    String orderID;
    String cart_amt;
    List<String> customerDetails;
    public static String customerid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_details);

        context = this;
        name = findViewById(R.id.checkout_name);
        addr = findViewById(R.id.checkout_addr);
        contact = findViewById(R.id.checkout_contact);

        product_total = findViewById(R.id.total_items_price);
        delivery_charges = findViewById(R.id.delivery_charges);
        Overall_total = findViewById(R.id.total_price);
        updatedetails = findViewById(R.id.update_addr);
        place_order = findViewById(R.id.complete_order);

        customerDetails = new PrefManager(this).getAddr();

        customerid = customerDetails.get(0);
        name.setText(customerDetails.get(1));
        addr.setText(customerDetails.get(2));
        contact.setText(customerDetails.get(3));

        Intent intent = getIntent();
        cart_amt = intent.getStringExtra("total_amt");
        product_total.setText(cart_amt);


        updatedetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent customderdetails = new Intent(context , customerDetails.class);
                customderdetails.putExtra("total_amt",cart_amt );
                customderdetails.putExtra("fetchdetails",1 );
                context.startActivity(customderdetails);
            }
        });

        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = new PrefManager(context).getCustomerId();
                List<cartInfo> allcartItems = new PrefManager(context).getCartItems();
                try {
                    placeOrder(id, allcartItems);
                    new PrefManager(context).emptyCart();
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });



    }
    public String placeOrder(String customerId, List<cartInfo> allCartInfo ) throws JSONException, UnsupportedEncodingException {
        JSONObject odr = new JSONObject();

        odr.put("email","foo@example.com");
        odr.put("fulfillment_status", "unfulfilled");
        odr.put("send_receipt", true);
        odr.put("send_fulfillment_receipt", false);
        JSONArray jarr = new JSONArray();
        for(int i =0 ; i < allCartInfo.size(); i++){
            JSONObject itm = new JSONObject();
            String str = allCartInfo.get(i).getCartid();
            String msg = new String(android.util.Base64.decode(str, android.util.Base64.DEFAULT));
            String[]assetClasses = msg.split("/");
            String converted_variant = assetClasses[4];
            itm.put("variant_id", converted_variant);
            itm.put("quantity", allCartInfo.get(i).getQuantity());
            jarr.put(itm);
        }

        odr.put("line_items", jarr);
        JSONObject customer = new JSONObject();
        customer.put("id", customerId);
        odr.put("customer", customer);

        JSONObject billing_address = new JSONObject();
        billing_address.put("first_name","");
        billing_address.put("last_name",customerDetails.get(1));
        billing_address.put("address1",customerDetails.get(2));
        billing_address.put("phone",customerDetails.get(3));
        billing_address.put("city",customerDetails.get(4));
        billing_address.put("province","");
        billing_address.put("country","PK");
        billing_address.put("zip","00");
        odr.put("shipping_address", billing_address);
        JSONObject jobj = new JSONObject();
        jobj.put("draft_order", odr);

        String url = "https://8e610e9a73931ce275e0fcea8fa2aa54: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@prism-woods.myshopify.com/admin/api/2020-07/draft_orders.json";

        StringEntity jsonEntity = null;
        try {
            jsonEntity = new StringEntity(jobj.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        Toast.makeText(getApplicationContext(), jobj.toString(), Toast.LENGTH_LONG).show();
        new AsyncHttpClient().post(this, url, jsonEntity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jData) {

                try {
                     orderID = jData.getJSONObject("draft_order").getString("id");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent ab = new Intent(context, orderComplete.class);
                ab.putExtra("orderID", orderID);
                context.startActivity(ab);
                Toast.makeText(getApplicationContext(), orderID, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject jData) {
                //toast server error here
                String http_request_error = String.valueOf(statusCode);;
                String server_error = "";
                try {
                    server_error = jData.toString();
                    Toast.makeText(getBaseContext(),  server_error + " " + http_request_error , Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(),  "Server is down " + http_request_error, Toast.LENGTH_LONG).show();
                }
                Log.d("http request", error.toString());
            }
        });
        return orderID;

    }

}