package com.example.prismwood;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shopify.graphql.support.ID;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PrefManager {
    Context context;

    PrefManager(Context context) {
        this.context = context;
    }



    public String getCustomerId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AddressDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("customerid", "");
    }

    public List<String> getAddr(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AddressDetails", Context.MODE_PRIVATE);
        List<String> list = new ArrayList<>();
        list.add(sharedPreferences.getString("customerid", ""));
        list.add(sharedPreferences.getString("name", ""));
        list.add(sharedPreferences.getString("addr", ""));
        list.add(sharedPreferences.getString("num", ""));
        list.add(sharedPreferences.getString("city", ""));
        list.add(sharedPreferences.getString("email", ""));
        return list;

    }

    public void saveAddr(String customer_id , String name, String addr, String city , String num, String email){
        SharedPreferences sharedPreferences = context.getSharedPreferences("AddressDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("customerid", customer_id);
        editor.putString("name", name);
        editor.putString("addr", addr);
        editor.putString("city", city);
        editor.putString("num", num);
        editor.putString("email", email);

        editor.commit();
    }
    public void removeAddr(){

        SharedPreferences sharedPreferences = context.getSharedPreferences("AddressDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("addressid");
        editor.remove("name");
        editor.remove("addr");
        editor.remove("cityid");
        editor.remove("num");

        editor.commit();
    }


    public  List<cartInfo> getCartItems(){
        SharedPreferences pref = context.getSharedPreferences("shopify_variant", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString("variant_details", null);
        Type type = new TypeToken<List<cartInfo>>(){}.getType();
        List<cartInfo> allVariants = gson.fromJson(json, type);
        return allVariants;
    }
    public void addCartItem(ID productid, String selectedvid, int qty){
        SharedPreferences pref = context.getSharedPreferences("shopify_variant", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString("variant_details", null);
        Type type = new TypeToken<List<cartInfo>>(){}.getType();
        List<cartInfo> allVariants = gson.fromJson(json, type);
        SharedPreferences.Editor editor = pref.edit();
        if(allVariants == null){
            allVariants = new ArrayList<>();
        }
        if(allVariants != null && allVariants.size() != 0){
            boolean found = false;
            for(int i=0 ; i < allVariants.size(); i++){
                if(allVariants.get(i).getCartid().equals(selectedvid)){
                    found = true;
                    Toast.makeText(context, "Product Exists", Toast.LENGTH_SHORT).show();
                    // allow addition but process the duplicate elements

                    int current_qty ;
                    if(qty == 1) {
                        current_qty =allVariants.get(i).getQuantity()+ 1;
                        allVariants.get(i).setQuantity(current_qty);
                    }else {
                        allVariants.get(i).setQuantity(qty);
                    }

                }
            }
            if(!found){
                Toast.makeText(context, "Product Added to Cart", Toast.LENGTH_SHORT).show();
                cartInfo obj = new cartInfo(productid, selectedvid,qty );
                allVariants.add(obj);
            }
        }else{
            Toast.makeText(context, "Product Added to Cart", Toast.LENGTH_SHORT).show();
            cartInfo obj = new cartInfo(productid, selectedvid,qty );
            allVariants.add(obj);
        }
        String final_json = gson.toJson(allVariants);
        editor.putString("variant_details", final_json);
        editor.commit();


    }
    public void removeFromCart(int index){
        SharedPreferences pref = context.getSharedPreferences("shopify_variant", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString("variant_details", null);
        Type type = new TypeToken<List<cartInfo>>(){}.getType();
        List<cartInfo> allVariants = gson.fromJson(json, type);
        SharedPreferences.Editor editor = pref.edit();

        allVariants.remove(index);


        String final_json = gson.toJson(allVariants);
        editor.putString("variant_details", final_json);
        editor.commit();


    }

    public void emptyCart(){
        SharedPreferences pref = context.getSharedPreferences("shopify_variant", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        editor.remove("variant_details");
        editor.commit();
    }




}
