package com.armjld.shopandgo.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.armjld.shopandgo.Helpers.PermisionActions;
import com.armjld.shopandgo.databinding.ActivityQrScannerBinding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.Collections;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    ActivityQrScannerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQrScannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v-> finish());

        // ---- Initialize Scanner
        mScannerView = new ZXingScannerView(this);
        mScannerView.setFormats(Collections.singletonList(BarcodeFormat.QR_CODE));
        if (PermisionActions.canFoucus()) mScannerView.setAutoFocus(false);
        mScannerView.setLaserEnabled(false);
        mScannerView.setBorderColor(Color.WHITE);
        mScannerView.stopCameraPreview();
        mScannerView.setIsBorderCornerRounded(true);
        mScannerView.setBorderCornerRadius(300);
        binding.contentFrame.addView(mScannerView);
    }


    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        mScannerView.stopCameraPreview();
        returnResult();
    }

    private void returnResult() {
        Intent intent = getIntent();
        setResult(RESULT_OK,intent);
        finish();
    }
}