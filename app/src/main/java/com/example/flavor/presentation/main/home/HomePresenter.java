package com.example.flavor.presentation.main.home;

import com.example.flavor.R;
import com.example.flavor.data.model.Recipe;
import java.util.ArrayList;
import java.util.List;

public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View view;

    public HomePresenter(HomeContract.View view) {
        this.view = view;
    }

    @Override
    public void loadRecipes() {
        if (view != null) {
            view.showLoading();

            List<Recipe> list = new ArrayList<>();
            list.add(new Recipe("Fresh Salmon Poke Bowl", "$12.50", "4.8 (1.2k reviews)", R.drawable.ic_launcher_background));
            list.add(new Recipe("Classic Margherita Pizza", "$14.00", "4.9 (850 reviews)", R.drawable.ic_launcher_background));
            list.add(new Recipe("Green Detox Bowl", "$10.00", "4.7 (2.1k reviews)", R.drawable.ic_launcher_background));

            view.showRecipes(list);
            view.hideLoading();
        }
    }

    @Override
    public void onRecipeClicked(Recipe recipe) {
        if (view != null) {
            view.navigateToDetails(recipe);
        }
    }
}