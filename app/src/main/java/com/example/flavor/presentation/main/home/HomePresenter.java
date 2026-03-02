package com.example.flavor.presentation.main.home;

import com.example.flavor.data.model.Recipe;
import com.example.flavor.data.repo.CategoryRepository;
import com.example.flavor.data.repo.MealRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View view;

    private final MealRepository mealRepository;
    private final CategoryRepository categoryRepository;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public HomePresenter(HomeContract.View view) {
        this.view = view;
        this.mealRepository = new MealRepository();
        this.categoryRepository = CategoryRepository.getInstance();
    }

    @Override
    public void loadRandomMeal() {
        compositeDisposable.add(
                mealRepository.getRandomMeal()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                recipe -> {
                                    if (view != null) view.showRandomMeal(recipe);
                                },
                                throwable -> {
                                    if (view != null) view.showError("Failed to load meal");
                                }
                        )
        );
    }

    @Override
    public void loadCategories() {
        compositeDisposable.add(
                categoryRepository.getCategories()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                categories -> {
                                    if (view == null) return;
                                    view.showCategories(categories);
                                    if (!categories.isEmpty()) {
                                        loadMealsByCategory(categories.get(0).getStrCategory());
                                    }
                                },
                                throwable -> {
                                    if (view != null) view.showError("Failed to load categories");
                                }
                        )
        );
    }

    @Override
    public void loadMealsByCategory(String category) {
        compositeDisposable.add(
                mealRepository.getMealsByCategory(category)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                recipes -> {
                                    if (view != null) view.showRecipes(recipes);
                                },
                                throwable -> {
                                    if (view != null) view.showError("Failed to load recipes");
                                }
                        )
        );
    }

    @Override
    public void searchMeals(String query) {
        compositeDisposable.add(
                mealRepository.searchMealsByName(query)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                recipes -> {
                                    if (view != null) view.showSearchResults(recipes);
                                },
                                throwable -> {
                                    if (view != null) view.showError("Search failed");
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
        compositeDisposable.clear();
        view = null;
    }
}