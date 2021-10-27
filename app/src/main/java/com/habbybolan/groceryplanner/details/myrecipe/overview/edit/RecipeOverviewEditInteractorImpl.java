package com.habbybolan.groceryplanner.details.myrecipe.overview.edit;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.myrecipe.overview.IngredientWithGroceryCheck;
import com.habbybolan.groceryplanner.details.myrecipe.overview.RecipeOverviewContract;
import com.habbybolan.groceryplanner.details.myrecipe.overview.RecipeOverviewInteractorImpl;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.MyRecipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeOverviewEditInteractorImpl extends RecipeOverviewInteractorImpl implements RecipeOverviewContract.InteractorEdit {

    public RecipeOverviewEditInteractorImpl(DatabaseAccess databaseAccess) {
        super(databaseAccess);
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
    public void fetchGroceriesNotHoldingRecipe(MyRecipe myRecipe, DbCallback<Grocery> callback) throws ExecutionException, InterruptedException {
        databaseAccess.fetchGroceriesNotHoldingRecipe(myRecipe, callback);
    }

    @Override
    public void updateRecipeIngredientsInGrocery(MyRecipe myRecipe, Grocery grocery, int amount, List<IngredientWithGroceryCheck> ingredients) {
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
            databaseAccess.deleteGroceryRecipeBridge(myRecipe, grocery);
        else
            // otherwise, update update or add the recipe and its selected/unselected ingredients to the grocery list
            databaseAccess.updateRecipeIngredientsInGrocery(myRecipe, grocery, amount, ingredients);
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
    public void fetchRecipeIngredients(MyRecipe myRecipe, Grocery grocery, boolean isNotInGrocery, DbCallback<IngredientWithGroceryCheck> callback) throws ExecutionException, InterruptedException {
        if (isNotInGrocery) {
            databaseAccess.fetchRecipeIngredientsNotInGrocery(myRecipe, callback);
        } else {
            databaseAccess.fetchRecipeIngredientsInGrocery(grocery, myRecipe, callback);
        }
    }

    @Override
    public void deleteRecipeFromGrocery(MyRecipe myRecipe, Grocery grocery) {
        databaseAccess.deleteRecipeFromGrocery(grocery, myRecipe);
    }

    @Override
    public boolean addTag(List<RecipeTag> recipeTags, MyRecipe myRecipe, String title) {
        String reformattedTitle = RecipeTag.reformatTagTitle(title);
        RecipeTag recipeTag = new RecipeTag(reformattedTitle);
        if (RecipeTag.isTagTitleValid(title) && !recipeTags.contains(recipeTag)) {
            databaseAccess.addRecipeTag(myRecipe.getId(), reformattedTitle);
            recipeTags.add(new RecipeTag(reformattedTitle));
            return true;
        }
        return false;
    }

    @Override
    public void deleteRecipeTag(MyRecipe myRecipe, RecipeTag recipeTag) {
        databaseAccess.deleteRecipeTagFromBridge(myRecipe.getId(), recipeTag);
    }

    @Override
    public void updateRecipe(MyRecipe myRecipe, String name, String servingSize, String cookTime, String prepTime, String description, RecipeCategory category) {
        myRecipe.setName(name);
        myRecipe.setServingSize(servingSize.length() > 0 ? Integer.parseInt(servingSize) : 0);
        myRecipe.setCookTime(cookTime.length() > 0 ? Integer.parseInt(cookTime) : 0);
        myRecipe.setPrepTime(prepTime.length() > 0 ? Integer.parseInt(prepTime) : 0);
        myRecipe.setDescription(description);
        if (category != null) {
            myRecipe.setCategoryId(category.getId());
        } else {
            // otherwise, the 'No Category' was selected
            myRecipe.setCategoryId(null);
        }
        databaseAccess.updateMyRecipe(myRecipe);
    }
}
