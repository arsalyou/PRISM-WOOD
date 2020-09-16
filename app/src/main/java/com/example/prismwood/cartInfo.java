package com.example.prismwood;

import com.shopify.graphql.support.ID;

public class cartInfo {
    ID productid;
    String cartid;
    int quantity;

    public cartInfo(ID productid, String cartid, int quantity) {
        this.productid = productid;
        this.cartid = cartid;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public ID getProductid() {
        return productid;
    }

    public void setProductid(ID productid) {
        this.productid = productid;
    }

    public String getCartid() {
        return cartid;
    }

    public void setCartid(String cartid) {
        this.cartid = cartid;
    }
}
