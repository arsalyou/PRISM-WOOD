package com.example.prismwood;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;

import java.util.ArrayList;
import java.util.List;

public class deleteit extends AppCompatActivity {
    public static final String SHOP_DOMAIN =BuildConfig.SHOP_DOMAIN;
    public static final String API_KEY = BuildConfig.API_KEY;
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleteit);
        mHandler = new Handler(Looper.getMainLooper());

        GraphClient client = GraphClient.builder(this)
                .shopDomain(SHOP_DOMAIN)
                .accessToken(API_KEY)
                .build();

        Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                .node(new ID("Z2lkOi8vc2hvcGlmeS9Db2xsZWN0aW9uLzIwOTA2Nzc3MDAyMg=="), nodeQuery -> nodeQuery
                        .onCollection(collectionConnectionQuery -> collectionConnectionQuery
                                .title()
                                .description()

                                .products(arg -> arg.first(16), productConnectionQuery -> productConnectionQuery
                                        .edges(productEdgeQuery -> productEdgeQuery
                                                .node(productQuery -> productQuery
                                                        .title()
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
        );
        QueryGraphCall as = client.queryGraph(query);
        List<Storefront.Collection> collections = new ArrayList<>();
        List<Storefront.Product> products = new ArrayList<>();
        as.enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
            @Override
            public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Storefront.CollectionEdge collectionEdge = response.data().getShop().getCollections().getEdges().get(0);

                        for (Storefront.ProductEdge productEdge : collectionEdge.getNode().getProducts().getEdges()) {
                            products.add(productEdge.getNode());


                        }

                        //df.setText( products.get(0).getTitle()); // must be inside run()
                        //categoryAdapter catadap = new categoryAdapter(collections);


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