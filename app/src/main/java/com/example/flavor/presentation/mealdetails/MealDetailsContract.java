package com.example.flavor.presentation.mealdetails;


import com.example.flavor.data.model.Ingredient;
import com.example.flavor.data.model.Recipe;
import java.util.List;

public interface MealDetailsContract {

    interface View {
        void showMealDetails(Recipe recipe);
        void showSteps(List<String> steps);
        void updateFavoriteIcon(boolean isFavorite);
        void onFavoriteUpdated(String mealId, boolean isFavorite);
    }

    interface Presenter {
        void loadMealDetails(String recipeId);
        void toggleFavorite(Recipe recipe);
        void detach();
    }
}