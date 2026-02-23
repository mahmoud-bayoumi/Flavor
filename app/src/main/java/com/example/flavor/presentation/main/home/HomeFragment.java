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
import com.example.flavor.data.model.Recipe;
import com.example.flavor.data.repo.CategoryRepository;
import com.example.flavor.data.repo.MealRepository;
import com.example.flavor.presentation.mealdetails.MealDetailsActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends Fragment implements HomeContract.View {

    private HomePresenter presenter;
    private HomeAdapter adapter;

    private LinearLayout bannerContainer;
    private LinearLayout llRecipes;
    private LinearLayout searchResultsContainer;
    private EditText searchEditText;

    private MealRepository mealRepository;
    private Recipe randomMeal;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final ActivityResultLauncher<Intent> mealDetailsLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    boolean isFavorite = result.getData().getBooleanExtra("IS_FAVORITE", false);
                    String mealId = result.getData().getStringExtra("MEAL_ID");

                    adapter.refreshRecipeFavorite(mealId, isFavorite);
                }
            });

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
        mealRepository = new MealRepository();

        adapter = new HomeAdapter(
                getContext(),
                bannerContainer,
                llRecipes,
                new ArrayList<>(),
                new ArrayList<>(),
                presenter::onRecipeClicked,
                (category, position) -> presenter.loadMealsByCategory(category.getStrCategory())
        );

        adapter.setRecipeItemClickListener(recipe -> navigateToDetails(recipe));

        presenter.loadRandomMeal();
        loadCategories();
        setupSearch();

        return view;
    }


    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    bannerContainer.setVisibility(View.GONE);
                    llRecipes.setVisibility(View.GONE);
                    searchResultsContainer.setVisibility(View.VISIBLE);

                    compositeDisposable.add(
                            mealRepository.searchMealsByName(query)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            recipes -> adapter.setSearchResults(searchResultsContainer, recipes),
                                            throwable -> Toast.makeText(getContext(), "Search failed", Toast.LENGTH_SHORT).show()
                                    )
                    );
                } else {
                    bannerContainer.setVisibility(View.VISIBLE);
                    llRecipes.setVisibility(View.VISIBLE);
                    searchResultsContainer.setVisibility(View.GONE);
                }
            }

            @Override public void afterTextChanged(Editable s) { }
        });
    }


    private void loadCategories() {
        compositeDisposable.add(
                new CategoryRepository()
                        .getCategories()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                categories -> {
                                    adapter.setCategories(categories);
                                    if (!categories.isEmpty()) {
                                        presenter.loadMealsByCategory(categories.get(0).getStrCategory());
                                    }
                                },
                                e -> Toast.makeText(getContext(), "Failed to load categories", Toast.LENGTH_SHORT).show()
                        )
        );
    }


    @Override
    public void showRecipes(List<Recipe> recipes) {
        adapter.setRecipes(recipes);
    }

    @Override
    public void showRandomMeal(Recipe recipe) {
        randomMeal = recipe;
        adapter.setRandomMeal(recipe);

        adapter.setRandomMealClickListener(v -> {
            Intent intent = new Intent(getContext(), MealDetailsActivity.class);
            intent.putExtra("MEAL_ID", randomMeal.getId());
            mealDetailsLauncher.launch(intent);
        });
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
        compositeDisposable.clear();
        if (adapter != null) adapter.clear();
    }
}