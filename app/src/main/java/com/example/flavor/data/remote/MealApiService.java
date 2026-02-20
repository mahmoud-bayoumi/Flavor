package com.example.flavor.data.remote;

import com.example.flavor.data.model.RandomMealResponse;

import retrofit2.http.GET;
import io.reactivex.Single;


public interface MealApiService {

    @GET("random.php")
    Single<RandomMealResponse> getRandomMeal();
}
