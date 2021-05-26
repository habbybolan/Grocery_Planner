package com.habbybolan.groceryplanner.models.primarymodels;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class OnlineModel implements Parcelable {

    protected Long onlineId;

    public OnlineModel(){}

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

    public boolean isUploadedOnline() {
        return onlineId != null;
    }
}
