package com.example.flavor.presentation.main.home;

import com.example.flavor.data.model.Recipe;
import com.example.flavor.data.repo.MealRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter implements HomeContract.Presenter {
    private final HomeContract.View view;
    private final MealRepository repository;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public HomePresenter(HomeContract.View view) {
        this.view = view;
        repository = new MealRepository();
    }

    @Override
    public void loadRandomMeal() {
        view.showLoading();
        disposable.add(
                repository.getRandomMeal()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(recipe -> {
                            view.hideLoading();
                            view.showRandomMeal(recipe);
                        }, throwable -> view.hideLoading())
        );
    }

    @Override
    public void loadMealsByCategory(String category) {
        view.showLoading();
        disposable.add(
                repository.getMealsByCategory(category)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(recipes -> {
                            view.hideLoading();
                            view.showRecipes(recipes);
                        }, throwable -> view.hideLoading())
        );
    }

    @Override
    public void onRecipeClicked(Recipe recipe) {
        view.navigateToDetails(recipe);
    }

    @Override
    public void clear() {
        disposable.clear();
    }
}