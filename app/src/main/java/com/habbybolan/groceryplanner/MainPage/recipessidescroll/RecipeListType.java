package com.habbybolan.groceryplanner.MainPage.recipessidescroll;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
        RecipeListType.NEW_TYPE,
        RecipeListType.TRENDING_DAY_TYPE,
        RecipeListType.TRENDING_WEEK_TYPE,
        RecipeListType.TRENDING_MONTH_TYPE,
        RecipeListType.TRENDING_YEAR_TYPE
})
public @interface RecipeListType {
    String NEW_TYPE = "new";
    String TRENDING_DAY_TYPE = "day";
    String TRENDING_WEEK_TYPE = "week";
    String TRENDING_MONTH_TYPE = "month";
    String TRENDING_YEAR_TYPE = "year";
}
