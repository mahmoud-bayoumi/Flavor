package com.example.flavor.presentation.mealdetails;


import com.example.flavor.data.model.Ingredient;
import com.example.flavor.data.model.Recipe;
import java.util.List;

public interface MealDetailsContract {
    interface View {
        void showMealDetails(Recipe recipe);
        void showIngredients(List<Ingredient> ingredients);
        void showSteps(List<String> steps);
        void onAddedToPlanner();
    }

    interface Presenter {
        void loadMealDetails(String recipeId);
        void addToPlanner(String recipeId);
        void toggleFavorite(String recipeId);
    }
}