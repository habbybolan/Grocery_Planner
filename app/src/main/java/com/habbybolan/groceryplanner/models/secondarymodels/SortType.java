package com.habbybolan.groceryplanner.models.secondarymodels;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class SortType {

    public static final String SORT_TYPE = "sort_type";

    private int sortKey;

    public SortType() {
        sortKey = SORT_ALPHABETICAL_ASC;
    }

    public SortType(@SortMethods int sortKey) {
        this.sortKey = sortKey;
    }

    @Retention(SOURCE)
    @IntDef({SORT_ALPHABETICAL_ASC, SORT_ALPHABETICAL_DEC, SORT_DATE_NEW, SORT_DATE_OLD, SORT_RELEVANT})
    public @interface SortMethods {}

    public void setSortType(@SortMethods int sortKey) {
        this.sortKey = sortKey;
    }

    public final static int SORT_ALPHABETICAL_ASC = 1;
    public final static String SORT_ALPHABETICAL_ASC_TITLE = "Ascending";
    public final static int SORT_ALPHABETICAL_DEC = 2;
    public final static String SORT_ALPHABETICAL_DEC_TITLE = "Descending";
    public final static int SORT_DATE_NEW = 3;
    public final static String SORT_DATE_NEW_TITLE = "Newest";
    public final static int SORT_DATE_OLD = 4;
    public final static String SORT_DATE_OLD_TITLE = "Oldest";
    public final static int SORT_RELEVANT = 5;
    public final static String SORT_RELEVANT_TITLE = "Relevance";

    public final static int SORT_LIST_MOST = 0;
    public final static int SORT_LIST_ALPHABETICAL = 1;
    public final static int SORT_LIST_ALL = 2;

    public int getSortKey() {
        return sortKey;
    }

    private static String[] allSortTypes() {
        return new String[]{SortType.SORT_ALPHABETICAL_ASC_TITLE,
                SortType.SORT_ALPHABETICAL_DEC_TITLE, SortType.SORT_DATE_NEW_TITLE, SortType.SORT_DATE_OLD_TITLE,
                SortType.SORT_RELEVANT_TITLE};
    }

    private static String[] mostSortTypes() {
        return new String[]{SortType.SORT_ALPHABETICAL_ASC_TITLE,
                SortType.SORT_ALPHABETICAL_DEC_TITLE, SortType.SORT_DATE_NEW_TITLE, SortType.SORT_DATE_OLD_TITLE};
    }

    private static String[] alphabeticalSortTypes() {
        return new String[]{SortType.SORT_ALPHABETICAL_ASC_TITLE,
                SortType.SORT_ALPHABETICAL_DEC_TITLE};
    }

    @Retention(SOURCE)
    @IntDef({SortType.SORT_LIST_MOST, SortType.SORT_LIST_ALPHABETICAL, SortType.SORT_LIST_ALL})
    public @interface SortListType {}

    public static String[] getSortListType(@SortListType int sortListType) {
        switch (sortListType) {
            case SORT_LIST_MOST:
                return mostSortTypes();
            case SORT_LIST_ALPHABETICAL:
                return alphabeticalSortTypes();
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
            case SORT_RELEVANT_TITLE:
                return SORT_RELEVANT;
            default:
                throw new IllegalStateException(title + " is not a valid sort type");
        }
    }

    public String getSortTitle() {
        switch (sortKey) {
            case SORT_ALPHABETICAL_ASC:
                return SORT_ALPHABETICAL_ASC_TITLE;
            case SORT_ALPHABETICAL_DEC:
                return SORT_ALPHABETICAL_DEC_TITLE;
            case SORT_DATE_NEW:
                return SORT_DATE_NEW_TITLE;
            case SORT_DATE_OLD:
                return SORT_DATE_OLD_TITLE;
            case SORT_RELEVANT:
                return SORT_RELEVANT_TITLE;
            default:
                throw new IllegalStateException(sortKey + " is not a valid sort key");
        }
    }
}
