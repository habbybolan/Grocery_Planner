package com.habbybolan.groceryplanner.http;

import com.google.gson.JsonObject;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.models.webmodels.Login;
import com.habbybolan.groceryplanner.models.webmodels.Signup;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestWebService {

    @GET("/api/recipes")
    Call<List<OnlineRecipe>> listRecipes();

    @GET("/api/recipes/date?sort=ascending")
    Call<List<OnlineRecipe>> listNewRecipes(@Query("offset") int offset, @Query("size") int size);

    @GET("/api/recipes/trending?intervalAmount=1")
    Call<List<OnlineRecipe>> listTrendingRecipes(@Query("offset") int offset, @Query("size") int size,
                                                 @Query("interval") String interval);

    @GET("/api/recipes/users/{userId}/saved")
    Call<List<OnlineRecipe>> listSavedRecipes(@Path("userId") long userId, @Query("offset") int offset, @Query("size") int size,
                                              @Query("sort") String sort);

    @GET("/api/recipes/users/{userId}/uploaded")
    Call<List<OnlineRecipe>> listUploadedRecipes(@Path("userId") long userId, @Query("offset") int offset, @Query("size") int size,
                                                 @Query("sort") String sort);

    @GET("/api/recipes")
    Call<List<OnlineRecipe>> listSearchedRecipes(@Query("offset") int offset, @Query("size") int size,
                                                 @Query("sort") String sort, @Query("nameSearch") String nameSearch,
                                                 @Query("tagSearch") List<String> tagSearches);

    @GET("/api/recipes/tags")
    Call<List<RecipeTag>> listSearchedRecipeTags(@Query("offset") int offset, @Query("size") int size,
                                              @Query("sort") String sort, @Query("tagSearch") String tagSearch);

    @POST("/api/login")
    Call<JsonObject> login(@Body Login login);

    @POST("/api/signup")
    Call<Void> signUp(@Body Signup signup);
}
