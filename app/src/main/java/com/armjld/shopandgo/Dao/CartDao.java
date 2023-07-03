package com.armjld.shopandgo.Dao;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartDao {
    CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("cart");

    public DocumentReference listenToUserCart(String id) {
        return collectionReference.document(id);
    }

    public Task<Void> clearCart(String id) {
        List<String> list = new ArrayList<>();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("listItems", list);
        return collectionReference.document(id).update(hashMap);
    }
}
