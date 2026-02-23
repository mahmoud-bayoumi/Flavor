package com.example.flavor.presentation.main.favorites;

import com.example.flavor.data.local.entities.FavoriteRecipe;
import com.example.flavor.data.model.Recipe;

import java.util.List;

public interface FavoritesContract {

    interface View {
        void showFavorites(List<FavoriteRecipe> recipes);
        void showEmptyState();
        void onRecipeDeleted(int position);
        void showError(String message);
    }

    interface Presenter {
        void loadFavorites();
        void deleteRecipe(FavoriteRecipe recipe, int position);
        void detach(); // prevent memory leaks
    }
}