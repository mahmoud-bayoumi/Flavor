package com.example.flavor.presentation.main.favorites;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.flavor.R;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flavor.data.model.Recipe;
import java.util.List;

public class FavoritesFragment extends Fragment implements FavoritesContract.View {

    private RecyclerView recyclerView;
    private FavoritesAdapter adapter;
    private FavoritesContract.Presenter presenter;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        initViews(view);

        // Initialize the Presenter
        presenter = new FavoritesPresenter(this);

        // Request data
        presenter.loadFavorites();

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.rvFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Adding fixed size for performance since cards are consistent
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void showFavorites(List<Recipe> recipes) {
        // Initialize adapter with the data and the delete click listener
        adapter = new FavoritesAdapter(recipes, position -> {
            // Pass the action to the presenter
            presenter.deleteRecipe(position);
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRecipeDeleted(int position) {
        // Update the UI via the adapter
        if (adapter != null) {
            adapter.removeItem(position);
            Toast.makeText(getContext(), "Recipe removed from favorites", Toast.LENGTH_SHORT).show();
        }
    }

    // Optional: If you decide to add an empty state view in your XML
    public void showEmptyState() {
        recyclerView.setVisibility(View.GONE);
        // binding.emptyView.setVisibility(View.VISIBLE);
    }

}