package com.armjld.shopandgo.Dao;

import com.armjld.shopandgo.Models.UserData;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class UsersDao {
    CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("users");

    public Task<QuerySnapshot> searchByGoogle(String googleId) {
        return collectionReference.whereEqualTo("googleId", googleId).get();
    }
    public Task<Void> createUser(GoogleSignInAccount account, String id) {
        CartDao cartDao = new CartDao();
        cartDao.createUserCart(id);
        UserData userData = new UserData(id, String.valueOf(account.getPhotoUrl()), account.getGivenName() + " " + account.getFamilyName(), account.getEmail(), account.getId());
        return collectionReference.document(id).set(userData);
    }

    public Task<DocumentSnapshot> getUser(String uid) {
        return collectionReference.document(uid).get();
    }

    public Task<Void> update(String id, HashMap<String, Object> hashMap) {
        return collectionReference.document(id).update(hashMap);
    }
}
