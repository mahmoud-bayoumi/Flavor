package com.example.flavor.data.repo;

import com.example.flavor.data.model.Category;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CategoryRepository {

    private final OkHttpClient client = new OkHttpClient();

    public Single<List<Category>> getCategories() {
        return Single.fromCallable(() -> {
            List<Category> categories = new ArrayList<>();
            Request request = new Request.Builder()
                    .url("https://www.themealdb.com/api/json/v1/1/categories.php")
                    .build();

            Response response = client.newCall(request).execute();
            String json = response.body().string();

            JSONObject obj = new JSONObject(json);
            JSONArray arr = obj.getJSONArray("categories");

            for (int i = 0; i < arr.length(); i++) {
                JSONObject c = arr.getJSONObject(i);
                categories.add(new Category(
                        c.getString("idCategory"),
                        c.getString("strCategory"),
                        c.getString("strCategoryThumb"),
                        c.getString("strCategoryDescription")
                ));
            }

            return categories;
        });
    }
}
