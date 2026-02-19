package com.example.flavor.presentation.mealdetails;


import com.example.flavor.R;
import com.example.flavor.data.model.Recipe;
import java.util.ArrayList;
import java.util.List;

public class MealDetailsPresenter implements MealDetailsContract.Presenter {
    private MealDetailsContract.View view;

    public MealDetailsPresenter(MealDetailsContract.View view) {
        this.view = view;
    }

    @Override
    public void loadMealDetails(String recipeId) {
        // Mocking Beef Wellington data
        Recipe recipe = new Recipe("Beef Wellington", "45 mins", "850 kcal", R.drawable.ic_launcher_background);
        view.showMealDetails(recipe);

        // Mocking Steps
        List<String> steps = new ArrayList<>();
        steps.add("Season the beef fillet with salt and pepper...");
        steps.add("Finely chop mushrooms and cook until moisture has evaporated...");
        steps.add("Layer prosciutto on plastic wrap, spread duxelles, place beef in center, and roll tightly. Chill for 30 minutes.");

        view.showSteps(steps);
    }

    @Override
    public void addToPlanner(String recipeId) {
        // Business logic for planner
        view.onAddedToPlanner();
    }

    @Override
    public void toggleFavorite(String recipeId) { /* Handle favorite click */ }
}