package com.habbybolan.groceryplanner.models.primarymodels;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.habbybolan.groceryplanner.models.secondarymodels.TimeModel;

import java.lang.reflect.Type;
import java.sql.Timestamp;

/**
 * A user, created on login for global use.
 * Stores necessary information to identify the user and communicate with API.
 * A default User object with isLoggedOn = false is created if user continues through offline mode.
 */
public class User {

    // sets if the user is logged on
    private boolean isLoggedOn;

    private long onlineId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    // true if the user is an admin
    private boolean isAdmin;
    // date the user's account was created
    private Timestamp dateCreated;
    // date the user last logged in
    private Timestamp dateLastLogin;
    private String photoURL;
    private String ip;
    // date the user accepted the terms and conditions
    private Timestamp dateTermsAccepted;
    private String token;

    public User() {
        isLoggedOn = false;
    }

    public void copyUser(User user) {
        // todo:
    }

    public static class UserDeserializer implements JsonDeserializer<User> {

        @Override
        public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            User user = new User();
            JsonObject userJson = json.getAsJsonObject();
            user.setOnlineId(userJson.get("id").getAsLong());
            if (userJson.has("firstName")) user.setFirstName(userJson.get("firstName").getAsString());
            if (userJson.has("lastName")) user.setFirstName(userJson.get("lastName").getAsString());
            user.setUsername(userJson.get("username").getAsString());
            user.setEmail(userJson.get("email").getAsString());
            user.setAdmin(userJson.get("admin").getAsBoolean());
            user.setDateCreated(TimeModel.getTimeStamp(userJson.get("dateCreated").getAsString()));
            user.setDateCreated(TimeModel.getTimeStamp(userJson.get("dateLastLogin").getAsString()));
            user.setPhotoURL(userJson.get("photoURL").getAsString());
            user.setIp(userJson.get("ip").getAsString());
            user.setDateTermsAccepted(TimeModel.getTimeStamp(userJson.get("dateTermsAccepted").getAsString()));
            user.setToken(userJson.get("token").getAsString());
            return user;
        }
    }

    public boolean isLoggedOn() {
        return isLoggedOn;
    }

    public void setLoggedOn(boolean loggedOn) {
        isLoggedOn = loggedOn;
    }

    public long getOnlineId() {
        return onlineId;
    }

    public void setOnlineId(long onlineId) {
        this.onlineId = onlineId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateLastLogin() {
        return dateLastLogin;
    }

    public void setDateLastLogin(Timestamp dateLastLogin) {
        this.dateLastLogin = dateLastLogin;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Timestamp getDateTermsAccepted() {
        return dateTermsAccepted;
    }

    public void setDateTermsAccepted(Timestamp dateTermsAccepted) {
        this.dateTermsAccepted = dateTermsAccepted;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
