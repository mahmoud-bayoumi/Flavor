package com.example.flavor.data.local.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_recipes")
public class FavoriteRecipe {

    @PrimaryKey
    @NonNull
    private String id;
    private String title;
    private String category;
    private String imageUrl;
    private String youtubeUrl;

    public FavoriteRecipe(@NonNull String id, String title, String category, String imageUrl, String youtubeUrl ) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.imageUrl = imageUrl;
        this.youtubeUrl = youtubeUrl;
    }

    @NonNull
    public String getId() { return id; }

    public String getTitle() { return title; }

    public String getCategory() { return category; }

    public String getImageUrl() { return imageUrl; }

    public String getYoutubeUrl() { return youtubeUrl; }
}