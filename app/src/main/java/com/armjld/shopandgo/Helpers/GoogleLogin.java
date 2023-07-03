package com.armjld.shopandgo.Helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.armjld.shopandgo.Dao.UsersDao;
import com.armjld.shopandgo.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

/*
 * A class that is used to login to Google.
 */
@SuppressLint("StaticFieldLeak")
public class GoogleLogin {

    public static final int RC_SIGN_IN = 500;
    public static GoogleSignInClient mGoogleSignInClient;
    public static Context mContext;
    public static FirebaseAuth mAuth;

    // The constructor of the class.
    public GoogleLogin(Context mContext) {
        GoogleLogin.mContext = mContext;

        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mContext.getResources().getString(R.string.google_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(mContext, gso);

        mGoogleSignInClient.signOut();
        signInGoogle();
    }

    /**
     * It signs in the user with the given credential.
     *
     * @param credential   The GoogleAuthCredential object that you get from the GoogleSignInAccount
     *                     object.
     * @param finalAccount
     */
    private static void createAccount(AuthCredential credential, GoogleSignInAccount finalAccount) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential).addOnSuccessListener(task -> {
            UsersDao usersDao = new UsersDao();
            usersDao.createUser(finalAccount, Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).addOnSuccessListener(unused -> {
               Toasty.success(mContext, "Your account was created ").show();
               LoginManager loginManager = new LoginManager();
               loginManager.saveUserData(mContext);
            });
        }).addOnFailureListener(e -> {
            Toasty.error(mContext, Objects.requireNonNull(e.getMessage())).show();
        });
    }

    private static void login(AuthCredential credential) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential).addOnSuccessListener(task -> {
            LoginManager loginManager = new LoginManager();
            loginManager.saveUserData(mContext);
        }).addOnFailureListener(e -> {
            Toasty.error(mContext, Objects.requireNonNull(e.getMessage())).show();
        });
    }

    /**
     * A function that is called when the user clicks on the Google button.
     *
     * @param data The data returned from the Google Sign-In API.
     */

    public static void activityResult(Intent data) {
        if (data == null) return;

        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

        GoogleSignInAccount account = null;

        try {
            account = task.getResult(ApiException.class);
        } catch (ApiException e) {
            e.printStackTrace();
            Log.i("Google Login", "ERROR GOOGLE : " + e);
        }

        if (account == null) return;
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        GoogleSignInAccount finalAccount = account;

        // -->
        UsersDao usersDao = new UsersDao();
        usersDao.searchByGoogle(finalAccount.getId()).addOnSuccessListener(queryDocumentSnapshots -> {
            if(queryDocumentSnapshots.isEmpty()) {
                createAccount(credential, finalAccount);
            } else {
                login(credential);
            }
        });
    }

    /**
     * > This function will start the Google Sign-In process
     */
    private static void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        ((Activity) mContext).startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
