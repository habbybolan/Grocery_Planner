package com.habbybolan.groceryplanner.details.recipe.recipeoverview;

import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.models.combinedmodels.GroceryRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.OfflineRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeOverviewInteractorImpl implements RecipeOverviewContract.Interactor {

    private DatabaseAccess databaseAccess;

    public RecipeOverviewInteractorImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void loadAllRecipeCategories(DbCallback<RecipeCategory> callback) throws ExecutionException, InterruptedException {
        SortType sortType = new SortType();
        sortType.setSortType(SortType.SORT_ALPHABETICAL_ASC);
        databaseAccess.fetchRecipeCategories(callback, sortType);
    }

    @Override
    public List<IngredientWithGroceryCheck> checkIfAllUnselected(List<IngredientWithGroceryCheck> ingredients) {
        boolean allUnselected = true;
        // find if all the ingredients are in the Grocery or not
        for (IngredientWithGroceryCheck ingredient : ingredients) {
            if (ingredient.getIsInGrocery()) {
                allUnselected = false;
                break;
            }
        }
        // if all ingredients not in grocery, then its recipe is not yet added to the grocery list
        // start the list with all ingredients selected
        if (allUnselected) {
            for (IngredientWithGroceryCheck ingredient : ingredients) {
                ingredient.setIsInGrocery(true);
            }
        }
        return ingredients;
    }

    @Override
    public String[] getNamedOfRecipeCategories(List<RecipeCategory> recipeCategories) {
        String[] categoryNames = new String[recipeCategories.size()+1];
        for (int i = 0; i < recipeCategories.size(); i++) {
            categoryNames[i] = recipeCategories.get(i).getName();
        }
        categoryNames[recipeCategories.size()] = "No Category";
        return categoryNames;
    }

    @Override
    public void fetchRecipeCategory(ObservableField<RecipeCategory> recipeCategoryObserver, long categoryId) throws ExecutionException, InterruptedException {
        databaseAccess.fetchRecipeCategory(recipeCategoryObserver, categoryId);
    }

    @Override
    public void fetchGroceriesHoldingRecipe(OfflineRecipe offlineRecipe, DbCallback<GroceryRecipe> callback) throws ExecutionException, InterruptedException {
        databaseAccess.fetchGroceriesHoldingRecipe(offlineRecipe, callback);
    }

    @Override
    public void fetchGroceriesNotHoldingRecipe(OfflineRecipe offlineRecipe, DbCallback<Grocery> callback) throws ExecutionException, InterruptedException {
        databaseAccess.fetchGroceriesNotHoldingRecipe(offlineRecipe, callback);
    }

    @Override
    public void updateRecipeIngredientsInGrocery(OfflineRecipe offlineRecipe, Grocery grocery, int amount, List<IngredientWithGroceryCheck> ingredients) {
        // loop through the ingredients and find if all of them are not added to Grocery list
        // if none added, then delete the recipe from the GroceryRecipeBridge
        boolean hasIngredientSelected = false;
        for (IngredientWithGroceryCheck ingredient : ingredients) {
            if (ingredient.getIsInGrocery()) {
                hasIngredientSelected = true;
                break;
            }
        }
        // if no ingredient inside the grocery list, then remove the recipe grocery relationship
        if (!hasIngredientSelected)
            databaseAccess.deleteGroceryRecipeBridge(offlineRecipe, grocery);
        else
            // otherwise, update update or add the recipe and its selected/unselected ingredients to the grocery list
            databaseAccess.updateRecipeIngredientsInGrocery(offlineRecipe, grocery, amount, ingredients);
    }

    @Override
    public String[] getArrayOfGroceryNames(List<Grocery> groceries) {
        String[] groceryNames = new String[groceries.size()];
        for (int i = 0; i < groceries.size(); i++) {
            groceryNames[i] = groceries.get(i).getName();
        }
        return groceryNames;
    }

    @Override
    public String[] getArrayOfIngredientNames(List<IngredientWithGroceryCheck> ingredients) {
        String[] ingredientNames = new String[ingredients.size()];
        for (int i = 0; i < ingredients.size(); i++) {
            ingredientNames[i] = ingredients.get(i).getName();
        }
        return ingredientNames;
    }

    @Override
    public void fetchRecipeIngredients(OfflineRecipe offlineRecipe, Grocery grocery, boolean isNotInGrocery, DbCallback<IngredientWithGroceryCheck> callback) throws ExecutionException, InterruptedException {
       if (isNotInGrocery) {
           databaseAccess.fetchRecipeIngredientsNotInGrocery(offlineRecipe, callback);
       } else {
           databaseAccess.fetchRecipeIngredientsInGrocery(grocery, offlineRecipe, callback);
       }
    }

    @Override
    public void deleteRecipeFromGrocery(OfflineRecipe offlineRecipe, Grocery grocery) {
        databaseAccess.deleteRecipeFromGrocery(grocery, offlineRecipe);
    }

    @Override
    public boolean addTag(List<RecipeTag> recipeTags, OfflineRecipe offlineRecipe, String title) {
        String reformattedTitle = RecipeTag.reformatTagTitle(title);
        RecipeTag recipeTag = new RecipeTag(reformattedTitle);
        if (RecipeTag.isTagTitleValid(title) && !recipeTags.contains(recipeTag)) {
            databaseAccess.addRecipeTag(offlineRecipe.getId(), reformattedTitle);
            recipeTags.add(new RecipeTag(reformattedTitle));
            return true;
        }
        return false;
    }

    @Override
    public void fetchTags(OfflineRecipe offlineRecipe, DbCallback<RecipeTag> callback) throws ExecutionException, InterruptedException {
        databaseAccess.fetchRecipeTags(offlineRecipe.getId(), callback);
    }

    @Override
    public void deleteRecipeTag(OfflineRecipe offlineRecipe, RecipeTag recipeTag) {
        databaseAccess.deleteRecipeTagFromBridge(offlineRecipe.getId(), recipeTag);
    }

    @Override
    public void updateRecipe(OfflineRecipe offlineRecipe, String name, String servingSize, String cookTime, String prepTime, String description, RecipeCategory category) {
        offlineRecipe.setName(name);
        offlineRecipe.setServingSize(servingSize.length() > 0 ? Integer.parseInt(servingSize) : 0);
        offlineRecipe.setCookTime(cookTime.length() > 0 ? Integer.parseInt(cookTime) : 0);
        offlineRecipe.setPrepTime(prepTime.length() > 0 ? Integer.parseInt(prepTime) : 0);
        offlineRecipe.setDescription(description);
        if (category != null) {
            offlineRecipe.setCategoryId(category.getId());
        } else {
            // otherwise, the 'No Category' was selected
            offlineRecipe.setCategoryId(null);
        }
        databaseAccess.updateRecipe(offlineRecipe);
    }

}
