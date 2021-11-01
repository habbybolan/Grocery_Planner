package com.habbybolan.groceryplanner.details.offlinerecipes.nutrition;

public class RecipeNutritionPresenterImpl<U extends RecipeNutritionContract.Interactor, T extends RecipeNutritionContract.NutritionView> implements RecipeNutritionContract.Presenter<T> {

    protected U interactor;
    protected T view;

    public RecipeNutritionPresenterImpl(U interactor) {
        this.interactor = interactor;
    }

    @Override
    public void setView(T view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }
}
