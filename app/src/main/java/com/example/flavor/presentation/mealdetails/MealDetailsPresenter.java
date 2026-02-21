package com.example.flavor.presentation.mealdetails;

import com.example.flavor.R;
import com.example.flavor.data.model.Ingredient;
import com.example.flavor.data.model.Meal;
import com.example.flavor.data.model.Recipe;
import com.example.flavor.data.repo.MealRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MealDetailsPresenter implements MealDetailsContract.Presenter {

    private final MealDetailsContract.View view;
    private final MealRepository repository = new MealRepository();

    public MealDetailsPresenter(MealDetailsContract.View view) {
        this.view = view;
    }

    @Override
    public void loadMealDetails(String mealId) {
        repository.getMealDetails(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleMealSuccess, Throwable::printStackTrace);
    }

    private void handleMealSuccess(Meal meal) {

        // Basic meal info
        Recipe recipe = new Recipe(
                meal.idMeal,
                meal.strMeal,
                meal.strCategory,
                meal.strMealThumb,
                meal.strYoutube,
                meal.strInstructions
        );
        view.showMealDetails(recipe);

        List<String> steps = formatSteps(meal.strInstructions);
        view.showSteps(steps);


    }

    private List<String> formatSteps(String instructions) {
        List<String> steps = new ArrayList<>();

        if (instructions == null || instructions.trim().isEmpty()) {
            return steps;
        }

        // Clean text
        instructions = instructions.replace("\r\n", ". ");
        instructions = instructions.replace("\n", ". ");

        String[] rawSteps = instructions.split("\\.");

        for (String step : rawSteps) {
            step = step.trim();
            if (!step.isEmpty()) {
                steps.add(step);
            }
        }
        return steps;
    }
    private void addIngredient(List<Ingredient> list, String name, String measure) {
        if (name != null && !name.trim().isEmpty()) {
            list.add(new Ingredient(
                    name + " (" + measure + ")",
                    R.drawable.ic_food
            ));
        }
    }

    @Override
    public void addToPlanner(String recipeId) {
        view.onAddedToPlanner();
    }

    @Override
    public void toggleFavorite(String recipeId) {
        // TODO later
    }
}