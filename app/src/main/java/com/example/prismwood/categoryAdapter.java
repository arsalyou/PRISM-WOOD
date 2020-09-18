package com.example.prismwood;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.QueryGraphCall;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;

import java.util.ArrayList;
import java.util.List;

public class categoryAdapter extends RecyclerView.Adapter<categoryAdapter.ViewHolder> {
    List<Storefront.Collection> collections;
    Handler mHandler;
    Context context;

    public categoryAdapter(List<Storefront.Collection> collections, Handler mHandler, Context context) {
        this.collections = collections;
        this.mHandler = mHandler;
        this.context = context;
    }

    public categoryAdapter(List<Storefront.Collection> collections) {
        this.collections = collections;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (collections.get(position).getImage() != null){
            holder.setdetails(collections.get(position).getTitle(), collections.get(position).getImage().getSrc() );
        }else{
            holder.setdetails(collections.get(position).getTitle(),"" );
        }
    }

    @Override
    public int getItemCount() {
        return collections.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title= itemView.findViewById(R.id.category_name);
            img= itemView.findViewById(R.id.category_icon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();
                    String cid = collections.get(index).getId().toString();
                    List<Storefront.Product> category_products = new ArrayList<>();
                     fetchProductsinCollection(cid);

                }
            });

        }
        public void setdetails(String a, String b){
            title.setText(a);
            Glide.with(itemView.getContext()).load(b).fitCenter().placeholder(R.drawable.splash_screen).into(img);
        }
        public void fetchProductsinCollection(String collectionid){
            String SHOP_DOMAIN ="prism-woods.myshopify.com";
             String API_KEY = "563bba3d0fec72a1903a6770831e08c2";
            GraphClient client = GraphClient.builder(context)
                    .shopDomain(SHOP_DOMAIN)
                    .accessToken(API_KEY)
                    .build();

            Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                    .node(new ID(collectionid), nodeQuery -> nodeQuery
                            .onCollection(collectionConnectionQuery -> collectionConnectionQuery
                                    .title()
                                    .description()

                                    .products(arg -> arg.first(16), productConnectionQuery -> productConnectionQuery
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
            );
            QueryGraphCall as = client.queryGraph(query);

            List<Storefront.Product> products = new ArrayList<>();
            as.enqueue(new GraphCall.Callback<Storefront.QueryRoot>() {
                @Override
                public void onResponse(@NonNull GraphResponse<Storefront.QueryRoot> response) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Storefront.Collection category =  (Storefront.Collection)  response.data().getNode();
                            for (int i =0 ; i < category.getProducts().getEdges().size() ; i++) {
                                products.add(category.getProducts().getEdges().get(i).getNode());
                            }
                            MainActivity.pro.removeAllViewsInLayout();
                            MainActivity.pro.setAdapter(null);
                            productAdapter proAdap = new productAdapter(products);
                            MainActivity.pro.setAdapter(proAdap);
                            MainActivity.pro.getAdapter().notifyDataSetChanged();
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
}
