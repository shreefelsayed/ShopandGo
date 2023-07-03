package com.armjld.shopandgo.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.armjld.shopandgo.Dao.UsersDao;
import com.armjld.shopandgo.ui.HomeActivity;
import com.armjld.shopandgo.Models.UserData;
import com.armjld.shopandgo.ui.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class LoginManager {
    UsersDao usersDao = new UsersDao();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    public void saveUserData(Context mContext) {
        if(mAuth.getCurrentUser() == null) {
            Toasty.error(mContext, "User not found !").show();
            return;
        }

        usersDao.getUser(mAuth.getUid()).addOnSuccessListener(documentSnapshot -> {
            if(!documentSnapshot.exists()) {
                Toasty.error(mContext, "User not found !").show();
                return;
            }

            UserData userData = documentSnapshot.toObject(UserData.class);


            // --> Save the user
            UserInFormation.setUser(userData, mContext);

            // --> Open the activity
            mContext.startActivity(new Intent(mContext, HomeActivity.class));
        });

    }

    public void logOut(Activity mContext) {
        mAuth.signOut();
        UserInFormation.clearUser(mContext);
        mContext.finish();
        mContext.startActivity(new Intent(mContext, LoginActivity.class));
    }
}
