package com.habbybolan.groceryplanner.MainPage.recipessidescroll;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        RecipeListType.NEW_TYPE,
        RecipeListType.TRENDING_TYPE,
        RecipeListType.SAVED_TYPE
})
public @interface RecipeListType {
    int NEW_TYPE = 0;
    int TRENDING_TYPE = 1;
    int SAVED_TYPE = 2;
}
