package com.example.prismwood;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopify.buy3.Storefront;

import java.util.List;

public class variantsAdapter  extends  RecyclerView.Adapter<variantsAdapter.ViewHolder>  {
    List<Storefront.ProductVariant> productVariants;

    public static Storefront.ProductVariant last_variantid;
    TextView price;
    TextView variation;
    int prev=0;
    int curr=0;
    RelativeLayout sale_layout;
    TextView sale_price;


    public variantsAdapter(List<Storefront.ProductVariant> productVariants, TextView price, TextView varselected, RelativeLayout sale_layout, TextView sale_price) {
        this.productVariants = productVariants;
        this.price = price;
        last_variantid= productVariants.get(0);
        variation = varselected;
        this.sale_layout = sale_layout;
        this.sale_price = sale_price;
    }

    public variantsAdapter(List<Storefront.ProductVariant> productVariants) {
        this.productVariants = productVariants;
        last_variantid= productVariants.get(0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.small_text,parent,false);
        return new variantsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title= productVariants.get(position).getTitle();
        String price = productVariants.get(position).getPrice().toString();

        String img = productVariants.get(position).getImage().getSrc();
        productVariants.get(position).getProduct().getTitle();



        holder.setPrice(title, price);

    }

    @Override
    public int getItemCount() {
        return productVariants.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button variant ;
        TextView variant_price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            variant_price=itemView.findViewById(R.id.pro_price);

            variant=itemView.findViewById(R.id.variant_button);
            variant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prev = curr;
                   int pos= getAdapterPosition();
                   curr=pos;
                   price.setText("Rs "+productVariants.get(pos).getPrice().toString());
                   last_variantid= productVariants.get(pos);
                   variation.setText(productVariants.get(pos).getTitle());
                   if(productVariants.get(pos).getAvailableForSale()){
                       sale_price.setText(productVariants.get(pos).getCompareAtPrice().toString());
                   }else{
                       sale_layout.setVisibility(View.INVISIBLE);
                   }



                }
            });
        }
        public void setPrice(String name, String pri){
            variant.setText(name);
            price.setText(pri);


        }

    }
}
