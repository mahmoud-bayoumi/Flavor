package com.example.flavor.presentation.main.home;

import com.example.flavor.R;
import com.example.flavor.data.model.Recipe;
import com.example.flavor.data.repo.MealRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.disposables.CompositeDisposable;



public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View view;

    private MealRepository repository;
    private CompositeDisposable disposable = new CompositeDisposable();


    public HomePresenter(HomeContract.View view) {
        this.view = view;
        this.repository = new MealRepository();
    }

    @Override
    public void loadRecipes() {
        if (view != null) {
            view.showLoading();

            List<Recipe> list = new ArrayList<>();
  //          list.add(new Recipe("Fresh Salmon Poke Bowl", "$12.50", "4.8 (1.2k reviews)", R.drawable.ic_launcher_background));
  //          list.add(new Recipe("Classic Margherita Pizza", "$14.00", "4.9 (850 reviews)", R.drawable.ic_launcher_background));
   //         list.add(new Recipe("Green Detox Bowl", "$10.00", "4.7 (2.1k reviews)", R.drawable.ic_launcher_background));

            view.showRecipes(list);
            view.hideLoading();
        }
    }

    @Override
    public void loadRandomMeal() {
        view.showLoading();

        disposable.add(
                repository.getRandomMeal()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                recipe -> {
                                    view.hideLoading();
                                    view.showRandomMeal(recipe);
                                },
                                throwable -> view.hideLoading()
                        )
        );
    }
    @Override
    public void onRecipeClicked(Recipe recipe) {
        if (view != null) {
            view.navigateToDetails(recipe);
        }
    }
    public void clear() {
        disposable.clear();
    }
}