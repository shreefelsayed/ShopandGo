package com.armjld.shopandgo.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.armjld.shopandgo.Dao.UsersDao;
import com.armjld.shopandgo.Helpers.UserInFormation;
import com.armjld.shopandgo.Models.UserData;
import com.armjld.shopandgo.R;
import com.armjld.shopandgo.databinding.ActivityEditInfoBinding;

import java.util.HashMap;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class EditInfoActivity extends AppCompatActivity {

    UserData myUser;
    ActivityEditInfoBinding binding;
    ProgressDialog progressDialog;
    UsersDao usersDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myUser = UserInFormation.getUser(this);
        progressDialog = new ProgressDialog(this);
        usersDao = new UsersDao();

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnEditInfo.setOnClickListener(v -> {
            if (!check()) return;
            changeInfo();
        });

        binding.txtName.setText(myUser.getName());
    }

    private boolean check() {
        String strName = Objects.requireNonNull(binding.txtName.getText()).toString().trim();
        return strName.length() >= 5;
    }

    private void changeInfo() {
        showProgressDialog();
        String strName = Objects.requireNonNull(binding.txtName.getText()).toString().trim();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", strName);

        usersDao.update(myUser.getId(), hashMap).addOnSuccessListener(unused -> {
            progressDialog.dismiss();
            Toasty.success(this, getString(R.string.info_changed), Toasty.LENGTH_SHORT, true).show();
            finish();
        }).addOnFailureListener(e -> {
            Toasty.error(this, Objects.requireNonNull(e.getMessage()), Toasty.LENGTH_SHORT, true).show();
            progressDialog.dismiss();
        });

    }

    private void showProgressDialog() {
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}