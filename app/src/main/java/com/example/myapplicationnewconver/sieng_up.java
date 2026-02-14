package com.example.myapplicationnewconver;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class sieng_up extends AppCompatActivity {
    TextView tvLogin;
    CircleImageView circleImageView;
 Button sign_up;
    String imageUriToSend = "";
    Bitmap bito;
    byte[] bytes;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    DatabaseReference databaseReference;
    Uri selectedImageUri=null;

    private final static int Gallery_code=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sieng_up);
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            window.setStatusBarColor(Color.TRANSPARENT);
        }

       firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");

        firebaseStorage = FirebaseStorage.getInstance();
        tvLogin = findViewById(R.id.tvLogin);
         circleImageView=findViewById(R.id.profile_image);
sign_up=findViewById(R.id.seing_up);
        getSupportActionBar().hide();
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);*/
                check();


            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextInputEditText usernameInput = findViewById(R.id.usernameEditText);
                TextInputEditText lastnameInput = findViewById(R.id.lastnameEditText);
                TextInputEditText regNumberInput = findViewById(R.id.regNumberEditText);

                String username = usernameInput.getText().toString();
                String regNumber = regNumberInput.getText().toString();
                String  lastname=lastnameInput.getText().toString();

          /*     Intent intent = new Intent(sieng_up.this, MainActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("regNumber", regNumber);
                intent.putExtra("lastname", lastname);
                intent.putExtra("imageUri", imageUriToSend);
                intent.putExtra("rawData","00/00/00");*/
                DataHolder.username = username;
                DataHolder.regNumber = regNumber;
                DataHolder.lastname = lastname;
                DataHolder.imageUri = imageUriToSend;
                DataHolder.rawData="00/00/00";

             //   startActivity(intent);

     //    cimp(bytes,username,regNumber,lastname);
               startActivity(new Intent(getApplicationContext(),MainActivity.class));


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==Gallery_code && resultCode == RESULT_OK && data != null){
            selectedImageUri = data.getData();
            CropImage.activity(selectedImageUri)
                    .setMaxCropResultSize(3000,3000)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setOutputCompressQuality(100)
                    .start(this);}
        if(requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE&& resultCode==RESULT_OK)
        {CropImage.ActivityResult  result=CropImage.getActivityResult(data);
            Uri resulturi=result.getUri();


            // File thumb_path = new File(resulturi.getPath());

            try {
                File imageFile = uriToFile(resulturi);
                bito = new Compressor(this)
                        .setMaxHeight(250)
                        .setMaxWidth(250)
                        .setQuality(100)
                        .compressToBitmap(imageFile);
                imageFile.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bito.compress(Bitmap.CompressFormat.WEBP, 50, stream);
            bytes = stream.toByteArray();
           // circleImageView.setImageURI(resulturi);
            circleImageView.setImageBitmap(bito);

            imageUriToSend=resulturi.toString();
        }
    }

    private  void check(){
        Dexter.withActivity(sieng_up.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent,Gallery_code);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }
    private File uriToFile(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        File file = new File(getCacheDir(), "temp_image.jpg");
        FileOutputStream outputStream = new FileOutputStream(file);

        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, len);
        }

        outputStream.close();
        inputStream.close();

        return file;
    }

    private void cimp (byte[] bytee,String name,String number,String last){
        ProgressDialog progressDialog = new ProgressDialog(sieng_up.this);
        progressDialog.setMessage("جاري رفع البيانات...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StorageReference fille=firebaseStorage.getReference().child("tramway_profile").child(number);
        fille.putBytes(bytee).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            fille.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(Task<Uri> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                    String t=task.getResult().toString();
                    HashMap userMap = new HashMap<>();
                    userMap.put("username", name);
                    userMap.put("lastname", last);
                    userMap.put("regNumber", number);
                    userMap.put("imageUrl", t);
                    databaseReference.child(number).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            progressDialog.dismiss(); // إخفاء الـ Progress
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(sieng_up.this, MainActivity.class);
                                intent.putExtra("username", name);
                                intent.putExtra("regNumber", number);
                                intent.putExtra("lastname", last);
                                intent.putExtra("imageUri", imageUriToSend);
                                startActivity(intent);
                                 // إنهاء صفحة التسجيل الحالية

                                Toast.makeText(sieng_up.this, "تم الحفظ بنجاح", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(sieng_up.this, "فشل في الحفظ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }}
            });
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}