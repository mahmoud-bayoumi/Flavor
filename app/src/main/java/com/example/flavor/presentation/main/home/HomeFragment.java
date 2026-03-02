package com.example.flavor.presentation.main.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.flavor.R;
import com.example.flavor.data.model.Category;
import com.example.flavor.data.model.Recipe;
import com.example.flavor.presentation.mealdetails.MealDetailsActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeContract.View {

    private HomePresenter presenter;
    private HomeAdapter adapter;

    private LinearLayout bannerContainer;
    private LinearLayout llRecipes;
    private LinearLayout searchResultsContainer;
    private EditText searchEditText;

    private Recipe randomMeal;

    private final ActivityResultLauncher<Intent> mealDetailsLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == getActivity().RESULT_OK
                                && result.getData() != null) {
                            boolean isFavorite = result.getData()
                                    .getBooleanExtra("IS_FAVORITE", false);
                            String mealId = result.getData()
                                    .getStringExtra("MEAL_ID");
                            adapter.refreshRecipeFavorite(mealId, isFavorite);
                        }
                    }
            );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        bannerContainer = view.findViewById(R.id.bannerContainer);
        llRecipes = view.findViewById(R.id.llRecipes);

        searchResultsContainer = new LinearLayout(getContext());
        searchResultsContainer.setOrientation(LinearLayout.VERTICAL);
        ((LinearLayout) view.findViewById(R.id.bannerContainer).getParent())
                .addView(searchResultsContainer);
        searchResultsContainer.setVisibility(View.GONE);

        TextInputLayout searchLayout = view.findViewById(R.id.searchLayout);
        searchEditText = searchLayout.getEditText();

        presenter = new HomePresenter(this);

        adapter = new HomeAdapter(
                getContext(),
                bannerContainer,
                llRecipes,
                new ArrayList<>(),
                new ArrayList<>(),
                presenter::onRecipeClicked,
                (category, position) ->
                        presenter.loadMealsByCategory(category.getStrCategory())
        );

        adapter.setRecipeItemClickListener(recipe -> navigateToDetails(recipe));

        presenter.loadRandomMeal();
        presenter.loadCategories();

        setupSearch();

        return view;
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    bannerContainer.setVisibility(View.GONE);
                    llRecipes.setVisibility(View.GONE);
                    searchResultsContainer.setVisibility(View.VISIBLE);

                    presenter.searchMeals(query);
                } else {
                    bannerContainer.setVisibility(View.VISIBLE);
                    llRecipes.setVisibility(View.VISIBLE);
                    searchResultsContainer.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    @Override
    public void showRecipes(List<Recipe> recipes) {
        adapter.setRecipes(recipes);
    }

    @Override
    public void showCategories(List<Category> categories) {
        adapter.setCategories(categories);
    }

    @Override
    public void showSearchResults(List<Recipe> recipes) {
        adapter.setSearchResults(searchResultsContainer, recipes);
    }

    @Override
    public void showRandomMeal(Recipe recipe) {
        randomMeal = recipe;
        adapter.setRandomMeal(recipe);
        adapter.setRandomMealClickListener(v -> navigateToDetails(randomMeal));
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override public void showLoading() { }
    @Override public void hideLoading() { }

    @Override
    public void navigateToDetails(Recipe recipe) {
        Intent intent = new Intent(getActivity(), MealDetailsActivity.class);
        intent.putExtra("MEAL_ID", recipe.getId());
        mealDetailsLauncher.launch(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.clear();
        if (adapter != null) adapter.clear();
    }
}