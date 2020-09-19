package com.example.prismwood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.Storefront;

import java.util.ArrayList;
import java.util.List;

public class searchResults extends AppCompatActivity {

    public static final String SHOP_DOMAIN =BuildConfig.SHOP_DOMAIN;
    public static final String API_KEY = BuildConfig.API_KEY;
    private Handler mHandler;
    Context context;
    ImageButton back;
    ImageButton tocart;
    SearchView searchbar;
    RecyclerView products_recyclerview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        mHandler = new Handler(Looper.getMainLooper());
        context = this;
        back = findViewById(R.id.back_btn);
        tocart = findViewById(R.id.search_tocart);
        searchbar= findViewById(R.id.searchbar);
        Intent searchquery = getIntent();
        String find = searchquery.getStringExtra("query");
        products_recyclerview= findViewById(R.id.search_results);
        fetchSearchResults(find);



        tocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tocart = new Intent(context, cartDetails.class );
                context.startActivity(tocart);

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tocart = new Intent(context, MainActivity.class );
                tocart.putExtra("backbtnhere",1);
                context.startActivity(tocart);


            }
        });
        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchSearchResults(find);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchbar.setIconified(false);
            }
        });



    }

    public void fetchSearchResults(String find){
        GraphClient client = GraphClient.builder(this)
                .shopDomain(SHOP_DOMAIN)
                .accessToken(API_KEY)
                .build();

        Storefront.QueryRootQuery query = Storefront.query(root -> root
                .shop(shop -> shop
                        .products(
                                arg -> arg
                                        .first(10)
                                        .query(find),
                                connection -> connection
                                        .edges(edges -> edges
                                                .node(node -> node
                                                        .title()
                                                        .availableForSale()
                                                        .images(arg -> arg.first(1), imageConnectionQuery -> imageConnectionQuery
                                                                .edges(imageEdgeQuery -> imageEdgeQuery
                                                                        .node(imageQuery -> imageQuery
                                                                                .src()
                                                                        )
                                                                )
                                                        )
                                                        .variants(arg -> arg.first(1), variantConnectionQuery -> variantConnectionQuery
                                                                .edges(variantEdgeQuery -> variantEdgeQuery
                                                                        .node(productVariantQuery -> productVariantQuery
                                                                                .price()
                                                                                .compareAtPrice()
                                                                                .title()
                                                                                .availableForSale()
                                                                                .available()
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                        )
                )
        );

        QueryGraphCall as =client.queryGraph(query);
        List<Storefront.Product> products = new ArrayList<>();
        as.enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
            @Override public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(response);
                        if(response.data().getShop().getProducts().getEdges().size()  == 0){
                            Toast.makeText(context, "No Result Found", Toast.LENGTH_LONG).show();
                        }else {
                            for (int i = 0; i < response.data().getShop().getProducts().getEdges().size(); i++) {
                                products.add(response.data().getShop().getProducts().getEdges().get(i).getNode());
                            }
                            productAdapter proAdap = new productAdapter(products);
                            products_recyclerview.setAdapter(proAdap);
                        }
                    }
                });

            }

            @Override
            public void onFailure(@NonNull GraphError error) {
                System.out.println(error);
            }


        });

    }
}