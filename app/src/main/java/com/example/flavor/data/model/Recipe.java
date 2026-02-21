package com.example.flavor.data.model;

import java.io.Serializable;

public class Recipe implements Serializable {

    private String id;
    private String title;
    private String category;
    private String imageUrl;
    private String youtubeUrl;
    private String instructions;

    public Recipe(String id, String title, String category,
                  String imageUrl, String youtubeUrl, String instructions) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.imageUrl = imageUrl;
        this.youtubeUrl = youtubeUrl;
        this.instructions = instructions;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getImageUrl() { return imageUrl; }
    public String getYoutubeUrl() { return youtubeUrl; }
    public String getInstructions() { return instructions; }

    public String getPrice(){ return "Free"; }
}