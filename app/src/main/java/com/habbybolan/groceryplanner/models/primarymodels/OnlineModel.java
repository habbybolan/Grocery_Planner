package com.habbybolan.groceryplanner.models.primarymodels;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;

/**
 * For models that have an equivalent object on the online database, creating a connection to that online
 * object by storing the online primary key, as well as dates it was synced and updated to.
 */
public abstract class OnlineModel implements Parcelable {

    protected Long onlineId;
    protected Timestamp dateUpdated;
    protected Timestamp dateSynchronized;

    protected OnlineModel(){}

    public OnlineModel(Parcel in) {
        long val = in.readLong();
        onlineId = val == 0 ? null : val;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(onlineId == null ? 0 : onlineId);
    }

    public Long getOnlineId() {
        return onlineId;
    }
    public void setOnlineId(Long onlineId) {
        this.onlineId = onlineId;
    }

    public boolean isUploadedOnline() {
        return onlineId != null;
    }

    public Timestamp getDateUpdated() {
        return dateUpdated;
    }
    public Timestamp getDateSynchronized() {
        return dateSynchronized;
    }

    public void setDateUpdated(Timestamp dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
    public void setDateSynchronized(Timestamp dateSynchronized) {
        this.dateSynchronized = dateSynchronized;
    }
}
