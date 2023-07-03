package com.armjld.shopandgo.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.armjld.shopandgo.Helpers.GoogleLogin;
import com.armjld.shopandgo.databinding.ActivityLoginBinding;
import com.armjld.shopandgo.databinding.ActivityMainBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnGoogle.setOnClickListener(v-> {
            new GoogleLogin(this);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == GoogleLogin.RC_SIGN_IN) {
                GoogleLogin.activityResult(data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}