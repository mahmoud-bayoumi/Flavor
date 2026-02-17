package com.example.flavor.presentation.main.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flavor.R;
import com.example.flavor.data.model.Recipe;

import java.util.List;

public class HomeFragment extends Fragment implements HomeContract.View {

    private RecyclerView recyclerView;
    private HomePresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate fragment_home.xml
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Presenter
        presenter = new HomePresenter(this);
        presenter.loadRecipes();

        return view;
    }

    @Override
    public void showRecipes(List<Recipe> recipes) {

        HomeAdapter adapter = new HomeAdapter(recipes);
        recyclerView.setAdapter(adapter);
    }

    @Override public void showLoading() { /* e.g. progressBar.setVisibility(View.VISIBLE); */ }
    @Override public void hideLoading() { /* e.g. progressBar.setVisibility(View.GONE); */ }
}