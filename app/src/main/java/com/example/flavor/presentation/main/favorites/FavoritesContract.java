package com.example.flavor.presentation.main.favorites;

import com.example.flavor.data.model.Recipe;

import java.util.List;

public interface FavoritesContract {
    interface View {
        void showFavorites(List<Recipe> recipes);
        void showEmptyState();
        void onRecipeDeleted(int position);
    }

    interface Presenter {
        void loadFavorites();
        void deleteRecipe(int position);
    }
}