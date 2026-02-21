package com.example.flavor.presentation.mealdetails;

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
import com.example.flavor.data.model.Ingredient;
import com.example.flavor.data.model.Recipe;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

public class MealDetailsActivity extends AppCompatActivity
        implements MealDetailsContract.View {

    private MealDetailsPresenter presenter;

    private RecyclerView rvSteps;
    private TextView tvMealTitle;
    private YouTubePlayerView youtubePlayerView;
    private ImageView ivMealImageHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);

        // Views
        tvMealTitle = findViewById(R.id.tvMealTitle);
        rvSteps = findViewById(R.id.rvSteps);
        youtubePlayerView = findViewById(R.id.youtube_player_view);
        ivMealImageHeader = findViewById(R.id.ivMealHeader);


        rvSteps.setLayoutManager(new LinearLayoutManager(this));

        getLifecycle().addObserver(youtubePlayerView);

        presenter = new MealDetailsPresenter(this);

        // Receive MEAL ID
        String mealId = getIntent().getStringExtra("MEAL_ID");
        if (mealId != null) {
            presenter.loadMealDetails(mealId);
        } else {
            Toast.makeText(this, "Meal not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void showMealDetails(Recipe recipe) {
        tvMealTitle.setText(recipe.getTitle());

        Glide.with(this)
                .load(recipe.getImageUrl())
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .into(ivMealImageHeader);

        loadYoutube(recipe.getYoutubeUrl());

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
}