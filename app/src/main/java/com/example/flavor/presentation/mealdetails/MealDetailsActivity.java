package com.example.flavor.presentation.mealdetails;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flavor.R;
import com.example.flavor.data.model.Recipe;
import com.example.flavor.data.repo.FavoriteRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MealDetailsActivity extends AppCompatActivity
        implements MealDetailsContract.View {

    private MealDetailsPresenter presenter;

    private RecyclerView rvSteps;
    private TextView tvMealTitle;
    private YouTubePlayerView youtubePlayerView;
    private ImageView ivMealImageHeader;
    private FloatingActionButton fabFavorite;

    private FavoriteRepository favoriteRepository;
    private Recipe currentRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);

        tvMealTitle = findViewById(R.id.tvMealTitle);
        rvSteps = findViewById(R.id.rvSteps);
        youtubePlayerView = findViewById(R.id.youtube_player_view);
        ivMealImageHeader = findViewById(R.id.ivMealHeader);
        fabFavorite = findViewById(R.id.fabFavorite);

        favoriteRepository = new FavoriteRepository(this);

        rvSteps.setLayoutManager(new LinearLayoutManager(this));
        getLifecycle().addObserver(youtubePlayerView);

        presenter = new MealDetailsPresenter(this);

        String mealId = getIntent().getStringExtra("MEAL_ID");
        if (mealId != null) {
            presenter.loadMealDetails(mealId);
        } else {
            Toast.makeText(this, "Meal not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        fabFavorite.setOnClickListener(v -> {
            if (currentRecipe != null) toggleFavorite(currentRecipe);
        });
    }

    @Override
    public void showMealDetails(Recipe recipe) {
        currentRecipe = recipe;

        tvMealTitle.setText(recipe.getTitle());

        Glide.with(this)
                .load(recipe.getImageUrl())
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .into(ivMealImageHeader);

        loadYoutube(recipe.getYoutubeUrl());

        updateFavoriteIcon(recipe);
    }

    private void loadYoutube(String youtubeUrl) {
        if (youtubeUrl == null || !youtubeUrl.contains("=")) return;

        String videoId = youtubeUrl.substring(youtubeUrl.lastIndexOf("=") + 1);

        youtubePlayerView.addYouTubePlayerListener(
                new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        youTubePlayer.cueVideo(videoId, 0f);
                    }
                }
        );
    }

    @Override
    public void showSteps(List<String> steps) {
        rvSteps.setAdapter(new StepsAdapter(steps));
    }

    @Override
    public void onAddedToPlanner() {
        Toast.makeText(this, "Added to planner", Toast.LENGTH_SHORT).show();
    }

    /** ------------------ Favorite Logic ------------------ **/

    private void updateFavoriteIcon(Recipe recipe) {
        favoriteRepository.isFavorite(recipe.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isFav -> {
                    recipe.setFavorite(isFav);
                    if (isFav) fabFavorite.setImageResource(R.drawable.ic_filled_heart);
                    else fabFavorite.setImageResource(R.drawable.ic_outlined_heart);
                });
    }

    private void toggleFavorite(Recipe recipe) {
        favoriteRepository.isFavorite(recipe.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isFav -> {
                    if (isFav) {
                        fabFavorite.setImageResource(R.drawable.ic_outlined_heart);
                        favoriteRepository.removeFromFavorites(recipe)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> returnFavoriteState(recipe.getId(), false));
                    } else {
                        fabFavorite.setImageResource(R.drawable.ic_filled_heart);
                        favoriteRepository.addToFavorites(recipe)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> returnFavoriteState(recipe.getId(), true));
                    }
                });
    }

    private void returnFavoriteState(String mealId, boolean isFavorite) {
        Intent data = new Intent();
        data.putExtra("MEAL_ID", mealId);
        data.putExtra("IS_FAVORITE", isFavorite);
        setResult(RESULT_OK, data);
    }
}