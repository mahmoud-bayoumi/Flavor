package com.example.flavor.data.repo;

import android.content.Context;

import com.example.flavor.core.storage.PrefsManager;
import com.example.flavor.data.local.AppDatabase;
import com.example.flavor.data.local.FavoriteRecipeDao;
import com.example.flavor.data.local.entities.FavoriteRecipe;
import com.example.flavor.data.model.Recipe;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class FavoriteRepository {

    private final FavoriteRecipeDao dao;
    private final PrefsManager prefsManager;

    public FavoriteRepository(Context context) {
        dao = AppDatabase.getInstance(context).favoriteRecipeDao();
        prefsManager = PrefsManager.getInstance(context);
    }


    private String getCurrentUserId() {
        return prefsManager.getLoggedInUser();
    }

    public Completable addToFavorites(Recipe recipe) {
        FavoriteRecipe fav = new FavoriteRecipe(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getCategory(),
                recipe.getImageUrl(),
                recipe.getYoutubeUrl(),
                getCurrentUserId()
        );
        return dao.insert(fav);
    }

    public Completable removeFromFavorites(Recipe recipe) {
        FavoriteRecipe fav = new FavoriteRecipe(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getCategory(),
                recipe.getImageUrl(),
                recipe.getYoutubeUrl(),
                getCurrentUserId()
        );
        return dao.delete(fav);
    }

    public Single<List<FavoriteRecipe>> getAllFavorites() {
        return dao.getAllFavorites(getCurrentUserId());
    }

    public Single<Boolean> isFavorite(String recipeId) {
        return dao.isFavorite(recipeId, getCurrentUserId());
    }
    public Completable removeFromFavorites(FavoriteRecipe recipe) {
        return dao.delete(recipe);
    }

}