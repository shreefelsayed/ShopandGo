package com.armjld.shopandgo.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.armjld.shopandgo.Helpers.LoginManager;
import com.armjld.shopandgo.R;
import com.armjld.shopandgo.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;


public class SplashScreen extends AppCompatActivity {

    ActivityMainBinding binding;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            System.exit(0);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.str_press_again, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseApp.initializeApp(this);
        whatToDo();
    }

    private void whatToDo() {
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    LoginManager loginManager = new LoginManager();
                    loginManager.saveUserData(SplashScreen.this);
                } else {
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                }
            }
        };

        // Schedule the task to be executed after 5 seconds
        timer.schedule(task, 1200);

    }
}