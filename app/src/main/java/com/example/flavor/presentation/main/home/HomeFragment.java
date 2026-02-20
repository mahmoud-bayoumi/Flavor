package com.example.flavor.presentation.main.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavor.R;
import com.example.flavor.data.model.Recipe;
import com.example.flavor.data.repo.CategoryRepository;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends Fragment implements HomeContract.View {

    private RecyclerView recyclerView;
    private HomePresenter presenter;
    private HomeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        presenter = new HomePresenter(this);

        adapter = new HomeAdapter(new ArrayList<>(), new ArrayList<>(), recipe -> presenter.onRecipeClicked(recipe));
        recyclerView.setAdapter(adapter);

        presenter.loadRecipes();
        presenter.loadRandomMeal();

        loadCategories();

        return view;
    }

    @Override
    public void showRecipes(java.util.List<Recipe> recipes) {
        adapter = new HomeAdapter(recipes, new ArrayList<>(), recipe -> presenter.onRecipeClicked(recipe));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showRandomMeal(Recipe recipe) {
        if (adapter != null) {
            adapter.setRandomMeal(recipe);
        }
    }

    @Override
    public void navigateToDetails(Recipe recipe) {
        android.content.Intent intent = new android.content.Intent(getActivity(),
                com.example.flavor.presentation.mealdetails.MealDetailsActivity.class);
        intent.putExtra("RECIPE_EXTRA", recipe);
        startActivity(intent);
    }

    @Override
    public void showLoading() { }

    @Override
    public void hideLoading() { }

    private void loadCategories() {
        CategoryRepository repository = new CategoryRepository();
        repository.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(categories -> {
                            if (adapter != null) adapter.setCategories(categories);
                        }, throwable ->
                                Toast.makeText(getContext(), "Failed to load categories", Toast.LENGTH_SHORT).show()
                );
    }
}
