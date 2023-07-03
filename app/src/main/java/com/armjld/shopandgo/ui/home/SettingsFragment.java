package com.armjld.shopandgo.ui.home;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.armjld.shopandgo.Dao.CartDao;
import com.armjld.shopandgo.Dao.UsersDao;
import com.armjld.shopandgo.Helpers.FileInfo;
import com.armjld.shopandgo.Helpers.LoginManager;
import com.armjld.shopandgo.Helpers.UserInFormation;
import com.armjld.shopandgo.Models.UserData;
import com.armjld.shopandgo.R;
import com.armjld.shopandgo.databinding.FragmentSettingsBinding;
import com.armjld.shopandgo.ui.About;
import com.armjld.shopandgo.ui.EditInfoActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;


public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    UserData myUser;
    FragmentSettingsBinding binding;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        myUser = UserInFormation.getUser(getContext());
        progressDialog = new ProgressDialog(getContext());


        binding.btnLogOut.setOnClickListener(v-> {
            LoginManager loginManager = new LoginManager();
            loginManager.logOut(getActivity());
        });

        binding.tvUserName.setOnClickListener(v-> {
            startActivity(new Intent(getContext(), EditInfoActivity.class));
        });

        binding.tvAboutApp.setOnClickListener(v-> {
            startActivity(new Intent(getContext(), About.class));
        });

        binding.tvRateApp.setOnClickListener(v-> rateApp());
        binding.tvShareApp.setOnClickListener(v-> shareApp());

        binding.imgUser.setOnClickListener(v -> choosePic());

        updateUI();
        return binding.getRoot();
    }

    private void choosePic() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    private void updateUI() {
        binding.txtName.setText(myUser.getName());
        if (!myUser.getImageUrl().equals(""))
            Glide.with(this).load(myUser.getImageUrl()).diskCacheStrategy(DiskCacheStrategy.DATA).into(binding.imgUser);

    }

    private void shareApp() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "https://play.google.com/store/apps/details?id=" + getContext().getPackageName();
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Armjld");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void rateApp() {
        final String appPackageName = getContext().getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException error) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data == null) return;
            Uri imageUri = data.getData();
            assert imageUri != null;
            saveImage(imageUri);
        }
    }

    private void saveImage(Uri imageUri) {
        showProgressDialog();
        FileInfo imageUtil = new FileInfo(getContext());
        UsersDao usersDao = new UsersDao();
        Bitmap profileBitmap = imageUtil.resizePhoto(imageUri, 750);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        profileBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("users").child(myUser.getId() + ".jpeg");
        imageRef.putBytes(baos.toByteArray()).addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
            HashMap<String, Object> updateValues = new HashMap<>();
            updateValues.put("imageUrl", uri1.toString());
            myUser.setImageUrl(uri1.toString());
            usersDao.update(myUser.getId(), updateValues);
            binding.imgUser.setImageBitmap(profileBitmap);
            progressDialog.dismiss();
            Toasty.success(getContext(), getString(R.string.image_updated), Toasty.LENGTH_SHORT).show();
        })).addOnFailureListener(exception -> {
            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void showProgressDialog() {
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}