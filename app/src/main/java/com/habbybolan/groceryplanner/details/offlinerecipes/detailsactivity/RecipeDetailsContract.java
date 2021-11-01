package com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity;

import com.habbybolan.groceryplanner.DbSingleCallback;
import com.habbybolan.groceryplanner.models.primarymodels.LikedRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

import java.util.concurrent.ExecutionException;

public interface RecipeDetailsContract {

    interface Presenter<U extends OfflineRecipe> {
        void setView(DetailsView<U> view);
        void destroy();

        void loadFullRecipe(long recipeId);
    }

    interface PresenterLikedRecipe extends Presenter<LikedRecipe> {}

    interface PresenterMyRecipe extends Presenter<MyRecipe> {}

    interface Interactor {

        /**
         * Retrieve the full recipe from the room database.
         * @param recipeId  Id of recipe to retrieve in full
         * @param callback  Callback to signal the OfflineRecipe is loaded
         */
        void fetchMyRecipe(long recipeId, DbSingleCallback<MyRecipe> callback) throws ExecutionException, InterruptedException;

        void fetchLikedRecipe(long recipeId, DbSingleCallback<LikedRecipe> callback) throws ExecutionException, InterruptedException;
    }

    interface DetailsView<U extends OfflineRecipe> {

        void showRecipe(U recipe);
    }
}
