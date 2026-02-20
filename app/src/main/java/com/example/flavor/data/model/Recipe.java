package com.example.flavor.data.model;

import java.io.Serializable;

public class Recipe implements Serializable {

    private String title;
    private String price;
    private String category;
    private String imageUrl;
     public Recipe(String title, String price, String category, String imageUrl ) {
        this.title = title;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
       }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
