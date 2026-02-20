package com.example.flavor.data.model;

import java.io.Serializable;

public class Recipe implements Serializable {

    private String title;
    private String price;
    private String rating;
    private String imageUrl;
    private String category;

    public Recipe(String title, String price, String rating, String imageUrl , String category) {
        this.title = title;
        this.price = price;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getRating() {
        return rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public String getCategory() {
        return category;
    }
}
