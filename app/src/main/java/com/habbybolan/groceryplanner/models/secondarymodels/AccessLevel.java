package com.habbybolan.groceryplanner.models.secondarymodels;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Represents the access level a user has to a recipe or grocery list.
 * Can be access level:
 *      - Read Only:    Can only view
 *      - Edit:         Everything before and can Edit
 *      - User Control: Everything before and can control the other user's access levels
 *      - Admin:        Owns the Recipe/Grocery list, has full control of everything
 */
public class AccessLevel {

    // Types of access levels for a user
    public static final int ACCESS_READ_ONLY = 0;
    public static final int ACCESS_EDIT = 1;
    public static final int ACCESS_USER_CONTROL = 2;
    public static final int ACCESS_ADMIN = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ACCESS_READ_ONLY, ACCESS_EDIT, ACCESS_USER_CONTROL, ACCESS_ADMIN})
    public @interface AccessLevelType {}

    // access level stored. Defaults to Read Only.
    private int accessLevel = 0;

    public AccessLevel(@AccessLevelType int accessLevel) {
        this.accessLevel = accessLevel;
    }

    // defaults to Read Only
    public AccessLevel() {}

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(@AccessLevelType int accessLevel) {
        this.accessLevel = accessLevel;
    }
}
