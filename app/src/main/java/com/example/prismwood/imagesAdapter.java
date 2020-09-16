package com.example.prismwood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shopify.buy3.Storefront;

import java.util.List;

public class imagesAdapter extends  RecyclerView.Adapter<imagesAdapter.ViewHolder> {

    List<Storefront.Image> productImages;
    private Context mContext;
    ImageView mainimg;

    public imagesAdapter(List<Storefront.Image> productImages, ImageView mainimg) {
        this.productImages = productImages;
        this.mainimg = mainimg;
    }

    public imagesAdapter(List<Storefront.Image> productImages, Context mContext) {
        this.productImages = productImages;
        this.mContext = mContext;
    }

    public imagesAdapter(List<Storefront.Image> productImages) {
        this.productImages = productImages;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.small_image,parent,false);
        return new imagesAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String src = productImages.get(position).getSrc();
        holder.setimg(src);



    }

    @Override
    public int getItemCount() {
        return productImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon= itemView.findViewById(R.id.product_icon);
            //mainimg= itemView.findViewById(R.id.main_pro_img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Glide.with(itemView.getContext()).load(productImages.get(getAdapterPosition()).getSrc()).fitCenter().placeholder(R.drawable.white_circular_border).into(mainimg);
                }
            });

        }
        public void setimg(String src){
            Glide.with(itemView.getContext()).load(src).fitCenter().placeholder(R.drawable.white_circular_border).into(icon);

        }
    }
}
