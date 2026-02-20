package com.example.flavor.presentation.main.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.flavor.R;
import com.example.flavor.data.model.Category;
import com.example.flavor.data.model.Recipe;
import com.example.flavor.data.repo.CategoryRepository;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends Fragment implements HomeContract.View {

    private HomePresenter presenter;
    private HomeAdapter adapter;

    private LinearLayout bannerContainer;
    private LinearLayout llRecipes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        bannerContainer = view.findViewById(R.id.bannerContainer);
        llRecipes = view.findViewById(R.id.llRecipes);

        presenter = new HomePresenter(this);

        adapter = new HomeAdapter(
                getContext(),
                bannerContainer,
                llRecipes,
                new ArrayList<>(),
                new ArrayList<>(),
                presenter::onRecipeClicked,
                (category, position) -> presenter.loadMealsByCategory(category.getStrCategory())
        );

        presenter.loadRandomMeal();
        loadCategories();

        return view;
    }

    @Override
    public void showRecipes(java.util.List<Recipe> recipes) {
        adapter.setRecipes(recipes);
    }

    @Override
    public void showRandomMeal(Recipe recipe) {
        adapter.setRandomMeal(recipe);
    }

    private void loadCategories() {
        new CategoryRepository()
                .getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categories -> {
                            adapter.setCategories(categories);
                            presenter.loadMealsByCategory(categories.get(0).getStrCategory());
                        },
                        e -> Toast.makeText(getContext(), "Failed to load categories", Toast.LENGTH_SHORT).show()
                );
    }

    @Override public void showLoading() {}
    @Override public void hideLoading() {}

    @Override
    public void navigateToDetails(Recipe recipe) {
        startActivity(
                new android.content.Intent(getActivity(),
                        com.example.flavor.presentation.mealdetails.MealDetailsActivity.class)
                        .putExtra("RECIPE_EXTRA", recipe)
        );
    }
}