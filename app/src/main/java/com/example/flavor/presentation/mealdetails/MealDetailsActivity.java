package com.example.flavor.presentation.mealdetails;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavor.R;
import com.example.flavor.data.model.Ingredient;
import com.example.flavor.data.model.Recipe;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

public class MealDetailsActivity extends AppCompatActivity implements MealDetailsContract.View {

    private MealDetailsPresenter presenter;
    private RecyclerView rvIngredients, rvSteps;
    private TextView tvMealTitle; // Added to bind the title
    private YouTubePlayerView youtubePlayerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);

        // 1. Initialize UI Views
        tvMealTitle = findViewById(R.id.tvMealTitle);
        rvIngredients = findViewById(R.id.rvIngredients);
        rvSteps = findViewById(R.id.rvSteps);
        // 2. Setup RecyclerViews
        rvIngredients.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvSteps.setLayoutManager(new LinearLayoutManager(this));

        // 3. Receive the Recipe object safely
        Recipe recipe;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            recipe = getIntent().getSerializableExtra("RECIPE_EXTRA", Recipe.class);
        } else {
            recipe = (Recipe) getIntent().getSerializableExtra("RECIPE_EXTRA");
        }

        presenter = new MealDetailsPresenter(this);

        if (recipe != null) {
            // Populate basic UI immediately from the passed object
            showMealDetails(recipe);

            // Load extended details (Ingredients/Steps) via Presenter
            // Using a hardcoded ID for now as per your snippet, but recipe.getTitle()
            // or a recipe.getId() is better for real apps.
            presenter.loadMealDetails("beef_wellington_id");
        }

        youtubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youtubePlayerView);
        youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId=  "kbKldiDOgEE" ;
                   youTubePlayer.cueVideo(videoId,0f);
            }
        });

    }

    @Override
    public void showMealDetails(Recipe recipe) {
        if (tvMealTitle != null) {
            tvMealTitle.setText(recipe.getTitle());
        }
        // Bind other fields here (Price, Rating, etc.) if they exist in your layout
    }

    @Override
    public void showIngredients(List<Ingredient> ingredients) {
        IngredientsAdapter adapter = new IngredientsAdapter(ingredients);
        rvIngredients.setAdapter(adapter);
    }

    @Override
    public void showSteps(List<String> steps) {
        StepsAdapter adapter = new StepsAdapter(steps);
        rvSteps.setAdapter(adapter);
    }

    @Override
    public void onAddedToPlanner() {
        Toast.makeText(this, "Added to your meal planner!", Toast.LENGTH_SHORT).show();
    }
}