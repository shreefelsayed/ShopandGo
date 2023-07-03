package com.armjld.shopandgo.Helpers;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.armjld.shopandgo.Models.UserData;
import com.google.gson.Gson;


public class UserInfoLocal {
    public static void saveUser(Context mContext, UserData user) {
        SharedPreferences mPrefs = mContext.getSharedPreferences(mContext.getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString("Current User", json);
        prefsEditor.apply();
    }

    public static UserData getUserData(Context mContext) {
        SharedPreferences mPrefs = mContext.getSharedPreferences(mContext.getPackageName(), MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("Current User", "");

        if (json.equals("")) return null;
        return gson.fromJson(json, UserData.class);
    }

    public static void clearUserData(Context mContext) {
        SharedPreferences mPrefs = mContext.getSharedPreferences(mContext.getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString("Current User", "");
        prefsEditor.apply();
    }
}
