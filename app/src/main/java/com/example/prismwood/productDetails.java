package com.example.prismwood;

        import android.content.Context;
        import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
        import com.bumptech.glide.request.RequestOptions;
        import com.shopify.buy3.GraphCall;
import com.shopify.buy3.GraphClient;
import com.shopify.buy3.GraphError;
import com.shopify.buy3.GraphResponse;
import com.shopify.buy3.Storefront;
import com.shopify.graphql.support.ID;

import java.util.ArrayList;
import java.util.List;

public class productDetails extends AppCompatActivity {
    public static final String SHOP_DOMAIN ="prism-woods.myshopify.com";
    public static final String API_KEY = "563bba3d0fec72a1903a6770831e08c2";
    private Handler mHandler;
    RecyclerView images ;
    RecyclerView variations;
    private LinearLayout addcart;
    Button buynow;
    WebView descriptor;
    ImageView mainimg;
    TextView price;
    TextView name;
    TextView varSelected;
    Storefront.Product product;
    View cartnow;
    Context context;
    RelativeLayout detail_sale_layer;
    TextView detail_cuttedprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        price = findViewById(R.id.pro_price);
        name= findViewById(R.id.pro_name);
        //String pid= getIntent().getStringExtra("pid") ;
        cartnow = findViewById(R.id.product_cart_icon);
        mHandler = new Handler(Looper.getMainLooper());
        images= findViewById(R.id.images_recyclerview);
        variations=findViewById(R.id.variations_recyclerview);
        context = this;
        detail_sale_layer = findViewById(R.id.detail_sale_layer);
        addcart= findViewById(R.id.add_to_cart_btn);
        buynow= findViewById(R.id.buy_now_btn);
        descriptor= findViewById(R.id.product_detail);
        mainimg= findViewById(R.id.main_pro_img);
        varSelected= findViewById(R.id.var_name);
        detail_cuttedprice = findViewById(R.id.detail_cuttedprice);

        String pid = getIntent().getStringExtra("producr_id");

        GraphClient client = GraphClient.builder(this)
                .shopDomain(SHOP_DOMAIN)
                .accessToken(API_KEY)
                .build();
        //"Z2lkOi8vc2hvcGlmeS9Qcm9kdWN0LzQ1NzI0MTExMzQwMjM="
        Storefront.QueryRootQuery query = Storefront.query(rootQuery -> rootQuery
                .node(new ID(pid), nodeQuery -> nodeQuery
                        .onProduct(productQuery -> productQuery
                                .title()
                                .description()
                                .descriptionHtml()


                                .images(arg -> arg.first(10), imageConnectionQuery -> imageConnectionQuery
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
                                                        .product(Storefront.ProductQuery::getClass)
                                                        .availableForSale()
                                                        .compareAtPrice()
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
                        Log.e("TAG", "PRO" + response.formatErrorMessage());
                        product = (Storefront.Product) response.data().getNode();
                        descriptor.getSettings().setJavaScriptEnabled(true);
                        descriptor.loadData(product.getDescriptionHtml(), "text/html; charset=utf-8", "UTF-8");
                        name.setText(product.getTitle());

                        //descriptor.setText(HtmlCompat.fromHtml(product.getDescriptionHtml(), 0));
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            descriptor.setText(Html.fromHtml(product.getDescription(), Html.FROM_HTML_MODE_COMPACT));
//                        } else {
//                            descriptor.setText(Html.fromHtml(product.getDescription()));
//                        }

                        List<Storefront.Image> productImages = new ArrayList<>();
                        for (final Storefront.ImageEdge imageEdge : product.getImages().getEdges()) {
                            productImages.add(imageEdge.getNode());
                        }
                        RequestOptions myOptions = new RequestOptions()
                                .override(500, 500);
                        Glide.with(getApplicationContext()).load(productImages.get(0).getSrc()).fitCenter().apply(myOptions).placeholder(R.drawable.white_circular_border).into(mainimg);

                        imagesAdapter allimages = new imagesAdapter(productImages, mainimg);
                        images.setAdapter(allimages);



                        List<Storefront.ProductVariant> productVariants = new ArrayList<>();
                        for (final Storefront.ProductVariantEdge productVariantEdge : product.getVariants().getEdges()) {
                            productVariants.add(productVariantEdge.getNode());

                        }
                        int intRate = productVariants.get(0).getPrice().intValue();
                        String pr = Integer.toString(intRate);
                        price.setText("Rs "+pr);
                        if(productVariants.size()==1){

                            variantsAdapter allvariants = new variantsAdapter(productVariants, price,varSelected,detail_sale_layer, detail_cuttedprice);
                            variations.setVisibility(View.GONE);
                            if(productVariants.get(0).getAvailableForSale() && productVariants.get(0).getCompareAtPrice() != null){
                                detail_cuttedprice.setText("Rs "+productVariants.get(0).getCompareAtPrice().toString());
                            }else{
                                detail_sale_layer.setVisibility(View.INVISIBLE);
                            }
                        }else{
                            varSelected.setVisibility(View.VISIBLE);
                            variantsAdapter allvariants = new variantsAdapter(productVariants, price,varSelected,detail_sale_layer, detail_cuttedprice);
                            variations.setAdapter(allvariants);
                        }



                    }
                });

            }

            @Override
            public void onFailure(@NonNull GraphError error) {

            }


        });






        addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent addAddrIntent = new Intent(productDetails.this, cartDetails.class);
                //startActivity(addAddrIntent);
                //Store Vriant obj to localDB
                new PrefManager(v.getContext()).addCartItem(variantsAdapter.last_variantid.getProduct().getId(),variantsAdapter.last_variantid.getId().toString(),1);
            }
        });
        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Store Vriant obj to localDB
                new PrefManager(v.getContext()).addCartItem(variantsAdapter.last_variantid.getProduct().getId(),variantsAdapter.last_variantid.getId().toString(),1);
                Intent addAddrIntent = new Intent(productDetails.this, cartDetails.class);
                startActivity(addAddrIntent);


            }
        });
        cartnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tocart = new Intent(context, cartDetails.class );
                context.startActivity(tocart);
            }
        });

    }
}
