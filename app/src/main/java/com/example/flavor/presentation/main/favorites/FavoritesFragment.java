package com.example.flavor.presentation.main.favorites;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavor.R;
import com.example.flavor.data.local.AppDatabase;
import com.example.flavor.data.local.entities.FavoriteRecipe;
import com.example.flavor.presentation.mealdetails.MealDetailsActivity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoritesAdapter adapter;
    private AppDatabase database;
    private TextView tvCount;

    // ActivityResultLauncher to handle results from MealDetailsActivity
    private final ActivityResultLauncher<Intent> mealDetailsLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    String mealId = result.getData().getStringExtra("MEAL_ID");
                    boolean isFavorite = result.getData().getBooleanExtra("IS_FAVORITE", true);

                    // If the meal was unfavorited, remove it from the list
                    if (!isFavorite) {
                        removeRecipeById(mealId);
                    }
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        tvCount = view.findViewById(R.id.tvCount);
        recyclerView = view.findViewById(R.id.rvFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        database = AppDatabase.getInstance(getContext());

        adapter = new FavoritesAdapter(getContext(), new FavoritesAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(FavoriteRecipe recipe, int position) {
                deleteFavorite(recipe, position);
            }

            @Override
            public void onItemClick(FavoriteRecipe recipe) {
                // Navigate to MealDetailsActivity
                Intent intent = new Intent(getContext(), MealDetailsActivity.class);
                intent.putExtra("MEAL_ID", recipe.getId());
                mealDetailsLauncher.launch(intent);
            }
        });

        recyclerView.setAdapter(adapter);

        loadFavorites();

        return view;
    }

    private void loadFavorites() {
        database.favoriteRecipeDao()
                .getAllFavorites()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(favorites -> {
                    if (favorites.isEmpty()) {
                        showEmptyState();
                    } else {
                        adapter.setItems(favorites);
                        tvCount.setText(favorites.size() + " saved recipes");
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }, throwable -> Toast.makeText(getContext(), "Error loading favorites", Toast.LENGTH_SHORT).show());
    }


    private void deleteFavorite(FavoriteRecipe recipe, int position) {
        database.favoriteRecipeDao()
                .delete(recipe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    adapter.removeItem(position);
                    tvCount.setText(adapter.getItemCount() + " saved recipes");
                    Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                }, throwable -> Toast.makeText(getContext(), "Failed to remove favorite", Toast.LENGTH_SHORT).show());
    }


    private void removeRecipeById(String mealId) {
        List<FavoriteRecipe> recipes = adapter.getRecipes();
        for (int i = 0; i < recipes.size(); i++) {
            if (recipes.get(i).getId().equals(mealId)) {
                adapter.removeItem(i);
                tvCount.setText(adapter.getItemCount() + " saved recipes");
                break;
            }
        }
        if (adapter.getItemCount() == 0) {
            showEmptyState();
        }
    }

    private void showEmptyState() {
        recyclerView.setVisibility(View.GONE);
        tvCount.setText("0 saved recipes");
        Toast.makeText(getContext(), "No favorites yet!", Toast.LENGTH_SHORT).show();
    }
}