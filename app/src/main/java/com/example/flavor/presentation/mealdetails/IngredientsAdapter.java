package com.example.flavor.presentation.mealdetails;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flavor.R;
import com.example.flavor.data.model.Ingredient;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private List<Ingredient> ingredients;

    public IngredientsAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient item = ingredients.get(position);
        holder.name.setText(item.getName());
        holder.image.setImageResource(item.getImageRes());
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView image;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.ivIngredient);
            name = itemView.findViewById(R.id.tvIngredientName);
        }
    }
}