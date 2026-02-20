package com.example.flavor.presentation.main.favorites;

import com.example.flavor.R;
import com.example.flavor.data.model.Recipe;

import java.util.ArrayList;
import java.util.List;
public class FavoritesPresenter implements FavoritesContract.Presenter {
    private FavoritesContract.View view;
    private List<Recipe> recipeList; // Local cache for the presenter

    public FavoritesPresenter(FavoritesContract.View view) {
        this.view = view;
    }

    @Override
    public void loadFavorites() {
        // Mapping your model fields: title, price (time), rating (kcal)
        recipeList = new ArrayList<>();
   //     recipeList.add(new Recipe("Summer Avocado & Quinoa Salad", "15m", "320 kcal", R.drawable.ic_launcher_background));
    //    recipeList.add(new Recipe("Creamy Wild Mushroom Pasta", "25m", "540 kcal", R.drawable.ic_launcher_background));
     //   recipeList.add(new Recipe("Berry Explosion Breakfast Bowl", "10m", "280 kcal", R.drawable.ic_launcher_background));

        view.showFavorites(recipeList);
    }

    @Override
    public void deleteRecipe(int position) {
        // In a real app, delete from database here
        recipeList.remove(position);
        view.onRecipeDeleted(position);
    }
}