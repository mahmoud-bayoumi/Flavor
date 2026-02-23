package com.example.flavor.presentation.main.favorites;

import com.example.flavor.data.local.AppDatabase;
import com.example.flavor.data.local.entities.FavoriteRecipe;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FavoritesPresenter implements FavoritesContract.Presenter {

    private FavoritesContract.View view;
    private final AppDatabase database;
    private final CompositeDisposable compositeDisposable;

    public FavoritesPresenter(FavoritesContract.View view, AppDatabase database) {
        this.view = view;
        this.database = database;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void loadFavorites() {
        compositeDisposable.add(
                database.favoriteRecipeDao()
                        .getAllFavorites()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                favorites -> {
                                    if (view == null) return;

                                    if (favorites.isEmpty()) {
                                        view.showEmptyState();
                                    } else {
                                        view.showFavorites(favorites);
                                    }
                                },
                                throwable -> {
                                    if (view != null) {
                                        view.showError("Error loading favorites");
                                    }
                                }
                        )
        );
    }

    @Override
    public void deleteRecipe(FavoriteRecipe recipe, int position) {
        compositeDisposable.add(
                database.favoriteRecipeDao()
                        .delete(recipe)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                () -> {
                                    if (view != null) {
                                        view.onRecipeDeleted(position);
                                    }
                                },
                                throwable -> {
                                    if (view != null) {
                                        view.showError("Failed to remove favorite");
                                    }
                                }
                        )
        );
    }

    @Override
    public void detach() {
        compositeDisposable.clear();
        view = null;
    }
}