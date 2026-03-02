package com.example.flavor.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Delete;


import com.example.flavor.data.local.entities.FavoriteRecipe;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface FavoriteRecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(FavoriteRecipe recipe);

    @Delete
    Completable delete(FavoriteRecipe recipe);

    @Query("SELECT * FROM favorite_recipes WHERE userId = :userId")
    Single<List<FavoriteRecipe>> getAllFavorites(String userId);

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_recipes WHERE id = :id AND userId = :userId)")
    Single<Boolean> isFavorite(String id, String userId);
}