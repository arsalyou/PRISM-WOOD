package com.example.prismwood;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class variant_Price  extends AsyncTask<Void,Void,Void> {
    String data="";
    boolean resp;
    private Context mContext;
    String variantid;

    public variant_Price(Context mContext, String variantid) {
        this.mContext = mContext;

        this.variantid = variantid;
        this.variantid="35326982226086";
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {

            URL url = new URL("https://8e610e9a73931ce275e0fcea8fa2aa54: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@prism-woods.myshopify.com/admin/api/2020-07/variants/"+variantid +".json");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";

            line = bufferedReader.readLine();
            data =  line;

            if(data.contains("Your Email is not Registered"))
            {
                //Toast.makeText(MainActivity.class, data, Toast.LENGTH_SHORT).show();
                resp=false;
            }
            else if (data.contains("Invalid Details")){
                resp=false;
            }
            else {
                resp=true;
            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
