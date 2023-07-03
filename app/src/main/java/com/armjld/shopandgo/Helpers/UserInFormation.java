package com.armjld.shopandgo.Helpers;


import android.content.Context;

import com.armjld.shopandgo.Models.UserData;


public class UserInFormation {

    public static UserData user;

    public static UserData getUser(Context mContext) {
        if (user == null) {
            user = UserInfoLocal.getUserData(mContext);
        }
        return user;
    }

    public static void updateUser(Context mContext) {
        setUser(getUser(mContext), mContext);
    }

    public static void setUser(UserData user, Context mContext) {
        UserInFormation.user = user;
        UserInfoLocal.saveUser(mContext, user);
    }

    public static void clearUser(Context mContext) {
        setUser(null, mContext);
        UserInfoLocal.clearUserData(mContext);
    }
}
