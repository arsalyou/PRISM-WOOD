package com.example.prismwood;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class postCustomer {
    String name;
    String phone;
    String address;
    String city;
    String email;
    Context mcontext;


    public postCustomer(String name, String phone, String address, String city, String email , Context context) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.email = email;
        this.mcontext=context;
    }
    public void getCustomerId() throws JSONException {


        JSONObject odr = new JSONObject();
        odr.put("first_name","");
        odr.put("last_name", name);
        odr.put("email", email);
        odr.put("phone", phone);
        JSONArray jarr = new JSONArray();
        JSONObject itm = new JSONObject();
        itm.put("address1", address);
        itm.put("city", city);
        itm.put("province", "");
        itm.put("phone", phone);
        itm.put("zip", "00");
        itm.put("last_name", name);
        itm.put("first_name", "");
        itm.put("country", "PK");



        jarr.put(itm);
        odr.put("addresses", jarr);
        JSONObject jobj = new JSONObject();
        jobj.put("customer", odr);

        String url = BuildConfig.BASE_URL+"/admin/api/2020-07/customers.json";

        StringEntity jsonEntity = null;
        try {
            jsonEntity = new StringEntity(jobj.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Toast.makeText(mcontext, jobj.toString(), Toast.LENGTH_LONG).show();
        new AsyncHttpClient().post(mcontext, url, jsonEntity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jData) {
                if (statusCode==200 || statusCode ==201) {

                    try {
                        String CustomerID = jData.getJSONObject("customer").getString("id");
                        new PrefManager(mcontext).saveAddr(CustomerID, name, address, city, phone , email);
                        Toast.makeText(mcontext, CustomerID + " "+ name, Toast.LENGTH_LONG).show();
                        Intent cont = new Intent(mcontext, addressDetails.class);
                        cont.putExtra("total_amt", customerDetails.amt);
                        mcontext.startActivity(cont);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
               // Toast.makeText(mcontext, String.valueOf(CustomerID), Toast.LENGTH_LONG).show();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject jData) {
                //toast server error here
                String http_request_error = String.valueOf(statusCode);;
                String server_error = "";
                try {
                    server_error = jData.toString();
                    Toast.makeText(mcontext,  server_error + " " + http_request_error , Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(mcontext,  "Server is down " + http_request_error, Toast.LENGTH_LONG).show();
                }
                Log.d("http request", error.toString());
            }
        });

    }
}
