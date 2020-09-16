package com.example.prismwood;

public class cartProducts {
    String productid;
    String vriationId;
    String product_var_name;
    int price;
    int qty;
    String imgUrl;

    public cartProducts(String productid, String product_var_name, int price, int qty, String imgUrl) {
        this.productid = productid;
        this.product_var_name = product_var_name;
        this.price = price;
        this.qty = qty;
        this.imgUrl = imgUrl;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getProduct_var_name() {
        return product_var_name;
    }

    public void setProduct_var_name(String product_var_name) {
        this.product_var_name = product_var_name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
