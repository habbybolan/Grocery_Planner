package com.habbybolan.groceryplanner.callbacks;

public interface SyncCompleteCallback {

    // TODO: add info to callback, including data updated/inserted online, and failed syncs
    void onSyncComplete();
    void onSyncFailed(String message);
}
