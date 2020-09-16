package com.example.prismwood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;

import java.util.ArrayList;
import java.util.List;

public class cartDetails extends AppCompatActivity {
    public static RecyclerView cartRecycler;
    public static cartAdapter CartAdapter;
    public static Context context;
    public static TextView total_amt;
    public static final String SHOP_DOMAIN ="prism-woods.myshopify.com";
    public static final String API_KEY = "563bba3d0fec72a1903a6770831e08c2";
    private Handler mHandler;
    List<Storefront.Product> product;
    Storefront.ProductVariant variant;
    cartModal local ;
    List<cartModal> cartModalList;
    List<ID> pids;
    List<String> cartids;
    List<Integer> quantities;
    Button continue_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_details);
        cartRecycler = findViewById(R.id.recyclerview_cart);
        LinearLayoutManager testinglayoutmanager = new LinearLayoutManager(context);
        testinglayoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        cartRecycler.setLayoutManager(testinglayoutmanager);
        continue_btn = findViewById(R.id.cart_continue_btn);
        context = this;
        pids=new ArrayList<>();
        cartids= new ArrayList<>();
        quantities = new ArrayList<>();
        local = new cartModal();
        mHandler = new Handler(Looper.getMainLooper());
        total_amt = findViewById(R.id.total_cart_amount);
        List<cartInfo> savedCartItems= new PrefManager(this).getCartItems();
        cartModalList = new ArrayList<>();
        if(savedCartItems != null) {
            for (int i = 0; i < savedCartItems.size(); i++) {
                pids.add(savedCartItems.get(i).getProductid());
                cartids.add(savedCartItems.get(i).getCartid());
                quantities.add(savedCartItems.get(i).getQuantity());
            }
        }else{
            Toast.makeText(getApplicationContext(), "Cart is Empty", Toast.LENGTH_LONG).show();
        }

        fetch_product();
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cartModalList != null && cartModalList.size() > 0) {
                    String cid = new PrefManager(context).getCustomerId();
                    if (cid.equals("")) {
                        Intent openAddrSccreen = new Intent(context, customerDetails.class);
                        openAddrSccreen.putExtra("total_amt", total_amt.getText().toString());
                        context.startActivity(openAddrSccreen);
                    } else {
                        Intent openAddrDetails = new Intent(context, addressDetails.class);
                        openAddrDetails.putExtra("total_amt", total_amt.getText().toString());
                        context.startActivity(openAddrDetails);
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Add Products to Cart to Proceed", Toast.LENGTH_LONG).show();

                }



            }
        });



    }





    public void fetch_product(){

        GraphClient client = GraphClient.builder(context)
                .shopDomain(SHOP_DOMAIN)
                .accessToken(API_KEY)
                .build();
        //"Z2lkOi8vc2hvcGlmeS9Qcm9kdWN0LzQ1NzI0MTExMzQwMjM="
        Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                .nodes(pids, nodeQuery -> nodeQuery
                        .onProduct(productQuery -> productQuery
                                .title()
                                .images(arg -> arg.first(1), imageConnectionQuery -> imageConnectionQuery
                                        .edges(imageEdgeQuery -> imageEdgeQuery
                                                .node(imageQuery -> imageQuery
                                                        .src()
                                                )
                                        )
                                )
                                .variants(arg -> arg.first(10), variantConnectionQuery -> variantConnectionQuery
                                        .edges(variantEdgeQuery -> variantEdgeQuery
                                                .node(productVariantQuery -> productVariantQuery
                                                        .price()
                                                        .title()
                                                        .availableForSale()
                                                        .image(Storefront.ImageQuery::src)
                                                        .selectedOptions(selectedOption -> selectedOption
                                                                .name()
                                                                .value()
                                                        )
                                                        .available()
                                                )
                                        )
                                )
                        )
                )
        );

        client.queryGraph(query).enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {

            @Override public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TAG", "PROme" + response.formatErrorMessage());
                        product = (List<Storefront.Product>) response.data().getNode();

                        List<Storefront.ProductVariant> productVariants = new ArrayList<>();

                        for(int i = 0 ; i < response.data().getNodes().size(); i++){
                            Storefront.Product local_product = (Storefront.Product) response.data().getNodes().get(i);
                            for(int j=0 ; j < local_product.getVariants().getEdges().size(); j++){
                                if(cartids.get(i).equals(local_product.getVariants().getEdges().get(j).getNode().getId().toString())){
                                    Storefront.ProductVariant local_vaiant = (Storefront.ProductVariant)  local_product.getVariants().getEdges().get(j).getNode();
                                    int localqty = quantities.get(i);
                                    cartModalList.add(new cartModal(localqty,local_product,local_vaiant));
                                    break;
                                }
                            }
                        }

                        cartAdapter adapter = new cartAdapter(cartModalList , context);
                        adapter.notifyDataSetChanged();
                        cartRecycler.setAdapter(adapter);
                    }
                });

            }

            @Override
            public void onFailure(@NonNull GraphError error) {

            }

        });






    }

    public static void refreshItem(int current){
        CartAdapter.notifyItemChanged(current);

    }
}