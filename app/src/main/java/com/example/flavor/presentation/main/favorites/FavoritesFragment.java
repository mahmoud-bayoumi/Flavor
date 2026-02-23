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

public class FavoritesFragment extends Fragment
        implements FavoritesContract.View {

    private RecyclerView recyclerView;
    private FavoritesAdapter adapter;
    private TextView tvCount;

    private FavoritesContract.Presenter presenter;

    // Handle result from MealDetailsActivity
    private final ActivityResultLauncher<Intent> mealDetailsLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == getActivity().RESULT_OK
                                && result.getData() != null) {

                            String mealId =
                                    result.getData().getStringExtra("MEAL_ID");
                            boolean isFavorite =
                                    result.getData().getBooleanExtra("IS_FAVORITE", true);

                            if (!isFavorite) {
                                removeRecipeById(mealId);
                            }
                        }
                    }
            );

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        tvCount = view.findViewById(R.id.tvCount);
        recyclerView = view.findViewById(R.id.rvFavorites);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        adapter = new FavoritesAdapter(
                getContext(),
                new FavoritesAdapter.OnItemClickListener() {

                    @Override
                    public void onDeleteClick(FavoriteRecipe recipe, int position) {
                        presenter.deleteRecipe(recipe, position);
                    }

                    @Override
                    public void onItemClick(FavoriteRecipe recipe) {
                        Intent intent =
                                new Intent(getContext(), MealDetailsActivity.class);
                        intent.putExtra("MEAL_ID", recipe.getId());
                        mealDetailsLauncher.launch(intent);
                    }
                }
        );

        recyclerView.setAdapter(adapter);

        presenter = new FavoritesPresenter(
                this,
                AppDatabase.getInstance(requireContext())
        );

        presenter.loadFavorites();

        return view;
    }


    @Override
    public void showFavorites(List<FavoriteRecipe> recipes) {
        adapter.setItems(recipes);
        recyclerView.setVisibility(View.VISIBLE);
        tvCount.setText(recipes.size() + " saved recipes");
    }

    @Override
    public void onRecipeDeleted(int position) {
        adapter.removeItem(position);
        tvCount.setText(adapter.getItemCount() + " saved recipes");

        if (adapter.getItemCount() == 0) {
            showEmptyState();
        }
    }

    @Override
    public void showEmptyState() {
        recyclerView.setVisibility(View.GONE);
        tvCount.setText("0 saved recipes");
        Toast.makeText(getContext(), "No favorites yet!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detach();
    }
}