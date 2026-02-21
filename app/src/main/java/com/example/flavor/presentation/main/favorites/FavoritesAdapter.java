package com.example.flavor.presentation.main.favorites;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flavor.R;
import com.example.flavor.data.local.entities.FavoriteRecipe;

import java.util.ArrayList;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {

    private final List<FavoriteRecipe> recipes = new ArrayList<>();
    private final OnItemClickListener listener;
    private final Context context;

    public interface OnItemClickListener {
        void onDeleteClick(FavoriteRecipe recipe, int position);
        void onItemClick(FavoriteRecipe recipe); // Trigger navigation to MealDetailsActivity
    }

    public FavoritesAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    // Replace the current list of recipes
    public void setItems(List<FavoriteRecipe> newRecipes) {
        recipes.clear();
        recipes.addAll(newRecipes);
        notifyDataSetChanged();
    }

    // Remove a recipe at a specific position
    public void removeItem(int position) {
        if (position >= 0 && position < recipes.size()) {
            recipes.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, recipes.size());
        }
    }

    // Getter for external access to recipes
    public List<FavoriteRecipe> getRecipes() {
        return recipes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteRecipe recipe = recipes.get(position);

        holder.title.setText(recipe.getTitle());
        holder.category.setText(recipe.getCategory());

        Glide.with(context)
                .load(recipe.getImageUrl())
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .into(holder.image);

        holder.deleteBtn.setOnClickListener(v -> listener.onDeleteClick(recipe, position));

        holder.itemView.setOnClickListener(v -> listener.onItemClick(recipe));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title, category;
        final ImageView image;
        final ImageButton deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvRecipeTitle);
            category = itemView.findViewById(R.id.tvCategory);
            image = itemView.findViewById(R.id.ivRecipeImage);
            deleteBtn = itemView.findViewById(R.id.btnDelete);
        }
    }
}