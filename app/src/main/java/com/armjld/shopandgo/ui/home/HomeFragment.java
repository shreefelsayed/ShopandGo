package com.armjld.shopandgo.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.armjld.shopandgo.Helpers.DialogMaker;
import com.armjld.shopandgo.Helpers.UserInFormation;
import com.armjld.shopandgo.Models.UserData;
import com.armjld.shopandgo.R;
import com.armjld.shopandgo.databinding.FragmentHomeBinding;
import com.armjld.shopandgo.ui.HomeActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Calendar;

public class HomeFragment extends Fragment {
    public HomeFragment() {
    }

    UserData myUser;
    FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        myUser = UserInFormation.getUser(getContext());

        binding.btnScan.setOnClickListener(v-> {
            ((HomeActivity) requireActivity()).openStoreScannerActivity();
        });

        binding.btnCart.setOnClickListener(v-> {
            DialogMaker.showOrderQrDialog(getActivity());
        });

        updateUI();

        return binding.getRoot();
    }

    private void updateUI() {
        binding.tvName.setText(myUser.getName());
        binding.tvType.setText(getGreetingMessage());
        if (!myUser.getImageUrl().equals(""))
            Glide.with(this).load(myUser.getImageUrl()).diskCacheStrategy(DiskCacheStrategy.DATA).into(binding.imgUser);

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private String getGreetingMessage() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        // Set the appropriate greeting based on the time
        if (hourOfDay >= 6 && hourOfDay < 12) {
           return "Good Morning";
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            return "Good Afternoon";
        } else {
            return"Good Evening";
        }
    }
}