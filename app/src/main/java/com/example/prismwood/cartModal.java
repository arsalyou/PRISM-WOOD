package com.example.prismwood;

import com.shopify.buy3.Storefront;

public class cartModal {
    int quantity;
    Storefront.Product product;
    Storefront.ProductVariant selected_variant;

    public cartModal() {
    }

    public Storefront.ProductVariant getProduct_variant() {
        return selected_variant;
    }

    public void setProduct_variant(Storefront.ProductVariant selected_variant) {
        this.selected_variant = selected_variant;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Storefront.Product getProduct() {
        return product;
    }

    public void setProduct(Storefront.Product product) {
        this.product = product;
    }

    public cartModal(int quantity, Storefront.Product product, Storefront.ProductVariant selected_variant) {
        this.quantity = quantity;
        this.product = product;
        this.selected_variant = selected_variant;
    }
}
