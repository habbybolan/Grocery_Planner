package com.habbybolan.groceryplanner.details.offlinerecipes.detailsactivity;

import com.habbybolan.groceryplanner.callbacks.DbSingleCallback;
import com.habbybolan.groceryplanner.callbacks.SyncCompleteCallback;
import com.habbybolan.groceryplanner.models.primarymodels.LikedRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;

import java.util.concurrent.ExecutionException;

public interface RecipeDetailsContract {

    interface Presenter<U extends OfflineRecipe> {
        void setView(DetailsView<U> view);
        void destroy();
    }

    interface PresenterLikedRecipe extends Presenter<LikedRecipe> {}

    interface PresenterMyRecipe extends Presenter<MyRecipe> {

        /**
         * Send to Interactor to sync myRecipe.
         * @param recipeId  id of Recipe to sync with online database.
         */
        void onSyncMyRecipe(long recipeId, SyncCompleteCallback callback);
    }

    interface Interactor {
    }

    interface InteractorMyRecipe extends Interactor{

        /**
         * Deserialize myRecipe to JSON for sending to online database and syncing.
         * @param recipeId  id of recipe to sync
         * @param callback  called when the data send from web service is all applied to offline database
         */
        void onSyncMyRecipe(long recipeId, SyncCompleteCallback callback);
    }

    interface InteractorLikedRecipe extends Interactor {

        /**
         * Retrieve the full LikedRecipe from the room database.
         * @param recipeId  Id of recipe to retrieve in full
         * @param callback  Callback to signal the LikedRecipe is loaded
         */
        void fetchLikedRecipe(long recipeId, DbSingleCallback<LikedRecipe> callback) throws ExecutionException, InterruptedException;
    }

    interface DetailsView<U extends OfflineRecipe> {

        /**
         * When recipe updates from a sync, notify the fragments to update the recipe values
         */
        void updateFragments();
    }
}
