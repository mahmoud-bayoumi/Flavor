package com.example.flavor.presentation.main.home;

import com.example.flavor.data.model.Recipe;
import com.example.flavor.data.repo.MealRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View view;
    private final MealRepository repository;
    private final CompositeDisposable disposable = new CompositeDisposable();

    public HomePresenter(HomeContract.View view) {
        this.view = view;
        repository = new MealRepository();
    }

    @Override
    public void loadRandomMeal() {
        if (view == null) return;

        view.showLoading();
        disposable.add(
                repository.getRandomMeal()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                recipe -> {
                                    if (view == null) return;
                                    view.hideLoading();
                                    view.showRandomMeal(recipe);
                                },
                                throwable -> {
                                    if (view != null) view.hideLoading();
                                }
                        )
        );
    }

    @Override
    public void loadMealsByCategory(String category) {
        if (view == null) return;

        view.showLoading();
        disposable.add(
                repository.getMealsByCategory(category)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                recipes -> {
                                    if (view == null) return;
                                    view.hideLoading();
                                    view.showRecipes(recipes);
                                },
                                throwable -> {
                                    if (view != null) view.hideLoading();
                                }
                        )
        );
    }

    @Override
    public void onRecipeClicked(Recipe recipe) {
        if (view != null) view.navigateToDetails(recipe);
    }

    @Override
    public void clear() {
        disposable.clear();
        view = null;
    }
}