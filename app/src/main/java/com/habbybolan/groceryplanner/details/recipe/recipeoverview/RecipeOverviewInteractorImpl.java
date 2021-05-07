package com.habbybolan.groceryplanner.details.recipe.recipeoverview;

import androidx.databinding.ObservableField;

import com.habbybolan.groceryplanner.DbCallback;
import com.habbybolan.groceryplanner.database.DatabaseAccess;
import com.habbybolan.groceryplanner.details.recipe.RecipeDetailsInteractorImpl;
import com.habbybolan.groceryplanner.models.combinedmodels.GroceryRecipe;
import com.habbybolan.groceryplanner.models.primarymodels.Grocery;
import com.habbybolan.groceryplanner.models.primarymodels.Recipe;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeCategory;
import com.habbybolan.groceryplanner.models.secondarymodels.RecipeTag;
import com.habbybolan.groceryplanner.models.secondarymodels.SortType;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RecipeOverviewInteractorImpl extends RecipeDetailsInteractorImpl implements RecipeOverviewInteractor {

    public RecipeOverviewInteractorImpl(DatabaseAccess databaseAccess) {
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
    public void fetchRecipeCategory(ObservableField<RecipeCategory> recipeCategoryObserver, long categoryId) throws ExecutionException, InterruptedException {
        databaseAccess.fetchRecipeCategory(recipeCategoryObserver, categoryId);
    }

    @Override
    public void fetchGroceriesHoldingRecipe(Recipe recipe, DbCallback<GroceryRecipe> callback) throws ExecutionException, InterruptedException {
        databaseAccess.fetchGroceriesHoldingRecipe(recipe, callback);
    }

    @Override
    public void fetchGroceriesNotHoldingRecipe(Recipe recipe, DbCallback<Grocery> callback) throws ExecutionException, InterruptedException {
        databaseAccess.fetchGroceriesNotHoldingRecipe(recipe, callback);
    }

    @Override
    public void updateRecipeIngredientsInGrocery(Recipe recipe, Grocery grocery, int amount, List<IngredientWithGroceryCheck> ingredients) {
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
            databaseAccess.deleteGroceryRecipeBridge(recipe, grocery);
        else
            // otherwise, update update or add the recipe and its selected/unselected ingredients to the grocery list
            databaseAccess.updateRecipeIngredientsInGrocery(recipe, grocery, amount, ingredients);
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
    public void fetchRecipeIngredients(Recipe recipe, Grocery grocery, boolean isNotInGrocery, DbCallback<IngredientWithGroceryCheck> callback) throws ExecutionException, InterruptedException {
       if (isNotInGrocery) {
           databaseAccess.fetchRecipeIngredientsNotInGrocery(recipe, callback);
       } else {
           databaseAccess.fetchRecipeIngredientsInGrocery(grocery, recipe, callback);
       }
    }

    @Override
    public void deleteRecipeFromGrocery(Recipe recipe, Grocery grocery) {
        databaseAccess.deleteRecipeFromGrocery(grocery, recipe);
    }

    @Override
    public void addTag(Recipe recipe, String title) {
        databaseAccess.addRecipeTag(recipe.getId(), title);
    }

    @Override
    public void fetchTags(Recipe recipe, DbCallback<RecipeTag> callback) throws ExecutionException, InterruptedException {
        databaseAccess.fetchRecipeTags(recipe.getId(), callback);
    }

    @Override
    public void deleteRecipeTag(Recipe recipe, RecipeTag recipeTag) {
        databaseAccess.deleteRecipeTag(recipe.getId(), recipeTag.getId());
    }

    @Override
    public boolean isTagTitleValid(String title) {
        boolean isNotEmpty = false;
        boolean noSpecialCharacter = true;
        boolean noLogicBreaks = true;

        for (char c : title.toCharArray()) {
            // title has to have at least one non-empty character
            if (c != ' ') {
                isNotEmpty = true;
                break;
            }
            // title chars must be either letters, space or a dash
            if (!(c >= 65 && c <= 90) && !(c >= 97 && c <= 122) && (c != ' ') && (c != '-')) {
                noSpecialCharacter = false;
                break;
            }
        }
        return isNotEmpty && noSpecialCharacter;
    }

    @Override
    public String reformatTagTitle(String title) {
        StringBuilder sb = new StringBuilder();
        // reformat the string
        for (char c : title.toCharArray()) {
            // if the last char added is not a space or dash, append the space
            if (c == ' ') {
                if (sb.charAt(sb.length() - 1) != ' ' && sb.charAt(sb.length() - 1) != '-')
                    sb.append(c);
                // if the last char added is not a space or dash, append the dash
            } else if (c == '-') {
                if (sb.charAt(sb.length() - 1) != ' ' && sb.charAt(sb.length() - 1) != '-')
                    sb.append(c);
                // if it's the first character, then capitalize it
            } else if (sb.length() == 0) {
                sb.append(Character.toUpperCase(c));
                // if the last character was a space or dash and current character is a letter, capitalize it
            } else if (sb.length() > 0 && (sb.charAt(sb.length()-1) == ' ' || sb.charAt(sb.length()-1) == '-')) {
                sb.append(Character.toUpperCase(c));
                // otherwise, append the character normally
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
