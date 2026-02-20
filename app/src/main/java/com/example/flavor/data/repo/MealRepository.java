package com.example.flavor.data.repo;

import com.example.flavor.data.model.Meal;
import com.example.flavor.data.model.Recipe;
import com.example.flavor.data.remote.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;



public class MealRepository {

    public Single<Recipe> getRandomMeal() {
        return RetrofitClient.getApi()
                .getRandomMeal()
                .map(response -> {
                    Meal meal = response.meals.get(0);

                    return new Recipe(
                            meal.strMeal,
                            "Free",
                            meal.strCategory,
                            meal.strMealThumb,
                            meal.strCategory
                    );
                });
    }
    public Single<List<Recipe>> getMealsByCategory(String category) {
        return RetrofitClient.getApi()
                .getMealsByCategory(category)
                .map(response -> {
                    List<Recipe> recipes = new ArrayList<>();

                    for (Meal meal : response.meals) {
                        recipes.add(new Recipe(
                                meal.strMeal,
                                "Free",
                                category,
                                meal.strMealThumb,
                                category
                        ));
                    }

                    return recipes;
                });
    }
}
