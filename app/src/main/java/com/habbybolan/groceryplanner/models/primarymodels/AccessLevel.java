package com.habbybolan.groceryplanner.models.primarymodels;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Level of access for a User for how they can interact with a recipe and grocery list.
 * Access levels stored in local database.
 */
public class AccessLevel {

    @Retention(RetentionPolicy.RUNTIME)
    @IntDef({READ_ONLY_ID, EDIT_ID, USER_CONTROL_ID, ADMIN_ID})
    public @interface AccessLevelId {}

    @Retention(RetentionPolicy.RUNTIME)
    @StringDef({READ_ONLY_TITLE, EDIT_TITLE, USER_CONTROL_TITLE, ADMIN_TITLE})
    public @interface AccessLevelTitle {}

    public static final int READ_ONLY_ID = 0;
    public static final String READ_ONLY_TITLE = "read-only";
    public static final int EDIT_ID = 1;
    public static final String EDIT_TITLE = "edit";
    public static final int USER_CONTROL_ID = 2;
    public static final String USER_CONTROL_TITLE = "user-control";
    public static final int ADMIN_ID = 3;
    public static final String ADMIN_TITLE = "admin";

    private int id;
    private String accessLevel;

    public AccessLevel(@AccessLevelId int id, @AccessLevelTitle String accessLevel) {
        this.id = id;
        this.accessLevel = accessLevel;
    }

    public int getId() {
        return id;
    }

    public String getAccessLevel() {
        return accessLevel;
    }
}
