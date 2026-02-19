package com.example.flavor.data.model;

public class Ingredient {
    private String name;
    private int imageRes;

    public Ingredient(String name, int imageRes) {
        this.name = name;
        this.imageRes = imageRes;
    }

    public String getName() { return name; }
    public int getImageRes() { return imageRes; }
}