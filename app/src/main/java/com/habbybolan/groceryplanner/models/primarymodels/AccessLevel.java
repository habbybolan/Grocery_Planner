package com.habbybolan.groceryplanner.models.primarymodels;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
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
    private String title;

    public AccessLevel(@AccessLevelId int id) {
        this.id = id;
        setTitleFromId();
    }

    public AccessLevel(@AccessLevelTitle String title) {
        this.title = title;
        setAccessIdFromTitle();
    }

    private void setTitleFromId() {
        switch (id) {
            case READ_ONLY_ID:
                title = READ_ONLY_TITLE;
            case EDIT_ID:
                title = EDIT_TITLE;
            case USER_CONTROL_ID:
                title = USER_CONTROL_TITLE;
            case ADMIN_ID:
                title = ADMIN_TITLE;
        }
    }

    private void setAccessIdFromTitle() {
        if (title.isEmpty()) throw new IllegalArgumentException("Title needs to be set");
        switch (title) {
            case READ_ONLY_TITLE:
                id = READ_ONLY_ID;
            case EDIT_TITLE:
                id = EDIT_ID;
            case USER_CONTROL_TITLE:
                id = USER_CONTROL_ID;
            case ADMIN_TITLE:
                id = ADMIN_ID;
        }
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj.getClass() != this.getClass()) return false;
        AccessLevel accessLevel = (AccessLevel) obj;
        return accessLevel.id == id;
    }
}
