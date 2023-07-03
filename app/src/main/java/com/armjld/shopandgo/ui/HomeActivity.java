package com.armjld.shopandgo.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.armjld.shopandgo.Dao.UsersDao;
import com.armjld.shopandgo.Helpers.DialogMaker;
import com.armjld.shopandgo.Helpers.PermisionActions;
import com.armjld.shopandgo.Models.UserData;
import com.armjld.shopandgo.R;
import com.armjld.shopandgo.databinding.ActivityHomeBinding;
import com.armjld.shopandgo.ui.home.CartFragment;
import com.armjld.shopandgo.ui.home.HomeFragment;
import com.armjld.shopandgo.ui.home.SettingsFragment;

import es.dmoral.toasty.Toasty;

public class HomeActivity extends AppCompatActivity {

    public static boolean inside = false;

    UserData myUser;
    boolean doubleBackToExitPressedOnce = false;

    UsersDao usersDao;

    HomeFragment homeFragment;
    CartFragment cartFragment;
    SettingsFragment settingsFragment;

    Fragment active;
    private FragmentManager fm;
    private int currentIndex = 0;

    ActivityHomeBinding binding;

    @Override
    public void onBackPressed() {

        if (currentIndex != 0) {
            binding.bottomNavigationView.setSelectedItemId(R.id.home);
            return;
        }

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
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initFrags();
    }

    private void initFrags() {
        clearFragments();
        initFragments();
        initBottomNav();
    }

    // When check out is made
    public void openCheckOutActivity() {
        startActivityForResult(new Intent(this, PayingActivity.class), 1);
    }

    // When user scan a qr code
    public void  openStoreScannerActivity() {
        if(inside) {
            Toasty.info(this, "You're already inside the store").show();
            return;
        }

        PermisionActions permisionActions = new PermisionActions(this);

        if(!permisionActions.camPerm()) return;
        startActivityForResult(new Intent(this, QrScanner.class), 2);
    }

    // When check out is made
    private void openCheckOutDialog() {
        DialogMaker.showPaymentDialog(this);
        inside = false;
    }

    // When user scan a qr code
    private void  openStoreDialog() {
        inside = true;
        DialogMaker.showStoreEntered(this);
    }

    private void clearFragments() {
        currentIndex = 0;
        cartFragment = null;
        homeFragment = null;
        settingsFragment = null;
        active = null;

        getFragmentManager().popBackStackImmediate();
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }

        binding.content.removeAllViews();
        Log.i("TAG", "clearFragments: Cleared");
    }

    private void initFragments() {
        fm = getSupportFragmentManager();

        homeFragment = new HomeFragment();
        cartFragment = new CartFragment();
        settingsFragment = new SettingsFragment();

        active = homeFragment;
        currentIndex = 0;

        fm.beginTransaction().add(R.id.content, settingsFragment, "3").hide(settingsFragment).commit();
        fm.beginTransaction().add(R.id.content, cartFragment, "1").hide(cartFragment).commit();
        fm.beginTransaction().add(R.id.content, homeFragment, "0").commit();
    }

    private void initBottomNav() {
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    showItemByIndex(0, false);
                    return true;
                case R.id.cart:
                    showItemByIndex(1, false);
                    return true;
                case R.id.settings:
                    showItemByIndex(3, false);
                    return true;
            }
            return false;
        });
    }

    public void showItemByIndex(int index, boolean navSelect) {
        switch (index) {
            case 0:
                fm.beginTransaction().hide(active).show(homeFragment).commit();
                active = homeFragment;
                currentIndex = index;
                if (navSelect) binding.bottomNavigationView.setSelectedItemId(R.id.home);
                break;
            case 1:
                fm.beginTransaction().hide(active).show(cartFragment).commit();
                active = cartFragment;
                currentIndex = index;
                if (navSelect) binding.bottomNavigationView.setSelectedItemId(R.id.cart);
                break;
            case 3:
                fm.beginTransaction().hide(active).show(settingsFragment).commit();
                active = settingsFragment;
                currentIndex = index;
                if (navSelect) binding.bottomNavigationView.setSelectedItemId(R.id.settings);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK)  return;

        if(requestCode == 1) {
            openCheckOutDialog();
        } else if(requestCode == 2) {
            openStoreDialog();
        }
    }
}