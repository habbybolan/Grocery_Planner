package com.habbybolan.groceryplanner.models.secondarymodels;

import java.sql.Timestamp;

public class TimeModel {

    /**
     * Converts the mssql datetime2 string into proper format for TimeStamp and return the created timestamp.
     * @param datetime2String   String date of DateTime2
     * @return                  The TimeStamp date
     */
    public static Timestamp getTimeStamp(String datetime2String) {
        // todo: does this work in every case?
        String timeStampString = datetime2String.replace('T', ' ');
        return Timestamp.valueOf(timeStampString);
    }
}
