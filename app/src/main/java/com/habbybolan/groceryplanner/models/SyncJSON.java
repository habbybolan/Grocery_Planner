package com.habbybolan.groceryplanner.models;

import com.google.gson.JsonObject;

import java.sql.Timestamp;

/**
 * Sync interface for methods that allow to deserialize for syncing.
 * Identifiers to signal type of object snet
 */
public interface SyncJSON {

    /**
     * JSON to sent to web service, determines if the JSONObject should be created as Update or Sync JSON
     * @return  deserialized object.
     */
    JsonObject createSyncJSON();

    /**
     * JSON to sent to web service when attempting to update online database.
     * @return  deserialized Update object.
     */
    JsonObject createSyncJSONUpdate();

    /**
     * JSON to sent to web service when attempting to sync with online database.
     * @return  deserialized Sync object.
     */
    JsonObject createSyncJSONSync();

    static boolean getIsUpdate(Timestamp dateUpdated, Timestamp dateSynchronized) {
        if (dateSynchronized == null) return true;
        return dateUpdated.getTime() > dateSynchronized.getTime();
    }

    String UpdateIdentifier = "update_identifier";
    String Identifier = "identifier";

    // Identifier value to send to web service
    enum OfflineUpdateIdentifier {
        UPDATE,
        SYNC
    }

    // Identifier value to retrieve from web service
    enum OnlineUpdateIdentifier {
        INSERT,
        UPDATE_ONLINE,
        UPDATE_OFFLINE,
        UP_TO_DATE
    }

    // Object identifier
    enum Identifier {
        RECIPE,
        MY_RECIPE,
        LIKED_RECIPE,
        RECIPE_TAG,
        NUTRITION,
        INGREDIENT
    }
}
