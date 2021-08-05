package com.habbybolan.groceryplanner.di.module;

import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.listing.recipelist.likedrecipelist.recipelist.LikedRecipeListContract;
import com.habbybolan.groceryplanner.listing.recipelist.likedrecipelist.recipelist.LikedRecipeListInteractorImpl;
import com.habbybolan.groceryplanner.listing.recipelist.likedrecipelist.recipelist.LikedRecipeListPresenterImpl;
import com.habbybolan.groceryplanner.listing.recipelist.myrecipelist.recipelist.MyRecipeListContract;
import com.habbybolan.groceryplanner.listing.recipelist.myrecipelist.recipelist.MyRecipeListInteractorImpl;
import com.habbybolan.groceryplanner.listing.recipelist.myrecipelist.recipelist.MyRecipeListPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class RecipeListModule {

    @Provides
    MyRecipeListContract.Interactor provideMyRecipeListInteractor(DatabaseAccess databaseAccess) {
        return new MyRecipeListInteractorImpl(databaseAccess);
    }

    @Provides
    MyRecipeListContract.Presenter provideMyRecipeListPresenter(MyRecipeListContract.Interactor interactor) {
        return new MyRecipeListPresenterImpl(interactor);
    }

    @Provides
    LikedRecipeListContract.Interactor provideLikedRecipeListInteractor(DatabaseAccess databaseAccess) {
        return new LikedRecipeListInteractorImpl(databaseAccess);
    }

    @Provides
    LikedRecipeListContract.Presenter provideLikedRecipeListPresenter(LikedRecipeListContract.Interactor interactor) {
        return new LikedRecipeListPresenterImpl(interactor);
    }
}
