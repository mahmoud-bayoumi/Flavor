package com.example.flavor.presentation.main.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flavor.R;
import com.example.flavor.data.model.Category;
import com.example.flavor.data.model.Recipe;
import com.example.flavor.data.repo.FavoriteRepository;
import com.example.flavor.presentation.mealdetails.MealDetailsActivity;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeAdapter {


    private Recipe randomMeal;
    private List<Category> categories;
    private List<Recipe> recipes;

    private int selectedCategoryIndex = 0;

    private final Context context;
    private final LinearLayout bannerContainer;
    private final LinearLayout llRecipes;

    private final OnRecipeClickListener recipeListener;
    private final OnCategoryClickListener categoryListener;

    private final FavoriteRepository favoriteRepository;

    private View.OnClickListener randomMealClickListener;
    private OnRecipeClickListener recipeItemClickListener;

    // Map to hold recipe ID -> item view for quick updates
    private final Map<String, View> recipeItemViews = new HashMap<>();

    public HomeAdapter(
            Context context,
            LinearLayout bannerContainer,
            LinearLayout llRecipes,
            List<Recipe> recipes,
            List<Category> categories,
            OnRecipeClickListener recipeListener,
            OnCategoryClickListener categoryListener
    ) {
        this.context = context;
        this.bannerContainer = bannerContainer;
        this.llRecipes = llRecipes;
        this.recipes = recipes;
        this.categories = categories;
        this.recipeListener = recipeListener;
        this.categoryListener = categoryListener;
        this.favoriteRepository = new FavoriteRepository(context);
    }

    public void setRandomMeal(Recipe recipe) {
        this.randomMeal = recipe;
        populateBanner();
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        populateBanner();
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        populateRecipes();
    }

    public void setSearchResults(LinearLayout container, List<Recipe> recipes) {
        container.removeAllViews();
        if (recipes == null) return;

        for (Recipe recipe : recipes) {
            View item = LayoutInflater.from(context).inflate(R.layout.item_recipe, container, false);

            TextView tvTitle = item.findViewById(R.id.tvTitle);
            TextView tvPrice = item.findViewById(R.id.tvPrice);
            TextView tvRating = item.findViewById(R.id.tvRating);
            ImageView ivRecipe = item.findViewById(R.id.ivRecipe);
            ImageButton btnFavorite = item.findViewById(R.id.btnFavorite);

            tvTitle.setText(recipe.getTitle());
            tvPrice.setText(recipe.getPrice());
            tvRating.setText(recipe.getCategory());

            Glide.with(context).load(recipe.getImageUrl())
                    .placeholder(R.drawable.ic_placeholder)
                    .centerCrop()
                    .into(ivRecipe);

            updateFavoriteIcon(recipe, btnFavorite);
            btnFavorite.setOnClickListener(v -> toggleFavorite(recipe, btnFavorite));

            item.setOnClickListener(v -> recipeListener.onRecipeClick(recipe));
            container.addView(item);
        }
    }

    // Random Meal Banner
    private void populateBanner() {
        bannerContainer.removeAllViews();
        if (randomMeal == null) return;

        View banner = LayoutInflater.from(context).inflate(R.layout.item_banner, bannerContainer, false);
        TextView tvTitle = banner.findViewById(R.id.tvBannerTitle);
        TextView tvCategory = banner.findViewById(R.id.tvBannerCategory);
        ImageView ivBanner = banner.findViewById(R.id.ivBannerImage);
        LinearLayout llCategories = banner.findViewById(R.id.llCategories);
        ImageButton btnFavoriteBanner = banner.findViewById(R.id.btnFavoriteBanner);

        tvTitle.setText(randomMeal.getTitle());
        tvCategory.setText(randomMeal.getCategory());

        Glide.with(context).load(randomMeal.getImageUrl())
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .into(ivBanner);

        updateFavoriteIcon(randomMeal, btnFavoriteBanner);
        btnFavoriteBanner.setOnClickListener(v -> toggleFavorite(randomMeal, btnFavoriteBanner));

        if (randomMealClickListener != null) {
            banner.setOnClickListener(randomMealClickListener);
        } else {
            banner.setOnClickListener(v -> {
                Intent intent = new Intent(context, MealDetailsActivity.class);
                intent.putExtra("MEAL_ID", randomMeal.getId());
                context.startActivity(intent);
            });
        }

        // Populate categories
        llCategories.removeAllViews();
        if (categories != null) {
            for (int i = 0; i < categories.size(); i++) {
                Category cat = categories.get(i);
                int index = i;

                MaterialButton btn = new MaterialButton(context, null,
                        com.google.android.material.R.attr.materialButtonOutlinedStyle);
                btn.setText(cat.getStrCategory());
                btn.setCornerRadius(20);

                if (i == selectedCategoryIndex) select(btn);
                else unselect(btn);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 20, 0);
                btn.setLayoutParams(params);

                btn.setOnClickListener(v -> {
                    selectedCategoryIndex = index;
                    categoryListener.onCategoryClick(cat, index);
                    populateBanner();
                });

                llCategories.addView(btn);
            }
        }

        bannerContainer.addView(banner);
    }

    // Recipes List
    private void populateRecipes() {
        llRecipes.removeAllViews();
        recipeItemViews.clear();

        if (recipes == null) return;

        for (Recipe recipe : recipes) {
            View item = LayoutInflater.from(context).inflate(R.layout.item_recipe, llRecipes, false);

            TextView tvTitle = item.findViewById(R.id.tvTitle);
            TextView tvPrice = item.findViewById(R.id.tvPrice);
            TextView tvRating = item.findViewById(R.id.tvRating);
            ImageView ivRecipe = item.findViewById(R.id.ivRecipe);
            ImageButton btnFavorite = item.findViewById(R.id.btnFavorite);

            tvTitle.setText(recipe.getTitle());
            tvPrice.setText(recipe.getPrice());
            tvRating.setText(recipe.getCategory());

            Glide.with(context).load(recipe.getImageUrl())
                    .placeholder(R.drawable.ic_placeholder)
                    .centerCrop()
                    .into(ivRecipe);

            updateFavoriteIcon(recipe, btnFavorite);
            btnFavorite.setOnClickListener(v -> toggleFavorite(recipe, btnFavorite));

            if (recipeItemClickListener != null) {
                item.setOnClickListener(v -> recipeItemClickListener.onRecipeClick(recipe));
            } else {
                item.setOnClickListener(v -> recipeListener.onRecipeClick(recipe));
            }

            llRecipes.addView(item);
            recipeItemViews.put(recipe.getId(), item); // save reference
        }
    }


    public void refreshRecipeFavorite(String recipeId, boolean isFavorite) {
        // Update banner if matches
        if (randomMeal != null && randomMeal.getId().equals(recipeId)) {
            randomMeal.setFavorite(isFavorite);
            populateBanner();
        }

        // Update list item only
        View item = recipeItemViews.get(recipeId);
        if (item != null) {
            ImageButton btnFavorite = item.findViewById(R.id.btnFavorite);
            btnFavorite.setImageResource(isFavorite ? R.drawable.ic_filled_heart : R.drawable.ic_outlined_heart);
        }

        // Also update internal recipe model
        if (recipes != null) {
            for (Recipe recipe : recipes) {
                if (recipe.getId().equals(recipeId)) {
                    recipe.setFavorite(isFavorite);
                    break;
                }
            }
        }
    }

    private void select(MaterialButton btn) {
        btn.setBackgroundColor(Color.parseColor("#F58D2D"));
        btn.setTextColor(Color.WHITE);
    }

    private void unselect(MaterialButton btn) {
        btn.setBackgroundColor(Color.WHITE);
        btn.setTextColor(Color.BLACK);
    }

    private void updateFavoriteIcon(Recipe recipe, ImageButton btnFavorite) {
        favoriteRepository.isFavorite(recipe.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isFav -> {
                    btnFavorite.setImageResource(isFav ? R.drawable.ic_filled_heart : R.drawable.ic_outlined_heart);
                });
    }

    private void toggleFavorite(Recipe recipe, ImageButton btnFavorite) {
        favoriteRepository.isFavorite(recipe.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isFav -> {
                    if (isFav) {
                        btnFavorite.setImageResource(R.drawable.ic_outlined_heart);
                        favoriteRepository.removeFromFavorites(recipe)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe();
                    } else {
                        btnFavorite.setImageResource(R.drawable.ic_filled_heart);
                        favoriteRepository.addToFavorites(recipe)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe();
                    }
                });
    }

    public void setRandomMealClickListener(View.OnClickListener listener) {
        this.randomMealClickListener = listener;
        populateBanner();
    }

    public void setRecipeItemClickListener(OnRecipeClickListener listener) {
        this.recipeItemClickListener = listener;
        populateRecipes();
    }

    public List<Recipe> getRecipes() { return recipes; }
}