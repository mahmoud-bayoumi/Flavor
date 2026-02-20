package com.example.flavor.presentation.main.home;


import com.example.flavor.data.model.Recipe;
import java.util.List;

public interface HomeContract {
    interface View {
        void showRecipes(List<Recipe> recipes);
        void showRandomMeal(Recipe recipe);
        void showLoading();
      //  void showCategories();
        void hideLoading();
        void navigateToDetails(Recipe recipe);
    }

    interface Presenter {
        void loadRecipes();
        void loadRandomMeal();
        void onRecipeClicked(Recipe recipe);
    }

}
