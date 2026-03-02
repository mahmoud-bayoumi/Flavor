package com.example.flavor.presentation.main.home;

import com.example.flavor.data.model.Category;
import com.example.flavor.data.model.Recipe;

import java.util.List;

public interface HomeContract {

    interface View {
        void showRecipes(List<Recipe> recipes);
        void showRandomMeal(Recipe recipe);
        void showLoading();
        void hideLoading();
        void navigateToDetails(Recipe recipe);
        void showCategories(List<Category> categories);
        void showSearchResults(List<Recipe> recipes);
        void showError(String message);
    }

    interface Presenter {
        void loadRandomMeal();
        void loadMealsByCategory(String category);
        void onRecipeClicked(Recipe recipe);
        void loadCategories();
        void searchMeals(String query);
        void clear();
    }
}