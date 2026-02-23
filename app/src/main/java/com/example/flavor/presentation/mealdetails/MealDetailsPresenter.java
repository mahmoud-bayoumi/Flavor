package com.example.flavor.presentation.mealdetails;

import android.content.Context;

import com.example.flavor.data.model.Meal;
import com.example.flavor.data.model.Recipe;
import com.example.flavor.data.repo.FavoriteRepository;
import com.example.flavor.data.repo.MealRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MealDetailsPresenter implements MealDetailsContract.Presenter {

    private MealDetailsContract.View view;
    private final MealRepository mealRepository;
    private final FavoriteRepository favoriteRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public MealDetailsPresenter(MealDetailsContract.View view, Context context) {
        this.view = view;
        this.mealRepository = new MealRepository();
        this.favoriteRepository = new FavoriteRepository(context);
    }

    @Override
    public void loadMealDetails(String mealId) {
        disposables.add(
                mealRepository.getMealDetails(mealId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleMealSuccess, Throwable::printStackTrace)
        );
    }

    private void handleMealSuccess(Meal meal) {
        Recipe recipe = new Recipe(
                meal.idMeal,
                meal.strMeal,
                meal.strCategory,
                meal.strMealThumb,
                meal.strYoutube,
                meal.strInstructions
        );

        disposables.add(
                favoriteRepository.isFavorite(recipe.getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(isFav -> {
                            recipe.setFavorite(isFav);
                            if (view != null) {
                                view.showMealDetails(recipe);
                            }
                        })
        );

        if (view != null) {
            view.showSteps(formatSteps(meal.strInstructions));
        }
    }

    private List<String> formatSteps(String instructions) {
        List<String> steps = new ArrayList<>();
        if (instructions == null || instructions.trim().isEmpty()) return steps;

        instructions = instructions.replace("\r\n", ". ").replace("\n", ". ");

        String[] rawSteps = instructions.split("\\.");
        for (String step : rawSteps) {
            step = step.trim();
            if (!step.isEmpty()) {
                steps.add(step);
            }
        }
        return steps;
    }

    @Override
    public void toggleFavorite(Recipe recipe) {
        disposables.add(
                favoriteRepository.isFavorite(recipe.getId())
                        .subscribeOn(Schedulers.io())
                        .flatMapCompletable(isFav -> {
                            recipe.setFavorite(!isFav);
                            return isFav
                                    ? favoriteRepository.removeFromFavorites(recipe)
                                    : favoriteRepository.addToFavorites(recipe);
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            if (view != null) {
                                view.updateFavoriteIcon(recipe.isFavorite());
                                view.onFavoriteUpdated(recipe.getId(), recipe.isFavorite());
                            }
                        })
        );
    }

    @Override
    public void detach() {
        disposables.clear();
        view = null;
    }
}