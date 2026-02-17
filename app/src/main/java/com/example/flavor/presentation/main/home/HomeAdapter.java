package com.example.flavor.presentation.main.home;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flavor.R;
import com.example.flavor.data.model.Recipe;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_BANNER = 0;
    private static final int TYPE_ITEM = 1;
    private List<Recipe> recipes;

    public HomeAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
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
        if (getItemViewType(position) == TYPE_ITEM) {
            Recipe recipe = recipes.get(position - 1); // Offset for banner
            RecipeViewHolder rvh = (RecipeViewHolder) holder;
            rvh.title.setText(recipe.getTitle());
            rvh.price.setText(recipe.getPrice());
            rvh.rating.setText(recipe.getRating());
        }
    }

    @Override
    public int getItemCount() {
        return recipes.size() + 1; // +1 for the Banner
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
        public BannerViewHolder(View v) { super(v); }
    }
}