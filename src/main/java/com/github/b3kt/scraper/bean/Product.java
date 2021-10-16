package com.github.b3kt.scraper.bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Product {

    private String productName;
    private String description;
    private String imageLink;
    private String price;
    private String rating;
    private String merchantName;

    public Product() {
    }

    public Product(String productName, String description, String imageLink, String price, String rating,
            String merchantName) {
        this.productName = productName;
        this.description = description;
        this.imageLink = imageLink;
        this.price = price;
        this.rating = rating;
        this.merchantName = merchantName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
    
    public String toString() {
    	Gson gson = new GsonBuilder().setPrettyPrinting().create();
    	return gson.toJson(this);
    }
    
    

}
