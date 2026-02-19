package com.example.flavor.core.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsManager {

    private static PrefsManager instance;
    private final SharedPreferences prefs;

    private static final String PREFS_NAME = "flavor_prefs";
    private static final String KEY_LOGGED_IN_USER = "logged_in_user";

    private PrefsManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static PrefsManager getInstance(Context context) {
        if (instance == null) {
            instance = new PrefsManager(context.getApplicationContext());
        }
        return instance;
    }

    public void setLoggedInUser(String userId) {
        prefs.edit().putString(KEY_LOGGED_IN_USER, userId).apply();
    }

    public String getLoggedInUser() {
        return prefs.getString(KEY_LOGGED_IN_USER, null);
    }

    public boolean isLoggedIn() {
        return getLoggedInUser() != null;
    }

    public void clearLoginState() {
        prefs.edit().remove(KEY_LOGGED_IN_USER).apply();
    }
}
