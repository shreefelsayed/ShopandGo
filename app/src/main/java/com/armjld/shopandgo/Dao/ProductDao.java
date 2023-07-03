package com.armjld.shopandgo.Dao;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ProductDao {
    CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("products");

    public Task<QuerySnapshot> getAll() {
        return collectionReference.get();
    }
    public Task<DocumentSnapshot> getProduct(String id) {
        return collectionReference.document(id).get();
    }
}
