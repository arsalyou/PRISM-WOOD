package com.example.prismwood;

import java.util.List;

public class myCart {
    int cartTotal;
    int shippingTotal;
    List<cartProducts> allCartProducts;

    public myCart(int cartTotal, int shippingTotal, List<cartProducts> allCartProducts) {
        this.cartTotal = cartTotal;
        this.shippingTotal = shippingTotal;
        this.allCartProducts = allCartProducts;
    }

    public int getCartTotal() {
        return cartTotal;
    }

    public void setCartTotal(int cartTotal) {
        this.cartTotal = cartTotal;
    }

    public int getShippingTotal() {
        return shippingTotal;
    }

    public void setShippingTotal(int shippingTotal) {
        this.shippingTotal = shippingTotal;
    }

    public List<cartProducts> getAllCartProducts() {
        return allCartProducts;
    }

    public void setAllCartProducts(List<cartProducts> allCartProducts) {
        this.allCartProducts = allCartProducts;
    }
}
