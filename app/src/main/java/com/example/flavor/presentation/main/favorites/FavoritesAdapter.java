package com.example.flavor.presentation.main.favorites;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flavor.R;
import com.example.flavor.data.model.Recipe;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    private List<Recipe> recipes;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public FavoritesAdapter(List<Recipe> recipes, OnItemClickListener listener) {
        this.recipes = recipes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.title.setText(recipe.getTitle());
        holder.time.setText(recipe.getPrice()); // Using price field for time
        holder.kcal.setText(recipe.getRating()); // Using rating field for kcal
      //  holder.image.setImageResource(recipe.getImageResId());

        holder.deleteBtn.setOnClickListener(v -> listener.onDeleteClick(position));
    }

    @Override
    public int getItemCount() { return recipes.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, time, kcal;
        ImageView image;
        View deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvRecipeTitle);
            time = itemView.findViewById(R.id.tvTime);
            kcal = itemView.findViewById(R.id.tvKcal);
            image = itemView.findViewById(R.id.ivRecipeImage);
            deleteBtn = itemView.findViewById(R.id.btnDelete);
        }
    }
    // Inside FavoritesAdapter class

    public void removeItem(int position) {
        if (position >= 0 && position < recipes.size()) {
            recipes.remove(position);
            // notifyItemRemoved triggers the sliding animation seen in modern apps
            notifyItemRemoved(position);

            // This ensures the positions of items below the deleted one are updated
            notifyItemRangeChanged(position, recipes.size());
        }
    }

}