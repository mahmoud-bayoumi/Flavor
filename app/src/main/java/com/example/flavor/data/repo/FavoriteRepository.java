package com.example.flavor.data.repo;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.example.flavor.data.local.AppDatabase;
import com.example.flavor.data.local.entities.FavoriteRecipe;
import com.example.flavor.data.model.Recipe;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class FavoriteRepository {

    private final AppDatabase db;

    public FavoriteRepository(Context context) {
        db = Room.databaseBuilder(context, AppDatabase.class, "flavor_db").build();
    }

    public Completable addToFavorites(Recipe recipe) {
        FavoriteRecipe fav = new FavoriteRecipe(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getCategory(),
                recipe.getImageUrl(),
                recipe.getYoutubeUrl()
        );
        return db.favoriteRecipeDao().insert(fav);
    }

    public Completable removeFromFavorites(Recipe recipe) {
        FavoriteRecipe fav = new FavoriteRecipe(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getCategory(),
                recipe.getImageUrl(),
                recipe.getYoutubeUrl()
        );

        return db.favoriteRecipeDao().delete(fav);
    }

    public Single<List<FavoriteRecipe>> getAllFavorites() {
        return db.favoriteRecipeDao().getAllFavorites();
    }

    public Single<Boolean> isFavorite(String recipeId) {
        return db.favoriteRecipeDao().isFavorite(recipeId);
    }
}