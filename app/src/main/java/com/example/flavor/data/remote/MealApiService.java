package com.example.flavor.data.remote;

import com.example.flavor.data.model.MealResponse;

import retrofit2.http.GET;
import io.reactivex.Single;
import retrofit2.http.Query;


public interface MealApiService {

    @GET("random.php")
    Single<MealResponse> getRandomMeal();

    @GET("filter.php")
    Single<MealResponse> getMealsByCategory(@Query("c") String category);

    @GET("search.php")
    Single<MealResponse> searchMealsByName(@Query("s") String mealName);

    @GET("lookup.php")
    Single<MealResponse> getMealDetails(@Query("i") String mealId);
}
