package com.example.flavor.data.model;
public class Recipe {
    private String title;
    private String price;
    private String rating;
    private int imageResId;

    public Recipe(String title, String price, String rating, int imageResId) {
        this.title = title;
        this.price = price;
        this.rating = rating;
        this.imageResId = imageResId;
    }

    // Getters
    public String getTitle() { return title; }
    public String getPrice() { return price; }
    public String getRating() { return rating; }
    public int getImageResId() { return imageResId; }
}