package com.example.android_bt2.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.miniprojectbt2.data.entities.UserEntity;

public class SessionManager {
    private static final String PREFS_NAME = "fruit_app_session";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";

    private final SharedPreferences sharedPreferences;

    public SessionManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void login(UserEntity userEntity) {
        sharedPreferences.edit()
                .putBoolean(KEY_IS_LOGGED_IN, true)
                .putLong(KEY_USER_ID, userEntity.userId)
                .putString(KEY_USERNAME, userEntity.username)
                .apply();
    }

    public void logout() {
        sharedPreferences.edit().clear().apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public long getUserId() {
        return sharedPreferences.getLong(KEY_USER_ID, -1L);
    }

    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "Guest");
    }
}

