package com.habbybolan.groceryplanner.models.secondarymodels;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class SortType {

    private int sortKey;

    public SortType() {
        sortKey = SORT_NONE;
    }

    @Retention(SOURCE)
    @IntDef({SORT_NONE, SORT_ALPHABETICAL_ASC, SORT_ALPHABETICAL_DEC, SORT_DATE_NEW, SORT_DATE_OLD})
    public @interface SortMethods {}

    public void setSortType(@SortMethods int sortKey) {
        this.sortKey = sortKey;
    }

    public final static int SORT_NONE = 0;
    public final static String SORT_NONE_TITLE = "None";
    public final static int SORT_ALPHABETICAL_ASC = 1;
    public final static String SORT_ALPHABETICAL_ASC_TITLE = "Ascending";
    public final static int SORT_ALPHABETICAL_DEC = 2;
    public final static String SORT_ALPHABETICAL_DEC_TITLE = "Descending";
    public final static int SORT_DATE_NEW = 3;
    public final static String SORT_DATE_NEW_TITLE = "Newest";
    public final static int SORT_DATE_OLD = 4;
    public final static String SORT_DATE_OLD_TITLE = "Oldest";

    public final static int SORT_LIST_ALL = 0;
    public final static int SORT_LIST_ALPHABETICAL = 1;

    public int getSortKey() {
        return sortKey;
    }

    public static String[] allSortTypes() {
        return new String[]{SortType.SORT_NONE_TITLE, SortType.SORT_ALPHABETICAL_ASC_TITLE,
                SortType.SORT_ALPHABETICAL_DEC_TITLE, SortType.SORT_DATE_NEW_TITLE, SortType.SORT_DATE_OLD_TITLE};
    }

    public static String[] sortTypesAlphabetical() {
        return new String[]{SortType.SORT_NONE_TITLE, SortType.SORT_ALPHABETICAL_ASC_TITLE,
                SortType.SORT_ALPHABETICAL_DEC_TITLE};
    }

    @Retention(SOURCE)
    @IntDef({SortType.SORT_LIST_ALL, SortType.SORT_LIST_ALPHABETICAL})
    public @interface SortListType {}

    public static String[] getSortListType(@SortListType int sortListType) {
        switch (sortListType) {
            case SORT_LIST_ALL:
                return allSortTypes();
            case SORT_LIST_ALPHABETICAL:
                return sortTypesAlphabetical();
            default:
                return allSortTypes();
        }
    }

    public static int getSortTypeFromTitle(String title) {
        switch (title) {
            case SORT_ALPHABETICAL_ASC_TITLE:
                return SORT_ALPHABETICAL_ASC;
            case SORT_ALPHABETICAL_DEC_TITLE:
                return SORT_ALPHABETICAL_DEC;
            case SORT_DATE_NEW_TITLE:
                return SORT_DATE_NEW;
            case SORT_DATE_OLD_TITLE:
                return SORT_DATE_OLD;
            default:
                return SORT_NONE;
        }
    }
}
