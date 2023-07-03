package com.armjld.shopandgo.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.armjld.shopandgo.Dao.CartDao;
import com.armjld.shopandgo.Dao.ProductDao;
import com.armjld.shopandgo.Helpers.UserInFormation;
import com.armjld.shopandgo.Models.Product;
import com.armjld.shopandgo.Models.UserData;
import com.armjld.shopandgo.R;
import com.armjld.shopandgo.databinding.FragmentCartBinding;
import com.armjld.shopandgo.ui.HomeActivity;
import com.armjld.shopandgo.ui.PayingActivity;
import com.armjld.shopandgo.ui.ProductAdapter;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CartFragment extends Fragment {
    public CartFragment() {
        // Required empty public constructor
    }

    CartDao cartDao;
    ProductDao productDao;
    UserData myUser;
    FragmentCartBinding binding;
    List<String> listItems = new ArrayList<>();
    List<Product> listProducts = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        myUser = UserInFormation.getUser(getContext());
        cartDao = new CartDao();
        productDao = new ProductDao();

        binding.btnCheckOut.setOnClickListener(v-> ((HomeActivity) requireActivity()).openCheckOutActivity());

        listenToCart();
        return binding.getRoot();
    }

    private void listenToCart() {
        cartDao.listenToUserCart(myUser.getId()).addSnapshotListener((value, error) -> {
            if (error != null) {
                return;
            }

            if (value != null && value.exists()) {
                onNewItemAdded((List<String>) value.getData().get("listItems"));
            }
        });
    }

    private void onNewItemAdded(List<String> listItems) {
        this.listItems = listItems;

        if(listItems.isEmpty()) {
            listProducts.clear();
            binding.recyclerView.setAdapter(null);
            checkForEmpty();
            return;
        }

        for (int i = 0; i < listItems.size(); i++) {
            String id = listItems.get(i);
            listProducts.clear();

            int finalI = i;
            productDao.getProduct(id).addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    listProducts.add(documentSnapshot.toObject(Product.class));
                }

                if ((listItems.size() - 1) == finalI) {
                    updateUI();
                }
            });
        }
    }

    private void updateUI() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(new ProductAdapter(getContext(), (ArrayList<Product>) listProducts));
        checkForEmpty();
    }

    private void checkForEmpty() {
        if(listProducts.isEmpty()) {
            binding.linEmpty.setVisibility(View.VISIBLE);
            binding.btnCheckOut.setVisibility(View.GONE);
        } else {
            binding.linEmpty.setVisibility(View.GONE);
            binding.btnCheckOut.setVisibility(View.VISIBLE);
        }
    }
}