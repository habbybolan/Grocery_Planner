package com.habbybolan.groceryplanner.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.habbybolan.groceryplanner.http.RequestInterceptor;
import com.habbybolan.groceryplanner.http.RestWebService;
import com.habbybolan.groceryplanner.models.lists.MyRecipeList;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.User;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Module
public class NetworkModule {

    public NetworkModule() {}

    public static final int CONNECT_TIMEOUT_IN_MS = 300;
    public static final String REST_BASE_URL = "http://192.168.0.16:8080";

    @Provides
    @Singleton
    Interceptor requestInterceptor(Context application) {
        return new RequestInterceptor(application);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(RequestInterceptor requestInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNECT_TIMEOUT_IN_MS, TimeUnit.MILLISECONDS)
                .addInterceptor(requestInterceptor);
        return builder.build();
    }

    Gson getRecipeGson() {
        return new GsonBuilder()
                .registerTypeAdapter(OnlineRecipe.class, new OnlineRecipe.OnlineRecipeDeserialize())
                .create();
    }

    Gson setRecipeListGson() {
        return new GsonBuilder()
                .registerTypeAdapter(MyRecipeList.class, new MyRecipeList.MyRecipeListSerializer())
                .create();
    }

    Gson getRecipeTagGson() {
        return new GsonBuilder()
                .registerTypeAdapter(RecipeTag.class, new RecipeTag.RecipeTagDeserializer())
                .create();
    }

    Gson getUserGson() {
        return new GsonBuilder()
                .registerTypeAdapter(User.class, new User.UserDeserializer())
                .create();
    }

    @Singleton
    @Provides
    Retrofit retrofit(OkHttpClient okHttpClient) {
        return new Retrofit
                .Builder()
                .baseUrl(REST_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(setRecipeListGson()))
                .addConverterFactory(GsonConverterFactory.create(getRecipeGson()))
                .addConverterFactory(GsonConverterFactory.create(getRecipeTagGson()))
                .addConverterFactory(GsonConverterFactory.create(getUserGson()))
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Singleton
    @Provides
    RestWebService restWebService(Retrofit retrofit) {
        return retrofit.create(RestWebService.class);
    }
}
