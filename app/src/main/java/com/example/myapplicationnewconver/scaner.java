package com.example.myapplicationnewconver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

public class scaner extends AppCompatActivity {
    private static final int QR_REQUEST_CODE = 1001;
    private DecoratedBarcodeView barcodeView;
    private TextView resultText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scaner);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        getSupportActionBar().hide();
        barcodeView = findViewById(R.id.barcode_scanner);
        resultText = findViewById(R.id.result_text);

        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if(result.getText() != null) {
                    barcodeView.pause();
                    processQrResult(result.getText());

                }
            }

            @Override
            public void possibleResultPoints(List resultPoints) {}
        });

    }

    private void processQrResult(String text) {
        String user = DataHolder.username;
        String reg = DataHolder.regNumber;
        String last = DataHolder.lastname;
        String image = DataHolder.imageUri;


        try {
            JSONObject qr = new JSONObject(text);
            String rawData = qr.getString("data");
            String signature = qr.getString("signature");

            boolean valid = verifySignature(rawData, signature);

            Toast.makeText(this, valid ? "QR صالح ✅: " + rawData : "QR غير صالح ❌", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(scaner.this, MainActivity.class);
            intent.putExtra("username", user);
            intent.putExtra("regNumber", reg);
            intent.putExtra("lastname", last);
            intent.putExtra("imageUri", image);
            intent.putExtra("rawData", rawData);
            DataHolder.rawData=rawData;
            startActivity(intent);
            finish();


        } catch (Exception e) {
            Toast.makeText(this, "فشل في تحليل QR", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(scaner.this, MainActivity.class);
            intent.putExtra("rawData", "00/00/00");
            intent.putExtra("username", user);
            intent.putExtra("regNumber", reg);
            intent.putExtra("lastname", last);
            intent.putExtra("imageUri", image);
            startActivity(intent);
            finish();
        }

    }


    private boolean verifySignature(String data, String sigBase64) {
        try {
            // فك التوقيع من base64
            byte[] sigBytes = Base64.decode(sigBase64, Base64.DEFAULT);

            // المفتاح العام بدون رؤوس/ذيول أو أسطر جديدة
            String keyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArSj5fN8xnEtog6Pg9tMhI3lCBnxwo4tbEjgAGS0WQjTKgmbJPURL+a2kHYD4qZznMG/yXgJh/spDiUweooc5EBMJniAP1pDw0RUoiCVNgrp0NCXg9qTukN+kJgMUP1iMUgTNchS0YtmVNEsOC8ki5Y0GSYA1x9IPR6VuOuS+EgZ5pn5CJ0wvxIN93B1rY9L/+S5kwHj29yE/FnPc2bKfIzwSvt1BvkiXgVnzUG9MOTzVc2VZsZ0KD02OMza6V8C6SAWomZhpcioZ5AxAWWTg+7FLAPurMUrz90+5BEhSXGVoq45ejZN11ULU6v52VVfd4rSWNFB6dcCddSdCAFiMHwIDAQAB";

            // فك المفتاح من base64
            byte[] keyBytes = Base64.decode(keyStr, Base64.DEFAULT);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey publicKey = kf.generatePublic(spec);

            // إعداد التوقيع والتحقق
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(data.getBytes(StandardCharsets.UTF_8));

            return signature.verify(sigBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }
}