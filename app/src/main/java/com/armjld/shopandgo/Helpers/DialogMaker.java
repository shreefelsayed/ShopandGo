package com.armjld.shopandgo.Helpers;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.armjld.shopandgo.Models.UserData;
import com.armjld.shopandgo.R;
import com.armjld.shopandgo.databinding.DialogPaymentMadeBinding;
import com.armjld.shopandgo.databinding.DialogQrBinding;
import com.armjld.shopandgo.databinding.DialogStoreEnteredBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import net.glxn.qrgen.android.QRCode;

public class DialogMaker {
    public static void showPaymentDialog(Activity mContext) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext, R.style.CustomBottomSheetDialogTheme);
        View sheetView = LayoutInflater.from(mContext).inflate(R.layout.dialog_payment_made, mContext.findViewById(R.id.bottom_sheet));
        DialogPaymentMadeBinding binding = DialogPaymentMadeBinding.bind(sheetView);
        bottomSheetDialog.setContentView(binding.getRoot());
        bottomSheetDialog.show();
    }

    public static void showStoreEntered(Activity mContext) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext, R.style.CustomBottomSheetDialogTheme);
        View sheetView = LayoutInflater.from(mContext).inflate(R.layout.dialog_store_entered, mContext.findViewById(R.id.bottom_sheet));
        DialogStoreEnteredBinding binding = DialogStoreEnteredBinding.bind(sheetView);
        bottomSheetDialog.setContentView(binding.getRoot());
        bottomSheetDialog.show();
    }

    public static void showOrderQrDialog(Activity mContext) {
        DialogQrBinding binding;
        UserData myUser = UserInFormation.getUser(mContext);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext, R.style.CustomBottomSheetDialogTheme);
        View sheetView = LayoutInflater.from(mContext).inflate(R.layout.dialog_qr, mContext.findViewById(R.id.bottom_sheet));
        binding = DialogQrBinding.bind(sheetView);
        bottomSheetDialog.setContentView(binding.getRoot());

        binding.imgQR.setImageBitmap(QRCode.from(myUser.getId()).withSize(150, 150).bitmap());
        bottomSheetDialog.show();
    }
}
