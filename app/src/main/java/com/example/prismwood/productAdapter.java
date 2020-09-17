package com.example.prismwood;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shopify.buy3.Storefront;

import java.math.BigDecimal;
import java.util.List;

public class productAdapter extends RecyclerView.Adapter<productAdapter.ViewHolder> {
    List<Storefront.Product> products;


    public productAdapter(List<Storefront.Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singleproductitem,parent,false);
        return new ViewHolder(view);
        //products.get(0).getVariants().

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //List<String> images = mapItems(products.get(position).getImages().getEdges(), imageEdge -> imageEdge.getNode().getSrc());
        //holder.setdetails(products.get(position).getTitle(), products.get(position).getId(), products.get(position).getPriceRange().getMinVariantPrice().getAmount());
        String name = products.get(position).getTitle();
        String id =  products.get(position).getId().toString();
        String img = products.get(position).getImages().getEdges().get(0).getNode().getSrc();
        List<Storefront.ImageEdge> images =products.get(position).getImages() != null ? products.get(position).getImages().getEdges() : null;
        //images.get(0).getNode().
        BigDecimal price = products.get(position).getVariants().getEdges().get(0).getNode().getPrice();
        int cutRate = -1;
        if (products.get(position).getVariants().getEdges().get(0).getNode().getAvailableForSale()){
            BigDecimal cutted_price = products.get(position).getVariants().getEdges().get(0).getNode().getCompareAtPrice();
            cutRate = cutted_price.intValue();
        }

        int intRate = price.intValue();
        String pr = Integer.toString(intRate);
        holder.setdetails(name, id , img,pr, cutRate );

    }

    @Override
    public int getItemCount() {

        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView img;
        private TextView sale_price;
        private TextView old_price;
        Integer id;
        private RelativeLayout sale_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title= itemView.findViewById(R.id.pro_name);
            img = itemView.findViewById(R.id.pro_img);
            old_price = itemView.findViewById(R.id.cutted_price);
            sale_view = itemView.findViewById(R.id.sale_view);
            sale_price= itemView.findViewById(R.id.pro_price);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailIntent = new Intent(itemView.getContext(),productDetails.class);
                    productDetailIntent.putExtra("producr_id", products.get(getAdapterPosition()).getId().toString());

                    itemView.getContext().startActivity(productDetailIntent);
                }
            });
        }
        public void setdetails(String tit,String id, String im, String sale, int cutrate){
            title.setText(tit);
            RequestOptions myOptions = new RequestOptions()
                    .override(500, 500);
            Glide.with(itemView.getContext()).load(im).fitCenter().apply(myOptions).placeholder(R.drawable.white_circular_border).into(img);

            sale_price.setText("Rs "+sale);
            if( cutrate == -1){
                sale_view.setVisibility(View.INVISIBLE);
            }else{
                old_price.setText(Integer.toString(cutrate));
            }
        }
    }
}
