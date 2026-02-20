package com.example.flavor.presentation.main.home;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flavor.R;
import com.example.flavor.data.model.Category;
import com.example.flavor.data.model.Recipe;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    private static final int TYPE_BANNER = 0;
    private static final int TYPE_ITEM = 1;

    private List<Recipe> recipes;
    private List<Category> categories;
    private OnRecipeClickListener listener;
    private Recipe randomMeal;

    public HomeAdapter(List<Recipe> recipes, List<Category> categories, OnRecipeClickListener listener) {
        this.recipes = recipes;
        this.categories = categories;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? TYPE_BANNER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_BANNER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false);
            return new BannerViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
            return new RecipeViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_BANNER) {
            if (randomMeal == null) return;

            BannerViewHolder bvh = (BannerViewHolder) holder;
            bvh.title.setText(randomMeal.getTitle());
            bvh.category.setText(randomMeal.getCategory());

            Glide.with(bvh.itemView.getContext())
                    .load(randomMeal.getImageUrl())
                    .placeholder(R.drawable.ic_placeholder)
                    .into(bvh.image);

            if (categories != null) {
                bvh.setupCategories(categories);
            }

            holder.itemView.setOnClickListener(v ->
                    listener.onRecipeClick(randomMeal)
            );
            return;
        }

         int recipePosition = position - 1;
        if (recipePosition < 0 || recipePosition >= recipes.size()) return;

        Recipe recipe = recipes.get(recipePosition);
        RecipeViewHolder rvh = (RecipeViewHolder) holder;

        rvh.title.setText(recipe.getTitle());
        rvh.price.setText(recipe.getPrice());
        rvh.rating.setText(recipe.getRating());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onRecipeClick(recipe);
        });
    }

    @Override
    public int getItemCount() {
        return (recipes == null || recipes.isEmpty()) ? 1 : recipes.size() + 1;
    }


    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView title, price, rating;

        public RecipeViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.tvTitle);
            price = v.findViewById(R.id.tvPrice);
            rating = v.findViewById(R.id.tvRating);
        }
    }


    static class BannerViewHolder extends RecyclerView.ViewHolder {
        TextView title, category;
        ImageView image;
        LinearLayout llCategories;
        private MaterialButton selectedButton = null;

        public BannerViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.tvBannerTitle);
            category = v.findViewById(R.id.tvBannerCategory);
            image = v.findViewById(R.id.ivBannerImage);
            llCategories = v.findViewById(R.id.llCategories);
        }

        public void setupCategories(List<Category> categories) {
            llCategories.removeAllViews();

            for (Category cat : categories) {
                MaterialButton btn = new MaterialButton(itemView.getContext(), null,
                        com.google.android.material.R.attr.materialButtonOutlinedStyle);
                btn.setText(cat.getStrCategory());
                btn.setTextColor(Color.BLACK);
                btn.setBackgroundColor(Color.WHITE);
                btn.setCornerRadius(20);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(8, 0, 8, 0);
                btn.setLayoutParams(params);

                btn.setOnClickListener(v -> {
                     if (selectedButton != null) {
                        selectedButton.setBackgroundColor(Color.WHITE);
                        selectedButton.setTextColor(Color.BLACK);
                    }
                     btn.setBackgroundColor(Color.parseColor("#F58D2D"));
                    btn.setTextColor(Color.WHITE);
                    selectedButton = btn;


                });

                llCategories.addView(btn);
            }
        }
    }


    public void setRandomMeal(Recipe recipe) {
        this.randomMeal = recipe;
        notifyItemChanged(0);
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyItemChanged(0);
    }
}
