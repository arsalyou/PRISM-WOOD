package com.example.prismwood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.SearchView;

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

public class MainActivity extends AppCompatActivity {
    public static final String SHOP_DOMAIN ="prism-woods.myshopify.com";
    public static final String API_KEY = "563bba3d0fec72a1903a6770831e08c2";
    private Handler mHandler;
    RecyclerView cat ;
    public static RecyclerView pro;
    View homecart;
    Context context;
    SearchView search_query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new Handler(Looper.getMainLooper());
        pro = findViewById(R.id.products_recycle);
        cat = findViewById(R.id.category_recycle);
        homecart = findViewById(R.id.home_cart_icon);
        context = this;
        search_query = findViewById(R.id.home_search);



//        Intent isback = getIntent();
//        int check = isback.getIntExtra("backbtnhere",0);
//        if (check == 1){
//            fetchHomeDetails();
//        }

        fetchHomeDetails();

        homecart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tocart = new Intent(context, cartDetails.class );
                context.startActivity(tocart);

            }
        });
        search_query.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent tosearch= new Intent(context , searchResults.class);
                tosearch.putExtra("query",query);
                context.startActivity(tosearch);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        search_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_query.setIconified(false);
            }
        });


        //category code


    }

    @Override
    protected void onStart() {
        super.onStart();

    }



    public void fetchHomeDetails(){


        GraphClient client = GraphClient.builder(this)
                .shopDomain(SHOP_DOMAIN)
                .accessToken(API_KEY)
                .build();

        Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                .shop(shopQuery -> shopQuery
                        .collections(arg -> arg.first(15), collectionConnectionQuery -> collectionConnectionQuery
                                .edges(collectionEdgeQuery -> collectionEdgeQuery
                                        .node(collectionQuery -> collectionQuery
                                                .title()
                                                .image(Storefront.ImageQuery::src)
                                                .description()



                                                .products(arg -> arg.first(6), productConnectionQuery -> productConnectionQuery
                                                        .edges(productEdgeQuery -> productEdgeQuery
                                                                .node(productQuery -> productQuery
                                                                        .title()
                                                                        .availableForSale()

                                                                        .images(arg -> arg.first(10), imageConnectionQuery -> imageConnectionQuery
                                                                                .edges(imageEdgeQuery -> imageEdgeQuery
                                                                                        .node(imageQuery -> imageQuery
                                                                                                .src()
                                                                                        )
                                                                                )
                                                                        )
                                                                        .productType()
                                                                        .variants(arg -> arg.first(10), variantConnectionQuery -> variantConnectionQuery
                                                                                .edges(variantEdgeQuery -> variantEdgeQuery
                                                                                        .node(productVariantQuery -> productVariantQuery
                                                                                                .price()
                                                                                                .title()
                                                                                                .availableForSale()
                                                                                                .compareAtPrice()
                                                                                                .available()
                                                                                        )
                                                                                )
                                                                        )
                                                                        .description()
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        QueryGraphCall as =client.queryGraph(query);
        List<Storefront.Collection> collections = new ArrayList<>();
        List<Storefront.Product> products = new ArrayList<>();
        as.enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
            @Override public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (Storefront.CollectionEdge collectionEdge : response.data().getShop().getCollections().getEdges()) {
                            collections.add(collectionEdge.getNode());



                            for (Storefront.ProductEdge productEdge : collectionEdge.getNode().getProducts().getEdges()) {
                                products.add(productEdge.getNode());




                            }
                        }
                        //df.setText( products.get(0).getTitle()); // must be inside run()
                        categoryAdapter catadap = new categoryAdapter(collections, mHandler , context);
                        cat.setAdapter(catadap);
                        productAdapter proAdap = new productAdapter(products);


                        pro.setAdapter(proAdap);



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
