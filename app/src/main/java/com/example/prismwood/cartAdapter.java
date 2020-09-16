package com.example.prismwood;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shopify.buy3.Storefront;

import java.math.BigDecimal;
import java.util.List;

public class cartAdapter  extends RecyclerView.Adapter<cartAdapter.ViewHolder> {
    List<cartModal> cartItemModalList ;
    Context mcontext;

    public cartAdapter(List<cartModal> cartItemModalList, Context context) {
        this.cartItemModalList = cartItemModalList;
        this.mcontext = context;
    }

    public cartAdapter(List<cartModal> cartItemModalList) {
        this.cartItemModalList = cartItemModalList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_view,parent,false);
        return new cartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setdetails(position, cartItemModalList.get(position).getQuantity(),cartItemModalList.get(position).getProduct().getTitle() , cartItemModalList.get(position).getProduct_variant().getPrice(),cartItemModalList.get(position).getProduct_variant().getTitle() ,cartItemModalList.get(position).getProduct_variant().getImage().getSrc() );


    }

    @Override
    public int getItemCount() {
        if(cartItemModalList != null) {
            return cartItemModalList.size();
        }else{
            return 0;
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView product_title;
        private TextView product_price;
        private ImageView product_image;
        private TextView product_remove;
        private TextView product_qty;
        private TextView variation_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_image = itemView.findViewById(R.id.cart_pro_image);
            product_title = itemView.findViewById(R.id.cart_pro_title);
            product_price = itemView.findViewById(R.id.cart_pro_price);
            product_remove = itemView.findViewById(R.id.cart_pro_remove);
            product_qty = itemView.findViewById(R.id.cart_pro_qty);
            variation_name = itemView.findViewById(R.id.cart_variatioin_name);

        }

        int find_total(){
            int total_amount=0;
            for(int i=0 ; i < cartItemModalList.size(); i++){
                BigDecimal price = cartItemModalList.get(i).getProduct_variant().getPrice();
                int actual_price = price.intValue();
                total_amount=total_amount+cartItemModalList.get(i).getQuantity()*actual_price;
            }
            return total_amount;
        }

        public void setdetails(final int i, int qty, String title, BigDecimal productPriceText, String variation_title, String img_src){
            product_qty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(cartDetails.context);
                    builder.setTitle("Quantity");

                    // Set up the input
                    final EditText input = new EditText(itemView.getContext());
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_NUMBER );
                    builder.setView(input);
                    // Set up the buttons

                    builder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            int newQty=  Integer.parseInt(input.getText().toString());
                            String l_converted_qty = Integer.toString(newQty);
                            product_qty.setText("Qty : "+l_converted_qty);
                            if(newQty > 1 ) {
                                cartItemModalList.get(i).setQuantity(Integer.parseInt(input.getText().toString()));


                                //cartDetails.refreshItem(i);
                                int total_amount=0;
//                                for(int i=0 ; i < cartItemModalList.size(); i++){
//                                    BigDecimal price = cartItemModalList.get(i).getProduct_variant().getPrice();
//                                    int actual_price = price.intValue();
//                                    total_amount=total_amount+cartItemModalList.get(i).getQuantity()*actual_price;
//                                }
                                total_amount=find_total();
                                cartDetails.total_amt.setText("Rs "+Integer.toString(total_amount));
                                getAdapterPosition();
                            }
                            //cartItemModalList.get(cartItemModalList.size()-1).
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    Button b = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                    Button c = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    if(b != null) {
                        b.setBackgroundColor(Color.RED);
                    }
                    if(c != null) {
                        c.setBackgroundColor(Color.DKGRAY);
                    }


                }
            });
            product_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Delete item
                    Storefront.ProductVariant vid =  cartItemModalList.get(i).getProduct_variant();
                    cartItemModalList.remove(i);
                    //cartDetails.cartRecycler.removeViewAt(i);
                    //cartDetails.CartAdapter.notifyItemRemoved(i);
                    cartDetails.cartRecycler.getAdapter().notifyItemRemoved(i);

                    //cartDetails.CartAdapter.notifyDataSetChanged();
                    //cartDetails.CartAdapter.notifyItemRemoved(i);
                    //cartDetails.CartAdapter.notifyItemRangeChanged(i, cartItemModalList.size());
                    int total_amount=0;
                    total_amount=find_total();
                    cartDetails.total_amt.setText("Rs "+total_amount);
                    new PrefManager(mcontext).removeFromCart(i);



                }
            });
            product_title.setText(title);
            int price = productPriceText.intValue();
            product_price.setText("Rs " +Integer.toString(price));
            if (variation_title.contains("Default")){
                variation_name.setVisibility(View.GONE);
            }else{
                variation_name.setText(variation_title);
            }

            int total_amount=find_total();
            String converted_qty =Integer.toString(total_amount);
            cartDetails.total_amt.setText("Rs " + converted_qty);
            product_qty.setText("Qty : "+Integer.toString(qty));
            RequestOptions myOptions = new RequestOptions()
                    .override(200, 200);
            Glide.with(itemView.getContext()).load(img_src).fitCenter().apply(myOptions).placeholder(R.drawable.white_circular_border).into(product_image);


        }


    }
}
