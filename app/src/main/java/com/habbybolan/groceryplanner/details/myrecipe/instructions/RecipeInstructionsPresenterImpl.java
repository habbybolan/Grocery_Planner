package com.habbybolan.groceryplanner.details.myrecipe.instructions;

import javax.inject.Inject;

public class RecipeInstructionsPresenterImpl<U extends RecipeInstructionsContract.Interactor, T extends RecipeInstructionsContract.RecipeInstructionsView> implements RecipeInstructionsContract.Presenter<T> {

    protected U interactor;
    protected T view;

    @Inject
    public RecipeInstructionsPresenterImpl(U interactor) {
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

    private boolean isViewAttached() {
        return view != null;
    }
}
