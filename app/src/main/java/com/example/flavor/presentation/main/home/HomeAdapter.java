package com.example.flavor.presentation.main.home;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flavor.R;
import com.example.flavor.data.model.Category;
import com.example.flavor.data.model.Recipe;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class HomeAdapter {

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category, int position);
    }

    private Recipe randomMeal;
    private List<Category> categories;
    private List<Recipe> recipes;

    private int selectedCategoryIndex = 0;

    private final Context context;
    private final LinearLayout bannerContainer;
    private final LinearLayout llRecipes;

    private final OnRecipeClickListener recipeListener;
    private final OnCategoryClickListener categoryListener;

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

        for (Recipe recipe : recipes) {
            View item = LayoutInflater.from(context).inflate(R.layout.item_recipe, container, false);

            TextView tvTitle = item.findViewById(R.id.tvTitle);
            TextView tvPrice = item.findViewById(R.id.tvPrice);
            TextView tvRating = item.findViewById(R.id.tvRating);
            ImageView ivRecipe = item.findViewById(R.id.ivRecipe);

            tvTitle.setText(recipe.getTitle());
            tvPrice.setText(recipe.getPrice());
            tvRating.setText(recipe.getCategory());

            Glide.with(context).load(recipe.getImageUrl())
                    .placeholder(R.drawable.ic_placeholder)
                    .centerCrop()
                    .into(ivRecipe);

            item.setOnClickListener(v -> recipeListener.onRecipeClick(recipe));

            container.addView(item);
        }
    }

    private void populateBanner() {
        bannerContainer.removeAllViews();
        if (randomMeal == null) return;

        View banner = LayoutInflater.from(context).inflate(R.layout.item_banner, bannerContainer, false);
        TextView tvTitle = banner.findViewById(R.id.tvBannerTitle);
        TextView tvCategory = banner.findViewById(R.id.tvBannerCategory);
        ImageView ivBanner = banner.findViewById(R.id.ivBannerImage);
        LinearLayout llCategories = banner.findViewById(R.id.llCategories);

        tvTitle.setText(randomMeal.getTitle());
        tvCategory.setText(randomMeal.getCategory());

        Glide.with(context).load(randomMeal.getImageUrl())
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .into(ivBanner);

        // Horizontal category buttons
        llCategories.removeAllViews();
        if (categories == null) return;
        for (int i = 0; i < categories.size(); i++) {
            Category cat = categories.get(i);
            int index = i;

            MaterialButton btn = new MaterialButton(context, null, com.google.android.material.R.attr.materialButtonOutlinedStyle);
            btn.setText(cat.getStrCategory());
            btn.setCornerRadius(20);

            if (i == selectedCategoryIndex) select(btn);
            else unselect(btn);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 20, 0); // Horizontal spacing
            btn.setLayoutParams(params);

            btn.setOnClickListener(v -> {
                selectedCategoryIndex = index;
                categoryListener.onCategoryClick(cat, index);
                populateBanner();
            });

            llCategories.addView(btn);
        }

        bannerContainer.addView(banner);
    }

    private void populateRecipes() {
        llRecipes.removeAllViews();

        if (recipes == null) return;

        for (Recipe recipe : recipes) {
            View item = LayoutInflater.from(context).inflate(R.layout.item_recipe, llRecipes, false);

            TextView tvTitle = item.findViewById(R.id.tvTitle);
            TextView tvPrice = item.findViewById(R.id.tvPrice);
            TextView tvRating = item.findViewById(R.id.tvRating);
            ImageView ivRecipe = item.findViewById(R.id.ivRecipe);

            tvTitle.setText(recipe.getTitle());
            tvPrice.setText(recipe.getPrice());
            tvRating.setText(recipe.getCategory());

            Glide.with(context).load(recipe.getImageUrl())
                    .placeholder(R.drawable.ic_placeholder)
                    .centerCrop()
                    .into(ivRecipe);

            item.setOnClickListener(v -> recipeListener.onRecipeClick(recipe));

            llRecipes.addView(item);
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
}