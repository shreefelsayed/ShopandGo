package com.armjld.shopandgo.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.armjld.shopandgo.Dao.CartDao;
import com.armjld.shopandgo.Helpers.UserInFormation;
import com.armjld.shopandgo.Models.UserData;
import com.armjld.shopandgo.databinding.ActivityPayingBinding;

import es.dmoral.toasty.Toasty;

public class PayingActivity extends AppCompatActivity {

    ActivityPayingBinding binding;
    CartDao cartDao = new CartDao();
    ProgressDialog progressDialog;
    UserData myUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPayingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myUser = UserInFormation.getUser(this);
        progressDialog = new ProgressDialog(this);

        binding.payview.setPayOnclickListener(v -> {
            if(binding.payview.isFillAllComponents()) {
                clearCart();
            }
        });
    }

    private void clearCart() {
        progressDialog.setMessage("Paying for the order ..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        cartDao.clearCart(myUser.getId()).addOnSuccessListener(unused -> {
            Toasty.success(this, "Thank you for your payment").show();
            progressDialog.dismiss();
            returnResult();
        }).addOnFailureListener(e -> {
            Toasty.error(this, e.getMessage()).show();
            progressDialog.dismiss();
        });
    }

    private void returnResult() {
        Intent intent = getIntent();
        setResult(RESULT_OK,intent);
        finish();
    }
}