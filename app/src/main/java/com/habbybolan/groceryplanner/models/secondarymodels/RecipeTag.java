package com.habbybolan.groceryplanner.models.secondarymodels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.habbybolan.groceryplanner.models.primarymodels.OnlineModel;

import java.lang.reflect.Type;
import java.sql.Timestamp;

public class RecipeTag extends OnlineModel implements Parcelable {

    public static final String RECIPE_TAG = "recipe_tag";

    private long id;
    private String title;
    private Timestamp dateUpdated;
    private Timestamp dateSynchronized;

    public RecipeTag(long id, Long onlineId, String title, Timestamp dateUpdated, Timestamp dateSynchronized) {
        this.onlineId = onlineId;
        this.id = id;
        this.title = title;
        this.dateUpdated = dateUpdated;
        this.dateSynchronized = dateSynchronized;
    }

    public RecipeTag(long onlineId, String title) {
        this.onlineId = onlineId;
        this.title = title;
    }

    public RecipeTag(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

    protected RecipeTag(Parcel in) {
        super(in);
        id = in.readLong();
        title = in.readString();
        dateSynchronized = (Timestamp) in.readSerializable();
        dateUpdated = (Timestamp) in.readSerializable();
    }

    /**
     * Check if the tag title is valid.
     * @param title title of the tag to check
     * @return      True if the tag title is valid, false otherwise.
     */
    public static boolean isTagTitleValid(String title) {
        boolean isNotEmpty = false;
        boolean noSpecialCharacter = true;
        boolean noLogicBreaks = true;

        for (char c : title.toCharArray()) {
            // title has to have at least one non-empty character
            if (c != ' ') {
                isNotEmpty = true;
                break;
            }
            // title chars must be either letters, space or a dash
            if (!(c >= 65 && c <= 90) && !(c >= 97 && c <= 122) && (c != ' ') && (c != '-')) {
                noSpecialCharacter = false;
                break;
            }
        }
        return isNotEmpty && noSpecialCharacter;
    }

    /**
     * Reformat the tag title so it is in Camel Case, no leading zeros, single space zeros, and single occurring dashes.
     * @param title Tag title to reformat if needed.
     * @return      The reformatted tag title String
     */
    public static String reformatTagTitle(String title) {
        StringBuilder sb = new StringBuilder();
        // reformat the string
        for (char c : title.toCharArray()) {
            // if the last char added is not a space or dash, append the space
            if (c == ' ') {
                if (sb.charAt(sb.length() - 1) != ' ' && sb.charAt(sb.length() - 1) != '-')
                    sb.append(c);
                // if the last char added is not a space or dash, append the dash
            } else if (c == '-') {
                if (sb.charAt(sb.length() - 1) != ' ' && sb.charAt(sb.length() - 1) != '-')
                    sb.append(c);
                // if it's the first character, then capitalize it
            } else if (sb.length() == 0) {
                sb.append(Character.toUpperCase(c));
                // if the last character was a space or dash and current character is a letter, capitalize it
            } else if (sb.length() > 0 && (sb.charAt(sb.length()-1) == ' ' || sb.charAt(sb.length()-1) == '-')) {
                sb.append(Character.toUpperCase(c));
                // otherwise, append the character normally
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final RecipeTag other = (RecipeTag) obj;
        if ((this.title == null) ? (other.title != null) : !this.title.equals(other.title)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.title != null ? this.title.hashCode() : 0);
        return hash;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeSerializable(dateSynchronized);
        dest.writeSerializable(dateUpdated);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RecipeTag> CREATOR = new Creator<RecipeTag>() {
        @Override
        public RecipeTag createFromParcel(Parcel in) {
            return new RecipeTag(in);
        }

        @Override
        public RecipeTag[] newArray(int size) {
            return new RecipeTag[size];
        }
    };

    public static class RecipeTagDeserializer implements JsonDeserializer<RecipeTag> {

        @Override
        public RecipeTag deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            return new RecipeTag(jsonObject.get("recipe_tag_id").getAsLong(), jsonObject.get("title").getAsString());
        }
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
