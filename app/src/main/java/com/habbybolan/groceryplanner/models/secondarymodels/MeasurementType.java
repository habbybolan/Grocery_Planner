package com.habbybolan.groceryplanner.models.secondarymodels;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.PopupMenu;

import androidx.annotation.LongDef;

import com.habbybolan.groceryplanner.R;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class MeasurementType implements Parcelable {

    Long measurementId;

    public MeasurementType(@measurementIds Long measurementId) {
        this.measurementId = measurementId;
    }

    @Retention(SOURCE)
    @LongDef({POUND_ID, OUNCE_ID, MILLIGRAM_ID, GRAM_ID, KILOGRAM_ID, TEASPOON_ID, TABLESPOON_ID, GILL_ID,
            CUP_ID, PINT_ID, QUART_ID, GALLON_ID, MILLILITER_ID, LITER_ID, DECILITER_ID,
            MILLIMETER_ID, CENTIMETER_ID, METER_ID, INCH_ID
    })
    public @interface measurementIds {}

    //  mass and weight
    public static final long POUND_ID = 1;
    public static final long OUNCE_ID = 2;
    public static final long MILLIGRAM_ID = 3;
    public static final long GRAM_ID = 4;
    public static final long KILOGRAM_ID = 5;

    public static final String POUND = "Pound";
    public static final String OUNCE = "Ounce";
    public static final String MILLIGRAM = "Milligram";
    public static final String GRAM = "Gram";
    public static final String KILOGRAM = "Kilogram";

    public static final String POUND_CODE = "lb";
    public static final String OUNCE_CODE = "oz";
    public static final String MILLIGRAM_CODE = "mg";
    public static final String GRAM_CODE = "g";
    public static final String KILOGRAM_CODE = "kg";

    // volume
    public static final long TEASPOON_ID = 6;
    public static final long TABLESPOON_ID = 7;
    public static final long GILL_ID = 8;
    public static final long CUP_ID = 9;
    public static final long PINT_ID = 10;
    public static final long QUART_ID = 11;
    public static final long GALLON_ID = 12;
    public static final long MILLILITER_ID = 13;
    public static final long LITER_ID = 14;
    public static final long DECILITER_ID = 15;

    public static final String TEASPOON = "Teaspoon";
    public static final String TABLESPOON = "Tablespoon";
    public static final String GILL = "Gill";
    public static final String CUP = "Cup";
    public static final String PINT = "Pint";
    public static final String QUART = "Quart";
    public static final String GALLON = "Gallon";
    public static final String MILLILITER = "Milliliter";
    public static final String LITER = "Liter";
    public static final String DECILITER = "Deciliter";

    public static final String TEASPOON_CODE = "tsp";
    public static final String TABLESPOON_CODE = "tbsp";
    public static final String GILL_CODE = "gill";
    public static final String CUP_CODE = "c";
    public static final String PINT_CODE = "pt";
    public static final String QUART_CODE = "qt";
    public static final String GALLON_CODE = "gal";
    public static final String MILLILITER_CODE = "ml";
    public static final String LITER_CODE = "l";
    public static final String DECILITER_CODE = "dl";

    // length
    public static final long MILLIMETER_ID = 16;
    public static final long CENTIMETER_ID = 17;
    public static final long METER_ID = 18;
    public static final long INCH_ID = 19;

    public static final String MILLIMETER = "Millimeter";
    public static final String CENTIMETER = "Centimeter";
    public static final String METER = "Meter";
    public static final String INCH = "Inch";

    public static final String MILLIMETER_CODE = "mm";
    public static final String CENTIMETER_CODE = "cm";
    public static final String METER_CODE = "m";
    public static final String INCH_CODE = "in";

    protected MeasurementType(Parcel in) {
        measurementId = in.readLong();
        measurementId = measurementId == 0 ? null : measurementId;
    }

    public static final Parcelable.Creator<Nutrition> CREATOR = new Parcelable.Creator<Nutrition>() {
        @Override
        public Nutrition createFromParcel(Parcel in) {
            return new Nutrition(in);
        }

        @Override
        public Nutrition[] newArray(int size) {
            return new Nutrition[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(measurementId != null ? measurementId : 0);
    }


    public Long getMeasurementId() {
        return measurementId;
    }

    public String getMeasurement() {
        return getMeasurement(getMeasurementId());
    }

    public void setMeasurementId(@measurementIds Long measurementId) {
        this.measurementId = measurementId;
    }

    public static String getMeasurement(long measurementId) {
        switch ((int) measurementId) {
            case (int) POUND_ID:
                return POUND;
            case (int) OUNCE_ID:
                return OUNCE;
            case (int) MILLIGRAM_ID:
                return MILLIGRAM;
            case (int) GRAM_ID:
                return GRAM;
            case (int) KILOGRAM_ID:
                return KILOGRAM;
            case (int) TEASPOON_ID:
                return TEASPOON;
            case (int) TABLESPOON_ID:
                return TABLESPOON;
            case (int) GILL_ID:
                return GILL;
            case (int) CUP_ID:
                return CUP;
            case (int) PINT_ID:
                return PINT;
            case (int) QUART_ID:
                return QUART;
            case (int) GALLON_ID:
                return GALLON;
            case (int) MILLILITER_ID:
                return MILLILITER;
            case (int) LITER_ID:
                return LITER;
            case (int) DECILITER_ID:
                return DECILITER;
            case (int) MILLIMETER_ID:
                return MILLIMETER;
            case (int) CENTIMETER_ID:
                return CENTIMETER;
            case (int) METER_ID:
                return METER;
            case (int) INCH_ID:
                return INCH;
            default:
                throw new IllegalArgumentException("No measurement id for: " + measurementId);
        }
    }

    public static String getMeasurementCode(long measurementId) {
        switch ((int) measurementId) {
            case (int) POUND_ID:
                return POUND_CODE;
            case (int) OUNCE_ID:
                return OUNCE_CODE;
            case (int) MILLIGRAM_ID:
                return MILLIGRAM_CODE;
            case (int) GRAM_ID:
                return GRAM_CODE;
            case (int) KILOGRAM_ID:
                return KILOGRAM_CODE;
            case (int) TEASPOON_ID:
                return TEASPOON_CODE;
            case (int) TABLESPOON_ID:
                return TABLESPOON_CODE;
            case (int) GILL_ID:
                return GILL_CODE;
            case (int) CUP_ID:
                return CUP_CODE;
            case (int) PINT_ID:
                return PINT_CODE;
            case (int) QUART_ID:
                return QUART_CODE;
            case (int) GALLON_ID:
                return GALLON_CODE;
            case (int) MILLILITER_ID:
                return MILLILITER_CODE;
            case (int) LITER_ID:
                return LITER_CODE;
            case (int) DECILITER_ID:
                return DECILITER_CODE;
            case (int) MILLIMETER_ID:
                return MILLIMETER_CODE;
            case (int) CENTIMETER_ID:
                return CENTIMETER_CODE;
            case (int) METER_ID:
                return METER_CODE;
            case (int) INCH_ID:
                return INCH_CODE;
            default:
                throw new IllegalArgumentException("No measurement id for: " + measurementId);
        }
    }

    public static long getMeasurementId(String type) {
        switch (type) {
            case POUND:
                return POUND_ID;
            case OUNCE:
                return OUNCE_ID;
            case MILLIGRAM:
                return MILLIGRAM_ID;
            case GRAM:
                return GRAM_ID;
            case KILOGRAM:
                return KILOGRAM_ID;
            case TEASPOON:
                return TEASPOON_ID;
            case TABLESPOON:
                return TABLESPOON_ID;
            case GILL:
                return GILL_ID;
            case CUP:
                return CUP_ID;
            case PINT:
                return PINT_ID;
            case QUART:
                return QUART_ID;
            case GALLON:
                return GALLON_ID;
            case MILLILITER:
                return MILLILITER_ID;
            case LITER:
                return LITER_ID;
            case DECILITER:
                return DECILITER_ID;
            case MILLIMETER:
                return MILLIMETER_ID;
            case CENTIMETER:
                return CENTIMETER_ID;
            case METER:
                return METER_ID;
            case INCH:
                return INCH_ID;
            default:
                throw new IllegalArgumentException("No measurement type of: " + type);
        }
    }

    public static void createMeasurementTypeMenu(PopupMenu popupMenu) {
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_measurement_types, popupMenu.getMenu());
        Menu menu = popupMenu.getMenu();
        menu.add(POUND);
        menu.add(OUNCE);
        menu.add(MILLIGRAM);
        menu.add(GRAM);
        menu.add(KILOGRAM);
        menu.add(TEASPOON);
        menu.add(TABLESPOON);
        menu.add(GILL);
        menu.add(CUP);
        menu.add(PINT);
        menu.add(QUART);
        menu.add(GALLON);
        menu.add(MILLILITER);
        menu.add(LITER);
        menu.add(DECILITER);
        menu.add(MILLIMETER);
        menu.add(CENTIMETER);
        menu.add(METER);
        menu.add(INCH);
    }
}
