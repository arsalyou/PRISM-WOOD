package com.example.prismwood;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class viewOrders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

    }

    public void getOrders(String cid){
        String url = BuildConfig.BASE_URL+"/admin/orders.json?customer_id="+cid;


        //Toast.makeText(getApplicationContext(), jobj.toString(), Toast.LENGTH_LONG).show();
        new AsyncHttpClient().get(this, url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jData) {
                if (statusCode==200) {
                    Toast.makeText(getApplicationContext(), jData.toString(), Toast.LENGTH_LONG).show();
                    try {
                        //jData.getJSONArray("orders");

                        JSONObject localobj = (JSONObject) jData.getJSONArray("orders").get(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(getApplicationContext(), String.valueOf(statusCode), Toast.LENGTH_LONG).show();
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


    }
}